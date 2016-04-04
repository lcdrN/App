package com.example.no.toilette;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends Activity {

    int PLACE_PICKER_REQUEST = 1;
    ArrayList nbResultats = new ArrayList();
    private double longitude = 0.0;
    private double latitude = 0.0;
    private ArrayList<Toilette> resultats, resultat2;
    private ToiletteAdapteur adapt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NumberPicker pick = (NumberPicker) findViewById(R.id.numberPicker);
        pick.setMaxValue(10);
        pick.setMinValue(1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void getPosition(View view) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        //Intent intent = intentBuilder.build(getApplicationContext());
        startActivityForResult(intentBuilder.build(this), PLACE_PICKER_REQUEST);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                EditText position = (EditText) findViewById(R.id.editText);
                Place place = PlacePicker.getPlace(data, this);
                String adresse = String.format(place.getAddress().toString());
                String toastMsg = String.format("Place: %s", place.getLatLng().longitude);
                this.longitude = place.getLatLng().longitude;
                System.out.println(this.longitude);
                this.latitude = place.getLatLng().latitude;
                position.setText(adresse);
            }
        }
    }

    public void rechercher(View view){
        resultats = new ArrayList<Toilette>();
        resultat2 = new ArrayList<Toilette>();
        ListView list = (ListView) findViewById(R.id.listView);
        adapt = new ToiletteAdapteur(this, resultat2);
        list.setAdapter(adapt);

        RequestQueue queue = Volley.newRequestQueue(this);
        final String temp = "{\"l:\"{\"$near\":["+this.latitude+", "+this.longitude+"]}}";
        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.GET,
                        "http://data.nantes.fr/api/publication/24440040400129_NM_NM_00170/Toilettes_publiques_nm_STBL/content",
                        new Response.Listener<String>() {
                            public void onResponse(String response) {
                                try {
                                    JSONObject reader = new JSONObject(response.toString());
                                    JSONArray tmp = reader.getJSONArray("data");
                                    for(int i =0; i<tmp.length(); i++) {
                                        JSONObject obj = tmp.getJSONObject(i);
                                        resultats.add(new Toilette(obj.getJSONObject("geo").getString("name"), obj.getJSONArray("_l").getDouble(0),
                                                obj.getJSONArray("_l").getDouble(1), obj.getString("COMMUNE"), obj.getString("ADRESSE")));
                                    resultats.get(i).setDistance(latitude, longitude);

                                    }
                                    Collections.sort(resultats, new Comparateur());
                                    NumberPicker pick = (NumberPicker) findViewById(R.id.numberPicker);

                                    for(int j=0;j<pick.getValue();j++){
                                        resultat2.add(resultats.get(j));
                                    }
                                    adapt.sort();
                                    adapt.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }},
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.getMessage());
                            }})
                {
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("filter", temp);
                        return params;
                    }
                };
        queue.add(stringRequest);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toilette item = adapt.getItem(position);
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                intentBuilder.setLatLngBounds(new LatLngBounds(new LatLng(item.getLatitude(), item.getLongitude()), new LatLng(item.getLatitude(), item.getLongitude())));
                try {
                    startActivityForResult(intentBuilder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }


        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package br.com.geolocalizacao.geolocalizacao.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Pais {

    private String geoID;
    private String nome;

    public Pais(String geoID, String nome) {
        this.geoID = geoID;
        this.nome = nome;
    }

    public String getGeoID() {
        return geoID;
    }

    public void setGeoID(String geoID) {
        this.geoID = geoID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static ArrayList<Pais> converterJson(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        JSONArray jArray = jo.getJSONArray("geonames");

        ArrayList<Pais> lista = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {

            JSONObject objeto = jArray.getJSONObject(i);

            lista.add(new Pais(
                    objeto.getString("countryId"),
                    objeto.getString("countryName")
            ));
        }

        return lista;
    }
}

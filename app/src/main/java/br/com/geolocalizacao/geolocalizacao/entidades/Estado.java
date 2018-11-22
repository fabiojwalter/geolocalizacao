package br.com.geolocalizacao.geolocalizacao.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Estado {

    private String geoID;
    private String nome;

    public Estado(String geoID, String nome) {
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

    public static ArrayList<Estado> converterJson(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        JSONArray jArray = jo.getJSONArray("geonames");

        ArrayList<Estado> lista = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {

            JSONObject objeto = jArray.getJSONObject(i);

            lista.add(new Estado(
                    objeto.getString("geonameId"),
                    objeto.getString("name")
            ));
        }

        return lista;
    }
}

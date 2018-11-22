package br.com.geolocalizacao.geolocalizacao.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 */
public class Continente implements Serializable {

    private String geoID;
    private String nome;

    public Continente(String geoID, String nome) {
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

    /**
     * Converter os dados vindos do JSON para o Array de Continentes
     * @param json
     * @return
     * @throws JSONException
     */
    public static ArrayList<Continente> converterJson(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        JSONArray jArray = jo.getJSONArray("geonames");

        ArrayList<Continente> lista = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {

            JSONObject objeto = jArray.getJSONObject(i);

            lista.add(new Continente(
                    objeto.getString("geonameId"),
                    objeto.getString("toponymName")
            ));
        }

        return lista;
    }
}

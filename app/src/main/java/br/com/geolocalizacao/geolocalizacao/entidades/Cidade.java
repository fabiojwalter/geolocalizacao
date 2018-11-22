package br.com.geolocalizacao.geolocalizacao.entidades;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 */
public class Cidade {

    private String geoID;
    private String nome;
    private Long populacao;

    public Cidade(String geoID, String nome, Long populacao) {
        this.geoID = geoID;
        this.nome = nome;
        this.populacao = populacao;
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

    public Long getPopulacao() {
        return populacao;
    }

    public void setPopulacao(Long populacao) {
        this.populacao = populacao;
    }


    @Override
    public String toString() {
        return "Nome: " + this.getNome() + "\n"
                + "População: " + this.getPopulacao();
    }

    public static ArrayList<Cidade> converterJson(String json) throws JSONException {
        JSONObject jo = new JSONObject(json);
        JSONArray jArray = jo.getJSONArray("geonames");

        ArrayList<Cidade> lista = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {

            JSONObject objeto = jArray.getJSONObject(i);

            lista.add(new Cidade(
                    objeto.getString("geonameId"),
                    objeto.getString("name"),
                    objeto.getLong("population")
            ));
        }

        return lista;
    }
}

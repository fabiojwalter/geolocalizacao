package br.com.geolocalizacao.geolocalizacao;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import br.com.geolocalizacao.geolocalizacao.entidades.Estado;
import br.com.geolocalizacao.geolocalizacao.entidades.Pais;

public class EstadoActivity extends AppCompatActivity {

    ListView listView;
    Toast toastCarregando;
    String paisID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais);

        Bundle b = getIntent().getExtras();
        this.paisID = b.getString("paisID");
        String paisNome = b.getString("paisNome");


        // Para aparecer a msg de carregando
        toastCarregando = Toast.makeText(getApplicationContext(),
                R.string.carregando,
                Toast.LENGTH_SHORT);

        ConsultaEstados consultaPaises = new ConsultaEstados();

        consultaPaises.execute(this.paisID);
    }


    /**
     *
     */
    class ConsultaEstados extends AsyncTask<String, Integer, ArrayList<Estado>> {

        @Override
        protected ArrayList<Estado> doInBackground(String... strings) {
            toastCarregando.show();
            String codigoPais = strings[0];


            Log.i("CONEXAO",
                    "Enviando REQUEST para http://www.geonames.org/childrenJSON?geonameId="
                            + codigoPais);

            String json = ConexaoUtils.retornaJSON(
                    "http://www.geonames.org/childrenJSON?geonameId="
                            + codigoPais);

            ArrayList<Estado> retorno = new ArrayList<>();
            try {
                retorno = Estado.converterJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                return retorno;
            }
        }


        @Override
        protected void onPostExecute(final ArrayList<Estado> estados) {
            toastCarregando.cancel();
            if (estados.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.nenhum_registro, Toast.LENGTH_LONG).show();
            } else {
                listView = findViewById(R.id.lvPaises);
                listView.setAdapter(new EstadoBA(getApplicationContext(), estados));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(EstadoActivity.this, CidadeActivity.class);

                        Estado atual = estados.get(i);

                        intent.putExtra("estadoID", atual.getGeoID());
                        intent.putExtra("estadoNome", atual.getNome());

                        startActivity(intent);
                    }
                });
            }
        }
    }

    /**
     *
     */
    class EstadoBA extends BaseAdapter {

        ArrayList<Estado> lista = new ArrayList<>();
        Context applicationContext = null;
        LayoutInflater inflater = null;

        public EstadoBA(Context context, ArrayList<Estado> lista) {
            this.applicationContext = context;
            this.lista = lista;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return lista != null ? lista.size() : 0;
        }

        @Override
        public Estado getItem(int i) {
            return lista.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(lista.get(i).getGeoID());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.list_view, null);
            TextView tvPais = view.findViewById(R.id.tvNome);
            tvPais.setText(lista.get(i).getNome());
            return view;
        }
    }
}

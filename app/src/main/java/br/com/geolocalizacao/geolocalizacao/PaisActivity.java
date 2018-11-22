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

import br.com.geolocalizacao.geolocalizacao.entidades.Pais;

public class PaisActivity extends AppCompatActivity {

    ListView listView;
    Toast toastCarregando;
    String continenteID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais);

        Bundle b = getIntent().getExtras();
        continenteID = b.getString("continenteID");
        String continenteNome = b.getString("continenteNome");


        // Para aparecer a msg de carregando
        toastCarregando = Toast.makeText(getApplicationContext(),
                R.string.carregando,
                Toast.LENGTH_SHORT);

        ConsultaPaises consultaPaises = new ConsultaPaises();

        consultaPaises.execute(this.continenteID);
    }


    /**
     *
     */
    class ConsultaPaises extends AsyncTask<String, Integer, ArrayList<Pais>> {

        @Override
        protected ArrayList<Pais> doInBackground(String... strings) {
            toastCarregando.show();
            String codigoPais = strings[0];


            Log.i("CONEXAO",
                    "Enviando REQUEST para http://www.geonames.org/childrenJSON?geonameId="
                            + codigoPais);

            String json = ConexaoUtils.retornaJSON(
                    "http://www.geonames.org/childrenJSON?geonameId="
                            + codigoPais);

            ArrayList<Pais> retorno = new ArrayList<>();
            try {
                retorno = Pais.converterJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                return retorno;
            }
        }


        @Override
        protected void onPostExecute(final ArrayList<Pais> paises) {
            toastCarregando.cancel();
            if (paises.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.nenhum_registro, Toast.LENGTH_LONG).show();
            } else {
                listView = findViewById(R.id.lvPaises);
                listView.setAdapter(new PaisBA(getApplicationContext(), paises));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Chamar a activity de estado passando o codigo do pais
                        Intent intent = new Intent(PaisActivity.this, EstadoActivity.class);

                        Pais atual = paises.get(i);

                        intent.putExtra("paisID", atual.getGeoID());
                        intent.putExtra("paisNome", atual.getNome());

                        startActivity(intent);

                    }
                });
            }
        }
    }

    /**
     *
     */
    class PaisBA extends BaseAdapter {

        ArrayList<Pais> listaPais = new ArrayList<>();
        Context applicationContext = null;
        LayoutInflater inflater = null;

        public PaisBA(Context context, ArrayList<Pais> listaPaiss) {
            this.applicationContext = context;
            this.listaPais = listaPaiss;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return this.listaPais != null ? this.listaPais.size() : 0;
        }

        @Override
        public Pais getItem(int i) {
            return this.listaPais.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(this.listaPais.get(i).getGeoID());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.list_view, null);
            TextView tvPais = view.findViewById(R.id.tvNome);
            tvPais.setText(this.listaPais.get(i).getNome());
            return view;
        }
    }
}

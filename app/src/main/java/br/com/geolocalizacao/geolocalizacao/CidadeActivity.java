package br.com.geolocalizacao.geolocalizacao;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import br.com.geolocalizacao.geolocalizacao.entidades.Cidade;

public class CidadeActivity extends AppCompatActivity {

    ListView listView;
    Toast toastCarregando;
    String paisID = "";
    Context contexto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pais);
        this.contexto = getApplicationContext();

        Bundle b = getIntent().getExtras();
        this.paisID = b.getString("estadoID");
        String paisNome = b.getString("estadoNome");


        // Para aparecer a msg de carregando
        toastCarregando = Toast.makeText(getApplicationContext(),
                R.string.carregando,
                Toast.LENGTH_SHORT);

        ConsultaCidades consultaPaises = new ConsultaCidades();

        consultaPaises.execute(this.paisID);
    }


    /**
     *
     */
    class ConsultaCidades extends AsyncTask<String, Integer, ArrayList<Cidade>> {

        @Override
        protected ArrayList<Cidade> doInBackground(String... strings) {
            toastCarregando.show();
            String codigoPais = strings[0];


            Log.i("CONEXAO",
                    "Enviando REQUEST para http://www.geonames.org/childrenJSON?geonameId="
                            + codigoPais);

            String json = ConexaoUtils.retornaJSON(
                    "http://www.geonames.org/childrenJSON?geonameId="
                            + codigoPais);

            ArrayList<Cidade> retorno = new ArrayList<>();
            try {
                retorno = Cidade.converterJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                return retorno;
            }
        }


        @Override
        protected void onPostExecute(final ArrayList<Cidade> cidades) {
            toastCarregando.cancel();
            if (cidades.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.nenhum_registro, Toast.LENGTH_LONG).show();
            } else {
                listView = findViewById(R.id.lvPaises);
                listView.setAdapter(new CidadeBA(getApplicationContext(), cidades));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        new AlertDialog.Builder(CidadeActivity.this)
                                .setTitle(R.string.dialog_cidade_titulo)
                                .setMessage(cidades.get(i).toString())
                                .setCancelable(true)
                                .create()
                                .show();
                    }
                });
            }
        }
    }

    /**
     *
     */
    class CidadeBA extends BaseAdapter {

        ArrayList<Cidade> lista = new ArrayList<>();
        Context applicationContext = null;
        LayoutInflater inflater = null;

        public CidadeBA(Context context, ArrayList<Cidade> lista) {
            this.applicationContext = context;
            this.lista = lista;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return lista != null ? lista.size() : 0;
        }

        @Override
        public Cidade getItem(int i) {
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

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
import java.util.concurrent.ExecutionException;

import br.com.geolocalizacao.geolocalizacao.entidades.Continente;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Toast toastCarregando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Para aparecer a msg de carregando
        toastCarregando = Toast.makeText(getApplicationContext(), R.string.carregando, Toast.LENGTH_SHORT);

        ConsultaContinentes consultaContinentes = new ConsultaContinentes();

        consultaContinentes.execute();
    }


    class ConsultaContinentes extends AsyncTask<Void, Integer, ArrayList<Continente>> {
        @Override
        protected ArrayList<Continente> doInBackground(Void... voids) {
            toastCarregando.show();
            Log.i("CONEXAO", "Enviando REQUEST para http://www.geonames.org/childrenJSON?geonameId=6295630");
            String json = ConexaoUtils.retornaJSON("http://www.geonames.org/childrenJSON?geonameId=6295630");

            ArrayList<Continente> retorno = new ArrayList<>(7);
            try {
                retorno = Continente.converterJson(json);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                return retorno;
            }
        }

        @Override
        protected void onPostExecute(final ArrayList<Continente> continentes) {
            toastCarregando.cancel();
            if (continentes.size() == 0) {
                Toast.makeText(getApplicationContext(),
                        R.string.nenhum_registro, Toast.LENGTH_LONG).show();
            } else {
                //Preenchendo o listview e criando um ouvidor para o objeto selecionado na lista
                listView = findViewById(R.id.lvContinentes);
                listView.setAdapter(new ContinenteBA(getApplicationContext(), continentes));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, PaisActivity.class);

                        Continente atual = continentes.get(i);

                        intent.putExtra("continenteID", atual.getGeoID());
                        intent.putExtra("continenteNome", atual.getNome());

                        startActivity(intent);
                    }
                });
            }
        }
    }


    /**
     *
     */
    class ContinenteBA extends BaseAdapter {

        ArrayList<Continente> listaContinentes = new ArrayList<>();
        Context applicationContext = null;
        LayoutInflater inflater = null;

        public ContinenteBA(Context context, ArrayList<Continente> listaContinentes) {
            this.applicationContext = context;
            this.listaContinentes = listaContinentes;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return this.listaContinentes != null ? this.listaContinentes.size() : 0;
        }

        @Override
        public Continente getItem(int i) {
            return this.listaContinentes.get(i);
        }

        @Override
        public long getItemId(int i) {
            return Long.parseLong(this.listaContinentes.get(i).getGeoID());
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.list_view, null);
            TextView tvContinente = view.findViewById(R.id.tvNome);
            tvContinente.setText(this.listaContinentes.get(i).getNome());
            return view;
        }
    }
}

package br.com.geolocalizacao.geolocalizacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */
public class ConexaoUtils {

    /**
     * Responsável por conectar ao WEbService e carregar o objeto JSON
     *
     * @param url
     * @return
     */
    public static String retornaJSON(String url) {
        String retorno = "";

        try {
            URL urlApi = new URL(url);
            int codigoResposta;
            HttpURLConnection conexao;
            InputStream is;

            conexao = (HttpURLConnection) urlApi.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setReadTimeout(15000);
            conexao.setConnectTimeout(15000);
            conexao.connect();

            //Verifica se a conexão foi bem sucedida
            //Se retornar código maior que 400
            //quer dizer que deu erro
            //Se menor que 400 a conexão foi bem sucedida
            codigoResposta = conexao.getResponseCode();
            if (codigoResposta < HttpURLConnection.HTTP_BAD_REQUEST) {
                is = conexao.getInputStream();
            } else {
                is = conexao.getErrorStream();
            }

            //pega o InputStream e converte em String
            retorno = converteInputStream(is);
            is.close();
            conexao.disconnect();

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return retorno;
    }

    /**
     * Converte o InputStream em String
     *
     * @param is
     * @return
     */
    private static String converteInputStream(InputStream is) {
        StringBuffer buffer = new StringBuffer();

        try {
            BufferedReader br;
            String linha;

            br = new BufferedReader(new InputStreamReader(is));
            while ((linha = br.readLine()) != null) {
                buffer.append(linha);
            }

            br.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();

    }
}

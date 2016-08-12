package com.rm.androidesentials.services.http;

import android.os.AsyncTask;
import android.util.Log;


import com.rm.androidesentials.model.utils.CoupleParams;
import com.rm.androidesentials.services.interfaces.IHTTPServices;
import com.rm.androidesentials.utils.Utils;
import com.rm.androidesentials.utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


/**
 * Created by oscargallon on 4/05/15.
 */
public class HTTPServices extends AsyncTask<String, String, Boolean> {


    /**
     * En este string se alamacenara la respuesta del servidor;
     */
    private String stringRes = null;

    private boolean isPlainUrlServices = false;
    /**
     * Tipos de servicios
     */
    private String servicesType = null;

    private final int readTimeOut = 20000;

    private final int connectionTimeOut = 20000;

    private final String encodingType = "UTF-8";
    /**
     * Mensaje cuando ocurra un erro en el formato de la url
     */
    private final String URL_ERROR_FORMAT = "La url a la  que esta intentando acceder es incorrecta";

    /**
     * Mensaje cuando ocuraa un error en la conexion
     */
    private final String CONNECTION_ERROR = "Error al intentar contactar el Servidor";

    /**
     * Mensaje cuando ocurra un error desconocido
     */
    private final String UNKNOW_ERROR = "Error desconocido Intente mas Tarde";

    /**
     * Variabe para almacenar el error que ocurra
     */
    private String ERROR_MESSAGE;

    private final String CONTENT_TYPE = "Content-Type";

    private final String APPLICATION_JSON = "application/json";

    /**
     * Lista de parametros a enviar en la peticion
     */
    private List<CoupleParams> paramsList;

    private IHTTPServices httpServicesCallback;

    private JSONObject errorJsonObject = null;

    private boolean sendAsRowData;

    public HTTPServices(IHTTPServices httpServicesCallback, List<CoupleParams> paramsList,
                        String servicesType, boolean isPlainUrlServices) {
        this.httpServicesCallback = httpServicesCallback;
        this.paramsList = paramsList;
        this.servicesType = servicesType;
        this.isPlainUrlServices = isPlainUrlServices;
    }

    public HTTPServices(IHTTPServices httpServicesCallback, List<CoupleParams> paramsList,
                        String servicesType, boolean isPlainUrlServices, boolean sendAsRowData) {
        this.httpServicesCallback = httpServicesCallback;
        this.paramsList = paramsList;
        this.servicesType = servicesType;
        this.isPlainUrlServices = isPlainUrlServices;
        this.sendAsRowData = sendAsRowData;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        //Cojemos la url a la que se realizara la peticion
        String urlString = strings[0];
        Log.i("Entre", "me registrare");

        //Se evalua que la url sea valida
        if (!Validation.validateURl(urlString)) {
            ERROR_MESSAGE = URL_ERROR_FORMAT;
            return false;

        }
        try {

            if (this.isPlainUrlServices) {
                /**
                 * Damos formato a los parametros para enviarlos por
                 * la url al backend
                 */
                String data = Utils.organizePostServicesParametres(paramsList);
                if (data != null) {
                    urlString += "?" + data;
                }
            }

            /**
             * Creamos un nuevo objeto de tipo url
             */
            URL url = new URL(urlString);

            /**
             * Creamos un nuevo objeto de tipo http con la url de destino
             */
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            /**
             * AÃ±adimos un tiempo de conexion maximo
             */
            httpURLConnection.setConnectTimeout(this.connectionTimeOut);


            /**
             * LE decimos que el tipo de conexion es POST
             */
            httpURLConnection.setRequestMethod(this.servicesType);

            /**
             * Si el tipo de servicio es post indicamos
             */
            if (this.servicesType.equals(Utils.POST_SERVICE_TYPE)) {
                httpURLConnection.setDoOutput(true);
                if (servicesType.equals(Utils.POST_SERVICE_TYPE)) {
                    httpURLConnection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
                }
            }




            if (!this.isPlainUrlServices) {

                if (!sendAsRowData) {
                    if (!writeData(httpURLConnection)) {
                        ERROR_MESSAGE = CONNECTION_ERROR;
                        return false;
                    }
                } else {
                    if(!writeRawData(httpURLConnection)){
                        ERROR_MESSAGE = CONNECTION_ERROR;
                        return false;
                    }
                }

            }

            /**
             * nos conectamos
             */
            httpURLConnection.connect();

            /**
             * Obtenemos el status de la conexion
             */
            int status = httpURLConnection.getResponseCode();

            switch (status) {

                /**
                 * Si el status es 200 o 201 que indican que everything salio bien procesamos la respuesta
                 */
                case 200:
                case 201: {
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    stringRes = sb.toString();
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                    break;
                }
                default: {
                    /**
                     * De lo contrario mostramos mensaje de error
                     */
                    ERROR_MESSAGE = CONNECTION_ERROR;
                    return false;
                }
            }


        } catch (Exception ex) {
            Log.i("Debug", "error: " + ex.getMessage(), ex);
            ERROR_MESSAGE = UNKNOW_ERROR + "; " + ex;
            return false;
        }


        return true;
    }

    private boolean writeRawData(HttpURLConnection httpURLConnection) {

        try {
            JSONObject jsonParam = new JSONObject();
            for (CoupleParams coupleParams : this.paramsList) {
                jsonParam.put(coupleParams.getKey(), coupleParams.getParam());
            }
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(dataOutputStream, this.encodingType));
            writer.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));


            writer.flush();
            writer.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    private boolean writeData(HttpURLConnection httpURLConnection) {

        try {
            OutputStream outputStream = null;
            outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(outputStream, this.encodingType));

            writer.write(Utils.organizePostServicesParametres(this.paramsList));

            writer.flush();
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;


    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean)
            httpServicesCallback.successFullResponse(stringRes);
        else
            httpServicesCallback.errorResponse(ERROR_MESSAGE, errorJsonObject);


    }


}
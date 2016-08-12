package com.rm.androidesentials.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.webkit.URLUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by oscargallon on 18/05/15.
 */
public class Validation {

    /**
     * Expresion regular para validar emails
     */
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Expresion regular para validar latitudes(coordenadas)
     */
    private static final String LAT_PATTERN = "(-?[0-8]?[0-9](\\.\\d*)?)|-?90(\\.[0]*)?";

    /**
     * Expresion regular para validar longitudes(coordenadas)
     */
    private static final String LNG_PATTERN = "(-?([1]?[0-7][1-9]|[1-9]?[0-9])?(\\.\\d*)?)|-?180(\\.[0]*)?";

    /**
     * Validar que una url tenga un formato adecuado
     *
     * @param urlString String con la url a validar
     * @return verdadero de ser una url valida, falso de lo contrario
     */
    public static boolean validateURl(String urlString) {
        return (!TextUtils.isEmpty(urlString) || URLUtil.isValidUrl(urlString));
    }

    /**
     * Metodo que valida si una fecha tiene el formato correcto
     *
     * @param fecha  fecha a validar
     * @param format formato de la fecha a validar ej "yyyy/mm/dd", "yyyy-MM-dd HH:mm:ss.SSSZ"
     * @return verdadero si es correcto falso de lo contrario
     */
    public static boolean validateDate(String fecha, String format) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat(format, Locale.getDefault());
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    /**
     * Metedo para validar un email
     *
     * @param str email a validar
     * @return verdadero si tiene forma de email, falso de lo contrario
     */
    public static boolean validateEmail(String str) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * Metodo para ver si dos cadenas de texto son iguales
     *
     * @param s1 primer string
     * @param s2 segundo string
     * @return verdadero si los string son iguales falso de lo contrario
     */
    public static boolean compareTwoStrings(String s1, String s2) {
        return (s1.equals(s2));
    }

    /**
     * Metodo para verificar si el tamano de una cadena es mayor que el parametro recibido
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean validateLengthGreaterThan(String str, int length) {
        if (validateStringEmpty(str) || str.length() < length) {
            return false;
        }

        return true;
    }

    /**
     * Metodo que valida un string que no puede ser vacio
     *
     * @param str string a validar
     * @return
     */
    public static boolean validateStringEmpty(String str) {
        return (isNull(str) || str.isEmpty());

    }

    /**
     * Metodo para validar si el string recibido tiene un formato de latitud correcto
     *
     * @param str
     * @return
     */
    public static boolean validateLat(String str) {
        Pattern pattern = Pattern.compile(LAT_PATTERN);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * Metodo para validar si el string recibido tiene un formato de longitud(coordenadas) correcto
     *
     * @param str
     * @return
     */
    public static boolean validateLng(String str) {
        Pattern pattern = Pattern.compile(LNG_PATTERN);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * Metodo que valida un String es nulo
     *
     * @param str string a validar
     * @return
     */
    public static boolean isNull(String str) {
        return (str == null);
    }

    /**
     * Valida si un string tiene solo numeros
     *
     * @param str string a validar
     * @return verdadero si la cadena de texto son solo numero
     * falso de lo contrario
     */
    public static boolean validateStringWithNumbers(String str) {
        if (str.isEmpty() || isNull(str)) {
            return false;
        }
        return TextUtils.isDigitsOnly(str);
    }

    public static boolean checkConnection(Activity activity) {
        ConnectivityManager conMgr = (ConnectivityManager)
                activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null || !i.isConnected() || !i.isAvailable()){
            return false;
        }
        return true;

    }


}

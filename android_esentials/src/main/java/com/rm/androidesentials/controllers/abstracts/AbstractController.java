package com.rm.androidesentials.controllers.abstracts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;

import com.rm.androidesentials.R;
import com.rm.androidesentials.model.utils.CoupleParams;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ogallonr on 05/04/2016.
 */
public class AbstractController {
    /**
     * Actividad a a la cual pertenece el controlador
     */
    private Activity activity;

    /**
     * Barra de progreso
     */
    private ProgressDialog progressDialog;

    /**
     * Fragmento al cual pertenece el controlador
     */
    private Fragment fragment;


    /**
     * Contructor de la clase
     *
     * @param activity actividad a la cual pertenece el controlador
     */
    public AbstractController(Activity activity) {
        super();
        this.activity = activity;

    }

    public void showProgressDialog(String title, String message, boolean cancelable) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(cancelable);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();

    }

    /**
     * Metodo que muestra un dialogo de progreso
     *
     * @param title   titulo del dialogo
     * @param message mensaje del dialogo
     */
    public void showProgressDialog(String title, String message) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();

    }

    /**
     * Metodo que oculta un dialogo de progreso
     */
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }


    public void changeActivity(Class<?> destinyClass) {
        Intent i = new Intent(getActivity(), destinyClass);
        getActivity().startActivity(i);

    }

    /**
     * Metodo que cambia de actividad
     *
     * @param destinyClass        clase da la actividad de destino
     * @param couplePostParamList parametros a enviar
     */
    public void changeActivity(Class<?> destinyClass, List<CoupleParams> couplePostParamList) {
        Intent i = new Intent(getActivity(), destinyClass);
        for (CoupleParams couplePostParam : couplePostParamList) {
            if (couplePostParam.getObject() == null) {
                i.putExtra(couplePostParam.getKey(), couplePostParam.getParam());
            } else {
                i.putExtra(couplePostParam.getKey(), (Serializable) couplePostParam.getObject());
            }

        }
        getActivity().startActivity(i);
    }


    public void showConfirmationWearActivity(String message, boolean success){
        Intent intent = new Intent(getActivity(), ConfirmationActivity.class);
        if(success){
            intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                    ConfirmationActivity.SUCCESS_ANIMATION);
        }else{
            intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                    ConfirmationActivity.FAILURE_ANIMATION);


        }

        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE
                ,message);
        getActivity().startActivity(intent);
    }

    public void showAlertDialog(String title, String message) {
        /**
         * Creamos el dialogo
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        /**
         * Le asignamos el titulo
         */
        builder.setTitle(title);

        /**
         * Le asignamos el mensaje
         */
        builder.setMessage(message);

        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        /**
         * Mostramos el dialogo
         */
        AlertDialog dialog = builder.show();
        dialog.show();

    }

    public void showAlertDialog(String title, String message,
                                DialogInterface.OnClickListener onCLickListenerPositiveButton,
                                String postiveButtonTitle) {

        /**
         * Creamos el dialogo
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        /**
         * Le asignamos el titulo
         */
        builder.setTitle(title);

        /**
         * Le asignamos el mensaje
         */
        builder.setMessage(message);

        builder.setPositiveButton(postiveButtonTitle, onCLickListenerPositiveButton);


        /**
         * Mostramos el dialogo
         */
        AlertDialog dialog = builder.show();
        dialog.show();

    }

    /**
     * Metodo que muestra un dialog de texto
     *
     * @param title                         titulo del dialogo
     * @param message                       mensaje del dialogo
     * @param onCLickListenerPositiveButton accion del boton 1
     * @param onCLickListenerNegativeButton accion del boton 2
     * @param positiveButtonTitle           titulo boton 1
     * @param negativeButtonTitle           titulo boton 2
     */
    public void showAlertDialog(String title, String message,
                                DialogInterface.OnClickListener onCLickListenerPositiveButton,
                                DialogInterface.OnClickListener onCLickListenerNegativeButton,
                                String positiveButtonTitle, String negativeButtonTitle) {
        /**
         * Creamos el dialogo
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        /**
         * Le asignamos el titulo
         */
        builder.setTitle(title);

        /**
         * Le asignamos el mensaje
         */
        builder.setMessage(message);


        builder.setPositiveButton(positiveButtonTitle,
                onCLickListenerPositiveButton);

        if (negativeButtonTitle == null || onCLickListenerNegativeButton == null) {
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else {
            builder.setNegativeButton(negativeButtonTitle, onCLickListenerNegativeButton);
        }


        /**
         * Mostramos el dialogo
         */
        AlertDialog dialog = builder.show();
        dialog.show();

    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }


}

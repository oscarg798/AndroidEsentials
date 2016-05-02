package com.rm.androidesentials.services.interfaces;

import org.json.JSONObject;

/**
 * Created by oscargallon on 4/4/16.
 */
public interface IHTTPServices {

    void successFullResponse(String response);

    void errorResponse(String message, JSONObject jsonObject);
}

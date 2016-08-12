package com.rm.androidesentials.model.utils;

import java.io.Serializable;

/**
 * Created by oscargallon on 4/4/16.
 */
public class CoupleParams implements Serializable{

    private final String key;
    private final String param;

    private final Serializable object;

    private CoupleParams(String key, String param, Serializable object) {
        this.key = key;
        this.param = param;
        this.object = object;
    }

    public String getKey() {
        return key;
    }


    public String getParam() {
        return param;
    }


    public Object getObject() {
        return object;
    }

    public static class CoupleParamBuilder {

        private String nestedKey;
        private String nestedParam;
        private Serializable nestedObject;

        public CoupleParamBuilder(String nestedKey) {
            this.nestedKey = nestedKey;
        }

        public CoupleParamBuilder nestedParam(String nestedParam) {
            this.nestedParam = nestedParam;
            return this;
        }

        public CoupleParamBuilder nestedObject(Serializable nestedObject) {
            this.nestedObject = nestedObject;
            return this;
        }

        public CoupleParams createCoupleParam() {
            return new CoupleParams(this.nestedKey,
                    this.nestedParam, this.nestedObject);
        }

    }


}

package com.github.Icear.Network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by icear on 2017/10/8.
 */

public class BasicNameValuePair implements NameValuePair{
    private String name;
    private String value;

    public BasicNameValuePair(@NonNull String name, @Nullable String value){
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

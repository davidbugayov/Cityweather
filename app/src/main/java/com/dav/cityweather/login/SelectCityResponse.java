package com.dav.cityweather.login;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by dav on 09.04.17.
 */

public class SelectCityResponse {

    private ArrayList<String> description;

    @JsonGetter("description")
    public ArrayList<String>  getDescription() {
        return description;
    }

    @JsonSetter("description")
    public void setDescription(ArrayList<String>  description) {
        this.description = description;
    }
}

package com.example.lab3_20200403.Dto;

import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("Source")
    private String source;

    @SerializedName("Value")
    private String value;

    public Rating(String source, String value) {
        this.source = source;
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

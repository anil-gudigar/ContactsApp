package com.mcma.app.custom;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anil on 2/6/2017.
 */

public class CustomResponse {
    @SerializedName("status")
    public String status;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @SuppressWarnings({"unused", "used by Retrofit"})
    public CustomResponse() {
    }

    public CustomResponse(String status) {
        this.status = status;
    }
}

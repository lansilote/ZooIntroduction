package com.example.zoointroduction;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class Results {
    public long _id;
    public int E_no;
    public String E_Geo;
    public String E_Info;
    public String E_Category;
    public String E_Memo;
    public String E_Pic_URL;
    public String E_Name;
    public String E_URL;

    public String F_Name_Ch;
    public String F_AlsoKnown;
    public String F_Name_En;
    public String F_Brief;
    public String F_Feature;
    public String F_Pic01_URL;
    @SerializedName("F_Function&Application")
    public String F_A;
    public String F_Update;
}

class Result {
    public List<Results> results;
    public int count;
    public int limit;
    public int offset;
    public boolean sort;
}

public class ZooApiResponse {
    public Result result;
}

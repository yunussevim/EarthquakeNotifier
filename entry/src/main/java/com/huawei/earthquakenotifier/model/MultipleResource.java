package com.huawei.earthquakenotifier.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultipleResource {
    @SerializedName("bbox")
    public List<Double> bbox;
    @SerializedName("features")
    public List<Feature> features;
    @SerializedName("metadata")
    public Metadata metadata;
    @SerializedName("type")
    public String type;

    public class Feature{
        @SerializedName("geometry")
        public Geometry geometry;
        @SerializedName("id")
        public String id;
        @SerializedName("properties")
        public Properties properties;
        @SerializedName("type")
        public String type;
    }

    public class Metadata{
        @SerializedName("api")
        public String api;
        @SerializedName("count")
        public int count;
        @SerializedName("generated")
        public long generated;
        @SerializedName("status")
        public int status;
        @SerializedName("title")
        public String title;
        @SerializedName("url")
        public String url;
    }

    public class Geometry{
        @SerializedName("coordinates")
        public List<Double> coordinates;
        @SerializedName("type")
        public String type;
    }

    public class Properties{
        @SerializedName("alert")
        public Object alert;
        @SerializedName("cdi")
        public Object cdi;
        @SerializedName("code")
        public String code;
        @SerializedName("detail")
        public String detail;
        @SerializedName("dmin")
        public double dmin;
        @SerializedName("felt")
        public Object felt;
        @SerializedName("gap")
        public double gap;
        @SerializedName("ids")
        public String ids;
        @SerializedName("mag")
        public double mag;
        @SerializedName("magType")
        public String magType;
        @SerializedName("mmi")
        public Object mmi;
        @SerializedName("net")
        public String net;
        @SerializedName("nst")
        public int nst;
        @SerializedName("place")
        public String place;
        @SerializedName("rms")
        public double rms;
        @SerializedName("sig")
        public int sig;
        @SerializedName("sources")
        public String sources;
        @SerializedName("status")
        public String status;
        @SerializedName("time")
        public long time;
        @SerializedName("title")
        public String title;
        @SerializedName("tsunami")
        public int tsunami;
        @SerializedName("type")
        public String type;
        @SerializedName("types")
        public String types;
        @SerializedName("tz")
        public Object tz;
        @SerializedName("updated")
        public long updated;
        @SerializedName("url")
        public String url;
    }
}

package com.veeritsolutions.uhelpme.api;

import android.content.Context;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.veeritsolutions.uhelpme.MyApplication;
import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.customdialog.CustomDialog;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by ${hitesh} on 12/6/2016.
 */
public class RestClient {

    private static RestClient ourInstance = new RestClient();

    private RestClient() {
    }

    public static RestClient getInstance() {
        return ourInstance;
    }

    public static Gson getGsonInstance() {

        return new GsonBuilder().setPrettyPrinting().create();
    }

    private static String getAbsoluteUrl(String url) {
        return ServerConfig.SERVER_LIVE_URL + url;
    }

    public void post(Context mContext, int mRequestMethod, /*final Map<String, String> mapParams,*/
                     JSONObject mPostParams, String url, boolean isDialogRequired, final RequestCode mRequestCode,
                     final DataObserver dataObserver) {

        try {
            if (Utils.isInternetAvailable()) {


                if (isDialogRequired) {
                    CustomDialog.getInstance().showProgress(mContext, mContext.getString(R.string.str_please_wait), false);
                }

                String postUrl = getAbsoluteUrl(url);

                Debug.trace("postUrl", postUrl);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(mRequestMethod, postUrl, mPostParams, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


                JsonRequest mPostRequest = new JsonRequest(mRequestMethod, postUrl, mPostParams.toString(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                CustomDialog.getInstance().dismiss();

                                verifyResponse(response, mRequestCode, dataObserver);
                                //checkResponse(response, dataObserver, mRequestCode);
                                //  Object object = ResponseManager.parseResponse(response.toString(), mRequestCode, getGsonInstance());
                                //  dataObserver.onSuccess(mRequestCode, object);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        CustomDialog.getInstance().dismiss();

                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Internet Connection is too slow! Please check your internet connection.";
                        }

                        dataObserver.onFailure(mRequestCode, message);

                    }
                }) {
                    @Override
                    protected Response parseNetworkResponse(NetworkResponse networkResponse) {

                        try {
                            String jsonString = new String(networkResponse.data,
                                    HttpHeaderParser
                                            .parseCharset(networkResponse.headers));
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser
                                            .parseCacheHeaders(networkResponse));
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }
                        // return null;
                    }
                };

                MyApplication.getInstance().addToRequestQueue(mPostRequest, mContext.getClass().getSimpleName());

            } else {

                CustomDialog.getInstance().showAlert(mContext, mContext.getString(R.string.str_no_internet_connection_available), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void verifyResponse(JSONObject response, RequestCode mRequestCode, DataObserver dataObserver) {

        ResponseStatus responseStatus = getGsonInstance().fromJson(response.toString(), ResponseStatus.class);

        if (responseStatus.isIsError()) {
            dataObserver.onFailure(mRequestCode, responseStatus.getError());
        } else {
            Object object = ResponseManager.parseResponse(response.toString(), mRequestCode, getGsonInstance());
            dataObserver.onSuccess(mRequestCode, object);
        }
    }

    private void checkResponse(JSONArray response, DataObserver dataObserver, RequestCode mRequestCode) {
        Object object;
        /*try {
            if (response != null && response.length() != 0) {

                JSONObject jsonObject = response.getJSONObject(0);
                if (jsonObject.has("DataId")) {
                    if (jsonObject.getInt("DataId") == 0) {
                        object = getGsonInstance().fromJson(response.getJSONObject(0).toString(), ErrorModel.class);
                        ErrorModel errorModel = (ErrorModel) object;

                        if (errorModel.getErrorNumber() == Constants.NO_DATA_FOUND) {
                            dataObserver.onSuccess(mRequestCode, object);
                        } else {
                            dataObserver.onFailure(mRequestCode, ((ErrorModel) object).getError());
                        }

                    } else {
                        object = ResponseManager.parseResponse(response.toString(), mRequestCode, getGsonInstance());
                        dataObserver.onSuccess(mRequestCode, object);
                    }
                } else {
                    object = ResponseManager.parseResponse(response.toString(), mRequestCode, getGsonInstance());
                    dataObserver.onSuccess(mRequestCode, object);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    public void post(Context mContext, int mRequestMethod, final Map<String, String> mapParams, /*JSONObject mPostParams,*/ String url, boolean isDialogRequired, final RequestCode mRequestCode, final DataObserver dataObserver) {

        try {
            if (Utils.isInternetAvailable()) {

                if (isDialogRequired) {
                    CustomDialog.getInstance().showProgress(mContext, mContext.getString(R.string.str_please_wait), false);
                }

                String postUrl = getAbsoluteUrl(url);

                Debug.trace("postUrl", postUrl);

                StringRequest mPostRequest = new StringRequest(mRequestMethod, postUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        CustomDialog.getInstance().dismiss();

                        try {
                            verifyResponse(new JSONObject(response), mRequestCode, dataObserver);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Object object = ResponseManager.parseResponse(response, mRequestCode, getGsonInstance());

                        //dataObserver.onSuccess(mRequestCode, object);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        CustomDialog.getInstance().dismiss();
                        String message = null;
                        if (volleyError instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (volleyError instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (volleyError instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (volleyError instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        dataObserver.onFailure(mRequestCode, message);
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        return mapParams;
                    }


                };

                MyApplication.getInstance().addToRequestQueue(mPostRequest, mContext.getClass().getSimpleName());

            } else {

                CustomDialog.getInstance().showAlert(mContext, mContext.getString(R.string.str_no_internet_connection_available), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

package com.example.zoointroduction;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSource extends PageKeyedDataSource<Integer, Results> {

    public static final int LIMIT = 10;
    private static final int OFFSET = 0;
    private static String RID = "5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a";
    private static String queryString = "";

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Results> callback) {
        RetrofitClient.getInstance()
            .getApi().getAnswers(OFFSET, LIMIT, RID, queryString)
            .enqueue(new Callback<ZooApiResponse>() {
                @Override
                public void onResponse(Call<ZooApiResponse> call, Response<ZooApiResponse> response) {
                    if (response.body() != null) {
                        callback.onResult(response.body().result.results, null, LIMIT);
                    }
                }

                @Override
                public void onFailure(Call<ZooApiResponse> call, Throwable t) {
                    Log.w("Lansilote", "loadInitial onFailure:" + t.toString());
                }
            });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Results> callback) {
        RetrofitClient.getInstance()
            .getApi().getAnswers(params.key, LIMIT, RID, queryString)
            .enqueue(new Callback<ZooApiResponse>() {
                @Override
                public void onResponse(Call<ZooApiResponse> call, Response<ZooApiResponse> response) {
                    Integer adjacentKey = (params.key > LIMIT) ? params.key - LIMIT : null;
                    if (response.body() != null) {
                        callback.onResult(response.body().result.results, adjacentKey);
                    }
                }

                @Override
                public void onFailure(Call<ZooApiResponse> call, Throwable t) {
                    Log.w("Lansilote", "loadBefore onFailure:" + t.toString());
                }
            });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Results> callback) {
        RetrofitClient.getInstance()
            .getApi()
            .getAnswers(params.key, LIMIT, RID, queryString)
            .enqueue(new Callback<ZooApiResponse>() {
                @Override
                public void onResponse(Call<ZooApiResponse> call, Response<ZooApiResponse> response) {
                    if (response.body() != null) {
                        callback.onResult(response.body().result.results, params.key + LIMIT);
                    }
                }

                @Override
                public void onFailure(Call<ZooApiResponse> call, Throwable t) {
                    Log.w("Lansilote", "loadAfter onFailure:" + t.toString());
                }
            });
    }
}

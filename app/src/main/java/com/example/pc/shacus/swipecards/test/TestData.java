package com.example.pc.shacus.swipecards.test;

import android.content.Context;


import com.example.pc.shacus.swipecards.util.BaseModel;
import com.example.pc.shacus.swipecards.util.CardEntity;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.example.pc.shacus.R;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestData {

    public static ArrayList<CardEntity> getApiData(Context context) {
        BaseModel<ArrayList<CardEntity>> model = null;
        try {
            model = new GsonBuilder().create().fromJson(
                    new InputStreamReader(context.getAssets().open("test.json")),
                    new TypeToken<BaseModel<ArrayList<CardEntity>>>() {}.getType()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return model != null ? model.results : null;
    }

}

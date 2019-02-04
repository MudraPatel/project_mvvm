package com.assignment.example.project.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.assignment.example.project.BR;
import com.assignment.example.project.adapter.DataAdapter;
import com.assignment.example.project.model.DataModel;
import com.assignment.example.project.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataViewModel extends BaseObservable {
    private static final String TAG = "DataViewModel";
    private DataAdapter adapter;
    private List<DataModel> data;
    String[] qualificationList = {"Computer Engineering", "Mechnical Engineering", "Balechor of Science", "Master-Computer/IT", "Master-Commerce"};


    public DataViewModel(Context context) {
        data = new ArrayList<>();
        adapter = new DataAdapter(context);
    }

    public void setUp(String response) {
        // perform set up tasks, such as adding listeners, data population, etc.
        populateData(response);
    }

    public void tearDown() {
        // perform tear down tasks, such as removing listeners
    }

    @Bindable
    public List<DataModel> getData() {
        return this.data;
    }

    @Bindable
    public DataAdapter getAdapter() {
        return this.adapter;
    }

    private void populateData(String response) {
        // populate the data from the source, such as the database.
        String name , address, qualification= "", id, profileUrl;
        try {
            JSONArray jsonArray = new JSONArray(response);
            int k = 0;
            for(int i = 0 ; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject jsonObjectName = jsonObject.getJSONObject("name");
                name = jsonObjectName.getString("title") + "\t" + jsonObjectName.getString("first") + "\t" + jsonObjectName.getString("last");

                JSONObject jsonObjectAddress = jsonObject.getJSONObject("location");
                String street = jsonObjectAddress.getString("street");
                address =  jsonObjectAddress.getString("city") +
                        "\t" + jsonObjectAddress.getString("state");

                JSONObject jsonObjectLogin = jsonObject.getJSONObject("login");
                id = jsonObjectLogin.getString("uuid");

                JSONObject jsonObjectImages = jsonObject.getJSONObject("picture");
                profileUrl = jsonObjectImages.getString("medium");
                if (k >= qualificationList.length)
                    k = 0;

                qualification = qualificationList[k++];

                DataModel model = new DataModel(id, name, address, qualification, profileUrl, Network.not , true);
                data.add(model);
                            /*realm.beginTransaction();
                            realm.copyToRealmOrUpdate(model);
                            realm.commitTransaction();*/

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyPropertyChanged(BR.data);
    }


}

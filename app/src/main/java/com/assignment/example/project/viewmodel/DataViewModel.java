package com.assignment.example.project.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.assignment.example.project.BR;
import com.assignment.example.project.adapter.DataAdapter;
import com.assignment.example.project.model.DataModel;
import com.assignment.example.project.network.Network;
import com.assignment.example.project.network.RealmController;
import com.assignment.example.project.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DataViewModel extends BaseObservable {
    private static final String TAG = "DataViewModel";
    private DataAdapter adapter;
    private List<DataModel> data;
    String[] qualificationList = {"Computer Engineering", "Mechnical Engineering", "Balechor of Science", "Master-Computer/IT", "Master-Commerce"};
    private boolean floatingStatus = false;

    public DataViewModel(Context context, boolean selectedUserStatus) {
        data = new ArrayList<>();
        adapter = new DataAdapter(context, selectedUserStatus);
        this.floatingStatus = selectedUserStatus;
    }

    public void setUp(String response) {
        // perform set up tasks, such as adding listeners, data population, etc.
        populateData(response);
        floatingStatus = false;
    }

    public void setUpSelectedUser() {
        // perform set up tasks, such as adding listeners, data population, etc.
        RealmResults<DataModel> realm = RealmController.getInstance().getDataListSelected();
        data.addAll(realm);
        floatingStatus = true;
        notifyPropertyChanged(BR.data);

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


    public int statusVisibility(){
        return floatingStatus? View.GONE : View.VISIBLE;
        //dataModel.isStatus() ? View.GONE : View.VISIBLE;
    }

    private void populateData(String response) {
        // populate the data from the source, such as the database.
        String name , address, qualification= "", id, profileUrl;
        Realm realm = RealmController.getInstance().getRealm();

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

                DataModel model = new DataModel(id, name, address, qualification, profileUrl, Network.not , false, false);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(model);
                realm.commitTransaction();
                data.add(model);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyPropertyChanged(BR.data);
    }

    public void onClickProceed(View view){
        Context context = view.getContext();
        Log.e("Data sixe", data.size() + "SIZE");
        for(int i = 0 ; i<data.size() ; i++){

            if(data.get(i).isCheckBoxStatus()){
                Intent intent  = new Intent(context, MainActivity.class);
                intent.putExtra("selectedUserStatus", true);
                context.startActivity(intent);
                break;
            }

            if(i == data.size()-1 && !data.get(i).isCheckBoxStatus()){
                Toast.makeText(context, "Atleast Select one", Toast.LENGTH_SHORT).show();

            }
        }

    }

}

package com.assignment.example.project.network;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.util.Log;

import com.assignment.example.project.model.DataModel;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    public RealmResults<DataModel> getDataListSelected() {

        return realm.where(DataModel.class).equalTo("checkBoxStatus", true).findAll();
    }

    public DataModel updateCheckBoxStatus(String id, boolean status){
        DataModel model = realm.where(DataModel.class).equalTo("uuid", id).findFirst();
        Log.e("REalm","Realm"+status);
        if(model != null) {
            realm.beginTransaction();
            model.setCheckBoxStatus(status);
            Log.e("INSIDE","SIDND"+status);
            realm.commitTransaction();

            return model;
        }
        return null;
    }
}

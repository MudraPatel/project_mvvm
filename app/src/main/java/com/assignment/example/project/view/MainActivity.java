/*
 * Copyright (c) 2018 Phunware Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.assignment.example.project.view;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.assignment.example.project.R;
import com.assignment.example.project.databinding.ActivityMainBinding;
import com.assignment.example.project.model.DataModel;
import com.assignment.example.project.network.DataCallback;
import com.assignment.example.project.network.Network;
import com.assignment.example.project.network.RealmController;
import com.assignment.example.project.viewmodel.DataViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;


public class MainActivity extends AppCompatActivity {
    private DataViewModel dataViewModel;
    Realm realm;
    boolean selectedUserStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().hasExtra("selectedUserStatus")) {
            selectedUserStatus = getIntent().getExtras().getBoolean("selectedUserStatus");
        }else{
            selectedUserStatus = false;
        }
        View view = bind();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();

        initRecyclerView(view);
        if(!selectedUserStatus) {
            getDataList();
        }else{
            getDataListSelectedUser();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ON RESUME", "ON RESUME" );

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ON Pause", "ON pause");
        dataViewModel.tearDown();
    }

    private View bind() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dataViewModel = new DataViewModel(this, selectedUserStatus);
        binding.setViewModel(dataViewModel);
        return binding.getRoot();
    }

    private void initRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.data_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), VERTICAL));
    }

    public void getDataListSelectedUser(){
        dataViewModel.setUpSelectedUser();
    }

    public void getDataList(){
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getString(R.string.loading));
        progress.setMessage(getString(R.string.loading));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.setCancelable(false);
        progress.show();

        if(Network.isInternetAvailable(this)){
            progress.dismiss();
            Network.getListData(MainActivity.this, new DataCallback() {
                @Override
                public void onSuccess(String response) {
                    Log.e("STring", response + "**");
                    dataViewModel.setUp(response);

                }
            });
        }else{
            progress.dismiss();
            Toast.makeText(MainActivity.this, "Network Not Available", Toast.LENGTH_SHORT).show();

        }
    }

}

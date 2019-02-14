package com.assignment.example.project.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.assignment.example.project.BR;
import com.assignment.example.project.R;
import com.assignment.example.project.model.DataModel;
import com.assignment.example.project.network.Network;
import com.assignment.example.project.network.RealmController;
import com.assignment.example.project.view.MainActivity;
import com.squareup.picasso.Picasso;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressImageButton;
import io.realm.Realm;


public class DataItemViewModel extends BaseObservable {
    private DataModel dataModel;
    private Context context;
    private boolean selectedUserStatus;


    public DataItemViewModel( Context context, DataModel dataModel, boolean selectedUserStatus) {
        this.context = context;
        this.dataModel = dataModel;
        this.selectedUserStatus = selectedUserStatus;
    }

    public void setUp() {
        // perform set up tasks, such as adding listeners
    }

    public void tearDown() {
        // perform tear down tasks, such as removing listeners
    }

//    @Bindable
//    public String getTitle() {
//        return !TextUtils.isEmpty(dataModel.getTitle()) ? dataModel.getTitle() : "";
//    }

    @Bindable
    public String getName() {
        return !TextUtils.isEmpty(dataModel.getName()) ? dataModel.getName() : "";
    }

    @Bindable
    public String getAddress() {
        return !TextUtils.isEmpty(dataModel.getAddress()) ? dataModel.getAddress() : "";
    }

    @Bindable
    public String getQualification() {
        return !TextUtils.isEmpty(dataModel.getQualification()) ? dataModel.getQualification() : "";
    }

    @Bindable
    public String getMessage() {
        return !TextUtils.isEmpty(dataModel.getMessage()) ? dataModel.getMessage() : "";
    }

    public String getImageUrl(){
        return dataModel.getImageUrl();
    }

    @Bindable
    public boolean getStatus(){
        return dataModel.isCheckBoxStatus();
    }


    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .into(view);
    }

    public void onClickCancel(View view){

        final CircularProgressImageButton imageView = (CircularProgressImageButton) view;
        final Drawable background = context.getResources().getDrawable(R.drawable.bg_rounded_shape_red);
        final int icon = R.drawable.ic_cancel;

        Handler handler = new Handler();
        imageView.startAnimation();
        imageView.setBackground(background);
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                imageView.setBackground(background);
                imageView.doneLoadingAnimation(R.color.colorAccent,
                        BitmapFactory.decodeResource(context.getResources(), icon));
            }
        };

        handler.postDelayed(runnable, 2500);

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                imageView.revertAnimation();
                dataModel.setStatus(false);
                dataModel.setMessage(Network.decline);
                notifyPropertyChanged(BR._all);


            }
        };
        handler.postDelayed(runnable1,5000);

//        statusVisibility();
//        actionButtonVisibility();
        notifyPropertyChanged(BR._all);

    }

    public void onClickAccept(View view){
        final CircularProgressImageButton imageView = (CircularProgressImageButton) view;
        final Drawable background = context.getResources().getDrawable(R.drawable.bg_rounded_shape_green);
        final int icon = R.drawable.ic_accept;

        Handler handler = new Handler();
        imageView.startAnimation();
        imageView.setBackground(background);
        imageView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                imageView.setBackground(background);
                imageView.doneLoadingAnimation(R.color.colorAccent,
                        BitmapFactory.decodeResource(context.getResources(), icon));
            }
        };

        handler.postDelayed(runnable, 2500);

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                imageView.revertAnimation();
                dataModel.setStatus(false);
                dataModel.setMessage(Network.accept);
//                statusVisibility();
//                actionButtonVisibility();
                notifyPropertyChanged(BR._all);

            }
        };
        handler.postDelayed(runnable1,5000);

        notifyPropertyChanged(BR._all);


    }

    public int statusVisibility(){
        return View.GONE;
        //dataModel.isStatus() ? View.GONE : View.VISIBLE;
    }

    public int checkBoxVisibility(){
        Log.e("CHECK", selectedUserStatus + ":::");
        return selectedUserStatus ? View.GONE : View.VISIBLE;
    }
    public int actionButtonVisibility(){
        return dataModel.isStatus() ? View.VISIBLE : View.GONE;
    }

    public void onClickCheckBox(CompoundButton compoundButton, boolean checked) {
        dataModel.setCheckBoxStatus(checked);
         RealmController.getInstance().updateCheckBoxStatus(dataModel.getUuid(), checked);
        notifyPropertyChanged(BR.data);

    }
}

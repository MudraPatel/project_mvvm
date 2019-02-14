package com.assignment.example.project.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assignment.example.project.R;
import com.assignment.example.project.model.DataModel;
import com.assignment.example.project.viewmodel.DataItemViewModel;
import com.assignment.example.project.databinding.ItemDataBinding;

import java.util.ArrayList;
import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressImageButton;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private static final String TAG = "DataAdapter";
    private List<DataModel> data;
    private String message = "";
    private Context context;
    private boolean selectedUserStatus;
    public DataAdapter(Context context, boolean selectedUserStatus) {
        this.context = context;
        this.data = new ArrayList<>();
        this.selectedUserStatus = selectedUserStatus;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data,
                new FrameLayout(parent.getContext()), false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, int position) {
        final DataModel dataModel = data.get(position);
        holder.setViewModel(new DataItemViewModel(context, dataModel, selectedUserStatus));

        if(!dataModel.isStatus()){
            holder.displayMessage.setVisibility(View.VISIBLE);
            holder.relativeLayout.setVisibility(View.GONE);
            holder.displayMessage.setText(dataModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    @Override
    public void onViewAttachedToWindow(DataViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.bind();
    }

    @Override
    public void onViewDetachedFromWindow(DataViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.unbind();
    }

    public void updateData(@Nullable List<DataModel> data) {
        if (data == null || data.isEmpty()) {
            this.data.clear();
        } else {
            this.data.addAll(data);
        }
        notifyDataSetChanged();
    }

    /* package */ static class DataViewHolder extends RecyclerView.ViewHolder {
        /* package */ ItemDataBinding binding;
//        CircularProgressImageButton btn, btn1;
        TextView displayMessage;
        RelativeLayout relativeLayout;


        /* package */ DataViewHolder(View itemView) {
            super(itemView);
//            btn = itemView.findViewById(R.id.img_cancel);
//            btn1 = itemView.findViewById(R.id.img_accept);
            relativeLayout = itemView.findViewById(R.id.action_button);
            displayMessage = itemView.findViewById(R.id.display_text);
            bind();

        }

        /* package */ void bind() {
            if (binding == null) {
                binding = DataBindingUtil.bind(itemView);
            }
        }

        /* package */ void unbind() {
            if (binding != null) {
                binding.unbind(); // Don't forget to unbind
            }
        }

        /* package */ void setViewModel(DataItemViewModel viewModel) {
            if (binding != null) {
                binding.setViewModel(viewModel);
            }
        }
    }
}

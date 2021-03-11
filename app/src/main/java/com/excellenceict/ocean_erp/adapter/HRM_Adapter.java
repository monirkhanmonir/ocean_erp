package com.excellenceict.ocean_erp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excellenceict.ocean_erp.Model.ChartOfAccountFirstView_model;
import com.excellenceict.ocean_erp.Model.MyLeave_Model;
import com.excellenceict.ocean_erp.R;

import java.util.List;

public class HRM_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String listItems[];
    private int imageItems[];
    private SelectedItems selectedItems;

    private Context context;

    public HRM_Adapter(String[] mTitle, int[] imageItem,SelectedItems selectedItems) {
        this.context = context;
        this.listItems =mTitle;
        this.imageItems =imageItem;
        this.selectedItems =selectedItems;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new HRMViewHolder(LayoutInflater.from(context).inflate(R.layout.hrm_listview, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        MyLeave_Model model = listItems.get(position);
        HRMViewHolder viewHolder = (HRMViewHolder) holder;

//        viewHolder.id.setText(model.getId());
        viewHolder.name.setText(listItems[position]);
        viewHolder.imageView.setImageResource(imageItems[position]);

    }

    @Override
    public int getItemCount() {
        return listItems.length;
    }
    private class HRMViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView imageView;
        private LinearLayout linearLayout;

        public HRMViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.hrm_textView);
            imageView = itemView.findViewById(R.id.hrmImg_icon);

//            linearLayout = itemView.findViewById(R.id.myleaveLog_row_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItems.getSelectedItems(getAdapterPosition());

                }
            });

        }


    }
    public interface SelectedItems {
        void getSelectedItems(int position);
    }

}

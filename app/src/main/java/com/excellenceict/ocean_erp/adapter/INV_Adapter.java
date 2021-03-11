package com.excellenceict.ocean_erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excellenceict.ocean_erp.R;

public class INV_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private String listItems[];
    private int imageItems[];
    private SelectedItems selectedItems;

    private Context context;

    public INV_Adapter(String[] listItems, int[] imageItems, SelectedItems selectedItems) {
        this.listItems = listItems;
        this.imageItems = imageItems;
        this.selectedItems = selectedItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new INVViewHolder(LayoutInflater.from(context).inflate(R.layout.inv_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        INVViewHolder viewHolder = (INVViewHolder) holder;

//        viewHolder.id.setText(model.getId());
        viewHolder.name.setText(listItems[position]);
        viewHolder.imageView.setImageResource(imageItems[position]);

    }

    @Override
    public int getItemCount() {
        return listItems.length;
    }
    private class INVViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView imageView;
        private LinearLayout linearLayout;

        public INVViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.inv_textView);
            imageView = itemView.findViewById(R.id.invImg_icon);

//            linearLayout = itemView.findViewById(R.id.myleaveLog_row_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItems.getSelectedItems(getAdapterPosition());
                }
            });

        }


    }
    public interface SelectedItems{
        void getSelectedItems(int position);
    }


}

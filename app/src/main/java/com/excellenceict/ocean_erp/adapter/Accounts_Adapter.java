package com.excellenceict.ocean_erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excellenceict.ocean_erp.Model.Leave_Log_Model;
import com.excellenceict.ocean_erp.Model.MyLeave_Model;
import com.excellenceict.ocean_erp.R;

import java.util.List;

public class Accounts_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private String title[];
    private int img[];
    private Context context;
    private SelecetedItems selecetedItems;

    public Accounts_Adapter(String[] title, int[] img, SelecetedItems selecetedItems) {
        this.title = title;
        this.img = img;
        this.selecetedItems = selecetedItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new AccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.inv_listview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AccountsViewHolder viewHolder = (AccountsViewHolder) holder;
        viewHolder.mytitle.setText(title[position]);
        viewHolder.images.setImageResource(img[position]);
    }

    @Override
    public int getItemCount() {
        return title.length;
    }
    private class AccountsViewHolder extends RecyclerView.ViewHolder {
        private TextView mytitle;
        private ImageView images;

        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.invImg_icon);

            mytitle =itemView.findViewById(R.id.inv_textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selecetedItems.selectItem(getAdapterPosition());

                }
            });
        }

    }
    public interface SelecetedItems{
        void selectItem(int position);
    }
}

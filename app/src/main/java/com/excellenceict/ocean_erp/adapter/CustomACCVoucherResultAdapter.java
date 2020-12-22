package com.excellenceict.ocean_erp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.excellenceict.ocean_erp.ACC_View_Voucher_Result_Entity;
import com.excellenceict.ocean_erp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomACCVoucherResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<ACC_View_Voucher_Result_Entity> voucherList;
    private List<ACC_View_Voucher_Result_Entity> voucherFilterList;
    private Context context;
    private SelectedVoucher selectedVoucher;

    public CustomACCVoucherResultAdapter(List<ACC_View_Voucher_Result_Entity> voucherList) {
        this.voucherList = voucherList;
       // this.selectedVoucher = selectedVoucher;
        this.voucherFilterList = voucherList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new VoucherViewHolder(LayoutInflater.from(context).inflate(R.layout.acc_view_voucher_result_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ACC_View_Voucher_Result_Entity voucherModel = voucherList.get(position);
        VoucherViewHolder  viewHolder = (VoucherViewHolder) holder;
        viewHolder.j_voucherRes_date.setText(": "+voucherModel.getDate());
        viewHolder.j_voucherRes_Amount.setText(": "+voucherModel.getAmount());
        viewHolder.j_voucherRes_ref.setText(": "+voucherModel.getRef());
        viewHolder.j_voucherRes_trType.setText(": "+voucherModel.getTr_type());
        viewHolder.j_voucherRes_voucher.setText(": "+voucherModel.getVoucher());
        viewHolder.j_voucherRes_Note.setText(": "+voucherModel.getNote());

        viewHolder.j_acc_voucher_row_layout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_animation));

    }

    @Override
    public int getItemCount() {
        return voucherList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint==null | constraint.length()==0){
                    filterResults.count = voucherFilterList.size();
                    filterResults.values = voucherFilterList;

                }else {
                    String searchChr = constraint.toString().toLowerCase();
                    List<ACC_View_Voucher_Result_Entity> resultData = new ArrayList<>();
                    for (ACC_View_Voucher_Result_Entity voucherModel: voucherFilterList){

                        if(voucherModel.getRef().toLowerCase().contains(searchChr) | voucherModel.getNote().toLowerCase().contains(searchChr)){
                            resultData.add(voucherModel);
                        }
                    }

                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                voucherList = (List<ACC_View_Voucher_Result_Entity>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    private class VoucherViewHolder extends RecyclerView.ViewHolder{
        private TextView j_voucherRes_date, j_voucherRes_voucher,j_voucherRes_ref, j_voucherRes_trType, j_voucherRes_Amount, j_voucherRes_Note ;
        private LinearLayout j_acc_voucher_row_layout;
        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            j_voucherRes_date = itemView.findViewById(R.id.voucherRes_date);
            j_voucherRes_voucher = itemView.findViewById(R.id.voucherRes_voucher);
            j_voucherRes_ref = itemView.findViewById(R.id.voucherRes_ref);
            j_voucherRes_trType = itemView.findViewById(R.id.voucherRes_trType);
            j_voucherRes_Amount = itemView.findViewById(R.id.voucherRes_Amount);
            j_voucherRes_Note = itemView.findViewById(R.id.voucherRes_Note);
            j_acc_voucher_row_layout = itemView.findViewById(R.id.acc_voucher_row_layout);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    selectedVoucher.getSelectedVoucher(voucherList.get(getAdapterPosition()));
//                }
//            });

        }
    }


    public interface SelectedVoucher{
        void getSelectedVoucher( ACC_View_Voucher_Result_Entity voucherModel);
    }



}

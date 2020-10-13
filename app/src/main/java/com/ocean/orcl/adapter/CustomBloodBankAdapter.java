package com.ocean.orcl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ocean.orcl.Blood_Bank_Activity;
import com.ocean.orcl.Blood_Bank_Entity;
import com.ocean.orcl.R;

import java.util.ArrayList;
import java.util.List;

public class CustomBloodBankAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private List<Blood_Bank_Entity> bloodBankList;
    private List<Blood_Bank_Entity> bloodBankFilterList;
    private Context context;
    private SelectedBloodDonner selectedBloodDonner;

    public CustomBloodBankAdapter(List<Blood_Bank_Entity> bloodBankList, SelectedBloodDonner selectedBloodDonner) {
        this.bloodBankList = bloodBankList;
        this.selectedBloodDonner = selectedBloodDonner;
        this.bloodBankFilterList = bloodBankList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BloodBankViewHolder(LayoutInflater.from(context).inflate(R.layout.blood_bank_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Blood_Bank_Entity bloodBankModel = bloodBankList.get(position);
        BloodBankViewHolder  viewHolder =(BloodBankViewHolder) holder;
        viewHolder.jdonner_blood_group.setText(bloodBankModel.getBlood_Grp());
        viewHolder.jblood_donner_name.setText(bloodBankModel.getF_name()+" "+bloodBankModel.getL_name());
        viewHolder.jdonner_desig_name.setText(bloodBankModel.getDesig_name());
        viewHolder.jdonner_dept_name.setText(bloodBankModel.getDept_name());
        viewHolder.jblood_donner_number.setText(bloodBankModel.getPhone_mobile());

        if(bloodBankModel.getBlood_Grp().equals("A+")){
            viewHolder.jdonner_blood_group.setTextColor(Color.parseColor("#5B6FE6"));
        }else if(bloodBankModel.getBlood_Grp().equals("A-")){
            viewHolder.jdonner_blood_group.setTextColor(Color.parseColor("#62b4cf"));
        }else if(bloodBankModel.getBlood_Grp().equals("B+")){
            viewHolder.jdonner_blood_group.setTextColor(Color.parseColor("#F34A17"));
        }else if(bloodBankModel.getBlood_Grp().equals("B-")){
            viewHolder.jdonner_blood_group.setTextColor(Color.parseColor("#F79F1E"));
        }else if(bloodBankModel.getBlood_Grp().equals("AB+")){
            viewHolder.jdonner_blood_group.setTextColor(Color.parseColor("#247428"));
//                bloodGrp.setTextColor(Color.LTGRAY);
        }else if(bloodBankModel.getBlood_Grp().equals("AB-")){
            viewHolder.jdonner_blood_group.setTextColor(Color.parseColor("#3CAA94"));
        }else if(bloodBankModel.getBlood_Grp().equals("O+")){
            viewHolder.jdonner_blood_group.setTextColor(Color.parseColor("#6B1515"));
        }else if(bloodBankModel.getBlood_Grp().equals("O-")){
            viewHolder.jdonner_blood_group.setTextColor(Color.parseColor("#D89837"));
        }
        if(bloodBankModel.getPhone_mobile() == null){
            viewHolder.call_layout.setVisibility(View.GONE);
        }else {
            viewHolder.call_layout.setVisibility(View.VISIBLE);;
        }
    }

    @Override
    public int getItemCount() {
        return bloodBankList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint==null | constraint.length()==0){
                    filterResults.count = bloodBankFilterList.size();
                    filterResults.values = bloodBankFilterList;

                }else {
                    String searchChr = constraint.toString().toLowerCase();
                    List<Blood_Bank_Entity> resultData = new ArrayList<>();

                    for (Blood_Bank_Entity donnerModel: bloodBankFilterList){

                        String name = donnerModel.getF_name()+" "+donnerModel.getL_name();

                        if(name.toLowerCase().contains(searchChr) | donnerModel.getBlood_Grp().toLowerCase().contains(searchChr) | donnerModel.getDesig_name().toLowerCase().contains(searchChr) | donnerModel.getDept_name().toLowerCase().contains(searchChr)){
                            resultData.add(donnerModel);
                        }
                    }

                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                bloodBankList = (List<Blood_Bank_Entity>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    private class BloodBankViewHolder extends RecyclerView.ViewHolder{

       private TextView jdonner_blood_group,jblood_donner_name,jdonner_desig_name,jdonner_dept_name,jblood_donner_number;
       private ImageView jcall_donner;
       private LinearLayout call_layout;

        public BloodBankViewHolder(@NonNull View itemView) {
            super(itemView);

            jdonner_blood_group = itemView.findViewById(R.id.donner_blood_group);
            jblood_donner_name = itemView.findViewById(R.id.blood_donner_name);
            jdonner_desig_name = itemView.findViewById(R.id.donner_desig_name);
            jdonner_dept_name = itemView.findViewById(R.id.donner_dept_name);
            jblood_donner_number = itemView.findViewById(R.id.blood_donner_number);
            call_layout =itemView.findViewById(R.id.call_btn_linerLayout);

            jcall_donner = itemView.findViewById(R.id.call_donner);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedBloodDonner.getSelectedBloodDonner(bloodBankList.get(getAdapterPosition()));
                }
            });

            jcall_donner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedBloodDonner.onCallBloodDonner(bloodBankList.get(getAdapterPosition()).getPhone_mobile());
                }
            });

        }
    }



    public interface SelectedBloodDonner{
        void getSelectedBloodDonner(Blood_Bank_Entity bloodDonnerModel);
        void onCallBloodDonner(String donner_number);
    }

}

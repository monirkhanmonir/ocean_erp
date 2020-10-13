package com.ocean.orcl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ocean.orcl.EmpInfo_Entity;
import com.ocean.orcl.R;

import java.util.ArrayList;
import java.util.List;

public class CustomEmpInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<EmpInfo_Entity> employeeList;
    private List<EmpInfo_Entity> employeeFilterList;
    private Context context;
    private SelectedEmployee selectedEmployee;

    public CustomEmpInfoAdapter(List<EmpInfo_Entity> employeeList, SelectedEmployee selectedEmployee) {
        this.employeeList = employeeList;
        this.employeeFilterList = employeeList;
        this.selectedEmployee = selectedEmployee;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        return new EmployeeViewHolder(LayoutInflater.from(context).inflate(R.layout.emp_info_listview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        EmpInfo_Entity employeeModel = employeeList.get(position);
        EmployeeViewHolder  viewHolder =(EmployeeViewHolder) holder;

        viewHolder.jPerson_no_text.setText(employeeModel.getPersong_no());
        viewHolder.jEmp_name.setText(employeeModel.getF_name()+" "+employeeModel.getL_name());
        viewHolder.emp_dept_name.setText(employeeModel.getDept_name());
        viewHolder.jEmp_info_desig_name.setText(employeeModel.getDesig_name());
        viewHolder.emp_mobile_number.setText(employeeModel.getPhone_mobile());
        viewHolder.emp_email_id.setText(employeeModel.getEmail_office());

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint==null | constraint.length()==0){
                    filterResults.count = employeeFilterList.size();
                    filterResults.values = employeeFilterList;

                }else {
                    String searchChr = constraint.toString().toLowerCase();
                    List<EmpInfo_Entity> resultData = new ArrayList<>();
                    for (EmpInfo_Entity employeeModel: employeeFilterList){
                        String name = employeeModel.getF_name()+" "+employeeModel.getL_name();
                        if(name.toLowerCase().contains(searchChr) | employeeModel.getDesig_name().toLowerCase().contains(searchChr) | employeeModel.getDept_name().toLowerCase().contains(searchChr)| employeeModel.getPersong_no().toLowerCase().contains(searchChr)){
                            resultData.add(employeeModel);
                        }
                    }

                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                employeeList = (List<EmpInfo_Entity>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    private class EmployeeViewHolder extends RecyclerView.ViewHolder{
        private TextView jPerson_no_text,jEmp_name,jEmp_info_desig_name,emp_dept_name,emp_mobile_number,emp_email_id;
        private LinearLayout jCallDialerLayout;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            jPerson_no_text = itemView.findViewById(R.id.emp_info_id);
            jEmp_name = itemView.findViewById(R.id.emp_name_text);
            jEmp_info_desig_name=itemView.findViewById(R.id.emp_info_desig_name);
            emp_dept_name=itemView.findViewById(R.id.emp_dept_name);
            emp_mobile_number=itemView.findViewById(R.id.emp_mobile_number);
            emp_email_id=itemView.findViewById(R.id.emp_email_id);
            jCallDialerLayout = itemView.findViewById(R.id.callDialerLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedEmployee.getSelectedEmployee(employeeList.get(getAdapterPosition()));
                }
            });

            jCallDialerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedEmployee.getSelectedNumber(employeeList.get(getAdapterPosition()).getPhone_mobile());
                }
            });
        }
    }

    public interface SelectedEmployee{
        void getSelectedEmployee( EmpInfo_Entity emp_info);
        void getSelectedNumber( String phone);
    }
}

//package com.ocean.orcl;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//public class CustomAdapter_BloodBank extends BaseAdapter implements Filterable {
//    Context context;
//    private ArrayList<Blood_Bank_Entity>Fname_blood;
//    private ArrayList<Blood_Bank_Entity>Lname_blood;
//    private ArrayList<Blood_Bank_Entity>DeptName_blood;
//    private ArrayList<Blood_Bank_Entity>DesignName_blood;
//    private ArrayList<Blood_Bank_Entity>PhoneMobile_blood;
//    private ArrayList<Blood_Bank_Entity>Blood_Grp_blood;
//    private ArrayList<Blood_Bank_Entity> allviewItems;
//     LayoutInflater inflater;
//
//    public CustomAdapter_BloodBank(Context context,  ArrayList<Blood_Bank_Entity> bloodBankList) {
//        this.context = context;
//        this.Fname_blood = bloodBankList;
//        this.Lname_blood = bloodBankList;
//        this.DeptName_blood = bloodBankList;
//        this.DesignName_blood = bloodBankList;
//        this.PhoneMobile_blood = bloodBankList;
//        this.Blood_Grp_blood = bloodBankList;
//        this.allviewItems =bloodBankList;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return Lname_blood.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return Lname_blood.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @SuppressLint("ResourceAsColor")
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
//        View row = layoutInflater.inflate(R.layout.blood_bank_listview, parent, false);
//
//        TextView fName = row.findViewById(R.id.f_name_blood_text);
//        TextView lName = row.findViewById(R.id.l_name_blood_text);
//        TextView department = row.findViewById(R.id.dept_name_blood_text);
//        TextView designation = row.findViewById(R.id.desig_name_blood_text);
//        TextView phone = row.findViewById(R.id.phone_mobile_blood_text);
//        TextView bloodGrp = row.findViewById(R.id.bloodGrp_blood_text);
//
//        fName.setText(Fname_blood.get(position).F_name);
//        lName.setText(Lname_blood.get(position).L_name);
//        department.setText(DeptName_blood.get(position).Dept_name);
////        datefill.setText(aDate.get(position).personDate);
//        designation.setText(DesignName_blood.get(position).Desig_name);
//        phone.setText(PhoneMobile_blood.get(position).Phone_mobile);
//        bloodGrp.setText(Blood_Grp_blood.get(position).Blood_Grp);
//
////       <<<<<<<<<<<<<<<<<<------ when textView value are null then textView fill are hide----->>>>>>>>>>>>>
//
//            if(fName.equals(isEmpty()) || fName.equals(null) || fName.length()==0 || fName.equals(""))
//            {
//                fName.setVisibility(View.GONE);
//            } else{
//                fName.setVisibility(View.VISIBLE);
//            }if(lName.equals(isEmpty()) || lName.equals(null) || lName.length()==0 || lName.equals(""))
//        {
//            lName.setVisibility(View.GONE);
//        } else{
//            lName.setVisibility(View.VISIBLE);
//        }if(department.equals(isEmpty()) || department.equals(null) || department.length()==0 || department.equals(""))
//        {
//            department.setVisibility(View.GONE);
//        } else{
//            department.setVisibility(View.VISIBLE);
//        }if(designation.equals(isEmpty()) || designation.equals(null) || designation.length()==0 || designation.equals(""))
//        {
//            designation.setVisibility(View.GONE);
//        } else{
//            designation.setVisibility(View.VISIBLE);
//        }if(phone.equals(isEmpty()) || phone.equals(null) || phone.length()==0 || phone.equals(""))
//        {
//            phone.setVisibility(View.GONE);
//        } else{
//            phone.setVisibility(View.VISIBLE);
//        }if(bloodGrp.equals(isEmpty()) || bloodGrp.equals(null) || bloodGrp.length()==0 || bloodGrp.equals(""))
//        {
//            phone.setVisibility(View.GONE);
//        } else{
//            bloodGrp.setVisibility(View.VISIBLE);
//        }
//            if(Blood_Grp_blood.get(position).Blood_Grp.equals("A+")){
//                bloodGrp.setTextColor(Color.parseColor("#5B6FE6"));
//            }else if(Blood_Grp_blood.get(position).Blood_Grp.equals("A-")){
//                bloodGrp.setTextColor(Color.parseColor("#62b4cf"));
//            }else if(Blood_Grp_blood.get(position).Blood_Grp.equals("B+")){
//                bloodGrp.setTextColor(Color.parseColor("#F34A17"));
//            }else if(Blood_Grp_blood.get(position).Blood_Grp.equals("B-")){
//                bloodGrp.setTextColor(Color.parseColor("#F79F1E"));
//            }else if(Blood_Grp_blood.get(position).Blood_Grp.equals("AB+")){
//                bloodGrp.setTextColor(Color.parseColor("#247428"));
////                bloodGrp.setTextColor(Color.LTGRAY);
//            }else if(Blood_Grp_blood.get(position).Blood_Grp.equals("AB-")){
//                bloodGrp.setTextColor(Color.parseColor("#3CAA94"));
//            }else if(Blood_Grp_blood.get(position).Blood_Grp.equals("O+")){
//                bloodGrp.setTextColor(Color.parseColor("#6B1515"));
//            }else if(Blood_Grp_blood.get(position).Blood_Grp.equals("O-")){
//                bloodGrp.setTextColor(Color.parseColor("#D89837"));
//            }
//
//
//        return row;
//    }
//
//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter(){
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//                ArrayList<Blood_Bank_Entity> FilteredArrList = new ArrayList<Blood_Bank_Entity>();
//
//                if (allviewItems == null) {
//                    allviewItems = new ArrayList<Blood_Bank_Entity>(allviewItems); // saves the original data in mOriginalValues
//                }
//
//                if (constraint == null || constraint.length() == 0) {
//
//                    // set the Original result to return
//                    results.count = allviewItems.size();
//                    results.values = allviewItems;
//
//                } else {
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < allviewItems.size(); i++) {
//                        String data = allviewItems.get(i).F_name;
//                        if (data.toLowerCase().contains(constraint.toString().trim())) {
////                            FilteredArrList.add(new AttendenceLog_Entity(allviewItems.get(i).persongID,allviewItems.get(i).persongName,allviewItems.get(i).persongDesignaton,allviewItems.get(i).personDepartment,allviewItems.get(i).persongLoginTime,allviewItems.get(i).personLogoutTime));
//                            FilteredArrList.add(new Blood_Bank_Entity(allviewItems.get(i).F_name,allviewItems.get(i).L_name,allviewItems.get(i).Dept_name,allviewItems.get(i).Desig_name,allviewItems.get(i).Phone_mobile,allviewItems.get(i).Blood_Grp));
//
//                        }
//
//                    }
//                    // set the Filtered result to return
//                    results.count = FilteredArrList.size();
//                    results.values = FilteredArrList;
//                }
//                return results;
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//
//                    Fname_blood = (ArrayList<Blood_Bank_Entity>) results.values;
//                    Lname_blood = (ArrayList<Blood_Bank_Entity>) results.values;
//                    DesignName_blood = (ArrayList<Blood_Bank_Entity>) results.values;
//                    DeptName_blood = (ArrayList<Blood_Bank_Entity>) results.values;
//                    PhoneMobile_blood = (ArrayList<Blood_Bank_Entity>) results.values;
//                    Blood_Grp_blood = (ArrayList<Blood_Bank_Entity>) results.values;// has the filtered values
//
//                    notifyDataSetChanged();
//
//            }
//        };
//        return filter;
//    }
//
//}

package com.excellenceict.ocean_erp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//public class CustomAdapter_MyTeamsAttendence extends BaseAdapter implements Filterable {
public class CustomAdapter_MyTeamsAttendence  extends RecyclerView.Adapter<CustomAdapter_MyTeamsAttendence.ExampleViewHolder>implements Filterable{
    private List<MyTeams_Entity> exampleList;
    private List<MyTeams_Entity> exampleListFull;
    private  Context context;
    class ExampleViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9;
        RelativeLayout relativeLayout;

        ExampleViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.empId_team);
            textView2 = itemView.findViewById(R.id.personName_team);
            textView3 = itemView.findViewById(R.id.designation_team);
            textView4 = itemView.findViewById(R.id.department_team);
            textView5 = itemView.findViewById(R.id.login_team);
            textView6 = itemView.findViewById(R.id.lateLogin_team);
            textView7 = itemView.findViewById(R.id.Logout_team);
            textView8 = itemView.findViewById(R.id.earlyLogOut_team);
            textView9 = itemView.findViewById(R.id.absentReason_team);
//            relativeLayout =itemView.findViewById(R.id.relative_lyout);

        }
    }

    CustomAdapter_MyTeamsAttendence(Context context, List<MyTeams_Entity> exampleList) {
        this.exampleList = exampleList;
        this.context =context;
        exampleListFull = new ArrayList<>(exampleList);
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_teams_attendence_listview,
                parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, final int position) {
        MyTeams_Entity currentItem = exampleList.get(position);
        holder.textView1.setText(currentItem.getPerson_no());
        holder.textView2.setText(currentItem.getEmp_name());
        holder.textView3.setText(currentItem.getDesignation());
        holder.textView4.setText(currentItem.getDepartmetn());
        holder.textView5.setText(currentItem.getLoginTime());
        holder.textView6.setText(currentItem.getLate_logInReason());
        holder.textView7.setText(currentItem.getLogoutTime());
        holder.textView8.setText(currentItem.getEarly_logOutReason());
        holder.textView9.setText(currentItem.getAbsent_Resason());

        //set onClickListener
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                removeItem(position);
//                Intent intent = new Intent(context,Test_Activity.class);
//                Log.d("itemsClick","=======1234======"+position);
//
//                Toast.makeText(context,"selected"+position,Toast.LENGTH_SHORT).show();
//                context.startActivity(intent);
//            }
//        });

        if(holder.textView1.equals("") || holder.textView1.equals(null) || holder.textView1.length()==0)
        {
            holder.textView1.setVisibility(View.GONE);
        } else{
            holder.textView1.setVisibility(View.VISIBLE);
        }
        if(holder.textView2.equals("") || holder.textView2.equals(null) || holder.textView2.length()==0)
        {
            holder.textView2.setVisibility(View.GONE);
        } else {
            holder.textView2.setVisibility(View.VISIBLE);
        }if( holder.textView3.equals(null) || holder.textView3.length()==0 || holder.textView3.equals(""))
        {
            holder.textView3.setVisibility(View.GONE);
        } else {
            holder.textView3.setVisibility(View.VISIBLE);
        }
        if( holder.textView4.equals(null) || holder.textView4.length()==0 || holder.textView4.equals(""))
        {
            holder.textView4.setVisibility(View.GONE);
        } else {
            holder.textView4.setVisibility(View.VISIBLE);
        }if( holder.textView5.equals(null) || holder.textView5.length()==0 || holder.textView5.equals(""))
        {
            holder.textView5.setVisibility(View.GONE);
        } else {
            holder.textView5.setVisibility(View.VISIBLE);
        }if( holder.textView6.equals(null) || holder.textView6.length()==0 || holder.textView6.equals(""))
        {
            holder.textView6.setVisibility(View.GONE);
        } else {
            holder.textView6.setVisibility(View.VISIBLE);
        }if( holder.textView7.equals(null) || holder.textView7.length()==0 || holder.textView7.equals(""))
        {
            holder.textView7.setVisibility(View.GONE);
        } else {
            holder.textView7.setVisibility(View.VISIBLE);
        }if( holder.textView8.equals(null) || holder.textView8.length()==0 || holder.textView8.equals(""))
        {
            holder.textView8.setVisibility(View.GONE);
        } else {
            holder.textView8.setVisibility(View.VISIBLE);
        }if( holder.textView9.equals(null) || holder.textView9.length()==0 || holder.textView9.equals(""))
        {
            holder.textView9.setVisibility(View.GONE);
        } else {
            holder.textView9.setVisibility(View.VISIBLE);
        }
        if(currentItem.getWeekend().equals("N")  && currentItem.getHoliday().equals("N") && currentItem.getLateLogin_Flag().equals("Y")){
            holder.textView5.setTextColor(Color.RED);
        }else {
//                    loginTime.setTextColor(Color.GREEN);
            holder.textView5.setTextColor(Color.parseColor("#4D850D"));
        }if(currentItem.getWeekend().equals("N")  && currentItem.getHoliday().equals("N") && currentItem.getLateLogin_Flag().equals("Y")){
            holder.textView7.setTextColor(Color.RED);
        }else {
//                    logoutTime.setTextColor(Color.GREEN);
            holder.textView7.setTextColor(Color.parseColor("#4D850D"));
            Log.d("text","=================text7===="+ holder.textView7.getText());
        }


    }

    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MyTeams_Entity> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyTeams_Entity item : exampleListFull) {
                    if (item.getEmp_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);

                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exampleList.clear();
            exampleList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



    public  void  updateList(List<MyTeams_Entity>newList){
        exampleList = new ArrayList<>();
        exampleList.addAll(newList);
        notifyDataSetChanged();
    }
    public void filterList(ArrayList<MyTeams_Entity> newList) {
        exampleList = new ArrayList<>();
        exampleList.addAll(newList);
        notifyDataSetChanged();
    }


}


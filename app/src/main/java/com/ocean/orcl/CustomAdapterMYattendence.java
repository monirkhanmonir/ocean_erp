//package com.ocean.orcl;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CustomAdapterMYattendence extends BaseAdapter {
//    Context context;
//
//    private ArrayList<MyAttendence_Entity> DATEs;
//    private ArrayList<MyAttendence_Entity> LOGINTIME;
//    private ArrayList<MyAttendence_Entity> LATE_LOGIN_REASON;
//    private ArrayList<MyAttendence_Entity> LOGOUTTIME;
//    private ArrayList<MyAttendence_Entity> EARLY_LOGOUT_REASON;
//    private ArrayList<MyAttendence_Entity> ABSENT_REASON;
//    private ArrayList<MyAttendence_Entity> weekend;
//    private ArrayList<MyAttendence_Entity> holiday;
//    private ArrayList<MyAttendence_Entity> lateLoginFlag;
//    private ArrayList<MyAttendence_Entity> earlyLogOutFlag;
//    private List<MyAttendence_Entity> allviewsItems;
//    private LayoutInflater inflater;
//
//public CustomAdapterMYattendence(Context context,ArrayList<MyAttendence_Entity> allItems) {
//        this.DATEs=allItems;
//        this.LOGINTIME=allItems;
//        this.LATE_LOGIN_REASON=allItems;
//        this.LOGOUTTIME=allItems;
//        this.EARLY_LOGOUT_REASON=allItems;
//        this.ABSENT_REASON=allItems;
//        this.weekend=allItems;
//        this.holiday=allItems;
//        this.lateLoginFlag=allItems;
//        this.earlyLogOutFlag=allItems;
//        this.allviewsItems =allItems;
//
//}
//
//    @Override
//    public int getCount() {
//        return LOGINTIME.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return getItem(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//       inflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
////            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            convertView =  inflater.inflate(R.layout.myattendence_listview,parent,false);
//            TextView TodayDate = convertView.findViewById(R.id.date_text);
//            TextView inTime = convertView.findViewById(R.id.inTime_text);
//            TextView lateIn = convertView.findViewById(R.id.lateLateReson_text);
//            TextView outTime = convertView.findViewById(R.id.out_text);
//            TextView earlyOut = convertView.findViewById(R.id.earlyLogoutReson_text);
//            TextView absent = convertView.findViewById(R.id.absenResion_text);
//
//
//            TodayDate.setText(String.valueOf(DATEs.get(position).getDATEs()));
//            inTime.setText(LOGINTIME.get(position).getLOGINTIME());
//            lateIn.setText(LATE_LOGIN_REASON.get(position).getLATE_LOGIN_REASON());
//            outTime.setText(LOGOUTTIME.get(position).getLOGOUTTIME());
//            earlyOut.setText(EARLY_LOGOUT_REASON.get(position).getEARLY_LOGOUT_REASON());
//            absent.setText(ABSENT_REASON.get(position).getABSENT_REASON());
//
//        if(lateIn.equals(isEmpty()) || lateIn.equals(null) || lateIn.length()==0 || lateIn.equals(""))
//        {
//            lateIn.setVisibility(View.GONE);
//        } else{
//            lateIn.setVisibility(View.VISIBLE);
//        }
//        if(earlyOut.equals(isEmpty()) || earlyOut.equals(null) || earlyOut.length()==0 || earlyOut.equals(""))
//        {
//            earlyOut.setVisibility(View.GONE);
//        } else {
//            earlyOut.setVisibility(View.VISIBLE);
//        }if(absent.equals(isEmpty()) || absent.equals(null) || absent.length()==0 || absent.equals(""))
//        {
//            absent.setVisibility(View.GONE);
//        } else {
//            absent.setVisibility(View.VISIBLE);
//        }
//        if(weekend.get(position).getWeekend().equals("N")  && holiday.get(position).getHoliday().equals("N") && lateLoginFlag.get(position).getLateLogin_Flag().equals("Y")){
//            inTime.setTextColor(Color.RED);
//        }else {
////                    loginTime.setTextColor(Color.GREEN);
//            inTime.setTextColor(Color.parseColor("#4D850D"));
//        }if(weekend.get(position).getWeekend().equals("N")  && holiday.get(position).getHoliday().equals("N") && earlyLogOutFlag.get(position).getEarlyLogOut_Flag().equals("Y")){
//            outTime.setTextColor(Color.RED);
//        }else {
////                    logoutTime.setTextColor(Color.GREEN);
//            outTime.setTextColor(Color.parseColor("#4D850D"));
//        }
//        return convertView;
//    }
//
//}
//

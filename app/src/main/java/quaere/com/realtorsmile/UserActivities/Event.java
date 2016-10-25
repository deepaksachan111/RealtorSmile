package quaere.com.realtorsmile.UserActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.PriorityAsyncTask;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.ReminderBeforeAsyncTask;
import quaere.com.realtorsmile.SessionManager;
import quaere.com.realtorsmile.StatusAsyncTask;

public class Event extends Activity implements View.OnClickListener {


    private String aciviityuserId, get_spinneritem;
    private String addmeetingurl, meetingname,  response_code;
    private int noofobjects;
    ArrayList<ModelClass> data = new ArrayList<>();
    private Spinner spinner_event_assignedTo,
            spinner_event_starttime, addevent_spinner_endtime, spinnerevent_relatedto,spinnerevent_relatedto2,
            spinner_event_priorty1, spinner_event_priorty2, spinnerevent_reminderbefore;
    private EditText edt_event_subject, edt_event_invetiies, edt_event_selectdate, edt_eventcal_end_Date, edt_task_venue, edt_event_remark;
    private CheckBox check_event_emailnotification, check_event_smsnotification;
    private Button btn_event_saveBtn, btn_task_event_n_NewBtn, btn_event_cancelBtn;

    ArrayList<String> spinnerasstionTo = new ArrayList<>();
    ArrayList<String> startTimelist = new ArrayList<>();

    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    private PopupWindow popupWindow;
    ArrayList<String> popup_inviteis_arraylist = new ArrayList<>();
   
    private ArrayAdapter<String> popup_inventiesarrayAdapter;
    private LinearLayout popupdismisslayout, spinneractivity_event_relatedto2_linear;
    private ListView lst_popup_inventies;

    ArrayList<String> relatedtolist2 = new ArrayList<>();
   ArrayList<ModelClass> modelClassRelatedto2ArrayList = new ArrayList<>();

    ArrayList<ModelClass> modeladdinvetiesList = new ArrayList<>();
    private  static String addinvitees_text = "";

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_event);
        findID();
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        //  HashMap<String, String> email = sessionManager.getUserDetails();
        aciviityuserId = emailid.get(SessionManager.KEY_EMAIL);

        edt_event_subject.setOnClickListener(this);
        edt_event_invetiies.setOnClickListener(this);
        edt_event_selectdate.setOnClickListener(this);
        edt_eventcal_end_Date.setOnClickListener(this);
        edt_task_venue.setOnClickListener(this);
        edt_event_remark.setOnClickListener(this);
        btn_event_saveBtn.setOnClickListener(this);
        btn_task_event_n_NewBtn.setOnClickListener(this);
        btn_event_cancelBtn.setOnClickListener(this);


        // setdate
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year, month, day);
        edt_event_selectdate.setText(dateFormatter.format(cal.getTime()));
        edt_eventcal_end_Date.setText(dateFormatter.format(cal.getTime()));
        //start time
        startTime();
        endtime();
        //assignTo
        assigntodata();
        relatedtodata();
        priorityasynctask();
        statusasynctask();
        reminderasynctask();
    }

    private void setdate() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                   /*   addmeeting_edt_selectdate.setText(dayOfMonth + "-"
                              + dateFormatter.format (monthOfYear + 1) + "-" + year);*/
                        cal.set(year, monthOfYear, dayOfMonth);
                        edt_event_selectdate.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);
        dpd.show();
    }

    private void enddate() {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox
                   /*   addmeeting_edt_selectdate.setText(dayOfMonth + "-"
                              + dateFormatter.format (monthOfYear + 1) + "-" + year);*/
                        cal.set(year, monthOfYear, dayOfMonth);
                        edt_eventcal_end_Date.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);
        dpd.show();
    }

    private void startTime() {

        startTimelist.add("06:00 AM");
        startTimelist.add("06:15 AM");
        startTimelist.add("06:30 AM");
        startTimelist.add("06:45 AM");
        startTimelist.add("07:00 AM");
        startTimelist.add("07:15 AM");
        startTimelist.add("07:30 AM");
        startTimelist.add("07:45 AM");
        startTimelist.add("08:00 AM");
        startTimelist.add("08:15 AM");
        startTimelist.add("08:30 AM");
        startTimelist.add("08:45 AM");
        startTimelist.add("09:00 AM");
        startTimelist.add("09:15 AM");
        startTimelist.add("09:30 AM");
        startTimelist.add("09:45 AM");
        startTimelist.add("10:00 AM");
        startTimelist.add("10:15 AM");
        startTimelist.add("10:30 AM");
        startTimelist.add("10:45 AM");
        startTimelist.add("11:00 AM");
        startTimelist.add("11:15 AM");
        startTimelist.add("11:30 AM");
        startTimelist.add("11:45 AM");
        startTimelist.add("12:00 PM");
        startTimelist.add("12:15 PM");
        startTimelist.add("12:30 PM");
        startTimelist.add("12:45 PM");
        startTimelist.add("1:00 PM");
        startTimelist.add("2:15 PM");
        startTimelist.add("2:30 PM");
        startTimelist.add("2:45 PM");
        startTimelist.add("3:00 PM");
        startTimelist.add("3:15 PM");
        startTimelist.add("3:30 PM");
        startTimelist.add("3:45 PM");
        startTimelist.add("4:00 PM");
        startTimelist.add("4:15 PM");
        startTimelist.add("4:30 PM");
        startTimelist.add("4:45 PM");
        startTimelist.add("5:00 PM");
        startTimelist.add("5:15 PM");
        startTimelist.add("5:30 PM");
        startTimelist.add("5:45 PM");
        startTimelist.add("6:00 PM");
        startTimelist.add("6:15 PM");
        startTimelist.add("6:30 PM");
        startTimelist.add("6:45 PM");
        startTimelist.add("7:00 PM");
        startTimelist.add("7:15 PM");
        startTimelist.add("7:30 PM");
        startTimelist.add("7:45 PM");
        startTimelist.add("8:00 PM");
        startTimelist.add("8:15 PM");
        startTimelist.add("8:30 PM");
        startTimelist.add("8:45 PM");
        startTimelist.add("9:00 PM");
        startTimelist.add("9:15 PM");
        startTimelist.add("9:30 PM");
        startTimelist.add("9:45 PM");
        startTimelist.add("10:00 PM");
        startTimelist.add("10:15 PM");
        startTimelist.add("10:30 PM");
        startTimelist.add("10:45 PM");
        startTimelist.add("11:00 PM");
        startTimelist.add("11:15 PM");
        startTimelist.add("11:30 PM");
        startTimelist.add("11:45 PM");

        ArrayAdapter<String> starttimeadapter = new ArrayAdapter<String>(Event.this, android.R.layout.simple_spinner_dropdown_item, startTimelist);
        spinner_event_starttime.setAdapter(starttimeadapter);
        spinner_event_starttime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void endtime() {
        ArrayAdapter<String> starttimeadapter = new ArrayAdapter<String>(Event.this, android.R.layout.simple_spinner_dropdown_item, startTimelist);
        addevent_spinner_endtime.setAdapter(starttimeadapter);
        addevent_spinner_endtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void assigntodata() {
        //spinnerasstionTo.add("Select");
        ArrayAdapter<String> spinner_assignToAdapter = new ArrayAdapter<String>(Event.this, R.layout.simple_spinner_text, spinnerasstionTo);
        spinner_event_assignedTo.setAdapter(spinner_assignToAdapter);

        String stringassginto_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/getEmployee/" + aciviityuserId;
        new assignToaAsynktask().execute(stringassginto_url);


        spinner_event_assignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ModelClass modelClass = (ModelClass) data.get(position);
                String ids = modelClass.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id  " + ids, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void relatedtodata() {
        ArrayList<String> relatedtolist1 = new ArrayList<>();

        relatedtolist1.add("Property");
        relatedtolist1.add("Potential");
        relatedtolist1.add("Project");
        relatedtolist1.add("Listing");
        relatedtolist1.add("Case");
        relatedtolist1.add("Closing");
        relatedtolist1.add("Booking");
        relatedtolist1.add("Transaction");


        // spinneractivity_meeting_relatedto2.setVisibility(View.GONE);


        ArrayAdapter relatedtoarrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, relatedtolist1);
        spinnerevent_relatedto.setAdapter(relatedtoarrayAdapter1);
        spinnerevent_relatedto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinneractivity_event_relatedto2_linear.setVisibility(View.VISIBLE);
                String selected_realatedto = parent.getItemAtPosition(position).toString();
                Log.v("Selected ITem :", selected_realatedto);

                String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetRelated/" + aciviityuserId + "/" + selected_realatedto;
                // RelatedToAsyncTask relatedtoAsyncTask = new RelatedToAsyncTask(MeetingActivity.this, relatedtolist2,relatedtoarrayAdapter2 , spinneractivity_meeting_relatedto2, url);
                // relatedtoAsyncTask.execute();

                new RelatedtoAsyncTask2().execute(url);

                //  new RelatedtoAsyncTask().execute(url);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinneractivity_event_relatedto2_linear.setVisibility(View.GONE);
            }
        });

        spinnerevent_relatedto2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //  String selected_realatedtoid = parent.getItemAtPosition(position).toString();
                ModelClass modelClassdata = (ModelClass) modelClassRelatedto2ArrayList.get(position);
                String selected_realatedtoid = modelClassdata.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id  " + selected_realatedtoid, Toast.LENGTH_SHORT).show();
                Log.v("Selected ITem :", selected_realatedtoid);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinneractivity_event_relatedto2_linear.setVisibility(View.GONE);
            }
        });

    }


    private void priorityasynctask() {
        ArrayList<String> priorityarrayList = new ArrayList<>();
        PriorityAsyncTask.PriorityAdapter priorityadapter = null;
        String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetPriority";
        PriorityAsyncTask priorityAsyncTask = new PriorityAsyncTask(this, priorityarrayList, priorityadapter, spinner_event_priorty1, url);
        priorityAsyncTask.execute(url);

        spinner_event_priorty1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ModelClass data = (ModelClass) parent.getItemAtPosition(position);
                String ids = data.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id" + ids, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void statusasynctask() {
        ArrayList<String> statusarrayList = new ArrayList<>();
        StatusAsyncTask.StatusAdapter statusadapter = null;
        String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetStatus/0";
        StatusAsyncTask statusAsyncTask = new StatusAsyncTask(this, statusarrayList, statusadapter, spinner_event_priorty2, url);
        statusAsyncTask.execute(url);

        spinner_event_priorty2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                ModelClass data = (ModelClass) parent.getItemAtPosition(position);
                String asignselecteid = data.getContactID();

                Toast.makeText(getApplicationContext(), "Selected ID" + asignselecteid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void reminderasynctask() {
        ArrayList<String> reminderarrayList = new ArrayList<>();
        ReminderBeforeAsyncTask.ReminderBeforeAdapter reminderadapter = null;
        String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetReminder/0";


        ReminderBeforeAsyncTask reminderAsyncTask = new ReminderBeforeAsyncTask(this, reminderarrayList, reminderadapter, spinnerevent_reminderbefore, url);
        reminderAsyncTask.execute(url);
        spinnerevent_reminderbefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ModelClass modelClass = (ModelClass) parent.getItemAtPosition(position);
                String asignselecteid = modelClass.getContactID();

                Toast.makeText(getApplicationContext(), "Selected ID" + asignselecteid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findID() {
        popupdismisslayout = (LinearLayout) findViewById(R.id.builder_group_popup_window);

        spinner_event_assignedTo = (Spinner) findViewById(R.id.spinner_event_assignedTo);
        edt_event_subject = (EditText) findViewById(R.id.edt_event_subject);
        edt_event_invetiies = (EditText) findViewById(R.id.edt_event_invitees);


        edt_event_selectdate = (EditText) findViewById(R.id.edt_event_selectdate);
        spinner_event_starttime = (Spinner) findViewById(R.id.spinner_event_starttime);

        edt_eventcal_end_Date = (EditText) findViewById(R.id.edt_eventcal_end_time);
        addevent_spinner_endtime = (Spinner) findViewById(R.id.spinnerevent_endtime);
        spinneractivity_event_relatedto2_linear= (LinearLayout)findViewById(R.id.spinneractivity_event_relatedto2_linear);
        spinnerevent_relatedto = (Spinner) findViewById(R.id.spinnerevent_relatedto);
        spinnerevent_relatedto2 =(Spinner)findViewById(R.id.spinnerevent_relatedto2);

        spinner_event_priorty1 = (Spinner) findViewById(R.id.spinner_event_priorty1);
        spinner_event_priorty2 = (Spinner) findViewById(R.id.spinner_event_priorty2);
        check_event_emailnotification = (CheckBox) findViewById(R.id.check_event_emailnotification);
        check_event_smsnotification = (CheckBox) findViewById(R.id.check_event_smsnotification);
        spinnerevent_reminderbefore = (Spinner) findViewById(R.id.spinnerevent_reminderbefore);
        edt_event_remark = (EditText) findViewById(R.id.edt_event_remark);
        edt_task_venue = (EditText) findViewById(R.id.edt_task_venue);
        btn_event_saveBtn = (Button) findViewById(R.id.btn_event_saveBtn);
        btn_task_event_n_NewBtn = (Button) findViewById(R.id.btn_task_event_n_NewBtn);
        btn_event_cancelBtn = (Button) findViewById(R.id.btn_event_cancelBtn);

    }



    private void enviteePopup() {

        LayoutInflater layoutInflater
                = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.invities_popup_layout,null);
        popupWindow = new PopupWindow(popupView, 600, WindowManager.LayoutParams.WRAP_CONTENT);

        lst_popup_inventies = (ListView)popupView.findViewById(R.id.invities_popup_listview);


        popup_inventiesarrayAdapter = new ArrayAdapter<String>(Event.this,android.R.layout.simple_list_item_1, popup_inviteis_arraylist);
        lst_popup_inventies.setAdapter(popup_inventiesarrayAdapter);

        lst_popup_inventies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               // String getfullname =(String)parent.getItemAtPosition(position).toString();

                ModelClass modelClass = (ModelClass)modeladdinvetiesList.get(position);
                String getfullname = modelClass.getContactName();
                String ids = modelClass.getContactID();

                addinvitees_text = addinvitees_text + getfullname + ",";
                edt_event_invetiies.setText(addinvitees_text);

             //   popup_inventiesarrayAdapter.remove(getfullname);
                popup_inventiesarrayAdapter.remove(popup_inventiesarrayAdapter.getItem(position));
                lst_popup_inventies.setAdapter(popup_inventiesarrayAdapter);


                Toast.makeText(getBaseContext(), getfullname, Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), ids, Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
          //  popup_inventiesarrayAdapter.notifyDataSetChanged();
        });
       // popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // final EditText et_popupaddnew_tpe1 = (EditText) popupView.findViewById(R.id.et_popupaddnew_tpe);
        popupWindow.update();
        popupWindow.showAsDropDown(edt_event_invetiies, 10, 5);

    }

    @Override
    public void onClick(View v) {
   if(v == edt_event_selectdate){
   setdate();
   }else if(v == edt_eventcal_end_Date){
        enddate();
   }else if(v == edt_event_invetiies){


       String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetInvitees/"+aciviityuserId;
       new InventiesAsyncTask().execute(url);
       enviteePopup();

   }

    }


    private class assignToaAsynktask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog = new ProgressDialog(Event.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("loading");
            //  pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(params[0]);

            try {
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.v("Source Response ", response);


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }
            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();

            try {
                JSONArray jsonArray = new JSONArray(s);
                noofobjects = jsonArray.length();
                Log.v("Number of json Obj " + noofobjects, "   pd Objects.....");
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);
                    meetingname = jObj.getString("EmpName");
                   String empid = jObj.getString("PK_EmployeeID");
                    response_code = jObj.getString("ResponseCode");
                    spinnerasstionTo.clear();
                    spinnerasstionTo.add(meetingname);


                    data.add(new ModelClass(meetingname,empid));

                }

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (response_code.equals("1")) {
                    System.out.println("respnse code 1............ inside condition");

                    ArrayAdapter<String> spinner_assignToAdapter = new ArrayAdapter<String>(Event.this, android.R.layout.simple_spinner_item, spinnerasstionTo);
                    spinner_assignToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_event_assignedTo.setAdapter(spinner_assignToAdapter);
                    spinner_assignToAdapter.notifyDataSetChanged();

                    //  source_detail_adapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + noofobjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();

                }

            } catch (Exception e) {
                Toast.makeText(Event.this, "Connection  Failed ", Toast.LENGTH_LONG).show();

            }

        }
    }
    private class InventiesAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog = new ProgressDialog(Event.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("loading");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(params[0]);

            try {
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.v("Source Response ", response);


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();

            popup_inviteis_arraylist.clear();

            try {

                JSONArray jsonArray = new JSONArray(result);

                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);

                    String group = jObj.getString("Invitees");
                    String type = jObj.getString("PK_InviteesID");
                    String responsecode = jObj.getString("ResponseCode");

                        popup_inviteis_arraylist.add(group);
                    modeladdinvetiesList.add(new ModelClass(group,type));

                }


                //  popuplist_employee_adapter.notifyDataSetChanged();
                lst_popup_inventies.setAdapter(new ArrayAdapter<String>(Event.this, android.R.layout.simple_list_item_1, popup_inviteis_arraylist));
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {


                Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : ", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.getView().setPadding(20, 20, 20, 20);
                toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                toast.show();
                pDialog.dismiss();



            } catch (Exception e) {
                Toast.makeText(Event.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }
    }


    private class RelatedtoAsyncTask2 extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Event.this);
            pDialog.setMessage("Loading Details ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            // pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String response = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url);

            try {
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.v("RelatedTO  Response ", response);
                // String response = jsonResponse;


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            relatedtolist2.clear();
            try {
                JSONArray jsonArray = new JSONArray(s);
                int noOfObjects = jsonArray.length();
                Log.v("Number of json Obj " + noOfObjects, "   pd Objects.....");
                // pd.dismiss();
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);

                    String relatedto = jObj.getString("Related");
                    String PK_RelatedID = jObj.getString("PK_RelatedID");

                response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);

                    if (relatedto.equals("null") || relatedto.equals("")) {
                        spinneractivity_event_relatedto2_linear.setVisibility(View.GONE);
                    } else {
                        relatedtolist2.add(relatedto);

                        modelClassRelatedto2ArrayList.add(new ModelClass(relatedto, PK_RelatedID));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (response_code.equals("1")) {

                Toast.makeText(getApplicationContext(), "responsecode 1", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(Event.this, android.R.layout.simple_list_item_1, relatedtolist2);

                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinnerevent_relatedto2.setAdapter(arrayAdapter2);
            } else {
                spinneractivity_event_relatedto2_linear.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_LONG).show();
            }
        }
    }

}
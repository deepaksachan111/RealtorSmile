package quaere.com.realtorsmile.UserActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import quaere.com.realtorsmile.ContactTypeAsyncTask;
import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.PriorityAsyncTask;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.ReminderBeforeAsyncTask;
import quaere.com.realtorsmile.SessionManager;
import quaere.com.realtorsmile.StatusAsyncTask;
import quaere.com.realtorsmile.UrlUtilities;

public class MeetingActivity extends Activity implements View.OnClickListener {

    private ArrayList<String> startTimelist = new ArrayList<>();
    ArrayList<ModelClass> statuslist;
    
    private ArrayList<String> contacttype2list2 = new ArrayList<>();
    ArrayList<ModelClass> modelClassContactType2ArrayList = new ArrayList<>();
    ArrayList<ModelClass> modelClassRelatedto2ArrayList = new ArrayList<>();



     private ArrayList<ModelClass> modelClassassignTo_ArrayList = new ArrayList<>();
    ArrayList<String> spinnerasstionTo = new ArrayList<>();

    private ArrayAdapter<String> contactype1Adapter1;
    private ArrayAdapter<String> contactype2Adapter2;

    private String aciviityuserId, get_spinneritemcontacttype1;
    private String addcontacttype1url, contacttpe1reponse_code;
    private int noofobjects;

    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    private Spinner spinner_add_meeting_assignedTo, spinner_meeting_contact_type1, spinner_meeting_contact_type2,
            addmeeting_spinner_starttime, addmeeting_spinner_endtime, spinneractivity_meeting_relatedto1, spinneractivity_meeting_relatedto2,
            spinner_meeting_priorty1, spinner_meeting_priorty2, spinner_meeting_reminderbefore;
    private EditText edt_meeting_subject, addmeeting_edt_selectdate, edt_meeting_venue, edt_meeting_remark;
    private CheckBox check_addnewmeeting_emailnotification, check_addnewmeeting_smsnotification;
    private Button saveBtn, btn_save_n_new, btn_cancel;
    private ImageView ivbackaddmeeting;

    private ArrayAdapter relatedtoarrayAdapter1;
    private ArrayAdapter relatedtoarrayAdapter2;

    ArrayList<String> relatedtolist2 = new ArrayList<>();

    private String relatedto_response_code;
    private boolean flag = true;
    private LinearLayout spinneractivity_meeting_relatedto2_linear;

    private String contactID;
    private String contactType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_meeting);
        findID();

        edt_meeting_subject.setOnClickListener(this);
        addmeeting_edt_selectdate.setOnClickListener(this);
        edt_meeting_venue.setOnClickListener(this);
        edt_meeting_remark.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        btn_save_n_new.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        addmeeting_edt_selectdate.setOnClickListener(this);
        ivbackaddmeeting.setOnClickListener(this);
        spinneractivity_meeting_relatedto2_linear.setOnClickListener(this);


        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        aciviityuserId = emailid.get(SessionManager.KEY_EMAIL);


        contacttypeasynktask();

        //select StartTime
        startTime();
        //select StartTime
        endTime();
        //for select Date
        // for AssignTo
        assigntodata();
        priorityasynctask();
        statusasynctask();
        reminderasynctask();

        //
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year, month, day);
        addmeeting_edt_selectdate.setText(dateFormatter.format(cal.getTime()));

        // addmeeting_edt_selectdate.setText(day + "/" + (month + 1) + "/" + year);

        //-------------------------------------------------------------------------------------

        relatedtodata();


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

        ArrayAdapter<String> starttimeadapter = new ArrayAdapter<String>(MeetingActivity.this, android.R.layout.simple_spinner_dropdown_item, startTimelist);
        addmeeting_spinner_starttime.setAdapter(starttimeadapter);
        addmeeting_spinner_starttime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void endTime() {
        ArrayAdapter<String> starttimeadapter = new ArrayAdapter<String>(MeetingActivity.this, android.R.layout.simple_spinner_dropdown_item, startTimelist);
        addmeeting_spinner_endtime.setAdapter(starttimeadapter);
        addmeeting_spinner_endtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDate() {
        // Launch Date Picker Dialog
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
                        addmeeting_edt_selectdate.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);
        dpd.show();
    }


   /* private void setTime() {
        // Process to get Current Time

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        // Display Selected time in textbox
                        addmeeting_edt_selectdate.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        tpd.show();
    }*/

    private void contacttypeasynktask() {
        //  Toast.makeText(getApplicationContext(), "selected item is " + item, Toast.LENGTH_LONG).show();
        addcontacttype1url = UrlUtilities.CONTACTTYPE1_URL;
        //  new ContactType1AsyncTask().execute(addcontacttype1url);

        ArrayList<String> contacttypenamelist1 = new ArrayList<String>();
        ArrayList<String> contacttypeidlist1 = new ArrayList<String>();
        ArrayList<ModelClass> customListArray = new ArrayList<>();
        ContactTypeAsyncTask.ContactTypeAdapter contactTypeAdapter1 = null;
        ContactTypeAsyncTask contactTypeAsyncTask = new ContactTypeAsyncTask(this, contacttypenamelist1, contacttypeidlist1, customListArray, contactTypeAdapter1, spinner_meeting_contact_type1, addcontacttype1url);
        contactTypeAsyncTask.execute();

        spinner_meeting_contact_type1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                //  get_spinneritemcontacttype1 = adapterView.getItemAtPosition(position).toString();

                ModelClass selectedItem = (ModelClass) adapterView.getItemAtPosition(position);
                get_spinneritemcontacttype1 = selectedItem.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id  " + get_spinneritemcontacttype1, Toast.LENGTH_SHORT).show();

                String contacttypeNameUrl = UrlUtilities.CONTACTTYPE2_URL + aciviityuserId + "/" + get_spinneritemcontacttype1;
                new ContactType2AsyncTask().execute(contacttypeNameUrl);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_meeting_contact_type2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                ModelClass selectedItem = (ModelClass) modelClassContactType2ArrayList.get(position);
                String contactTypeId = selectedItem.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id  " + contactTypeId, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void relatedtodata(){
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

        relatedtolist2 = new ArrayList<>();

        relatedtoarrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, relatedtolist1);
        spinneractivity_meeting_relatedto1.setAdapter(relatedtoarrayAdapter1);

        spinneractivity_meeting_relatedto1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinneractivity_meeting_relatedto2_linear.setVisibility(View.VISIBLE);
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
                spinneractivity_meeting_relatedto2_linear.setVisibility(View.GONE);
            }
        });

        spinneractivity_meeting_relatedto2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                spinneractivity_meeting_relatedto2_linear.setVisibility(View.GONE);
            }
        });

    }

    private void priorityasynctask() {
        ArrayList<String> priorityarrayList = new ArrayList<>();
        PriorityAsyncTask.PriorityAdapter priorityadapter = null;
        String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetPriority";
        PriorityAsyncTask priorityAsyncTask = new PriorityAsyncTask(this, priorityarrayList, priorityadapter, spinner_meeting_priorty1, url);
        priorityAsyncTask.execute(url);

        spinner_meeting_priorty1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        StatusAsyncTask statusAsyncTask = new StatusAsyncTask(this, statusarrayList, statusadapter, spinner_meeting_priorty2, url);
        statusAsyncTask.execute(url);

        spinner_meeting_priorty2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                ModelClass data =(ModelClass)parent.getItemAtPosition(position);
                String asignselecteid = data.getContactID();

                Toast.makeText(getApplicationContext(),"Selected ID"+ asignselecteid,Toast.LENGTH_SHORT).show();
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


        ReminderBeforeAsyncTask reminderAsyncTask = new ReminderBeforeAsyncTask(this, reminderarrayList, reminderadapter, spinner_meeting_reminderbefore, url);
        reminderAsyncTask.execute(url);
        spinner_meeting_reminderbefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    
               ModelClass modelClass = (ModelClass)parent.getItemAtPosition(position);
                String asignselecteid = modelClass.getContactID();

                Toast.makeText(getApplicationContext(),"Selected ID"+ asignselecteid,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void assigntodata() {


        String stringassginto_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/getEmployee/" + aciviityuserId;
        new assignToaAsynktask().execute(stringassginto_url);


        spinner_add_meeting_assignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 ModelClass data =(ModelClass)modelClassassignTo_ArrayList.get(position);
                  String asignselecteid = data.getContactID();

                 Toast.makeText(getApplicationContext(),"Selected ID"+ asignselecteid,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findID() {
        spinner_add_meeting_assignedTo = (Spinner) findViewById(R.id.add_meeting_assignedTo);
        edt_meeting_subject = (EditText) findViewById(R.id.edt_meeting_subject);
        spinner_meeting_contact_type1 = (Spinner) findViewById(R.id.spinner_meeting_contact_type);
        spinner_meeting_contact_type2 = (Spinner) findViewById(R.id.spinner_meeting_contact_type2);

        addmeeting_edt_selectdate = (EditText) findViewById(R.id.addmeeting_edt_selectdate);
        addmeeting_spinner_starttime = (Spinner) findViewById(R.id.addmeeting_spinner_starttime);
        addmeeting_spinner_endtime = (Spinner) findViewById(R.id.addmeeting_spinner_endtime);
        spinneractivity_meeting_relatedto1 = (Spinner) findViewById(R.id.spinneractivity_meeting_relatedto1);
        spinneractivity_meeting_relatedto2 = (Spinner) findViewById(R.id.spinneractivity_meeting_relatedto2);
        spinneractivity_meeting_relatedto2_linear = (LinearLayout) findViewById(R.id.spinneractivity_meeting_relatedto2_linear);
        spinner_meeting_priorty1 = (Spinner) findViewById(R.id.spinner_meeting_priorty1);
        spinner_meeting_priorty2 = (Spinner) findViewById(R.id.spinner_meeting_priorty2);

        check_addnewmeeting_emailnotification = (CheckBox) findViewById(R.id.check_addnewmeeting_emailnotification);
        check_addnewmeeting_smsnotification = (CheckBox) findViewById(R.id.check_addnewmeeting_smsnotification);

        spinner_meeting_reminderbefore = (Spinner) findViewById(R.id.spinner_meeting_reminderbefore);
        edt_meeting_remark = (EditText) findViewById(R.id.edt_meeting_remark);
        edt_meeting_venue = (EditText) findViewById(R.id.edt_activities_meeting_venue);
        saveBtn = (Button) findViewById(R.id.add_meeting_saveBtn);
        btn_save_n_new = (Button) findViewById(R.id.add_meeting_save_n_NewBtn);
        btn_cancel = (Button) findViewById(R.id.add_meeting_cancelBtn);
        ivbackaddmeeting = (ImageView) findViewById(R.id.iv_back_addmeeting);

    }

    @Override
    public void onClick(View v) {
        if (v == addmeeting_edt_selectdate) {
            setDate();
        } else if (v == ivbackaddmeeting) {
            finish();

        }
    }


    private class ContactType2AsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
            contacttype2list2.clear();


            try {
                JSONArray jsonArray = new JSONArray(s);
                noofobjects = jsonArray.length();
                Log.v("Number of json Obj " + noofobjects, "   pd Objects.....");
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);
                    String contactType = jObj.getString("FullName");

                    contactID = jObj.getString("PK_ContactID");
                    // Log.v("contactName",contactName);
                    contacttpe1reponse_code = jObj.getString("ResponseCode");

                    contacttype2list2.add(contactType);
                    modelClassContactType2ArrayList.add(new ModelClass(contactType, contactID));


                }


            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (contacttpe1reponse_code.equals("1")) {

                    System.out.println("respnse code 1............ inside condition");
                    contactype2Adapter2 = new ArrayAdapter<String>(MeetingActivity.this, android.R.layout.simple_list_item_1, contacttype2list2);
                    spinner_meeting_contact_type2.setAdapter(contactype2Adapter2);
                    contactype2Adapter2.notifyDataSetChanged();

                    //  source_detail_adapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + noofobjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    //  toast.show();

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();

                }

            } catch (Exception e) {
                Toast.makeText(MeetingActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();

            }

        }
    }

    private class assignToaAsynktask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog = new ProgressDialog(MeetingActivity.this);

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
                    String asignToname = jObj.getString("EmpName");
                    String assignto_id = jObj.getString("PK_EmployeeID");
                    contacttpe1reponse_code = jObj.getString("ResponseCode");
                    spinnerasstionTo.clear();

                    spinnerasstionTo.add(asignToname);
                       modelClassassignTo_ArrayList.add(new ModelClass(asignToname,assignto_id));

                }
             /*   for (int i = 0; i<spinnerasstionTo.size();i++){
                    ModelClass data = new ModelClass();
                    data.setContactName();
                }*/

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (contacttpe1reponse_code.equals("1")) {
                    System.out.println("respnse code 1............ inside condition");

                    ArrayAdapter<String> spinner_assignToAdapter = new ArrayAdapter<String>(MeetingActivity.this, android.R.layout.simple_spinner_item, spinnerasstionTo);
                    spinner_assignToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_add_meeting_assignedTo.setAdapter(spinner_assignToAdapter);

                    spinner_assignToAdapter.notifyDataSetChanged();

                    //  source_detail_adapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + noofobjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    //   toast.show();

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();

                }

            } catch (Exception e) {
                Toast.makeText(MeetingActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();

            }

        }


    }


    private class RelatedtoAsyncTask2 extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MeetingActivity.this);
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

                    relatedto_response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);

                    if (relatedto.equals("null") || relatedto.equals("")) {
                        spinneractivity_meeting_relatedto2_linear.setVisibility(View.GONE);
                    } else {
                        relatedtolist2.add(relatedto);

                        modelClassRelatedto2ArrayList.add(new ModelClass(relatedto, PK_RelatedID));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (relatedto_response_code.equals("1")) {

                Toast.makeText(getApplicationContext(), "responsecode 1", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(MeetingActivity.this, android.R.layout.simple_list_item_1, relatedtolist2);

              //  arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinneractivity_meeting_relatedto2.setAdapter(arrayAdapter2);
            } else {
                spinneractivity_meeting_relatedto2_linear.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_LONG).show();
            }
        }
    }

}

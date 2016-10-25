package quaere.com.realtorsmile.UserActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import quaere.com.realtorsmile.AssignToAsyncTask;
import quaere.com.realtorsmile.ContactTypeAsyncTask;
import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.SessionManager;
import quaere.com.realtorsmile.UrlUtilities;

public class CallActivity extends Activity {

    private Button btn_startcall,btn_stop_call;
    private EditText edt_call_mintue, edt_call_second ,edt_call_startdate;
    private Spinner spi_calltime, spinner_assignto,spiner_contacttype1,spiner_contacttype2,spinner_relatedto1,spinner_relatedto2;

    private TextView tv_call_timer_start;

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    int secs;
    int mins;
    private  SimpleDateFormat dateFormatter;
    private Calendar cal;
    int year;
    int month;
    int day,no_of_objects;
    private  RadioGroup radioGroup, radioGroup_currentcall;
    private  RadioButton radioButton;

    private  String selectedtext_rb, selectedtext_contacttype1;
    private String aciviityuserId,response_code;
    private ArrayList<String> contacttype2list2 = new ArrayList<>();
    private ArrayList<String> startTimelist = new ArrayList<>();
    private ArrayAdapter contactype2Adapter2;
    private LinearLayout visible_relatedto;
      ArrayList<String> relatedtolist2= new ArrayList<>();

    ArrayList<ModelClass> relatedtolist2data = new ArrayList<>();

   private ArrayList<ModelClass> modelClassContactType2ArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> map = sessionManager.getUserDetails();
        aciviityuserId = map.get(SessionManager.KEY_EMAIL);

        findviewbyids();
        callfunction();
        startTime();
        assignTo();
        contacttypeasynktask();
        relatedtodata();



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) group.findViewById(checkedId);
                if (null != radioButton && checkedId > -1) {

                    selectedtext_rb = radioButton.getText().toString();
                    Toast.makeText(CallActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                    //  radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                }

            }
        });

        radioGroup_currentcall.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) group.findViewById(checkedId);
                if (null != radioButton && checkedId > -1) {

                    selectedtext_rb = radioButton.getText().toString();
                    Toast.makeText(CallActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                    //  radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                }

            }
        });

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DATE);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yy",Locale.US);
        cal.set(year,month,day);
        edt_call_startdate.setText(dateFormatter.format(cal.getTime()));
        edt_call_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdate();
            }
        });


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
                        edt_call_startdate.setText(dateFormatter.format(cal.getTime()));

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

        ArrayAdapter<String> starttimeadapter = new ArrayAdapter<String>(CallActivity.this, android.R.layout.simple_spinner_dropdown_item, startTimelist);
        spi_calltime.setAdapter(starttimeadapter);
        spi_calltime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void callfunction(){
        btn_startcall.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
                btn_startcall.setVisibility(View.GONE);
                btn_stop_call.setVisibility(View.VISIBLE);
                edt_call_second.setText("");
                edt_call_mintue.setText("");

            }
        });

        btn_stop_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = String.valueOf(secs);
                String m = String.valueOf(mins);
                edt_call_second.setText(s);

                edt_call_mintue.setText(m);
                timeSwapBuff += timeInMilliseconds;
                // tv_call_timer_start.setText("0:00");
                updatedTime = 0;
                customHandler.removeCallbacks(updateTimerThread);

                btn_startcall.setVisibility(View.VISIBLE);
                btn_stop_call.setVisibility(View.GONE);
            }
        });
    }

    private void assignTo(){

         ArrayList<String> assignlist = new ArrayList<>();
        AssignToAsyncTask.AssignToAdapter assignToAdapter = null ;
        String stringassginto_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/getEmployee/" + aciviityuserId;
        AssignToAsyncTask  assignToAsyncTask = new AssignToAsyncTask(this,assignlist,assignToAdapter,spinner_assignto,stringassginto_url);
        assignToAsyncTask.execute(stringassginto_url);

    }

    private void contacttypeasynktask() {
        //  Toast.makeText(getApplicationContext(), "selected item is " + item, Toast.LENGTH_LONG).show();
       String addcontacttype1url = UrlUtilities.CONTACTTYPE1_URL;
        //  new ContactType1AsyncTask().execute(addcontacttype1url);

        ArrayList<String> contacttypenamelist1 = new ArrayList<String>();
        ArrayList<String> contacttypeidlist1 = new ArrayList<String>();

        ArrayList<ModelClass> customListArray = new ArrayList<>();
        ContactTypeAsyncTask.ContactTypeAdapter contactTypeAdapter1 = null;
        ContactTypeAsyncTask contactTypeAsyncTask = new ContactTypeAsyncTask(this, contacttypenamelist1, contacttypeidlist1, customListArray, contactTypeAdapter1, spiner_contacttype1, addcontacttype1url);
        contactTypeAsyncTask.execute();

        spiner_contacttype1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                //  get_spinneritemcontacttype1 = adapterView.getItemAtPosition(position).toString();

                ModelClass selectedItem = (ModelClass) adapterView.getItemAtPosition(position);
                selectedtext_contacttype1 = selectedItem.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id  " + selectedtext_contacttype1, Toast.LENGTH_SHORT).show();

                String contacttypeNameUrl = UrlUtilities.CONTACTTYPE2_URL + aciviityuserId + "/" + selectedtext_contacttype1;
                new ContactType2AsyncTask().execute(contacttypeNameUrl);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spiner_contacttype2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void relatedtodata() {

        ArrayList<String> relatedtolist1 = new ArrayList<>();
        final  ArrayAdapter arrayAdapter = null;
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
        spinner_relatedto1.setAdapter(relatedtoarrayAdapter1);
        spinner_relatedto1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             //   visible_relatedto.setVisibility(View.VISIBLE);
                String selected_realatedto = parent.getItemAtPosition(position).toString();
                Log.v("Selected ITem :", selected_realatedto);

                String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetRelated/" + aciviityuserId + "/" + selected_realatedto;
                // RelatedToAsyncTask relatedtoAsyncTask = new RelatedToAsyncTask(MeetingActivity.this, relatedtolist2,relatedtoarrayAdapter2 , spinneractivity_meeting_relatedto2, url);
                // relatedtoAsyncTask.execute();


                new RelatedtoAsyncTask2().execute(url);
              //  new RelatedtoAsyncTask2().execute(url);

                //  new RelatedtoAsyncTask().execute(url);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                visible_relatedto.setVisibility(View.GONE);
            }
        });

        spinner_relatedto2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // String selected_realatedtoid = parent.getItemAtPosition(position).toString();
              ModelClass modelClassdata = (ModelClass)relatedtolist2data.get(position);
                String selected_realatedtoid = modelClassdata.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id  " + selected_realatedtoid, Toast.LENGTH_SHORT).show();
                Log.v("Selected ITem :", selected_realatedtoid);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                visible_relatedto.setVisibility(View.GONE);
            }
        });

    }
 private void    findviewbyids()
 {
     spinner_assignto =(Spinner)findViewById(R.id.addnew_call_spiner_assignto);
     radioGroup = (RadioGroup)findViewById(R.id.radiogroup_call);

     radioGroup_currentcall = (RadioGroup)findViewById(R.id.rd_currentcall);

     edt_call_startdate =(EditText)findViewById(R.id.edt_call_startdate);
     spi_calltime =(Spinner)findViewById(R.id.spi_call_time);
     btn_startcall = (Button) findViewById(R.id.btn_call_timerstart);
     btn_stop_call = (Button) findViewById(R.id.btn_call_timerstop);
     edt_call_mintue =(EditText)findViewById(R.id.edt_calltime_mintue);
     edt_call_second =(EditText)findViewById(R.id.edt_calltime_sec);
     tv_call_timer_start=(TextView)findViewById(R.id.tv_call_timer_start);
     spiner_contacttype1 =(Spinner)findViewById(R.id. addnew_call_spiner_contacttype1);
     spiner_contacttype2 =(Spinner)findViewById(R.id. edt_addnewcall_spiner_contacttype2);
     spinner_relatedto1 =(Spinner)findViewById(R.id. spinner_call_realatedto);
     spinner_relatedto2 =(Spinner)findViewById(R.id. spinner_call_realatedto2);
     visible_relatedto =(LinearLayout)findViewById(R.id.linear_spinner_call_realatedto2);


    }


    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

          //  updatedTime = timeSwapBuff + timeInMilliseconds;
            updatedTime = timeInMilliseconds;
            tv_call_timer_start.setText("");
             secs = (int) (updatedTime / 1000);
             mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            tv_call_timer_start.setText("" + mins + ":"
                    + String.format("%02d", secs) );
            customHandler.postDelayed(this, 0);
        }

    };



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
                no_of_objects = jsonArray.length();
                Log.v("Number of json Obj " + no_of_objects, "   pd Objects.....");
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);
                    String contactType = jObj.getString("FullName");

                   String contactID = jObj.getString("PK_ContactID");
                    // Log.v("contactName",contactName);
                    response_code = jObj.getString("ResponseCode");

                      contacttype2list2.add(contactType);



                    modelClassContactType2ArrayList.add(new ModelClass(contactType, contactID));



                }


            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (response_code.equals("1")) {

                    System.out.println("respnse code 1............ inside condition");
                    contactype2Adapter2 = new ArrayAdapter<String>(CallActivity.this, android.R.layout.simple_list_item_1, contacttype2list2);
                    spiner_contacttype2.setAdapter(contactype2Adapter2);
                    contactype2Adapter2.notifyDataSetChanged();

                    //  source_detail_adapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + no_of_objects, Toast.LENGTH_LONG);
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
                Toast.makeText(CallActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();

            }

        }
    }
    private class RelatedtoAsyncTask2 extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CallActivity.this);
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
                        visible_relatedto.setVisibility(View.GONE);
                    } else {
                        relatedtolist2.add(relatedto);
                        visible_relatedto.setVisibility(View.VISIBLE);
                        relatedtolist2data.add(new ModelClass(relatedto, PK_RelatedID));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (response_code.equals("1")) {

                Toast.makeText(getApplicationContext(), "responsecode 1", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(CallActivity.this, android.R.layout.simple_list_item_1, relatedtolist2);

                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner_relatedto2.setAdapter(arrayAdapter2);
            } else {
                visible_relatedto.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_LONG).show();
            }
        }
    }

}

package quaere.com.realtorsmile.UserActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import quaere.com.realtorsmile.AssignToAsyncTask;
import quaere.com.realtorsmile.ContactTypeAsyncTask;
import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.PriorityAsyncTask;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.RelatedToAsyncTask;
import quaere.com.realtorsmile.ReminderBeforeAsyncTask;
import quaere.com.realtorsmile.SessionManager;
import quaere.com.realtorsmile.StatusAsyncTask;
import quaere.com.realtorsmile.UrlUtilities;

public class Task extends Activity implements View.OnClickListener{

private ArrayList<String> addcontacttypelist2 = new ArrayList<>();
    private  ArrayList<ModelClass> modelclasscontacttype2list = new ArrayList<>();
    private ArrayList<String> startTimelist = new ArrayList<>();

    private String aciviityuserId, getselected_itecontacttype1;
    private String reponse_code;
    ArrayList<ModelClass>modelrelatedtolist2 = new ArrayList<>();
    ArrayList<String>relatedtolist2 = new ArrayList<>();
    private int noofobjects;

    private Spinner task_assignedTo ,spinnertask_contact_type1,spinnertask_contact_type2,
            spinnertask_starttime,spinnertask_relatedto1,spinnertask_relatedto2,
            spinnertask_priorty1,spinnertask_priorty2 ,spinnertask_reminderbefore;
    private EditText edt_task_subject,edttask_selectdate, edt_task_remark;
    private CheckBox check_task_emailnotification,check_task_smsnotification;
    private Button btn_task_saveBtn,btn_task_save_n_NewBtn, btn_task_cancelBtn;

    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    private ArrayAdapter relatedtoAdapter;
    private LinearLayout spinneractivity_task_relatedto2_linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_task);


            findID();

        edt_task_subject.setOnClickListener(this);
        edttask_selectdate.setOnClickListener(this);
        edt_task_remark.setOnClickListener(this);
        btn_task_saveBtn.setOnClickListener(this);
        btn_task_save_n_NewBtn.setOnClickListener(this);
        btn_task_cancelBtn.setOnClickListener(this);
            SessionManager sessionManager = new SessionManager(this);
            HashMap<String, String> emailid = sessionManager.getUserDetails();
            //  HashMap<String, String> email = sessionManager.getUserDetails();
            aciviityuserId = emailid.get(SessionManager.KEY_EMAIL);


        // setdate
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year, month, day);
        edttask_selectdate.setText(dateFormatter.format(cal.getTime()));
        //start time
        startTime();
        //assignTo
        assigntodata();
        contacttypedata();

        realtedtodata();
        priorityasynctask();
        statusasynctask();
        reminderasynctask();
    }

    private void setdate(){
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
                        edttask_selectdate.setText(dateFormatter.format(cal.getTime()));

                    }
                }, year, month, day);
        dpd.show();
    }

    private void startTime(){
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

        ArrayAdapter<String> starttimeadapter = new ArrayAdapter<String>(Task.this,android.R.layout.simple_spinner_dropdown_item,startTimelist);
        spinnertask_starttime.setAdapter(starttimeadapter);
        spinnertask_starttime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void assigntodata() {

        ArrayList<String> assignToArray = new ArrayList<>();

        AssignToAsyncTask.AssignToAdapter spinner_assignToAdapter = null;

        String stringassginto_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/getEmployee/" + aciviityuserId;
      //  new assignToaAsynktask().execute(stringassginto_url);
        AssignToAsyncTask assignToAsyncTask = new AssignToAsyncTask(this, assignToArray, spinner_assignToAdapter, task_assignedTo, stringassginto_url);
             assignToAsyncTask.execute();

        task_assignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ModelClass modelClass = (ModelClass)parent.getItemAtPosition(position);
                 String ids =  modelClass.getContactID();
                Toast.makeText(getApplicationContext(), "Selected id " + ids, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void contacttypedata() {
        //  Toast.makeText(getApplicationContext(), "selected item is " + item, Toast.LENGTH_LONG).show();
        String addcontacttype1url = UrlUtilities.CONTACTTYPE1_URL;
        //  new ContactType1AsyncTask().execute(addcontacttype1url);

        ArrayList<String> contacttypenamelist1 = new ArrayList<String>();
        ArrayList<String> contacttypeidlist1 = new ArrayList<String>();

        ArrayList<ModelClass> customListArray = new ArrayList<>();
        ContactTypeAsyncTask.ContactTypeAdapter contactTypeAdapter1 = null;
        ContactTypeAsyncTask contactTypeAsyncTask = new ContactTypeAsyncTask(this, contacttypenamelist1, contacttypeidlist1, customListArray, contactTypeAdapter1, spinnertask_contact_type1, addcontacttype1url);
        contactTypeAsyncTask.execute();


        spinnertask_contact_type1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ModelClass data = (ModelClass) adapterView.getItemAtPosition(i);
                getselected_itecontacttype1 = data.getContactID();
                Toast.makeText(getApplicationContext(), "Selected Id  " + getselected_itecontacttype1, Toast.LENGTH_SHORT).show();

                String contacttypeNameUrl = UrlUtilities.CONTACTTYPE2_URL + aciviityuserId + "/" + getselected_itecontacttype1;
                new ContactType2AsyncTask().execute(contacttypeNameUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnertask_contact_type2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   ModelClass data = modelclasscontacttype2list.get(i);
                    String id = data.getContactID();
                Toast.makeText(getApplicationContext(), "Selected Id  " + id, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void realtedtodata(){
        ArrayList<String> relatedtolist1 = new ArrayList<>();

        relatedtolist1.add("Property");
        relatedtolist1.add("Potential");
        relatedtolist1.add("Project");
        relatedtolist1.add("Listing");
        relatedtolist1.add("Case");
        relatedtolist1.add("Closing");
        relatedtolist1.add("Booking");
        relatedtolist1.add("Transaction");

        final ArrayList<String> assignToArray = new ArrayList<>();
        Activity activity;

     relatedtoAdapter  =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,relatedtolist1);
        spinnertask_relatedto1.setAdapter(relatedtoAdapter);

        spinnertask_relatedto1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String isiss = parent.getItemAtPosition(position).toString();

                String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetRelated/" + aciviityuserId + "/" + isiss;


                new RelatedtoAsyncTask2().execute(url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
          spinnertask_relatedto2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  ModelClass data = modelrelatedtolist2.get(position);
                  String ids = data.getContactID();

                  Toast.makeText(getApplicationContext(), "Selected Id  " + ids, Toast.LENGTH_SHORT).show();

              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
          });



    }


    private void priorityasynctask() {
        ArrayList<String> priorityarrayList = new ArrayList<>();
        PriorityAsyncTask.PriorityAdapter priorityadapter = null;
        String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetPriority";
        PriorityAsyncTask priorityAsyncTask = new PriorityAsyncTask(this, priorityarrayList, priorityadapter, spinnertask_priorty1, url);
        priorityAsyncTask.execute(url);

        spinnertask_priorty1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        StatusAsyncTask statusAsyncTask = new StatusAsyncTask(this, statusarrayList, statusadapter, spinnertask_priorty2, url);
        statusAsyncTask.execute(url);

        spinnertask_priorty2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


        ReminderBeforeAsyncTask reminderAsyncTask = new ReminderBeforeAsyncTask(this, reminderarrayList, reminderadapter, spinnertask_reminderbefore, url);
        reminderAsyncTask.execute(url);
        spinnertask_reminderbefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void findID(){
        task_assignedTo =(Spinner)findViewById(R.id.task_assignedTo);
        edt_task_subject =(EditText)findViewById(R.id.edt_task_subject);
        spinnertask_contact_type1 =(Spinner)findViewById(R.id.spinnertask_contact_type1);
        spinnertask_contact_type2 =(Spinner)findViewById(R.id.spinnertask_contact_type2);
        edttask_selectdate =(EditText)findViewById(R.id.edttask_selectdate);
        spinnertask_starttime =(Spinner)findViewById(R.id.spinnertask_starttime);
       // spinnertask_endtime =(Spinner)findViewById(R.id.spinnertask_endtime);
        spinnertask_relatedto1 = (Spinner)findViewById(R.id.spinnertask_relatedto1);
        spinnertask_relatedto2 = (Spinner)findViewById(R.id.spinner_task_relatedto2);
        spinneractivity_task_relatedto2_linear=(LinearLayout)findViewById(R.id.spinneractivity_task_relatedto2_linear);

        spinnertask_priorty1 = (Spinner)findViewById(R.id.spinnertask_priorty1);
        spinnertask_priorty2 =(Spinner)findViewById(R.id.spinnertask_priorty2);
        check_task_emailnotification =(CheckBox)findViewById(R.id.check_task_emailnotification);
        check_task_smsnotification =(CheckBox)findViewById(R.id.check_task_smsnotification);
        spinnertask_reminderbefore =(Spinner)findViewById(R.id.spinnertask_reminderbefore);
        edt_task_remark =(EditText)findViewById(R.id.edt_task_remark);
        btn_task_saveBtn =(Button)findViewById(R.id.btn_task_saveBtn);
        btn_task_save_n_NewBtn =(Button)findViewById(R.id.btn_task_save_n_NewBtn);
        btn_task_cancelBtn =(Button)findViewById(R.id.btn_task_cancelBtn);

    }

    @Override
    public void onClick(View v) {
    if(v == edttask_selectdate){
    setdate();
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



            try {
                JSONArray jsonArray = new JSONArray(s);
                noofobjects = jsonArray.length();
                Log.v("Number of json Obj " + noofobjects, "   pd Objects.....");
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);
                    String contactType = jObj.getString("FullName");
                    String contactID = jObj.getString("PK_ContactID");
                    // Log.v("contactName",contactName);
                    reponse_code = jObj.getString("ResponseCode");

                    addcontacttypelist2.add(contactType);

                  /*  contacttypename2.add(contactType);
                    contacttypeid2.add(contactID);*/

                    modelclasscontacttype2list.add(new ModelClass(contactType, contactID));
                }
               /* for (int i = 0; i < contacttypename2.size(); i++) {
                    ModelClass data = new ModelClass();
                    data.setContactName(contacttypename2.get(i));
                    data.setContactID(contacttypeid2.get(i));
                    modelclasscontacttype2list.add(data);
                }*/


            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (reponse_code.equals("1")) {

                    System.out.println("respnse code 1............ inside condition");
                    ArrayAdapter contacttype2Adapter2 = new ArrayAdapter<String>(Task.this, android.R.layout.simple_list_item_1, addcontacttypelist2);
                    spinnertask_contact_type2.setAdapter(contacttype2Adapter2);
                    contacttype2Adapter2.notifyDataSetChanged();

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
                Toast.makeText(Task.this, "Connection  Failed ", Toast.LENGTH_LONG).show();

            }

        }
    }
    private class RelatedtoAsyncTask2 extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Task.this);
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
            try {
                JSONArray jsonArray = new JSONArray(s);
                int noOfObjects = jsonArray.length();
                Log.v("Number of json Obj " + noOfObjects, "   pd Objects.....");
                // pd.dismiss();
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);

                    String relatedto = jObj.getString("Related");
                    String PK_RelatedID = jObj.getString("PK_RelatedID");

                    reponse_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);

                    if (relatedto.equals("null") || relatedto.equals("")) {
                        spinneractivity_task_relatedto2_linear.setVisibility(View.GONE);
                    } else {
                        spinneractivity_task_relatedto2_linear.setVisibility(View.VISIBLE);
                        relatedtolist2.add(relatedto);

                        modelrelatedtolist2.add(new ModelClass(relatedto, PK_RelatedID));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (reponse_code.equals("1")) {

                Toast.makeText(getApplicationContext(), "responsecode 1", Toast.LENGTH_LONG).show();
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(Task.this, android.R.layout.simple_list_item_1, relatedtolist2);

                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinnertask_relatedto2.setAdapter(arrayAdapter2);
            } else {
                spinneractivity_task_relatedto2_linear.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_LONG).show();
            }
        }
    }

}

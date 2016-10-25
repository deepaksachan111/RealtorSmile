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

public class FollowUp extends Activity implements View.OnClickListener{



    ArrayList<ModelClass> folloup_realtedto1list = new ArrayList<>();
    ArrayList<String>  relatedto1_namelist =new ArrayList<>();
    ArrayList<String>  relatedto1_idlist =new ArrayList<>();


    ArrayList<ModelClass> assignto_arraylist = new ArrayList<>();
    ArrayList<String> assignto_name_arraylist = new ArrayList<>();
    ArrayList<String> assignto_id_arraylist = new ArrayList<>();

    private ArrayList<ModelClass> modeladdfollowup_contacttypelist2 = new ArrayList<>();
    private ArrayList<String> addfollowup_contacttype_namelist2 = new ArrayList<>();
    private  ArrayList<String> addfollowup_contacttype_idlist2 = new ArrayList<>();

    private ArrayAdapter<String> folloup_contacttypeAdapter;
    private ArrayAdapter<String> folloup_contacttypeAdapter2;

    ArrayList<String> spinnerasstionTo = new ArrayList<>();
    private ArrayList<String> startTimelist = new ArrayList<>();

    private String aciviityuserId, get_spinneritem;
    private String  addmeetingurl, assignto_name,meetingreponse_code;
    private int noofobjects;

    private Spinner spinner_followup_assignedTo,popup_followup_contact_type1,popup_followup_contact_type2,
            spinnerpopup_followup_starttime,spinnerpopup_followup_endtime,spinneractivity_followup_relatedto1,spinneractivity_followup_relatedto2,
            spinner_popup_followup_priorty1,spinner_popup_followup_priorty2 ,spinner_popup_followup_reminderbefore;
    private EditText edt_popup_followup_subject1,edtpopup_followup_selectdate, edt_followup_remark;
    private CheckBox check_popup_followup_emailnotification,check_popup_followup_smsnotification;
    private Button popup_followup_saveBtn,popup_followup_save_n_NewBtn, popup_followup_cancelBtn;

    private SimpleDateFormat dateFormatter;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    private ImageView back_btn;

    private String responsecode , relatedto_name,relatedto_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_follow_up);
        findID();

        edt_popup_followup_subject1.setOnClickListener(this);
        edtpopup_followup_selectdate.setOnClickListener(this);
        edt_followup_remark.setOnClickListener(this);
        popup_followup_saveBtn.setOnClickListener(this);
        popup_followup_save_n_NewBtn.setOnClickListener(this);
        popup_followup_cancelBtn.setOnClickListener(this);
        back_btn.setOnClickListener(this);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        aciviityuserId = emailid.get(SessionManager.KEY_EMAIL);

        // setdate
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        cal.set(year, month, day);
        edtpopup_followup_selectdate.setText(dateFormatter.format(cal.getTime()));
      //start time
        startTime();
      //assignTo
        assigntodata();
        contacttype();
        priorityasynctask();
        statusasynctask();
        reminderasynctask();

        popup_followup_contact_type1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
               // get_spinneritem = adapterView.getItemAtPosition(position).toString();
               // String get_spinneritemcontacttype2 = adapterView.getItemAtPosition(position).toString();

                ModelClass selectedItem = (ModelClass) adapterView.getItemAtPosition(position);
                String contactTypeId = selectedItem.getContactID();
                Toast.makeText(getApplicationContext(), "Selected Id  " + contactTypeId, Toast.LENGTH_SHORT).show();

                String contacttypeNameUrl = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetContactTypeData/" + aciviityuserId + "/" + contactTypeId;
                new ContactType2AsyncTask().execute(contacttypeNameUrl);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        popup_followup_contact_type2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                ModelClass data =(ModelClass)modeladdfollowup_contacttypelist2.get(i);
                String id = data.getContactID();
                //  get_spinneritem = adapterView.getItemAtPosition(i).toString();
                // startasynktask(get_spinneritem);

                Toast.makeText(getApplicationContext(),"Selected id :" +id,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


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



      ArrayAdapter  relatedtoarrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, relatedtolist1);
        spinneractivity_followup_relatedto1.setAdapter(relatedtoarrayAdapter1);

        spinneractivity_followup_relatedto1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinneractivity_followup_relatedto2.setEnabled(false);

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

            }
        });

        spinneractivity_followup_relatedto2 .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //  String selected_realatedtoid = parent.getItemAtPosition(position).toString();
                ModelClass modelClassdata = (ModelClass)folloup_realtedto1list.get(position);
                String selected_realatedtoid = modelClassdata.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id  " + selected_realatedtoid, Toast.LENGTH_SHORT).show();
                Log.v("Selected ITem :", selected_realatedtoid);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                        edtpopup_followup_selectdate.setText(dateFormatter.format(cal.getTime()));

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

        ArrayAdapter<String> starttimeadapter = new ArrayAdapter<String>(FollowUp.this,android.R.layout.simple_spinner_dropdown_item,startTimelist);
        spinnerpopup_followup_starttime.setAdapter(starttimeadapter);
        spinnerpopup_followup_starttime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void assigntodata() {


        String stringassginto_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/getEmployee/" + aciviityuserId;
        new assignToaAsynktask().execute(stringassginto_url);


        spinner_followup_assignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                   // String name = parent.getItemAtPosition(position).toString();
                ModelClass selectedItem = (ModelClass) parent.getItemAtPosition(position);
                String selected_id = selectedItem.getContactID();

                Toast.makeText(getApplicationContext(), "Selected Id  "+ selected_id +"name" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void contacttype(){
        String url="http://rsmile.quaeretech.com/RealtorSmile.svc/GetContactType1";
        ArrayList<String> contacttypenamelist1 = new ArrayList<String>();
        ArrayList<String> contacttypeidlist1 = new ArrayList<String>();
        ArrayList<ModelClass> customListArray = new ArrayList<>();
        ContactTypeAsyncTask.ContactTypeAdapter contactTypeAdapter1 = null;
        ContactTypeAsyncTask  contactTypeAsyncTask = new ContactTypeAsyncTask(this, contacttypenamelist1,contacttypeidlist1,customListArray, contactTypeAdapter1, popup_followup_contact_type1, url);
        contactTypeAsyncTask.execute();
    }

    private void statusasynctask() {
        ArrayList<String> statusarrayList = new ArrayList<>();
        StatusAsyncTask.StatusAdapter statusadapter = null;
        String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetStatus/0";
        StatusAsyncTask statusAsyncTask = new StatusAsyncTask(this, statusarrayList, statusadapter, spinner_popup_followup_priorty2, url);
        statusAsyncTask.execute(url);
        spinner_popup_followup_priorty2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void priorityasynctask() {
        ArrayList<String> priorityarrayList = new ArrayList<>();
        PriorityAsyncTask.PriorityAdapter priorityadapter = null;
        String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetPriority";
        PriorityAsyncTask priorityAsyncTask = new PriorityAsyncTask(this, priorityarrayList, priorityadapter, spinner_popup_followup_priorty1, url);
        priorityAsyncTask.execute(url);

        spinner_popup_followup_priorty1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void reminderasynctask() {
        ArrayList<String> reminderarrayList = new ArrayList<>();
        ReminderBeforeAsyncTask.ReminderBeforeAdapter reminderadapter = null;
        String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetReminder/0";
        ReminderBeforeAsyncTask reminderAsyncTask = new ReminderBeforeAsyncTask(this, reminderarrayList, reminderadapter,spinner_popup_followup_reminderbefore , url);
        reminderAsyncTask.execute(url);
        spinner_popup_followup_reminderbefore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        spinner_followup_assignedTo =(Spinner)findViewById(R.id.popmeeting_followup_assignedTo);
        edt_popup_followup_subject1 =(EditText)findViewById(R.id.edt_popup_followup_subject);
        popup_followup_contact_type1 =(Spinner)findViewById(R.id.popup_followup_contact_type);
        popup_followup_contact_type2 =(Spinner)findViewById(R.id.popup_followup_contact_type2);
        edtpopup_followup_selectdate =(EditText)findViewById(R.id.edtpopup_followup_selectdate);
        spinnerpopup_followup_starttime =(Spinner)findViewById(R.id.spinnerpopup_followup_starttime);
       // spinnerpopup_followup_endtime =(Spinner)findViewById(R.id.spinnerpopup_followup_endtime);
        spinneractivity_followup_relatedto1 = (Spinner)findViewById(R.id.spinneractivity_followup_relatedto1);
        spinneractivity_followup_relatedto2 = (Spinner)findViewById(R.id.spinneractivity_followup_relatedto2);

        back_btn =(ImageView)findViewById(R.id.btn_followup_back);
        spinner_popup_followup_priorty1 = (Spinner)findViewById(R.id.spinner_popup_followup_priorty1);
        spinner_popup_followup_priorty2 =(Spinner)findViewById(R.id.spinner_popup_followup_priorty2);
        check_popup_followup_emailnotification =(CheckBox)findViewById(R.id.check_popup_followup_emailnotification);
        check_popup_followup_smsnotification =(CheckBox)findViewById(R.id.check_popup_followup_smsnotification);
        spinner_popup_followup_reminderbefore =(Spinner)findViewById(R.id.spinner_popup_followup_reminderbefore);
        edt_followup_remark =(EditText)findViewById(R.id.edt_followup_remark);
        popup_followup_saveBtn =(Button)findViewById(R.id.popup_followup_saveBtn);
        popup_followup_save_n_NewBtn =(Button)findViewById(R.id.popup_followup_save_n_NewBtn);
        popup_followup_cancelBtn =(Button)findViewById(R.id.popup_followup_cancelBtn);

    }

    @Override
    public void onClick(View v) {
     if(v == edtpopup_followup_selectdate){
         setdate();
     }else if(v == back_btn){
         finish();
     }

    }


    private class assignToaAsynktask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog = new ProgressDialog(FollowUp.this);

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
            spinnerasstionTo.clear();

            try {
                JSONArray jsonArray = new JSONArray(s);
                noofobjects = jsonArray.length();
                Log.v("Number of json Obj " + noofobjects, "   pd Objects.....");
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);
                    assignto_name = jObj.getString("EmpName");
                    String assignto_id = jObj.getString("PK_EmployeeID");
                    meetingreponse_code = jObj.getString("ResponseCode");


                    assignto_name_arraylist.add(assignto_name);
                    assignto_id_arraylist.add(assignto_id);

                }
                for(int i = 0; i <assignto_name_arraylist.size();i++){
                    ModelClass data = new ModelClass();
                     data.setContactName(assignto_name_arraylist.get(i));
                     data.setContactID(assignto_id_arraylist.get(i));

                    assignto_arraylist.add(data);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (meetingreponse_code.equals("1")) {
                    System.out.println("respnse code 1............ inside condition");

                    AssignToAdapter spinner_assignToAdapter = new AssignToAdapter(FollowUp.this, android.R.layout.simple_spinner_item, assignto_arraylist);
                  //  spinner_assignToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_followup_assignedTo.setAdapter(spinner_assignToAdapter);
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
                Toast.makeText(FollowUp.this, "Connection  Failed ", Toast.LENGTH_LONG).show();

            }



        }
     private   class AssignToAdapter extends BaseAdapter {
            private Activity activity;
            private ArrayList<ModelClass> data;
            private LayoutInflater inflators = null;


            public AssignToAdapter(Activity context, int textViewResourceId,
                                   ArrayList<ModelClass> values) {
                this.activity = context;
                this.data = values;
                // inflators = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            public class ViewHolder{
                public TextView contactType;
                public ViewHolder(View vi) {
                    contactType = (TextView) vi.findViewById(R.id.contact_spinner_rowTv);
                }
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View vi = convertView;
                ViewHolder holder;
                inflators = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    vi = inflators.inflate(R.layout.contact_type_spinner_row, null);
                    holder = new ViewHolder(vi);
                    vi.setTag(holder);
                }
                else

                    holder = (ViewHolder) vi.getTag();

                if (data.size() <= 0) {

                } else {

                    ModelClass  tempValues = (ModelClass) data.get(position);
                    holder.contactType.setText(tempValues.getContactName());
                }
                return vi;
            }
        }
    }

    private class ContactType2AsyncTask extends AsyncTask<String,Void,String> {
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
                    responsecode = jObj.getString("ResponseCode");

                       addfollowup_contacttype_namelist2.add(contactType);
                      addfollowup_contacttype_idlist2.add(contactID);

                    modeladdfollowup_contacttypelist2.add(new ModelClass(contactType,contactID));

                }
                for(int i =0;i<addfollowup_contacttype_namelist2.size();i++){
                    ModelClass data = new ModelClass();
                    data.setContactName(addfollowup_contacttype_namelist2.get(i));
                    data.setContactID(addfollowup_contacttype_idlist2.get(i));

                   // modeladdfollowup_contacttypelist2.add(data);
                }




            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (responsecode.equals("1")) {
                    System.out.println("respnse code 1............ inside condition");
                    folloup_contacttypeAdapter2 = new ArrayAdapter<String>(FollowUp.this, android.R.layout.simple_list_item_1, addfollowup_contacttype_namelist2);
                    popup_followup_contact_type2.setAdapter(folloup_contacttypeAdapter2);
                    folloup_contacttypeAdapter2.notifyDataSetChanged();
                    //  source_detail_adapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + noofobjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
               //     toast.show();

                } else {

                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();

                }

            } catch (Exception e) {
                Toast.makeText(FollowUp.this, "Connection  Failed ", Toast.LENGTH_LONG).show();

            }

        }
    }

    private class RelatedtoAsyncTask2 extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FollowUp.this);
            pDialog.setMessage("Loading Details ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
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
            folloup_realtedto1list.clear();
            try {
                JSONArray jsonArray = new JSONArray(s);
                int noOfObjects = jsonArray.length();
                Log.v("Number of json Obj " + noOfObjects, "   pd Objects.....");
                // pd.dismiss();
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);

                    relatedto_name = jObj.getString("Related");
                    relatedto_id = jObj.getString("PK_RelatedID");

                    responsecode = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);


                    if (relatedto_name.equals("null")|| relatedto_name.equals("")) {
                        spinneractivity_followup_relatedto2.setEnabled(false);
                    } else{
                        spinneractivity_followup_relatedto2.setEnabled(true);

                        relatedto1_namelist.add(relatedto_name);
                        relatedto1_idlist.add(relatedto_id);

                    }
                }
                for(int i =0;i< relatedto1_namelist.size();i++){
                   ModelClass modelClass = new ModelClass();
                    modelClass.setContactName(relatedto1_namelist.get(i));
                    modelClass.setContactID(relatedto1_idlist.get(i));

                    folloup_realtedto1list.add(modelClass);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (responsecode.equals("1")) {

             //   Toast.makeText(getApplicationContext(), "responsecode 1", Toast.LENGTH_LONG).show();
                RelatedTo2Adapter arrayAdapter2 = new RelatedTo2Adapter(FollowUp.this, android.R.layout.simple_list_item_1, folloup_realtedto1list);

               // arrayAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinneractivity_followup_relatedto2.setAdapter(arrayAdapter2);
            } else {
                Toast.makeText(getApplicationContext(), "No Value", Toast.LENGTH_LONG).show();
            }
        }

        private   class RelatedTo2Adapter extends BaseAdapter {
            private Activity activity;
            private ArrayList<ModelClass> data;
            private LayoutInflater inflators = null;


            public RelatedTo2Adapter(Activity context, int textViewResourceId,
                                     ArrayList<ModelClass> values) {
                this.activity = context;
                this.data = values;
                // inflators = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            public class ViewHolder{
                public TextView contactType;
                public ViewHolder(View vi) {
                    contactType = (TextView) vi.findViewById(R.id.contact_spinner_rowTv);
                }
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View vi = convertView;
                ViewHolder holder;
                inflators = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    vi = inflators.inflate(R.layout.contact_type_spinner_row, null);
                    holder = new ViewHolder(vi);
                    vi.setTag(holder);
                }
                else

                    holder = (ViewHolder) vi.getTag();

                if (data.size() <= 0) {

                } else {

                    ModelClass  tempValues = (ModelClass) data.get(position);
                    holder.contactType.setText(tempValues.getContactName());
                }
                return vi;
            }
        }
    }
}

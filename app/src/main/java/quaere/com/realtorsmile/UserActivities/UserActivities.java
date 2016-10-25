package quaere.com.realtorsmile.UserActivities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quaere.com.realtorsmile.ContactActivity.ContactProfileActivity;
import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.SessionManager;

public class UserActivities extends Activity implements View.OnClickListener{

     private  ActivityDetailAdapter activityadapter;
     private int noOfObjects;
     private  ProgressDialog pDialog;
     private String aciviityuserId, acti_subject, acivity_contact, aciviity_type, aciviitycontact_mobile, aciviitycontact_email,activitysearch_url;

    private ArrayList<ModelClass> activitycustomSearchlist = new ArrayList<>();
    private List<String> activitySubjectArrayList = new ArrayList<String>();
    private List<String> activitycontactArrayList = new ArrayList<String>();
    private List<String> activity_typeArrayList = new ArrayList<String>();

    private ArrayList<String> searchFor = new ArrayList<String>();

    private  boolean isNotAdded = true;
    private RelativeLayout headerLayout;
    private ListView listview1;
    private EditText edtsearch1;
    private Spinner searchSpinner1;
    private Button searchBtn1;
    private Toolbar toolbar1;
    private TextView home1,add_new1,mail1,msg1,delete1;
    private CheckBox   checkBox_header1;
    private  SparseBooleanArray mChecked = new SparseBooleanArray();
    private String activity_subject,activitycontact,activity_type,activity_response_code,selectedactivityspinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activities);
        findID();

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        aciviityuserId = emailid.get(SessionManager.KEY_EMAIL);

        activitysearch_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetActivityRecords/" + aciviityuserId;
        new ActitivityAsyncTask().execute(activitysearch_url);


        if (isNotAdded) {
            checkBox_header1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < activitycustomSearchlist.size(); i++) {
                        mChecked.put(i, checkBox_header1.isChecked());
                    }
                    activityadapter.notifyDataSetChanged();
                }
            });
            isNotAdded = false;
        }
        // Spinner Drop down elements
        searchFor = new ArrayList<String>();
        searchFor.add("All");
        searchFor.add("Subject");
        searchFor.add("ActivityType");
        searchFor.add("Contact");
        searchFor.add("AssignedTo");
        searchFor.add("Priority");
        searchFor.add("Status");

        //adding data to search spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchFor);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner1.setAdapter(dataAdapter);
        searchSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedactivityspinner = parent.getItemAtPosition(position).toString();
                Log.v("Selected ITem :", selectedactivityspinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtsearch1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String text = searchEt.getText().toString().toLowerCase(Locale.getDefault());
                //adapter.filter(text);
                activityadapter.getFilter().filter(edtsearch1.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

public void findID(){

    headerLayout = (RelativeLayout) findViewById(R.id.activities_listview_header);
    listview1 = (ListView) findViewById(R.id.activities_list_view);
    edtsearch1 = (EditText) findViewById(R.id.activies_search_editText);
    searchSpinner1 = (Spinner) findViewById(R.id.activies_search_spinner);
    searchBtn1 = (Button) findViewById(R.id.activies_search_btn);
    checkBox_header1 = (CheckBox) headerLayout.findViewById(R.id.activities_checkBox_header);

    toolbar1 = (Toolbar) findViewById(R.id.activities_toolbar);
    home1 = (TextView) toolbar1.findViewById(R.id.activities_tool_home);
    add_new1 = (TextView) toolbar1.findViewById(R.id.activities_addNew);
    mail1 = (TextView) toolbar1.findViewById(R.id.activities_mail);
    msg1 = (TextView) toolbar1.findViewById(R.id.activities_sms);
    delete1 = (TextView) toolbar1.findViewById(R.id.activities_delete);

    home1.setOnClickListener(this);
    add_new1.setOnClickListener(this);
    mail1.setOnClickListener(this);
    msg1.setOnClickListener(this);
    delete1.setOnClickListener(this);
    searchBtn1.setOnClickListener(this);

}

    public void addnewActivity() {
        final PopupMenu popupMenu = new PopupMenu(UserActivities.this, add_new1);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup_activities, popupMenu.getMenu());

 /*   popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Selectedmenuitem = (String) menuItem.getTitle();
            Toast.makeText(getApplicationContext(), "You have Selected  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
            Log.v("Selected Menuitem", Selectedmenuitem);
            Intent intent = new Intent(ContactsActivity.this, AddGeneralActivity.class);
            intent.putExtra("selectitem",Selectedmenuitem);
            startActivity(intent);
            return true;
        }
    });
    popupMenu.show();*/


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //   Toast.makeText(getApplicationContext(), "You have Selected  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();

                if (menuItem.getTitle().equals("Meeting")) {
                    Intent intent = new Intent(UserActivities.this, MeetingActivity.class);
                    // intent.putExtra("selectitem",Selectedmenuitem);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "inside Meeting  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("FollowUp")) {
                    startActivity(new Intent(UserActivities.this, FollowUp.class));
                    Toast.makeText(getApplicationContext(), "Inside Followup  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Inspection")) {
                    startActivity(new Intent(UserActivities.this, Inspection.class));
                    Toast.makeText(getApplicationContext(), "inside Inspection  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Task")) {
                    startActivity(new Intent(UserActivities.this, Task.class));
                    Toast.makeText(getApplicationContext(), "Inside Task  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Call")) {
                    startActivity(new Intent(UserActivities.this, CallActivity.class));
                    Toast.makeText(getApplicationContext(), "Inside Call " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Event")) {
                    startActivity(new Intent(UserActivities.this, Event.class));
                    Toast.makeText(getApplicationContext(), "Inside Event " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
        popupMenu.show();


    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.activities_tool_home :
              onBackPressed();
              break;
          case R.id.activities_addNew:
              addnewActivity();
              Toast.makeText(getApplicationContext(), "add new user will be added soon", Toast.LENGTH_SHORT).show();
              break;
          case R.id.activities_mail:
              sendEmail();
              Toast.makeText(getApplicationContext(), "Mail function will be added soon", Toast.LENGTH_SHORT).show();
              break;

          case R.id.activities_sms:
              sendSms();
              Toast.makeText(getApplicationContext(), "Msg will be added soon", Toast.LENGTH_SHORT).show();
              break;
          case R.id.activities_delete:
              deleteSelectedItem();
              Toast.makeText(getApplicationContext(), "Delete functionality will be added soon", Toast.LENGTH_SHORT).show();
              break;
          case R.id.activies_search_btn:
             contactSearchFilter();
              Toast.makeText(getApplicationContext(), "Searching begins", Toast.LENGTH_SHORT).show();
              break;
      }
    }

    private void sendEmail() {
        ArrayList<String> Email = new ArrayList<>();

        int itemCount = listview1.getCount();
        ModelClass modelClass;
        for (int i = itemCount - 1; i >= 0; i--) {
            if (mChecked.get(i)) {
                modelClass = activitycustomSearchlist.get(i);
                String emailId = modelClass.getContact_Email();
                Log.v("Email Id's", emailId);
                Email.add(emailId);
            }
        }
        String toNumbers = "";
        for (String s : Email) {
            toNumbers = toNumbers + s + ";";
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + toNumbers));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My email's subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "My email's body");


        try {
            startActivityForResult(Intent.createChooser(emailIntent, "Choose App to send email:"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


    private void contactSearchFilter() {
        //String s = searchEt.getText().toString();
        String searchItem = edtsearch1.getText().toString();
        String search_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/SearchContact/" + searchItem + "/" + selectedactivityspinner + "/" + aciviityuserId;
        Toast.makeText(getApplicationContext(), "searching for " + searchItem + " and " + searchItem, Toast.LENGTH_SHORT).show();
        activityadapter.clear();
        activityadapter.notifyDataSetChanged();
        new ContactSearchAsyncTask().execute(search_url);
        //  new SearchFilterAsyncTask().execute();


    }

    private void sendSms() {
        int itemCount = listview1.getCount();
        ModelClass modelClass;
        ArrayList<String> multipleMobile = new ArrayList<String>();
        for (int i = itemCount - 1; i >= 0; i--) {
            if (mChecked.get(i)) {
                modelClass = activitycustomSearchlist.get(i);
                //  Log.v("checked items ",""+i);

                String mobileNo = modelClass.getContact_mobile();
                Log.v("mobileNo", mobileNo);
                multipleMobile.add(mobileNo);
            }
        }
  /*      multipleMobile.add("7834908329");
        multipleMobile.add("9455693525");
        multipleMobile.add("9219498477");
        multipleMobile.add("9716605131");
        multipleMobile.add("7838483225");
*/

         /* SmsManager smsManager = SmsManager.getDefault();
        ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
        ArrayList<PendingIntent> deliveryIntents = new ArrayList<PendingIntent>();

      for (int i = 0; i < multipleMobile.size(); i++) {
            sentIntents.add(PendingIntent.getBroadcast(getApplicationContext(), 0, mSendIntent, 0));
            deliveryIntents.add(PendingIntent.getBroadcast(getApplicationContext(), 0, mDeliveryIntent, 0));
        }

        sm.sendMultiPartTextMessage(mDestAddr,null, parts, sentIntents, deliveryIntents)*/


        String toNumbers = "";
        for (String s : multipleMobile) {
            toNumbers = toNumbers + s + ";";
        }

        //  toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
        String message = "this is a custom message for testing";

        Uri sendSmsTo = Uri.parse("smsto:" + toNumbers);
        Log.v("Contact no :", toNumbers);
        Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, sendSmsTo);
        intent.putExtra("Hi, This sms is for testing from inside the application", message);
        startActivity(intent);

    }


    private void deleteSelectedItem() {

        ArrayList<String> allcontactid = new ArrayList<>();
        ArrayList<String> multiplecontactid = new ArrayList<>();


        if (checkBox_header1.isChecked()) {

            for (int i = 0; i < activitycustomSearchlist.size(); i++) {
                ModelClass modelClass = activitycustomSearchlist.get(i);

                String name = modelClass.getContactID();
                allcontactid.add(name);
            }
            //  all =allcontactid;
            String s = TextUtils.join(",", allcontactid);
            Log.v("All String", s);
            String delete_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/DeleteContact/" + aciviityuserId + "/" + s;
            new deletealldataAsyncTask().execute(delete_url);

            activitycustomSearchlist.clear();
            activityadapter.notifyDataSetChanged();
            activityadapter.remove(activitycustomSearchlist);

        } else {


            // SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
            int itemCount = listview1.getCount();

            for (int i = itemCount - 1; i >= 0; i--) {
                if (mChecked.get(i)) {
                    ModelClass modelClass = activitycustomSearchlist.get(i);

                    String contactID = modelClass.getContactID();
                    multiplecontactid.add(contactID);
                    Toast.makeText(getApplicationContext(), contactID, Toast.LENGTH_SHORT).show();
                    Log.v("Contact  ID ", contactID);
                    activityadapter.remove(activitycustomSearchlist.get(i));
                }
            }
            String multicheckvalue = TextUtils.join(",", multiplecontactid);
            String delete_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/DeleteContact/" + aciviityuserId + "/" + multicheckvalue;
            new deletealldataAsyncTask().execute(delete_url);
            Toast.makeText(getApplicationContext(), multicheckvalue, Toast.LENGTH_SHORT).show();
            mChecked.clear();
            activityadapter.notifyDataSetChanged();


        }


    }
     class ActivityDetailAdapter extends ArrayAdapter {
        int count;
         private ArrayList<ModelClass> mOriginalValues; // Original Values
         private ArrayList<ModelClass> mDisplayedValues;
       // ArrayList<ModelClass> mylistdata;
        private Activity activity;

        // ModelClass tempValues;
        CheckBox checkBox;
        // SparseBooleanArray mChecked= listView.getCheckedItemPositions();

        public ActivityDetailAdapter(UserActivities userActivities,int id,ArrayList<ModelClass> modeldata) {
            super(userActivities,id,modeldata);
            activity = userActivities;
            mDisplayedValues = modeldata;
            mOriginalValues = modeldata;

        }

        @Override
        public int getCount() {
            // Length of our listView
            count = mDisplayedValues.size();
            return count;
        }

        @Override
        public Object getItem(int position) {
            //Current Item
            return position;
        }

        @Override
        public long getItemId(int position) {
            // Current Item's ID
            return position;
        }

        public class ViewHolder {


        }

        @Override
        public View getView( final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (vi == null) {
              //   LayoutInflater sInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                LayoutInflater sInflater = activity.getLayoutInflater();
                vi = sInflater.inflate(R.layout.activities_list_view_row, null, false);

            }


            TextView  subject = (TextView) vi.findViewById(R.id.activities_subject);
            TextView type = (TextView) vi.findViewById(R.id.activities_typeTv);
            TextView contact = (TextView) vi.findViewById(R.id.activities_contact);
            checkBox = (CheckBox) vi.findViewById(R.id.activities_checkBox);
            // checkBox.setVisibility(View.INVISIBLE);

                  /* **************ADDING CONTENTS**************** */

            final ModelClass  tempValues = (ModelClass) mDisplayedValues.get(position);
            subject.setText(tempValues.getContactID());
            String s= tempValues.getContactName();
            type.setText(tempValues.getContact_ConType());
            contact.setText(tempValues.getContactName());



            if(s.equals("null")){
                checkBox.setVisibility(View.INVISIBLE);
                subject.setVisibility(View.INVISIBLE);
                type.setVisibility(View.INVISIBLE);
                contact.setVisibility(View.INVISIBLE);

                Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds : " , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.getView().setPadding(20, 20, 20, 20);
                toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                toast.show();
            }
            // holder.joiningDateTv.setText(tempValues.getJoiningDate());

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelClass modelClass = mDisplayedValues.get(position);

                    String name = modelClass.getContactID();
                   // String email =modelClass.getContact_Email();
                    //Toast.makeText(getApplicationContext(), name + "  "+ email, Toast.LENGTH_SHORT).show();
                    Log.v("Contact  ID ", name);
                    //System.out.println(" Exception is caught here ......." + email);

                    //adapter.remove(customListArray.get(i));
                }
            });

            checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {


                                //Saving Checked Position

                                mChecked.put(position, isChecked);


                                // Find if all the check boxes are true

                                if (isAllValuesChecked()) {


                                    // set HeaderCheck box to true

                                    checkBox_header1.setChecked(isChecked);
                                }

                            } else {


                                mChecked.delete(position);

                                checkBox_header1.setChecked(isChecked);

                            }

                        }
                    });


            // Set CheckBox "TRUE" or "FALSE" if mChecked == true

            checkBox.setChecked((mChecked.get(position) == true ? true : false));




            return vi  ;
        }
        /*



         * Find if all values are checked.
         */

        protected boolean isAllValuesChecked() {

            for (int i = 0; i < count; i++) {
                if (!mChecked.get(i)) {
                    return false;
                }
            }

            return true;
        }
         @Override
         public Filter getFilter() {
             Filter filter = new Filter() {

                 @SuppressWarnings("unchecked")
                 @Override
                 protected void publishResults(CharSequence constraint, FilterResults results) {

                     mDisplayedValues = (ArrayList<ModelClass>) results.values; // has the filtered values
                     notifyDataSetChanged();  // notifies the data with new filtered values
                 }

                 @Override
                 protected FilterResults performFiltering(CharSequence constraint) {
                     FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                     ArrayList<ModelClass> FilteredArrList = new ArrayList<ModelClass>();

                     if (mOriginalValues == null) {
                         mOriginalValues = new ArrayList<ModelClass>(mDisplayedValues); // saves the original data in mOriginalValues
                     }

                     /********
                      *
                      *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                      *  else does the Filtering and returns FilteredArrList(Filtered)
                      *
                      ********/
                     if (constraint == null || constraint.length() == 0) {

                         // set the Original result to return
                         results.count = mOriginalValues.size();
                         results.values = mOriginalValues;
                     } else {
                         constraint = constraint.toString().toLowerCase();
                         for (int i = 0; i < mOriginalValues.size(); i++) {
                             String name = mOriginalValues.get(i).getContactName();
                             String conType = mOriginalValues.get(i).getContact_ConType();
                             String subject = mOriginalValues.get(i).getContactID();
                             if (name.toLowerCase().startsWith(constraint.toString())) {
                                 FilteredArrList.add(new ModelClass(subject,conType,name,""));
                             } else if (conType.toLowerCase().startsWith(constraint.toString())) {
                                 FilteredArrList.add(new ModelClass(subject,conType,name,""));
                             } else if (subject.toLowerCase().startsWith(constraint.toString())) {
                                 FilteredArrList.add(new ModelClass(subject,conType,name,""));
                             }
                         }
                         // set the Filtered result to return
                         results.count = FilteredArrList.size();
                         results.values = FilteredArrList;
                     }
                     return results;
                 }
             };
             return filter;
         }

    }


    public class ActitivityAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserActivities.this);
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
                Log.v("Contact  Response ", response);
                // String response = jsonResponse;


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
                noOfObjects = jsonArray.length();
                Log.v("Number of json Obj " + noOfObjects, "   pd Objects.....");
               // Toast.makeText(UserActivities.this,"Total no of records  "+noOfObjects, Toast.LENGTH_SHORT).show();
                // pd.dismiss();
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);
                    //   Log.v("No of times " + j, "shhhhhhhhh");
                    // int k = j + 1;
                    // detailNo = ""+k;
                    acti_subject = jObj.getString("Subject");
                    acivity_contact = jObj.getString("Contact");
                    aciviity_type = jObj.getString("ActivityType");
                    activity_response_code= jObj.getString("ResponseCode");

                    activitySubjectArrayList.add(acti_subject);
                    activitycontactArrayList.add(acivity_contact);
                    activity_typeArrayList.add(aciviity_type);

                }
                for (int k = 0; k < activitycontactArrayList.size(); k++) {

                    final ModelClass model = new ModelClass();

                    //******* Firstly take data in model object ******//**//*
                    model.setContactID(activitySubjectArrayList.get(k));
                    model.setContactName(activitycontactArrayList.get(k));
                    model.setContact_ConType(activity_typeArrayList.get(k));


                    activitycustomSearchlist.add(model);
                }

                activityadapter = new ActivityDetailAdapter(UserActivities.this,R.layout.contact_list_view_row,activitycustomSearchlist);
                listview1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listview1.setAdapter(activityadapter);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (activity_response_code.equals("1")) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + noOfObjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();
                    pDialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds : " + noOfObjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();
                    pDialog.dismiss();
                }

            } catch (Exception e) {
                Toast.makeText(UserActivities.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

    public class deletealldataAsyncTask extends AsyncTask<String, Void, String> {
          ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserActivities.this);
            pDialog.setMessage("Loading Details ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            HttpClient httpClient = new DefaultHttpClient();

            String s = params[0];
            HttpGet httppost = new HttpGet(params[0]);

            try {
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.v("Contact  Response ", response);
                // String response = jsonResponse;


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }
            return response;
        }


        @Override
        protected void onPostExecute(String s) {

            String res = s;

            try {
                if (activity_response_code.equals("1")) {



                 /*   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.v("inside on Itemclick", "" + position);
                            // Use "if else" only if header is added
                            ModelClass name = customListArray.get(position);
                            String Contactno =  name.getContactName();
                            Toast.makeText(getApplicationContext(), Contactno + "\n"+
                                            checkBox_header.getId() + "\n" + checkBox_header.isChecked(),
                                    Toast.LENGTH_SHORT).show();



                        }
                    });*/
                    Toast toast = Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();
                    pDialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds : " + noOfObjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();
                    pDialog.dismiss();
                }

            } catch (Exception e) {
                Toast.makeText(UserActivities.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

    class ContactSearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UserActivities.this);
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
                Log.v("Contact  Response ", response);
                // String response = jsonResponse;


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
                noOfObjects = jsonArray.length();
                Log.v("Number of json Obj " + noOfObjects, "   pd Objects.....");
                // pd.dismiss();
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);
                    //   Log.v("No of times " + j, "shhhhhhhhh");
                    // int k = j + 1;
                    // detailNo = ""+k;
                    activity_subject = jObj.getString("FirstName");

                    //contact_id = jObj.getString("PK_ContactID");

                    activity_type = jObj.getString("ContactType");

                   // contact_mobile = jObj.getString("Mobile");
                    activitycontact = jObj.getString("Email");
                    activity_response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);


                    activitycustomSearchlist.add(new ModelClass(activity_subject, activity_type, activitycontact,""));


                }
                activityadapter = new ActivityDetailAdapter(UserActivities.this, R.layout.contact_list_view_row, activitycustomSearchlist);
                listview1.setAdapter(activityadapter);
                activityadapter.notifyDataSetChanged();

                if (activity_response_code.equals("1")) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + noOfObjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();
                    pDialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds : " + noOfObjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();
                    pDialog.dismiss();
                }
            }

            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(UserActivities.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                System.out.println(" Exception is caught here ......." + e.toString());
                pDialog.dismiss();
            }
            }




        }
    }




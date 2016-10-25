package quaere.com.realtorsmile.ListingActivities;

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

import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.SessionManager;
import quaere.com.realtorsmile.UserActivities.Event;
import quaere.com.realtorsmile.UserActivities.FollowUp;
import quaere.com.realtorsmile.UserActivities.Inspection;
import quaere.com.realtorsmile.UserActivities.MeetingActivity;
import quaere.com.realtorsmile.UserActivities.Task;

public class ListingActivity extends Activity implements View.OnClickListener {
    private ListingDetailAdapter listingDetailAdapter;
    private int noOfObjects;
    private ProgressDialog pDialog;
    private String listinguserId, listingpropertyownner, listingtittle,response_code,listing_getdata_url,selectedspinner_item;

    private ArrayList<ModelClass> activitycustomSearchlist = new ArrayList<>();
    private List<String> listingproperty_ownerlist = new ArrayList<String>();
    private List<String> listingtittlelist = new ArrayList<String>();
    private List<String> searchFor = new ArrayList<String>();

    private  boolean isNotAdded = true;
    private RelativeLayout headerLayout;
    private ListView listview1;
    private EditText edtsearch1;
    private Spinner searchSpinner1;
    private Button searchBtn1;
    private Toolbar toolbar1;
    private TextView home1,add_new1,mail1,msg1,delete1;
    private CheckBox checkBox_header1;
    private SparseBooleanArray mChecked = new SparseBooleanArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_activity);
        findID();
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        listinguserId = emailid.get(SessionManager.KEY_EMAIL);

        listing_getdata_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetListingRecords/" + listinguserId;
        new ListingAsynctask().execute(listing_getdata_url);

        edtsearch1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String text = searchEt.getText().toString().toLowerCase(Locale.getDefault());
                //adapter.filter(text);
                listingDetailAdapter.getFilter().filter(edtsearch1.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (isNotAdded) {
            checkBox_header1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < activitycustomSearchlist.size(); i++) {
                        mChecked.put(i, checkBox_header1.isChecked());
                    }
                    listingDetailAdapter.notifyDataSetChanged();
                }
            });
            isNotAdded = false;
        }

    }

    public void findID(){

        headerLayout = (RelativeLayout) findViewById(R.id.listing_list_view_header);
        listview1 = (ListView) findViewById(R.id.listing_list_view);
        edtsearch1 = (EditText) findViewById(R.id.listing_search_editText);

        checkBox_header1 = (CheckBox) headerLayout.findViewById(R.id.listing_checkBox_header);

        toolbar1 = (Toolbar) findViewById(R.id.listing_toolbar);
        home1 = (TextView) toolbar1.findViewById(R.id.listing_tool_home);
        add_new1 = (TextView) toolbar1.findViewById(R.id.listing_addNew);
        mail1 = (TextView) toolbar1.findViewById(R.id.listing_mail);
        msg1 = (TextView) toolbar1.findViewById(R.id.listing_sms);
        delete1 = (TextView) toolbar1.findViewById(R.id.listing_delete);

        home1.setOnClickListener(this);
        add_new1.setOnClickListener(this);
        mail1.setOnClickListener(this);
        msg1.setOnClickListener(this);
        delete1.setOnClickListener(this);


    }

    public void addnewActivity() {
        final PopupMenu popupMenu = new PopupMenu(ListingActivity.this, add_new1);
        popupMenu.getMenuInflater().inflate(R.menu.popup_listing_menu, popupMenu.getMenu());

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
                if (menuItem.getTitle().equals("Residential Listing")) {
                    Intent intent = new Intent(ListingActivity.this, AddResidentialListing.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "inside Residential Listing  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Commercial Listing")) {
                    startActivity(new Intent(ListingActivity.this, AddCommercialListing.class));
                    Toast.makeText(getApplicationContext(), "inside Commercial Listing  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Land/Plot Listing")) {
                    startActivity(new Intent(ListingActivity.this, AddLandListing.class));
                    Toast.makeText(getApplicationContext(), "inside Land/Plot Listing  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
        popupMenu.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.listing_tool_home :
                onBackPressed();
                break;
            case R.id.listing_addNew:
                addnewActivity();
                Toast.makeText(getApplicationContext(), "add new user will be added soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.listing_mail:
                sendEmail();
                Toast.makeText(getApplicationContext(), "Mail function will be added soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.listing_sms:
                sendSms();
                Toast.makeText(getApplicationContext(), "Msg will be added soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.listing_delete:
                deleteSelectedItem();
                Toast.makeText(getApplicationContext(), "Delete functionality will be added soon", Toast.LENGTH_SHORT).show();
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

            String delete_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/DeleteContact/" + listinguserId + "/" + s;
            new deletealldataAsyncTask().execute(delete_url);

            activitycustomSearchlist.clear();
            listingDetailAdapter.notifyDataSetChanged();
            listingDetailAdapter.remove(activitycustomSearchlist);

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
                    listingDetailAdapter.remove(activitycustomSearchlist.get(i));
                }
            }
            String multicheckvalue = TextUtils.join(",", multiplecontactid);
            String delete_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/DeleteContact/" + listinguserId + "/" + multicheckvalue;
            new deletealldataAsyncTask().execute(delete_url);
            Toast.makeText(getApplicationContext(), multicheckvalue, Toast.LENGTH_SHORT).show();
            mChecked.clear();
            listingDetailAdapter.notifyDataSetChanged();

        }


    }
    class ListingDetailAdapter extends ArrayAdapter {
        int count;
        ArrayList<ModelClass> mDisplayedValues;
        ArrayList<ModelClass> mOriginalValues;
       // ArrayList<ModelClass> mylistdata;
        private Activity activity;
        private LayoutInflater inflators = null;
        // ModelClass tempValues;
        CheckBox checkBox;
        // SparseBooleanArray mChecked= listView.getCheckedItemPositions();

        public ListingDetailAdapter(ListingActivity listingActivity,int id,ArrayList<ModelClass> modeldata) {
            super(listingActivity,id,modeldata);
            activity = listingActivity;
            mDisplayedValues = modeldata;
            mOriginalValues = modeldata;
            inflators = (LayoutInflater) ((Activity) activity).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                final LayoutInflater sInflater = (LayoutInflater) activity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                vi = sInflater.inflate(R.layout.listing_listview_row, null, false);

            }

            TextView  tv_propertyowner = (TextView) vi.findViewById(R.id.listing_properties_owner);
            TextView tv_listingtitle = (TextView) vi.findViewById(R.id.listing_title);
            checkBox = (CheckBox) vi.findViewById(R.id.listing_checkBox);
            // checkBox.setVisibility(View.INVISIBLE);

                  /* **************ADDING CONTENTS**************** */

            ModelClass  tempValues = (ModelClass) mDisplayedValues.get(position);
            tv_propertyowner.setText(tempValues.getListingpropertyowner());

            tv_listingtitle.setText(tempValues.getListingtittle());


            // holder.joiningDateTv.setText(tempValues.getJoiningDate());

           /* checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelClass modelClass = mDisplayedValues.get(position);
                    String name = modelClass.getContactID();
                    String email =modelClass.getContact_Email();
                    //Toast.makeText(getApplicationContext(), name + "  "+ email, Toast.LENGTH_SHORT).show();
                    Log.v("Contact  ID ", name);
                    System.out.println(" Exception is caught here ......." + email);
                    Log.v("Contact  Email ", email);
                    //adapter.remove(customListArray.get(i));
                }
            });*/

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
                            String subject = mOriginalValues.get(i).getListingtittle();
                            String list = mOriginalValues.get(i).getListingpropertyowner();

                            if (subject.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new ModelClass(subject,list));
                            } else if (list.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new ModelClass(subject,list));
                            } else if (subject.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new ModelClass(subject,list));
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


    public class ListingAsynctask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListingActivity.this);
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
                    listingtittle = jObj.getString("Title");
                    listingpropertyownner = jObj.getString("PK_ListingId");


                    listingproperty_ownerlist.add(listingpropertyownner);
                    listingtittlelist.add(listingtittle);


                }
                for (int k = 0; k < listingtittlelist.size(); k++) {

                ModelClass model = new ModelClass();

                    //******* Firstly take data in model object ******//**//*
                    model.setListingtittle(listingtittlelist.get(k));
                    model.setListingpropertyowner(listingproperty_ownerlist.get(k));
                    // model.setPdrecordno(detailNoArray.get(k));
                    activitycustomSearchlist.add(model);
                }

                listingDetailAdapter = new ListingDetailAdapter(ListingActivity.this,R.layout.listing_listview_row,activitycustomSearchlist);
                listview1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listview1.setAdapter(listingDetailAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (response_code.equals("1")) {



                /*    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                    });
                    */

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
                Toast.makeText(ListingActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

    public class deletealldataAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ListingActivity.this);
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
                System.out.println("Exception is caught here ......." + e.toString());
            }
            return response;
        }


        @Override
        protected void onPostExecute(String s) {

            String res = s;

            try {
                if (response_code.equals("1")) {

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
                Toast.makeText(ListingActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }




}

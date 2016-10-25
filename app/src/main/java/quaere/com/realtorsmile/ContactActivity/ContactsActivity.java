package quaere.com.realtorsmile.ContactActivity;

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
import android.view.Menu;
import android.view.MenuInflater;
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
import android.widget.Filterable;
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
import java.util.Locale;

import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.SessionManager;


public class ContactsActivity extends Activity implements View.OnClickListener {
    private int position;
    private ListView listView;
    EditText searchEt;
    private static int count = 0;
    private boolean isNotAdded = true;
    private CheckBox checkBox_header;
    RelativeLayout headerLayout;
    Spinner searchSpinner;
    Button searchBtn;
    private Toolbar toolbar;
    TextView home, add_new, mail, msg, delete;
    private ProgressDialog pDialog;
    List<String> searchFor;
    //  static String[] textviewContent;
    String userId, contactName, contact_id, contact_type, contact_mobile, contact_response_code, contact_email;
    String selectedcontcatspinner;
    String search_url;

    String Selectedmenuitem;
    String contact_url;
    int noOfObjects;
    ContactDetailAdapter adapter;
    private ArrayList<ModelClass> customSearchlist = new ArrayList<>();

    List<String> contactidArray = new ArrayList<String>();
    List<String> contactNameArray = new ArrayList<String>();
    List<String> contact_typeArray = new ArrayList<String>();
    List<String> contact_mobileArray = new ArrayList<String>();
    List<String> contact_EmailArray = new ArrayList<String>();

    private ArrayList<ModelClass> customListArray = new ArrayList<ModelClass>();

    //To save checked items, and <b>re-add</b> while scrolling.
    SparseBooleanArray mChecked = new SparseBooleanArray();
    ArrayList<String> allcontactid = new ArrayList<>();
    ArrayList<String> multiplecontactid = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        headerLayout = (RelativeLayout) findViewById(R.id.contact_list_view_header);
        listView = (ListView) findViewById(R.id.contact_list_view);
        searchEt = (EditText) findViewById(R.id.contact_search_editText);
        searchSpinner = (Spinner) findViewById(R.id.contact_search_spinner);
        searchBtn = (Button) findViewById(R.id.contact_search_btn);

        toolbar = (Toolbar) findViewById(R.id.contact_toolbar);
        home = (TextView) toolbar.findViewById(R.id.contact_tool_home);
        add_new = (TextView) toolbar.findViewById(R.id.contact_addNew);
        mail = (TextView) toolbar.findViewById(R.id.contact_mail);
        msg = (TextView) toolbar.findViewById(R.id.contact_sms);
        delete = (TextView) toolbar.findViewById(R.id.contact_delete);
        checkBox_header = (CheckBox) headerLayout.findViewById(R.id.checkBox_header);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        userId = emailid.get(SessionManager.KEY_EMAIL);


        // userId = getIntent().getStringExtra("userId");
        Toast.makeText(getApplicationContext(), "your email is " + userId, Toast.LENGTH_LONG).show();

        contact_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetContactRecords/" + userId;
        new ContactAsyncTask().execute(contact_url);

        home.setOnClickListener(this);
        add_new.setOnClickListener(this);
        mail.setOnClickListener(this);
        msg.setOnClickListener(this);
        delete.setOnClickListener(this);
        searchBtn.setOnClickListener(this);


        // Spinner Drop down elements
        searchFor = new ArrayList<String>();
        searchFor.add("All");
        searchFor.add("FirstName");
        searchFor.add("ContactType");
        searchFor.add("Mobile");

        //adding data to search spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchFor);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(dataAdapter);


        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedcontcatspinner = parent.getItemAtPosition(position).toString();
                Log.v("Selected ITem :", selectedcontcatspinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //String text = searchEt.getText().toString().toLowerCase(Locale.getDefault());
                //adapter.filter(text);
                adapter.filter(searchEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        if (isNotAdded) {
            checkBox_header.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    for (int i = 0; i < customListArray.size(); i++) {
                        mChecked.put(i, checkBox_header.isChecked());
                    }

                    adapter.notifyDataSetChanged();

                }
            });

            isNotAdded = false;
        }


        // listView.setItemsCanFocus(false);


    }
   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen for landscape and portrait and set portrait mode always
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }*/


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_tool_home:
                onBackPressed();
                // startActivity(new Intent(ContactsActivity.this,MainPage.class));
                // setResult(2, new Intent(ContactsActivity.this, MainPage.class));
                //finish();
                break;
            case R.id.contact_addNew:
                Toast.makeText(getApplicationContext(), "Add Contact will be added soon", Toast.LENGTH_SHORT).show();
                addnewConctact();
                break;

            case R.id.contact_mail:
                sendEmail();
                Toast.makeText(getApplicationContext(), "Mail function will be added soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.contact_sms:
                sendSms();
                Toast.makeText(getApplicationContext(), "Msg will be added soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact_delete:
                deleteItem();
                //Toast.makeText(getApplicationContext(), "Delete functionality will be added soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.contact_search_btn:
                contactSearchFilter();
                Toast.makeText(getApplicationContext(), "Searching begins", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void addnewConctact() {

        final PopupMenu popupMenu = new PopupMenu(ContactsActivity.this, add_new);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());



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

                if (menuItem.getTitle().equals("General")) {
                    Intent intent = new Intent(ContactsActivity.this, AddGeneralActivity.class);
                    intent.putExtra("selectitem", Selectedmenuitem);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "inside General  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Client")) {
                    startActivity(new Intent(ContactsActivity.this, AddContactClientActivity.class));
                    Toast.makeText(getApplicationContext(), "Inside Client  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Agent")) {
                    startActivity(new Intent(ContactsActivity.this, AddContactClientActivity.class));
                    Toast.makeText(getApplicationContext(), "inside Agent  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Builder")) {
                    startActivity(new Intent(ContactsActivity.this, AddContactBuilderActivity.class));
                    Toast.makeText(getApplicationContext(), "Inside Builder  " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                } else if (menuItem.getTitle().equals("Vendor")) {
                    startActivity(new Intent(ContactsActivity.this, AddContactVendorActivity.class));
                    Toast.makeText(getApplicationContext(), "Inside Vendor " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
        popupMenu.show();


    }

    private void sendEmail() {
        ArrayList<String> Email = new ArrayList<>();

        int itemCount = listView.getCount();
        ModelClass modelClass;
        for (int i = itemCount - 1; i >= 0; i--) {
            if (mChecked.get(i)) {
                modelClass = customListArray.get(i);
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
        String s = searchEt.getText().toString();
        String searchItem = searchEt.getText().toString();
        search_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/Search/" + s + "/" + selectedcontcatspinner + "/" + userId;
        Toast.makeText(getApplicationContext(), "searching for " + s + " and " + searchItem, Toast.LENGTH_SHORT).show();
        adapter.clear();
        adapter.notifyDataSetChanged();
        new ContactSearchAsyncTask().execute(search_url);
        //  new SearchFilterAsyncTask().execute();


    }

    private void sendSms() {
        int itemCount = listView.getCount();
        ModelClass modelClass;
        ArrayList<String> multipleMobile = new ArrayList<String>();
        for (int i = itemCount - 1; i >= 0; i--) {
            if (mChecked.get(i)) {
                modelClass = customListArray.get(i);
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


    private void deleteItem() {

          deleteallItem();
             // SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();

             deleteselectedItem();



    }

    private void deleteallItem(){


        if (checkBox_header.isChecked()) {

            for (int i = 0; i < customListArray.size(); i++) {
                ModelClass modelClass = customListArray.get(i);

                String name = modelClass.getContactID();
                allcontactid.add(name);
            }
            //  all =allcontactid;
            String s = TextUtils.join(",", allcontactid);
            String saadagagh = s;
            Log.v("All String", saadagagh);
            String delete_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/DeleteContact/" + userId + "/" + s;
            new deletealldataAsyncTask().execute(delete_url);

            customListArray.clear();
            adapter.notifyDataSetChanged();
            adapter.remove(customListArray);

        }
    }
  private void deleteselectedItem(){
      int itemCount = listView.getCount();

      for (int i = itemCount - 1; i >= 0; i--) {
          if (mChecked.get(i)) {
              ModelClass modelClass = customListArray.get(i);

              String contactID = modelClass.getContactID();
              multiplecontactid.add(contactID);
              Toast.makeText(getApplicationContext(), contactID, Toast.LENGTH_SHORT).show();
              Log.v("Contact  ID ", contactID);
              adapter.remove(customListArray.get(i));

              String multicheckvalue = TextUtils.join(",", multiplecontactid);
              String delete_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/DeleteContact/" + userId + "/" + multicheckvalue;
              new deletealldataAsyncTask().execute(delete_url);
              Toast.makeText(getApplicationContext(), multicheckvalue, Toast.LENGTH_SHORT).show();
              mChecked.clear();
              adapter.notifyDataSetChanged();
          }else if(i == 0) {
              Toast.makeText(getApplicationContext(),"Select at least one item",Toast.LENGTH_LONG).show();
          }
      }

   }

    public class ContactAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ContactsActivity.this);
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

            if(s == null){
                Toast.makeText(ContactsActivity.this, "Internet not available ", Toast.LENGTH_LONG).show();
            }else


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
                    contactName = jObj.getString("FirstName");
                    contact_id = jObj.getString("PK_ContactID");
                    contact_type = jObj.getString("ContactType");
                    contact_mobile = jObj.getString("Mobile");
                    contact_email = jObj.getString("Email");
                    contact_response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);

                    //Adding detail to Array
                    contactidArray.add(contact_id);
                    contactNameArray.add(contactName);
                    contact_mobileArray.add(contact_mobile);
                    contact_typeArray.add(contact_type);
                    contact_EmailArray.add(contact_email);
                    // contact_typeArray.add(contact_type);


                }
                for (int k = 0; k < contactNameArray.size(); k++) {

                    final ModelClass model = new ModelClass();

                    //******* Firstly take data in model object ******//**//*
                    model.setContactName(contactNameArray.get(k));
                    model.setContact_ConType(contact_typeArray.get(k));
                    model.setContact_mobile(contact_mobileArray.get(k));
                    model.setContactID(contactidArray.get(k));
                    model.setContact_Email(contact_EmailArray.get(k));
                    // model.setPdrecordno(detailNoArray.get(k));
                    customListArray.add(model);
                }

                adapter = (new ContactDetailAdapter(ContactsActivity.this, R.layout.contact_list_view_row, customListArray));
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (contact_response_code.equals("1")) {



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
                Toast.makeText(ContactsActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

    // CustomAdapter
    public class ContactDetailAdapter extends ArrayAdapter implements Filterable {

        private ArrayList<ModelClass> mOriginalValues; // Original Values
        private ArrayList<ModelClass> mDisplayedValues;
        // ArrayList<ModelClass> mylistdata;
        private Activity activity;
        private LayoutInflater inflators = null;
        // ModelClass tempValues;
        CheckBox checkBox;

        // SparseBooleanArray mChecked= listView.getCheckedItemPositions();

        public ContactDetailAdapter(ContactsActivity contactsActivity, int id, ArrayList<ModelClass> modeldata) {
            super(contactsActivity, id, modeldata);
            activity = contactsActivity;
            this.mOriginalValues = modeldata;
            this.mDisplayedValues = modeldata;

            inflators = (LayoutInflater) ((Activity) activity)
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


            public ViewHolder(View vi) {


            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;


            if (vi == null) {

                /*
                 * LayoutInflater
                 */
                final LayoutInflater sInflater = (LayoutInflater) activity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                /*
                 * Inflate Custom List View
                 */
                vi = sInflater.inflate(R.layout.contact_list_view_row, null, false);

            }


            TextView contactNameTv = (TextView) vi.findViewById(R.id.contact_fullNameTv);
            TextView contactTypeTv = (TextView) vi.findViewById(R.id.contact_typeTv);
            TextView contactMobileNoTv = (TextView) vi.findViewById(R.id.contact_mobileNoTv);
            checkBox = (CheckBox) vi.findViewById(R.id.contact_checkBox);
            // checkBox.setVisibility(View.INVISIBLE);

                  /* **************ADDING CONTENTS**************** */

            final ModelClass tempValues = (ModelClass) mDisplayedValues.get(position);
            contactNameTv.setText(tempValues.getContactName());
            String s = tempValues.getContactName();
            contactTypeTv.setText(tempValues.getContact_ConType());

            contactMobileNoTv.setText(tempValues.getContact_mobile());


            contactNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(ContactsActivity.this, ContactProfileActivity.class);
                    intent.putExtra("cname", tempValues.getContactName());
                    intent.putExtra("type", tempValues.getContact_ConType());
                    intent.putExtra("mob", tempValues.getContact_mobile());
                    startActivity(intent);
                }
            });
            if (s.equals("null")) {
                checkBox.setVisibility(View.INVISIBLE);
                contactNameTv.setVisibility(View.INVISIBLE);
                contactTypeTv.setVisibility(View.INVISIBLE);
                contactMobileNoTv.setVisibility(View.INVISIBLE);

                Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds : ", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.getView().setPadding(20, 20, 20, 20);
                toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                toast.show();
                pDialog.dismiss();
            }
            // holder.joiningDateTv.setText(tempValues.getJoiningDate());

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelClass modelClass = mDisplayedValues.get(position);

                    String name = modelClass.getContactID();
                    String email = modelClass.getContact_Email();
                    //Toast.makeText(getApplicationContext(), name + "  "+ email, Toast.LENGTH_SHORT).show();
                    Log.v("Contact  ID ", name);
                    System.out.println(" Exception is caught here ......." + email);
                    Log.v("Contact  Email ", email);
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

                                    checkBox_header.setChecked(isChecked);
                                }

                            } else {


                                mChecked.delete(position);

                                checkBox_header.setChecked(isChecked);

                            }

                        }
                    });


            // Set CheckBox "TRUE" or "FALSE" if mChecked == true

            checkBox.setChecked((mChecked.get(position) == true ? true : false));


            return vi;
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


        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            mDisplayedValues.clear();
            if (charText.length() == 0) {
                mDisplayedValues.addAll(mDisplayedValues);
            }
            else
            {
                for (ModelClass wp : mDisplayedValues)
                {
                    if (wp.getContactName().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        mDisplayedValues.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }


   /*     @Override
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

                    *//********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********//*
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();



                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String name = mOriginalValues.get(i).getContactName();
                            String conType = mOriginalValues.get(i).getContact_ConType();
                            String mobileNo = mOriginalValues.get(i).getContact_mobile();
                            if (name.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new ModelClass(mOriginalValues.get(i).getContactName(),mOriginalValues.get(i).getContact_ConType(), mOriginalValues.get(i).getContact_mobile() ));
                            } else if (conType.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new ModelClass(mOriginalValues.get(i).getContactName(), mOriginalValues.get(i).getContact_ConType(), mOriginalValues.get(i).getContact_mobile()));
                            } else if (mobileNo.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new ModelClass(mOriginalValues.get(i).getContactName(), mOriginalValues.get(i).getContact_ConType(), mOriginalValues.get(i).getContact_mobile()));
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
*/
    }

    public class deletealldataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ContactsActivity.this);
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
                if (contact_response_code.equals("1")) {



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
                Toast.makeText(ContactsActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

    class ContactSearchAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ContactsActivity.this);
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
                    contactName = jObj.getString("FirstName");

                    contact_id = jObj.getString("PK_ContactID");

                    contact_type = jObj.getString("ContactType");

                    contact_mobile = jObj.getString("Mobile");
                    contact_email = jObj.getString("Email");
                    contact_response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);


                    customListArray.add(new ModelClass(contactName, contact_type, contact_mobile));


                }
                adapter = new ContactDetailAdapter(ContactsActivity.this, R.layout.contact_list_view_row, customListArray);

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (contact_response_code.equals("1")) {



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
                Toast.makeText(ContactsActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

}
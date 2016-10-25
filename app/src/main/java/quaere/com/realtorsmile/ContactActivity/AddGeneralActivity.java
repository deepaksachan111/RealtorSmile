package quaere.com.realtorsmile.ContactActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.thomashaertel.widget.MultiSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import quaere.com.realtorsmile.AssignToAsyncTask;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.SessionManager;

public class AddGeneralActivity extends Activity {

    private boolean flag = true;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private static final int SELECT_PICTURE = 1;
    private ImageView setImg;
    private CheckBox checkbox_addnewgene_isfeatured;
    private String selectedImagePath;
    private RadioGroup radioGroup_addnewgeneral;
    private MultiSpinner spi_multi_select;
    private ArrayAdapter<String> adapter, adapter2;
    private String gettexteditext, gettexteditext2;
    private String selectedbuilder;
    private RadioGroup radioGroup;
    private RadioButton radioButton_public, radioButton_private, radioButton_myteam, radioButton_custom;
    private CheckBox checkBox_isfeatured, checkBox_emailnotification, checkBox_smsnotification;
    private Button btn_save, btn_save_new, btn_cancel, btn_select_profilephoto;

    private boolean[] selectedItems;

    ArrayList<String> spinerassignlist = new ArrayList<>();
    ArrayList<String> spinerassignlist2 = new ArrayList<>();
    ArrayList<String> spinertypemalelist = new ArrayList<>();

    ArrayList<String> customsharinglist = new ArrayList<>();
    ArrayList<String> customsharinglist2 = new ArrayList<>();

    TextView contact_type;
    private String selectedcontcatspinner, selectedmaletype;
    private ImageView iv_plus, iv_plus2;
    private EditText ed_firstname, ed_lastname, ed_mobile, ed_email, edtselectsource, edt_remark, edt_profilephoto;
    String buyers, sellers, landlords, tenantss, investors;
    private Spinner  spinnerassign, spiner_maletype, spiner_firstnametype,spinner_selectsource2;
    private static String myItem = "";
    private String userid;

    private EditText edt_custom_sharing;
     PopupWindow cusompopupwindow;
    private ListView add_custom_all_employee_dropdown_list,add_custom_all_associate_dropdown_list;

    private static String  new_custom_string= "";
    private ArrayAdapter<String> popuplist_employee_adapter,popuplist_associates_adapter;

    AssignToAsyncTask.AssignToAdapter assignToAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_new_add);
        findviewbyid();
         SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> map = sessionManager.getUserDetails();
        userid = map.get(SessionManager.KEY_EMAIL);


        spinerassignlist.add("Select");
        spinerassignlist.add("Select2");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinerassignlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerassign.setAdapter(dataAdapter);

        // AssignToAsyncTask(Activity activity, ArrayList assignToArray, ArrayAdapter assignToAdapter, Spinner assingToSpin, String url)

        ArrayList<String> assignToArray = new ArrayList<String>();
        String assignto_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/getEmployee/"+userid;
        AssignToAsyncTask assgnTo = new AssignToAsyncTask(this,assignToArray,assignToAdapter,spinnerassign,assignto_url);
        assgnTo.execute();


        spinnerassign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedcontcatspinner = parent.getItemAtPosition(position).toString();
                Log.v("Selected ITem :", selectedcontcatspinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinertypemalelist.add("Mr.");
        spinertypemalelist.add("Dr.");
        spinertypemalelist.add("Er.");
        spinertypemalelist.add("Lt.");
        spinertypemalelist.add("Ms.");
        spinertypemalelist.add("Major");
        spinertypemalelist.add("Sri");
        spinertypemalelist.add("Shri");
        spinertypemalelist.add("Shrimati");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinertypemalelist);
       dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_maletype.setAdapter(dataAdapter2);

        spiner_maletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedmaletype = parent.getItemAtPosition(position).toString();
                Log.v("Selected ITem :", selectedmaletype);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.add("Friends");
        adapter.add("Relative");
        adapter.add("Others");


        // get spi_multi_select and set adapter
        spi_multi_select.setAdapter(adapter, false, onSelectedListener);

        // set initial selection
        selectedItems = new boolean[adapter.getCount()];
        selectedItems[1] = true; // select second item
        spi_multi_select.setSelected(selectedItems);

        iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupwindowgrop();
            }


        });


        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        // get spi_multi_select and set adapter

        // edt_selectsource.setAdapter(adapter2);
        edtselectsource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generalSourceDialog();
            }
        });
        // set initial selection

        iv_plus2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                addGeneralGroup();

            }


        });

        btn_select_profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

               /* Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);*/
            }
        });

        customsharing();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup_addnewgeneral.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioSexButton = (RadioButton) findViewById(selectedId);

                //  Toast.makeText(AddGeneralActivity.this, radioSexButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });
   edt_custom_sharing.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           edt_custom_sharing.setVisibility(View.VISIBLE);
           String s = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetCustomSharing/" + userid;
           new CustomVisibilityAsyncTask().execute(s);
           custumshareingPopup();
           flag = false;
       }
   });



    }

    private void customsharing(){
        radioGroup_addnewgeneral.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radiobutton = (RadioButton)findViewById(checkedId);
                // find which radio button is selected

                if (checkedId == R.id.radio_general_public) {
                    edt_custom_sharing.setVisibility(View.GONE);
                    cusompopupwindow.dismiss();
                    Toast.makeText(getApplicationContext(), radiobutton.getText(), Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radio_general_private) {
                    edt_custom_sharing.setVisibility(View.GONE);
                    cusompopupwindow.dismiss();
                    Toast.makeText(getApplicationContext(),radiobutton.getText(), Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radio_general_myteam) {
                    edt_custom_sharing.setVisibility(View.GONE);
                    cusompopupwindow.dismiss();
                    Toast.makeText(getApplicationContext(), radiobutton.getText(), Toast.LENGTH_SHORT).show();

                } else {


                        edt_custom_sharing.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), radiobutton.getText(), Toast.LENGTH_SHORT).show();
                        String s = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetCustomSharing/" + userid;
                        new CustomVisibilityAsyncTask().execute(s);
                        custumshareingPopup();
                        flag =false;
                    }


            }


        });

        RadioButton  chkbtn_public = (RadioButton) findViewById(R.id.radio_general_public);
        RadioButton chkbtn_private = (RadioButton) findViewById(R.id.radio_general_private);
        RadioButton chkbtn_myteam = (RadioButton) findViewById(R.id.radio_general_myteam);
        RadioButton chkbtn_custom = (RadioButton) findViewById(R.id.radio_general_custom);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String uri = selectedImageUri.toString();

                selectedImagePath = getRealPathFromURI(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                // Bitmap bitmap= BitmapFactory.decodeFile(selectedImagePath);
                // setImg.setImageBitmap(bitmap);
                setImg.setImageURI(selectedImageUri);
                edt_profilephoto.setText(selectedImagePath);
            } else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }
    }

    private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddGeneralActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_PICTURE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // edt_profilephoto.setText((CharSequence) destination);

        //String p = getRealPathFromURI(selectedImageUri);

        System.out.println(destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
        String path = (destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
        edt_profilephoto.setText("");
        edt_profilephoto.setText(path);
        setImg.setImageBitmap(thumbnail);
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    /*    public String getPath(Uri uri) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }*/
    private void addGeneralGroup() {

        final Dialog openDialog = new Dialog(this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialogeaddnewgeneral);

        final EditText contact_grpEt = (EditText) openDialog.findViewById(R.id.et_dialogeaddnewgeneral_contactgroup1);
        Button save = (Button) openDialog.findViewById(R.id.btn_dialogeaddnewgeneral_save1);
        Button close = (Button) openDialog.findViewById(R.id.btn_dialogeaddnewgeneral_close1);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gettexteditext2 = contact_grpEt.getText().toString();
                //  editor = sharedpreferences.edit();
                //   editor.putString("new_builder_group", new_builder_group);
                //  editor.commit();
                // adapter2.add(gettexteditext2);

                //  adapter.notifyDataSetChanged();
                openDialog.dismiss();
                myItem = myItem + gettexteditext2 + ",";

                adapter2.add(gettexteditext2);
                adapter2.notifyDataSetChanged();
                edtselectsource.setText(gettexteditext2);
                //  build_groupEt.setText(myItem);
                Toast.makeText(getApplicationContext(), gettexteditext2 + "Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        // AlertDialog alertDialog = openDialog.create();


        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        openDialog.getWindow().getAttributes().verticalMargin = 0.50F;
        openDialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        openDialog.show();
    }

    private void findviewbyid() {

        contact_type = (TextView) findViewById(R.id.tv_contact_type);
        spinnerassign = (Spinner) findViewById(R.id.addnewgene_spiner_assignto);
        spi_multi_select = (MultiSpinner) findViewById(R.id.addnewgene_spinnerMultiselect);
        spiner_maletype = (Spinner) findViewById(R.id.addnewgene_spiner_firstnametype);
        iv_plus = (ImageView) findViewById(R.id.iv_plusicon);
        iv_plus2 = (ImageView) findViewById(R.id.iv_plusicon2);
        edtselectsource = (EditText) findViewById(R.id.ed_addnewgene_selectsource);
        spinner_selectsource2 = (Spinner) findViewById(R.id.spinner_addnewgene_selectsource2);
        ed_firstname = (EditText) findViewById(R.id.edt_addnewgroup_genereal_firstname);
        ed_lastname = (EditText) findViewById(R.id.edt_addnewgroup_genereal_lastname);
        ed_mobile = (EditText) findViewById(R.id.edt_addnewgroup_genereal_mobileno_);
        ed_email = (EditText) findViewById(R.id.edt_addnewgroup_genereal_email);
        edt_remark = (EditText) findViewById(R.id.edt_addnewgroup_genereal_remark);
        edt_custom_sharing = (EditText) findViewById(R.id.edt_addnewgene_customsahreing);
        edt_profilephoto = (EditText) findViewById(R.id.edt_addnewgene_selectprofilephoto);
        btn_select_profilephoto = (Button) findViewById(R.id.btn_addnewgene_selectprofilephoto);
        btn_save = (Button) findViewById(R.id.btn_addnewgene_save);
        btn_save_new = (Button) findViewById(R.id.btn_addnewgene_saveandnew);
        btn_cancel = (Button) findViewById(R.id.btn_addnewgene_cancel);
        radioGroup_addnewgeneral = (RadioGroup) findViewById(R.id.radiogroup_addnewgroup_genereal_visibility);
        checkbox_addnewgene_isfeatured =(CheckBox)findViewById(R.id.checkbox_addnewgene_isfeatured);
        setImg = (ImageView) findViewById(R.id.iv_profile_picset);
    }

    private void popupwindowgrop() {
      //  LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupView = LayoutInflater.from(getBaseContext()).inflate(R.layout.popupwindowaddnew, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
       // final EditText et_popupaddnew_tpe1 = (EditText) popupView.findViewById(R.id.et_popupaddnew_tpe);
        final EditText et_popupaddnew_contactgroup1 = (EditText) popupView.findViewById(R.id.et_popupaddnew_contactgroup);
        et_popupaddnew_contactgroup1.requestFocus();


        Button btn_popupaddnew_save1 = (Button) popupView.findViewById(R.id.btn_popupaddnew_save);
        Button btn_popupaddnew_close1 = (Button) popupView.findViewById(R.id.btn_popupaddnew_close);

        btn_popupaddnew_save1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_popupaddnew_contactgroup1.getText().toString();
                if (s.equals("")) {
                    et_popupaddnew_contactgroup1.setError("enter value");
                } else
                    gettexteditext = et_popupaddnew_contactgroup1.getText().toString();

                adapter.add(gettexteditext);

                selectedItems = new boolean[adapter.getCount()];
                selectedItems[selectedItems.length - 1] = true; // select second item
                spi_multi_select.setSelected(selectedItems);
                popupWindow.dismiss();
            }
        });

        btn_popupaddnew_close1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

        popupWindow.update();
        popupWindow.showAsDropDown(iv_plus, 50, -200);
    }



    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {

        public void onItemsSelected(boolean[] selected) {
            // Do something here with the selected items

            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < selected.length; i++) {
                if (selected[i]) {
                    builder.append(adapter.getItem(i)).append(" ");
                }
                selectedbuilder = builder.toString();
            }

            Toast.makeText(AddGeneralActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
        }
    };


    private void generalSourceDialog() {
        // Create custom dialog object
        final Dialog dialog = new Dialog(this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.listview_popup);
        // dialog.getWindow().setLayout(600, LinearLayout.LayoutParams.WRAP_CONTENT);
        Window window = dialog.getWindow();
        window.setLayout(500, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);


        // set values for custom dialog components - text, image and button
        final ListView listviewgeneralsource = (ListView) dialog.findViewById(R.id.listviewpopup2);
        TextView textViewclient = (TextView) dialog.findViewById(R.id.tv_addnew_gene_client);
        TextView textView_agents = (TextView) dialog.findViewById(R.id.tv_addnew_gene_agent);
        TextView textView_builder = (TextView) dialog.findViewById(R.id.tv_addnew_gene_builder);
        TextView textView_vendor = (TextView) dialog.findViewById(R.id.tv_addnew_gene_vendor);
        final TextView textView_general = (TextView) dialog.findViewById(R.id.tv_addnew_gene_general);
        TextView textView_lead = (TextView) dialog.findViewById(R.id.tv_addnew_gene_lead);
        TextView textView_associates = (TextView) dialog.findViewById(R.id.tv_addnew_gene_associate);
        TextView textView_employee = (TextView) dialog.findViewById(R.id.tv_addnew_gene_employee);
        TextView textView_compaign = (TextView) dialog.findViewById(R.id.tv_addnew_gene_compaign);


        listviewgeneralsource.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

    final String url ="http://rsmile.quaeretech.com/RealtorSmile.svc/SearchContact/";
        listviewgeneralsource.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = adapter2.getItem(position);
                edtselectsource.setText(selectedFromList);
                spinner_selectsource2.setVisibility(View.GONE);


                adapter2.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        textViewclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtselectsource.setText("Client");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString()+"/ContactType/"+userid);
                dialog.dismiss();
            }
        });

        textView_agents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtselectsource.setText("Agents");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString() + "/ContactType/" + userid);
                flag = false;
                dialog.dismiss();
            }
        });
        textView_builder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtselectsource.setText("Builder");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString() + "/ContactType/" + userid);
                flag = false;

                dialog.dismiss();
            }
        });
        textView_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtselectsource.setText("Vendor");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString() + "/ContactType/" + userid);
                flag = false;
                dialog.dismiss();
            }
        });
        textView_general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtselectsource.setText("General");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString() + "/ContactType/" + userid);
                flag = false;
                dialog.dismiss();
            }
        });
        textView_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtselectsource.setText("Lead");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString() + "/ContactType/" + userid);
                flag = false;
                dialog.dismiss();
            }
        });
        textView_associates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtselectsource.setText("Associate");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString() + "/ContactType/" + userid);
                flag = false;
                dialog.dismiss();
            }

        });
        textView_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtselectsource.setText("Employee");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString() + "/ContactType/" + userid);
                flag = false;
                dialog.dismiss();
            }
        });
        textView_compaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtselectsource.setText("Compaign");
                spinner_selectsource2.setVisibility(View.VISIBLE);
                new SelectSourceAsyncTask().execute(url + edtselectsource.getText().toString()+"/ContactType/"+userid);
                flag = false;
                dialog.dismiss();
            }
        });
        dialog.show();

        Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });
//The below code is EXTRA - to dim the parent view by 70%
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.1f;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dialog.getWindow().setAttributes(lp);
        // Set dialog title
        // dialog.setTitle("Custom Dialog");
        dialog.show();


    }

    private void custumshareingPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) AddGeneralActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.custumsgaring_popup_layout, (ViewGroup) findViewById(R.id.custom_group_popup_window));

        View dummyview = (View)popupView.findViewById(R.id._customsharing_dropdown_dummyView);
        dummyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cusompopupwindow.dismiss();
            }
        });

        cusompopupwindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        add_custom_all_employee_dropdown_list = (ListView) popupView.findViewById(R.id.add_all_employee_dropdown_list);
        add_custom_all_associate_dropdown_list = (ListView) popupView.findViewById(R.id.add_all_associates_dropdown_list);

        popuplist_employee_adapter = new ArrayAdapter<String>(AddGeneralActivity.this,android.R.layout.simple_list_item_1, customsharinglist);
        add_custom_all_employee_dropdown_list.setAdapter(popuplist_employee_adapter);

        popuplist_associates_adapter = new ArrayAdapter<String>(AddGeneralActivity.this,android.R.layout.simple_list_item_1, customsharinglist2);
        add_custom_all_associate_dropdown_list.setAdapter(popuplist_associates_adapter);

        add_custom_all_employee_dropdown_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                new_custom_string = new_custom_string + item + ",";
                edt_custom_sharing.setText(new_custom_string);
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                cusompopupwindow.dismiss();
            }
        });
        add_custom_all_associate_dropdown_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s  = parent.getItemAtPosition(position).toString();
                new_custom_string = new_custom_string + s + ",";
                edt_custom_sharing.setText(new_custom_string);
                Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                cusompopupwindow.dismiss();
            }
        });


        cusompopupwindow.setOutsideTouchable(true);
        cusompopupwindow.setTouchable(true);
        cusompopupwindow.setBackgroundDrawable(new BitmapDrawable());
        cusompopupwindow.update();
        cusompopupwindow.showAsDropDown(edt_custom_sharing);


    }
   private class SelectSourceAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddGeneralActivity.this);
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
            ArrayList<String> selectdata = new ArrayList<>();

            try {

                JSONArray jsonArray = new JSONArray(s);

                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);

                    String contactName = jObj.getString("FirstName");

                    //contact_id = jObj.getString("PK_ContactID");

                    //contact_type = jObj.getString("ContactType");

                    String contact_mobile = jObj.getString("Mobile");
                    // contact_email =  jObj.getString("Email");
                    // contact_response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);

                    //Adding detail to Array
                    // contactidArray.add(contact_id);
                    // contactNameArray.add(contactName);
                    // contact_mobileArray.add(contact_mobile);
                    // contact_typeArray.add(contact_type);
                    // contact_EmailArray.add(contact_email);
                    // contact_typeArray.add(contact_type);
                    selectdata.add(contactName);

                }
                //for (int k = 0; k < contactNameArray.size(); k++) {

                //final ModelClass model = new ModelClass();

                //******* Firstly take data in model object ******//**//*
                // model.setContactName(contactNameArray.get(k));
                // model.setContact_ConType(contact_typeArray.get(k));
                //model.setContact_mobile(contact_mobileArray.get(k));
                // model.setContactID(contactidArray.get(k));
                // model.setContact_Email(contact_EmailArray.get(k));
                // model.setPdrecordno(detailNoArray.get(k));
                // selectdata.add(contactName);


                spinner_selectsource2.setAdapter(new ArrayAdapter<String>(AddGeneralActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        selectdata));
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                // if (contact_response_code.equals("1")) {



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

                Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : ", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.getView().setPadding(20, 20, 20, 20);
                toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                toast.show();
                pDialog.dismiss();
                // } else {
                Toast toast2 = Toast.makeText(getApplicationContext(), "No Records Founds : " , Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.BOTTOM, 0, 0);
                toast2.getView().setPadding(20, 20, 20, 20);
                toast2.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                toast2.show();
                pDialog.dismiss();


            } catch (Exception e) {
                Toast.makeText(AddGeneralActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

    private class CustomVisibilityAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog = new ProgressDialog(AddGeneralActivity.this);

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

            customsharinglist.clear();
            customsharinglist2.clear();
            try {

                JSONArray jsonArray = new JSONArray(result);

                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);

                    String group = jObj.getString("SharingGroup");
                    String type = jObj.getString("Type");
                    String responsecode = jObj.getString("ResponseCode");

                  //  customsharinglist.add(type);

                    if(type.contains("Employee")){
                        customsharinglist.add(group);
                    }

                    if(type.contains("Associate")){
                        customsharinglist2.add(group);
                    }

                }


              //  popuplist_employee_adapter.notifyDataSetChanged();
                add_custom_all_employee_dropdown_list.setAdapter(new ArrayAdapter<String>(AddGeneralActivity.this,
                        android.R.layout.simple_list_item_1, customsharinglist));
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                // if (contact_response_code.equals("1")) {



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

                Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : ", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.getView().setPadding(20, 20, 20, 20);
                toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                toast.show();
                pDialog.dismiss();
                // } else {
                Toast toast2 = Toast.makeText(getApplicationContext(), "No Records Founds : ", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.BOTTOM, 0, 0);
                toast2.getView().setPadding(20, 20, 20, 20);
                toast2.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                toast2.show();
                pDialog.dismiss();


            } catch (Exception e) {
                Toast.makeText(AddGeneralActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }
    }
}

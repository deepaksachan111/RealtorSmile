package quaere.com.realtorsmile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intex on 12/24/2015.
 */
public class RegisterActivity extends Activity implements AdapterView.OnItemSelectedListener {
    EditText firstNameEt,lastNameEt,companyNameEt,mobileNoEt,emailEt,pswdEt,cnfrmPswdEt;
    String firstName,lastName,companyName,mobileNo,email,pswd,confrmPswd;
    RadioButton agentBtn,builderBtn;
    Spinner citySpinner, localitySpinner, peopleSpinner;
    Button register;
    RadioGroup radioGroup;
    RadioButton agent, builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        firstNameEt = (EditText) findViewById(R.id.register_firstNameEt);
        lastNameEt = (EditText) findViewById(R.id.register_lastNameEt);
        companyNameEt = (EditText) findViewById(R.id.register_companyNameEt);
        mobileNoEt = (EditText) findViewById(R.id.register_mobileNoEt);
        emailEt = (EditText) findViewById(R.id.register_emailEt);
        pswdEt = (EditText) findViewById(R.id.register_pswdEt);
        cnfrmPswdEt = (EditText) findViewById(R.id.register_cnfrmPswdEt);
        citySpinner = (Spinner) findViewById(R.id.register_citySpinner);
        localitySpinner = (Spinner) findViewById(R.id.register_localitySpinner);
        peopleSpinner = (Spinner) findViewById(R.id.register_peopleSpinner);
        radioGroup = (RadioGroup) findViewById(R.id.register_radioGroup);
        agent = (RadioButton) findViewById(R.id.agent_radioButton);
        builder = (RadioButton) findViewById(R.id.builder_radioButton);
        register = (Button) findViewById(R.id.register_submitBtn);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainPage.class));
               // register();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.agent_radioButton) {
                    Toast.makeText(getApplicationContext(), "Agent Selected",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.builder_radioButton) {
                    Toast.makeText(getApplicationContext(), "Builder Selected",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Spinner Drop down elements
        List<String> noOfPeople = new ArrayList<String>();
        noOfPeople.add("How many people will use realtorSmile ?");
        noOfPeople.add("Single User");
        noOfPeople.add("1 - 3 Users");
        noOfPeople.add("4 - 10 Users");
        noOfPeople.add("11 - 25 Users");
        noOfPeople.add("26 - 50 Users");
        noOfPeople.add("51 - 100 Users");
        noOfPeople.add("100+ Users");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, noOfPeople);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        peopleSpinner.setAdapter(dataAdapter);
    }

    private void register() {
        firstName = firstNameEt.getText().toString();
        lastName = lastNameEt.getText().toString();
        companyName = companyNameEt.getText().toString();
        mobileNo = mobileNoEt.getText().toString();
        email = emailEt.getText().toString();
        pswd = pswdEt.getText().toString();
        confrmPswd = cnfrmPswdEt.getText().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

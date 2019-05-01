package app.facedetection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import app.facedetection.db.DatabaseManager;

import java.util.Random;

public class Add_Names extends AppCompatActivity {

    String TAG=Add_Names.class.getSimpleName();
    EditText etName;
    Button btnSave;
    DatabaseManager mDb;
    Context mContext=Add_Names.this ;
    int abcd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__names);
        this.setTitle("Add Students");
        mDb=new DatabaseManager(mContext);
        etName=findViewById(R.id.et_studentName);
        Random rand = new Random();
        abcd = rand.nextInt(10000);
        btnSave=findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: " +abcd );
                mDb.saveUserName(etName.getText().toString(),"A", String.valueOf(abcd));
                startActivity(new Intent(Add_Names.this,UserLists.class));
            }
        });





    }

}

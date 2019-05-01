package app.facedetection;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.facedetection.dao.AttendanceDao;
import app.facedetection.db.DatabaseManager;

import java.util.ArrayList;

/**
 * Created by Satish on 30/04/2019 5:47 PM.
 */
public class UserLists  extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 100;
    DatabaseManager mDb;
    private RecyclerView mRecyclerView;
    String TAG = UserLists.class.getSimpleName();
    Context mContext;
    Adapter adapter ;
    TextView tv_AddUser, tvNoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        this.setTitle("Students Attendance System");
        mContext = UserLists.this;
        mDb = new DatabaseManager(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
            }
        }
        mRecyclerView = findViewById(R.id.rv);
        tvNoUser=findViewById(R.id.tv_nostudents);
        showNoUser();
        tv_AddUser=findViewById(R.id.add_user);
        tv_AddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserLists.this,Add_Names.class));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new Adapter(mDb.getAll());
        mRecyclerView.setAdapter(adapter);


    }

    void showNoUser(){
        if(mDb.getAll().size()==0){
            tvNoUser.setVisibility(View.VISIBLE);
        }else{
            tvNoUser.setVisibility(View.VISIBLE);
        }
    }

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Camera permission granted.", Toast.LENGTH_LONG).show();

                // Do stuff here for Action Image Capture.

            } else {

                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showNoUser();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new Adapter(mDb.getAll());
        mRecyclerView.setAdapter(adapter);

    }

    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        private ArrayList<AttendanceDao> l = new ArrayList<>();


        Adapter(ArrayList<AttendanceDao> l) {
            this.l = l;


        }


        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup p, int t) {
            return new Adapter.ViewHolder(LayoutInflater.from(p.getContext()).inflate(R.layout.venue_student_item, p, false));
        }

        @Override
        public void onBindViewHolder(Adapter.ViewHolder h, int i) {

            h.bind(l.get(i), i);


        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return l.size();
        }


        @Override
        public int getItemCount() {
            return l.size();
        }





        class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout ll;
            ImageView pic, who;
            TextView name, userStatus,iv_student_id;
            CardView mCard;
            ViewHolder(View v) {
                super(v);

                name = (TextView) v.findViewById(R.id.tv_student_name);
                userStatus = (TextView) v.findViewById(R.id.status_attend);
                iv_student_id=v.findViewById(R.id.iv_student_id);
                mCard=v.findViewById(R.id.lcv);

            }

            void bind(final AttendanceDao m, int i) {
                i++;
                Log.e(TAG, "bind: " +m );
                tvNoUser.setVisibility(View.GONE);
                name.setText(m.studentName);
                userStatus.setText(m.student_status);
                iv_student_id.setText(String.valueOf(i));
                mCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(UserLists.this,MainActivity.class).putExtra("id",m.students_roll));
                    }
                });



            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
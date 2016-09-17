package me.fangtian.lxt.timecard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailStudentActivity extends AppCompatActivity {

    private ArrayList<String> student = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_student);


        student = getIntent().getStringArrayListExtra(ClassRoomActivity.STUDENT);

        TextView student_id = (TextView) findViewById(R.id.detail_student_id);
        student_id.setText(student.get(0));

        TextView student_name = (TextView) findViewById(R.id.detail_student_name);
        student_name.setText(student.get(1));


        Button doLeft = (Button) findViewById(R.id.left_button);
        doLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(ClassRoomActivity.RETURN_BARCODE, student.get(0));
                setResult(RESULT_OK, data);
                finish();
            }
        });


    }
}

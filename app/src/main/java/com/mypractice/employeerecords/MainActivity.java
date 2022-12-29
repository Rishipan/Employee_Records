package com.mypractice.employeerecords;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText ed_id, ed_name, ed_sal;
    Button b_save, b_showAll, b_find, b_update;
    SQLiteDatabase db;
        @Override
        protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_id=findViewById(R.id.text_id);
        ed_name=findViewById(R.id.text_name);
        ed_sal=findViewById(R.id.text_salary);
        b_save=findViewById(R.id.btn_save);
        b_showAll=findViewById(R.id.btn_show_all);
        b_find=findViewById(R.id.btn_find);
        b_update=findViewById(R.id.btn_update);
        db=openOrCreateDatabase("EmpDbase.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists employee(id text, name text, salary int)");
        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_id.getText().toString().length()==0 ||
                    ed_name.getText().toString().length()==0 ||
                    ed_sal.getText().toString().length()==0){
                    Toast.makeText(MainActivity.this, "Please enter all the Values!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String id = ed_id.getText().toString();
                    String nm = ed_name.getText().toString();
                    int sal = Integer.parseInt(ed_sal.getText().toString());
                    Cursor c1 = db.rawQuery("select * from employee where id='" + id + "'", null);
                    if (c1.getCount() > 0) {
                        Toast.makeText(MainActivity.this, "Already registered!!", Toast.LENGTH_SHORT).show();
                    } else {
                        db.execSQL("insert into employee(id,name,salary) values('" + id + "','" + nm + "'," + sal + ") ");
                        Toast.makeText(MainActivity.this, "data saved", Toast.LENGTH_SHORT).show();
                        ed_id.setText("");
                        ed_name.setText("");
                        ed_sal.setText("0");
                    }
                }
            }

        });
            b_showAll.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Cursor c = db.rawQuery("select * from employee", null);
                                                 StringBuilder sb = new StringBuilder();
                                                 while (c.moveToNext()) {
                                                     sb.append(c.getString(0) + "\n");
                                                     sb.append(c.getString(1) + "\n");
                                                     sb.append(c.getInt(2) + "\n\n");
                                                 }
                                                 showmsg("Employee Records", sb.toString());
                                             }
                                         });
            b_find.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ed_id.getText().toString().length()==0){
                        Toast.makeText(MainActivity.this, "Please enter id", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String id=ed_id.getText().toString();
                        Cursor c2=db.rawQuery("select name,salary from employee where id='"+id+"'",null);
                        if(c2.moveToNext()){
                            String nm=c2.getString(0);
                            String sal=String.valueOf(c2.getInt(1));
                            ed_name.setText(nm);
                            ed_sal.setText(sal);

                        }
                        else{
                            Toast.makeText(MainActivity.this, "No Such Record!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            b_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ed_id.getText().toString().length()==0||
                            ed_name.getText().toString().length()==0||
                            ed_sal.getText().toString().length()==0){
                        Toast.makeText(MainActivity.this, "Enter all values!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String id=ed_id.getText().toString();
                        String nm=ed_name.getText().toString();
                        int sal=Integer.parseInt(ed_sal.getText().toString());
                        db.execSQL("update employee set name='"+nm+"',salary="+sal+" where id='"+id+"'");
                        Toast.makeText(MainActivity.this, "Data Updated Successfully!!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    public void showmsg(String title,String msg){
        AlertDialog.Builder b=new AlertDialog.Builder(MainActivity.this);
        b.setTitle(title);
        b.setMessage(msg);
        b.setCancelable(true);
        b.show();
    }
}



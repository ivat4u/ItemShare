package example.com.itemshare;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActy extends AppCompatActivity {
    static int logined=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button key_login= (Button) findViewById(R.id.log_in);
        final EditText  usr=(EditText)findViewById(R.id.ed_name);
        final EditText psw=(EditText)findViewById(R.id.ed_pass);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this,"test.db",null,1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues() ;
        values.put("username", "root");
        values.put("password", "123");
        db.insert("users", null, values);
        values.clear();

        values.put("username", "kei");
        values.put("password", "kei");
        db.insert("users", null, values);

        key_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String id = usr.getText().toString().trim();
                final String pwd = psw.getText().toString().trim();
                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(LoginActy.this, "用户名为空登录失败", 0).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(LoginActy.this, "密码为空登陆失败", 0).show();
                    return;
                }

                Cursor cursor = db.query("users", null, null, null, null, null, null);
                if (cursor != null) {
// 循环遍历cursor 

                    while (cursor.moveToNext()) {
// 拿到每一行name 与hp的数值 
                        String name = cursor.getString(cursor.getColumnIndex("username"));
                        if (name.equals(id)) {
                            String hp = cursor.getString(cursor.getColumnIndex("password"));
                            if (hp.equals(pwd)) {
                                Toast.makeText(LoginActy.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActy.this, MainActivity.class);
                                startActivity(intent);
                                logined=1;
                                break;

                            } else
                                Toast.makeText(LoginActy.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                            logined=1;
                              break;
                        }

                    }
                    if(logined==0)
                    Toast.makeText(LoginActy.this, "无此用户", Toast.LENGTH_SHORT).show();
                logined=0;
            }
        }});

}
}

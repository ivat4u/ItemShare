package example.com.itemshare;


import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActy extends AppCompatActivity {
    static int logined=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button key_login= (Button) findViewById(R.id.log_in);
        TextView key_logup=(TextView)findViewById(R.id.tv_name);
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
        key_logup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //file:///android_asset/register.html
                Intent intent=new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri CONTENT_URI_BROWSERS = Uri.parse("http://123.207.247.90/sharegoods/register.html");
                intent.setData(CONTENT_URI_BROWSERS);
                intent.setComponent(new ComponentName("com.android.browser", "com.android.browser.BrowserActivity"));
                startActivity(intent);
            }

        });
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
    public static void installApkFile(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.yuneec.android.saleelfin.fileprovider", new File(filePath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}

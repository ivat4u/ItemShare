package example.com.itemshare;


import android.content.Intent;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button key_login= (Button) findViewById(R.id.log_in);
        final EditText  usr=(EditText)findViewById(R.id.ed_name);
        final EditText psw=(EditText)findViewById(R.id.ed_pass);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection cn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test",
                                    "xialong", "123456");
                            Statement stmt=cn.createStatement();
                            String logSql = "select * from users where username ='" + id
                                    + "' and password ='" + pwd + "'";
                            ResultSet rs = DBUtils.executeQuery(logSql,stmt);
                            if ( rs.next()){
                                Message msg = new Message();
                                msg.what=1;
                                msg.obj=new String();
                                Toast.makeText(LoginActy.this, "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActy.this,MainActivity.class);
                                startActivity(intent);

                            }
                            else
                                Toast.makeText(LoginActy.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }


                    }}).start();
                    }
            }
        );

}
}

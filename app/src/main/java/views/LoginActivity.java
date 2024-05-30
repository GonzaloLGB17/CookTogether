package views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooktogether.R;

import java.sql.SQLException;

import controllers.UserController;
import models.UserModel;


public class LoginActivity extends AppCompatActivity {

    private TextView tvRegisterLogin;
    private EditText etUsernameLogin, etPasswordLogin;
    private UserController userController = new UserController();
    private CheckBox chSesion;
    private ImageView logo;
    UserModel user = new UserModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        boolean sesion = loadCredentials();
        if(sesion){
            Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        initComponents();
    }

    private void initComponents(){
        etUsernameLogin = findViewById(R.id.etUsernameLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        tvRegisterLogin = findViewById(R.id.tvRegisterLogin);
        chSesion = findViewById(R.id.chSesion);
        logo = findViewById(R.id.imgLogoLogin);
        tvRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

    }

    public void login(View view){
        String username = etUsernameLogin.getText().toString();
        String password = etPasswordLogin.getText().toString();
        try {
            userController.iniciarSesion(username,password);
            user = userController.buscarUsuario(username);
            if(chSesion.isChecked()){
                saveCredentials();
            }
            Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean loadCredentials(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuario = preferences.getString("user","no");
        String pass = preferences.getString("pass","no");
        if(usuario.equals("no") && pass.equals("no")){
            return false;
        }
        try {
            user = userController.buscarUsuario(usuario);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void saveCredentials(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuario = etUsernameLogin.getText().toString();
        String pass = etPasswordLogin.getText().toString();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user",usuario);
        editor.putString("pass", pass);

        editor.commit();
    }

}
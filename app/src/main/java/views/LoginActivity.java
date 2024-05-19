package views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telecom.Connection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cooktogether.R;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;

import controllers.UserController;
import database.ConexionBBDD;
import models.UserModel;


public class LoginActivity extends AppCompatActivity {

    private TextView tvRegisterLogin;
    private EditText etUsernameLogin, etPasswordLogin;
    private UserController userController = new UserController();
    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
    }

    private void initComponents(){
        etUsernameLogin = findViewById(R.id.etUsernameLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        tvRegisterLogin = findViewById(R.id.tvRegisterLogin);
        etUsernameLogin.setText("glpz00");
        etPasswordLogin.setText("1234");
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
            UserModel user = userController.buscarUsuario(username);
            Intent publicar = new Intent(LoginActivity.this, InicioActivity.class);
            publicar.putExtra("username", username);
            startActivity(publicar);
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
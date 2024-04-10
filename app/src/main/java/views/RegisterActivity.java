package views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cooktogether.R;

import java.sql.SQLException;

import controllers.UserController;
import models.UserModel;

public class RegisterActivity extends AppCompatActivity {
    private UserController userController = new UserController();
    private EditText etNombreRegister, etApellidosRegister, etCorreoRegister, etUsernameRegister, etPasswordRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initComponents();
    }

    private void initComponents(){
        etNombreRegister = findViewById(R.id.etNombreRegister);
        etApellidosRegister = findViewById(R.id.etApellidosRegister);
        etCorreoRegister = findViewById(R.id.etCorreoRegister);
        etUsernameRegister = findViewById(R.id.etUsernameRegister);
        etPasswordRegister = findViewById(R.id.etPasswordRegister);
    }

    public void registrar(View view){
        UserModel userModel = new UserModel(
                etNombreRegister.getText().toString(),
                etApellidosRegister.getText().toString(),
                etCorreoRegister.getText().toString(),
                etUsernameRegister.getText().toString(),
                etPasswordRegister.getText().toString(),
                0
        );
        try {
            userController.insertarUsuario(userModel);
            Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(login);

    }
}
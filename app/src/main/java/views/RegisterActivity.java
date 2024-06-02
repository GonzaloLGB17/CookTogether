package views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import utils.ImageUtil;

public class RegisterActivity extends AppCompatActivity {
    private UserController userController = new UserController();
    private EditText etNombreRegister, etApellidosRegister, etCorreoRegister, etUsernameRegister, etPasswordRegister, etConfirmarPasswordRegister;
    private ImageView imgUserRegister;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap = null;
    private ImageUtil imageUtil = new ImageUtil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.consRegister), (v, insets) -> {
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
        etConfirmarPasswordRegister = findViewById(R.id.etConfirmarPasswordRegister);
        imgUserRegister = findViewById(R.id.imgUserRegister);

        imgUserRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la galería cuando el ImageView sea clickeado
                Intent intent = new Intent();
                intent.setType("image/*"); // Establece el tipo de datos que se espera seleccionar en la galería como imágenes.
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); // Inicia una actividad para seleccionar una imagen de la galería y espera el resultado.
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Cuando se selecciona una imagen de la galería, obtener la URI de la imagen
            Uri uri = data.getData(); // Obtiene la URI de la imagen seleccionada.
            try {
                // Convertir la URI en un Bitmap
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Establecer el Bitmap en el ImageView
                imgUserRegister.setImageBitmap(imageUtil.redimensionarImagen(bitmap,1024,1024));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void registrar(View view){
        if(bitmap == null){
            Toast.makeText(this, "Selecciona una foto para tu perfil.", Toast.LENGTH_SHORT).show();
        }
        else if(etUsernameRegister.getText().toString().isEmpty() || etNombreRegister.getText().toString().isEmpty() ||
                etApellidosRegister.getText().toString().isEmpty() || etCorreoRegister.getText().toString().isEmpty() ||
                etPasswordRegister.getText().toString().isEmpty() || etConfirmarPasswordRegister.getText().toString().isEmpty()){
            Toast.makeText(this, "Ningún campo puede estar vacío.", Toast.LENGTH_SHORT).show();
        }else if(!etPasswordRegister.getText().toString().equals(etConfirmarPasswordRegister.getText().toString())){
            Toast.makeText(this, "Las contraseñas introducidas no coinciden.", Toast.LENGTH_SHORT).show();
        }else if(etUsernameRegister.getText().toString().length()>10){
            Toast.makeText(this, "El nombre de usuario es demasiado largo.", Toast.LENGTH_SHORT).show();
        }else{
            UserModel userModel = new UserModel(
                    etNombreRegister.getText().toString(),
                    etApellidosRegister.getText().toString(),
                    etCorreoRegister.getText().toString(),
                    etUsernameRegister.getText().toString().toLowerCase(),
                    etPasswordRegister.getText().toString(),
                    imageUtil.optimizarImagen(bitmap,1024,1024,90)
            );
            try {
                userController.insertarUsuario(userModel);
                Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show();
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(login);
            } catch (SQLException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void volverLogin(View view){
        Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(login);
    }


}
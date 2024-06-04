package views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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

public class EditActivity extends AppCompatActivity {
    private ImageView imgUserEdit, imgReturnEdit, imgConfirmarEdit;
    private EditText etUsernameEdit, etOldPasswordEdit, etNewPasswordEdit, etConfirmarNewPasswordEdit;
    private CheckBox chCambiarPass;
    private UserModel user = new UserModel();
    private ImageUtil imageUtil = new ImageUtil();
    private Bitmap bitmap = null;
    private UserController userController = new UserController();
    private boolean checked = false;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.consEdit), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();
    }

    private void initComponents(){
        imgUserEdit = findViewById(R.id.imgUserEdit);
        imgReturnEdit = findViewById(R.id.imgBtReturnEdit);
        imgConfirmarEdit = findViewById(R.id.imgConfirmar);
        etUsernameEdit = findViewById(R.id.etUsernameEdit);
        etOldPasswordEdit = findViewById(R.id.etOldPasswordEdit);
        etNewPasswordEdit = findViewById(R.id.etNewPasswordEdit);
        etConfirmarNewPasswordEdit = findViewById(R.id.etConfirmarNewPasswordEdit);
        chCambiarPass = findViewById(R.id.chCambiarPass);

        etNewPasswordEdit.setVisibility(View.GONE);
        etConfirmarNewPasswordEdit.setVisibility(View.GONE);
        etOldPasswordEdit.setVisibility(View.GONE);

        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");
        bitmap = imageUtil.transformarBytesBitmap(user.getFotoUsuario());
        imgUserEdit.setImageBitmap(imageUtil.redimensionarImagen(bitmap,1920,1080));
        etUsernameEdit.setText(user.getUsername());
        chCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chCambiarPass.isChecked()){
                    etNewPasswordEdit.setVisibility(View.VISIBLE);
                    etConfirmarNewPasswordEdit.setVisibility(View.VISIBLE);
                    etOldPasswordEdit.setVisibility(View.VISIBLE);
                    checked = true;
                }else{
                    etNewPasswordEdit.setVisibility(View.GONE);
                    etConfirmarNewPasswordEdit.setVisibility(View.GONE);
                    etOldPasswordEdit.setVisibility(View.GONE);
                    checked = false;
                }
            }
        });

        imgReturnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, PerfilActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        imgConfirmarEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    editarUser();
            }
        });

        imgUserEdit.setOnClickListener(new View.OnClickListener() {
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
                imgUserEdit.setImageBitmap(imageUtil.redimensionarImagen(bitmap,143,146));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void editarUser(){
        boolean usuarioDispo = comprobarUsername();
        boolean df = true;
        if(checked){
            if(etOldPasswordEdit.getText().toString().isEmpty() || etNewPasswordEdit.getText().toString().isEmpty()
                    || etConfirmarNewPasswordEdit.getText().toString().isEmpty()){
                Toast.makeText(this, "Ningún campo de contraseña puede estar vacío.", Toast.LENGTH_SHORT).show();
                df = false;
            }else if(etNewPasswordEdit.getText().toString().equals(etOldPasswordEdit.getText().toString())){
                Toast.makeText(this, "La contraseña actual y la contraseña vieja son la misma.", Toast.LENGTH_SHORT).show();
                df = false;
            }else if(!etConfirmarNewPasswordEdit.getText().toString().equals(etNewPasswordEdit.getText().toString())){
                Toast.makeText(this, "Las contraseñas nuevas introducidas no coinciden.", Toast.LENGTH_SHORT).show();
                df = false;
            }else {
                try {
                    userController.cambiarPass(etNewPasswordEdit.getText().toString(), etOldPasswordEdit.getText().toString(),user.getId());
                    df = true;
                } catch (SQLException e) {
                    df = false;
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(df){
            if(etUsernameEdit.getText().toString().length()>10){
                Toast.makeText(this, "El nombre de usuario es demasiado largo.", Toast.LENGTH_SHORT).show();
            }else if(etUsernameEdit.getText().toString().isEmpty()){
                Toast.makeText(this, "El campo de usuario no puede estar vacío.", Toast.LENGTH_SHORT).show();
            }else if(usuarioDispo){
                Toast.makeText(this, "El nombre de usuario no esta disponible", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    userController.actualizarUsuario(user,checked,etUsernameEdit.getText().toString(),
                            imageUtil.optimizarImagen(bitmap,1024,1024,80),
                            etNewPasswordEdit.getText().toString(), etOldPasswordEdit.getText().toString());

                    user = userController.buscarUsuario(etUsernameEdit.getText().toString());
                    saveCredentials();
                    Intent intent = new Intent(EditActivity.this, PerfilActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                } catch (SQLException e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveCredentials(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String usuario = etUsernameEdit.getText().toString();
        String pass = etNewPasswordEdit.getText().toString();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user",usuario);
        editor.putString("pass", pass);

        editor.commit();
    }

    private boolean comprobarUsername(){
     boolean userDispo = false;
        if(!etUsernameEdit.getText().toString().equals(user.getUsername())){
            // Uso este condicional para comprobar que el nombre nuevo del usuario no esté ya usado por otro
            try {
                userDispo = userController.comprobarUsuario(etUsernameEdit.getText().toString());
            } catch (SQLException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return userDispo;
    }
}
package views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cooktogether.R;

import models.UserModel;
import utils.ImageUtil;

public class EditActivity extends AppCompatActivity {
    private ImageView imgUserEdit, imgReturnEdit, imgConfirmarEdit;
    private EditText etUsernameEdit, etOldPasswordEdit, etNewPasswordEdit;
    private CheckBox chCambiarPass;
    private UserModel user = new UserModel();
    private ImageUtil imageUtil = new ImageUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
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
        chCambiarPass = findViewById(R.id.chCambiarPass);

        etNewPasswordEdit.setVisibility(View.GONE);
        etOldPasswordEdit.setVisibility(View.GONE);

        Intent intent = getIntent();
        user = (UserModel) intent.getSerializableExtra("user");

        imgUserEdit.setImageBitmap(imageUtil.transformarBytesBitmap(user.getFotoUsuario()));

        chCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chCambiarPass.isChecked()){
                    etNewPasswordEdit.setVisibility(View.VISIBLE);
                    etOldPasswordEdit.setVisibility(View.VISIBLE);
                }else{
                    etNewPasswordEdit.setVisibility(View.GONE);
                    etOldPasswordEdit.setVisibility(View.GONE);
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
    }
}
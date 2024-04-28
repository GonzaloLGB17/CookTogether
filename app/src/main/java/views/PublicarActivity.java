package views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

public class PublicarActivity extends AppCompatActivity {
    private ImageView imgUserPublicar;
    private TextView tvUserPublicar;
    private UserController userController = new UserController();
    private ImageUtil imageUtil = new ImageUtil();
    UserModel user = new UserModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_publicar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgUserPublicar = findViewById(R.id.imgUserPublicar);
        tvUserPublicar = findViewById(R.id.tvUserPublicar);
        Intent login = getIntent();
        String username = login.getStringExtra("username");
        try {
            user = userController.buscarUsuario(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tvUserPublicar.setText(username);
        imgUserPublicar.setImageBitmap(imageUtil.transformarBytesBitmap(user.getFotoUsuario()));
    }
}
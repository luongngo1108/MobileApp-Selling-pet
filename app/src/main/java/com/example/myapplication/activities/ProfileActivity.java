package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.models.User;

public class ProfileActivity extends AppCompatActivity {
    int user_id;
    String user_name, user_email, user_image;
    ImageView imageViewAvatar;
    TextView textViewId;
    TextView textViewName;
    TextView textViewEmail;
    TextView txtBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        Anhxa();

        Glide.with(this)
                .load(user_image)
                .apply(RequestOptions.circleCropTransform())
                .into(imageViewAvatar);
        textViewId.setText(String.valueOf(user_id));
        textViewEmail.setText(user_email);
        textViewName.setText(user_name);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homepage = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(homepage);
                finish();
            }
        });
    }

    private void Anhxa() {
        user_id = getIntent().getIntExtra("user_id", 0);
        user_name = getIntent().getStringExtra("user_name");
        user_email = getIntent().getStringExtra("user_email");
        user_image = getIntent().getStringExtra("user_image");

        imageViewAvatar = (ImageView) findViewById(R.id.imgUserInfo);
        textViewId = (TextView) findViewById(R.id.tvId);
        textViewName = (TextView) findViewById(R.id.tvHoTen);
        textViewEmail = (TextView) findViewById(R.id.tvEmail);
        txtBack = (TextView) findViewById(R.id.txtBack);
    }
}
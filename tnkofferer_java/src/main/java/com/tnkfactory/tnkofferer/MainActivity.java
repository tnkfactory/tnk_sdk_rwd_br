package com.tnkfactory.tnkofferer;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.tnkfactory.ad.TnkOfferwall;

public class MainActivity extends AppCompatActivity {
    TnkOfferwall offerwall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        offerwall = new TnkOfferwall(MainActivity.this);

        offerwall.setCOPPA(false);
        offerwall.setUserName("unique_user_id");

        TextView tvStartOfferwall = findViewById(R.id.tv_start_offerwall);
        tvStartOfferwall.setOnClickListener(v -> {
            offerwall.startOfferwallActivity(MainActivity.this);
        });
    }
}

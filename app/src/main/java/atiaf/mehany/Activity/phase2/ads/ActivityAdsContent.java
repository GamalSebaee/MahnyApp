package atiaf.mehany.Activity.phase2.ads;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import atiaf.mehany.Activity.phase2.BaseActivity;
import atiaf.mehany.R;

public class ActivityAdsContent extends BaseActivity {

    ImageView iv_close;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_content);
        initViews();
    }

    @Override
    public void initViews() {
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(view -> super.onBackPressed());
    }
}

package com.tnkfactory.tnkofferer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tnkfactory.ad.PlacementEventListener;
import com.tnkfactory.ad.TnkAdConfig;
import com.tnkfactory.ad.TnkOfferwall;
import com.tnkfactory.ad.basic.AdPlacementView;
import com.tnkfactory.ad.basic.PlacementFeedViewLayout;
import com.tnkfactory.ad.basic.PlacementScrollViewLayout;
import com.tnkfactory.ad.basic.PlacementViewPagerLayout;
import com.tnkfactory.ad.basic.TnkAdPlacementFeedImageItem;
import com.tnkfactory.ad.basic.TnkAdPlacementFeedItem;
import com.tnkfactory.ad.basic.TnkAdPlacementIconItem;
import com.tnkfactory.ad.basic.TnkAdPlacementListItem;
import com.tnkfactory.ad.rwd.AdvertisingIdInfo;
import com.tnkfactory.offerrer.TnkAdManager;
import com.tnkfactory.tnkofferer.databinding.ActivityMainBinding;

import kotlin.reflect.KClass;

public class MainActivityJava extends AppCompatActivity {
    TnkOfferwall offerwall;
    ViewGroup placementContainerView;
    AdPlacementView adPlacementView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        placementContainerView = binding.flPlacementAd;
        offerwall = new TnkOfferwall(this);


        Runnable rn = () -> {
            // 고유 아이디는 매체사에서 유저 식별을 위한 고유값을 사용하셔야 하며
            // 이 예제에서는 google adid를 사용 합니다.
            AdvertisingIdInfo adInfo = AdvertisingIdInfo.requestIdInfo(MainActivityJava.this); // backgroud thread 처리 필요
            String id = adInfo.getId();

            // 2) 유저 식별값 설정
            offerwall.setUserName(id);
            // 3) COPPA 설정 (https://www.ftc.gov/business-guidance/privacy-security/childrens-privacy)
            offerwall.setCOPPA(false);

            offerwall.getEarnPoint(point -> {
                runOnUiThread(() -> {
                    binding.tvPoint.setText(String.format("받을 수 있는 포인트 : %d p", point));
                });
                return null;
            });
        };
        https://adm.tnkad.net/adx/upload/java_sample_0708.apk
        new Thread(rn).start();

        binding.btnDefault.setOnClickListener(view -> {
            TnkAdManager.INSTANCE.setCustomClass();
            TnkAdManager.INSTANCE.startDefaultActivity(this);
        });

//        binding.btnRecyclerView.setOnClickListener(view -> {
//            startActivity(new Intent(MainActivityJava.this, RecyclerViewSample.class));
//        });


        binding.tvPlacement1.setOnClickListener(view -> {
            TnkAdConfig.INSTANCE.setPlacementLayout("open_ad", classToKClass(TnkAdPlacementFeedItem.class), classToKClass(PlacementFeedViewLayout.class));
            setupPlacementView();
        });
        binding.tvPlacement2.setOnClickListener(view -> {
            TnkAdConfig.INSTANCE.setPlacementLayout("open_ad", classToKClass(TnkAdPlacementFeedImageItem.class), classToKClass(PlacementFeedViewLayout.class));
            setupPlacementView();
        });
        binding.tvPlacement3.setOnClickListener(view -> {
            TnkAdConfig.INSTANCE.setPlacementLayout("open_ad", classToKClass(TnkAdPlacementIconItem.class), classToKClass(PlacementScrollViewLayout.class));
            setupPlacementView();
        });
        binding.tvPlacement4.setOnClickListener(view -> {
            TnkAdConfig.INSTANCE.setPlacementLayout("open_ad", classToKClass(TnkAdPlacementListItem.class), classToKClass(PlacementViewPagerLayout.class));
            setupPlacementView();
            adPlacementView.setSpanCount(1);
            adPlacementView.setPageRowCount(3);
        });
    }

    KClass classToKClass(Class jClass) {
        return kotlin.jvm.JvmClassMappingKt.getKotlinClass(jClass);
    }

    void setupPlacementView() {
        adPlacementView = offerwall.getAdPlacementView(this);
        placementContainerView.removeAllViews();
        placementContainerView.addView(adPlacementView);
        loadPlacementView();


        adPlacementView.setPlacementEventListener(new PlacementEventListener() {

            @Override
            public void didAdDataLoaded(@NonNull String placementId, @Nullable String customData) {
                adPlacementView.showAdList();
            }

            @Override
            public void didFailedToLoad(@NonNull String placementId) {
                Toast.makeText(MainActivityJava.this, "광고 로딩 실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void didAdItemClicked(@NonNull String appId, @NonNull String appName) {
                Log.d("didAdItemClicked", "appId : $appId, appName : $appName");
            }

            @Override
            public void didMoreLinkClicked() {
                offerwall.startOfferwallActivity(MainActivityJava.this);
            }
        });
    }

    void loadPlacementView() {
        adPlacementView.loadAdList("open_ad");
    }

}

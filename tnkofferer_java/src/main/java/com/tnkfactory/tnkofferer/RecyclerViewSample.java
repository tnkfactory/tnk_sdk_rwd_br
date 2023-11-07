package com.tnkfactory.tnkofferer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tnkfactory.ad.PlacementEventListener;
import com.tnkfactory.ad.TnkAdConfig;
import com.tnkfactory.ad.TnkOfferwall;
import com.tnkfactory.ad.basic.AdPlacementView;
import com.tnkfactory.ad.basic.PlacementFeedViewLayout;
import com.tnkfactory.ad.basic.TnkAdPlacementFeedImageItem;
import com.tnkfactory.ad.basic.TnkAdPlacementFeedItem;
import com.tnkfactory.ad.rwd.AdvertisingIdInfo;
import com.tnkfactory.tnkofferer.databinding.ActivityRecyclerViewSampleBinding;

import java.util.ArrayList;

import kotlin.reflect.KClass;

public class RecyclerViewSample extends AppCompatActivity {

    // 리사이클러뷰 뷰 타입
    static final int VIEW_TYPE_NORMAL = 0;
    static final int VIEW_TYPE_TNK_AD = 2;

    TnkOfferwall offerwall;
    AdPlacementView adPlacementView;


    // 리아시클러뷰 아답터
    MyTestAdapter adapter;
    // 리사이클러뷰에 출력 할 아이템
    ArrayList<DataClass> items = new ArrayList<>();
    // diff util
    DiffUtil.ItemCallback<DataClass> mDifferCallBack;
    AsyncListDiffer<DataClass> mDiffer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // data binding객체를 얻습니다.
        ActivityRecyclerViewSampleBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view_sample);

        // 1) 오퍼월 초기화
        offerwall = new TnkOfferwall(this);
        // 2) 유저 식별값 설정
        offerwall.setUserName("s_test_user");
        // 3) COPPA 설정 (https://www.ftc.gov/business-guidance/privacy-security/childrens-privacy)
        offerwall.setCOPPA(false);

        binding.tvLoad.setOnClickListener(view -> {
            // open_ad 라는 광고의 레이아웃을 설정
            TnkAdConfig.INSTANCE.setPlacementLayout("open_ad", classToKClass(TnkAdPlacementFeedItem.class), classToKClass(PlacementFeedViewLayout.class));
            setupPlacementView();
        });


        // 리사이클러뷰에 출력 할 더미 아이템 생성
        for (int i = 0; i < 30; i++) {
            items.add(new DataClass(i, VIEW_TYPE_NORMAL));
        }

        // 리사이클러뷰 설정
        mDifferCallBack = new DiffUtil.ItemCallback<DataClass>() {

            @Override
            public boolean areItemsTheSame(@NonNull DataClass oldItem, @NonNull DataClass newItem) {
                return oldItem.type == newItem.type;
            }

            @Override
            public boolean areContentsTheSame(@NonNull DataClass oldItem, @NonNull DataClass newItem) {
                return oldItem.idx == newItem.idx;
            }
        };

        adapter = new MyTestAdapter();
        mDiffer = new AsyncListDiffer<>(adapter, mDifferCallBack);
        adapter.submitList(items);

        binding.tnkRecyclerView.setAdapter(adapter);
        binding.tnkRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (adPlacementView != null) {
            adPlacementView.update();
        }
    }

    // java클래스타입을 kotlin KClass타입으로
    KClass classToKClass(Class jClass) {
        return kotlin.jvm.JvmClassMappingKt.getKotlinClass(jClass);
    }

    // placement ad 설정
    void setupPlacementView() {
        // adPlacementView 객체를 얻습니다.
        adPlacementView = offerwall.getAdPlacementView(this);

        // open_ad라는 광고를 로드합니다.
        adPlacementView.loadAdList("open_ad");

        adPlacementView.setPlacementEventListener(new PlacementEventListener() {

            @Override
            public void didAdDataLoaded(@NonNull String placementId, @Nullable String customData) {
                Toast.makeText(RecyclerViewSample.this, "광고 로딩 성공", Toast.LENGTH_SHORT).show();
                // 리사이클러뷰의 5번쨰 아이템으로 삽입
                items.add(5, new DataClass(0, VIEW_TYPE_TNK_AD));
                adapter.notifyDataSetChanged();
//                adPlacementView.showAdList();
            }

            @Override
            public void didFailedToLoad(@NonNull String placementId) {
                Toast.makeText(RecyclerViewSample.this, "광고 로딩 실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void didAdItemClicked(@NonNull String appId, @NonNull String appName) {
                Log.d("didAdItemClicked", "appId : $appId, appName : $appName");
            }

            @Override
            public void didMoreLinkClicked() {
                offerwall.startOfferwallActivity(RecyclerViewSample.this);
            }
        });
    }


    // 이하 리사이클러뷰 설정
    // ** view type를 유니크 하게 설정해서 tnk광고가 출력되는 뷰가 restore되지 않도록 해야 중복 처리가 되지 않고 포지션도 유지됩니다.

    class MyTestAdapter extends RecyclerView.Adapter<IViewHolder> {
        @Override
        public int getItemCount() {
            return mDiffer.getCurrentList().size();
        }

        public void submitList(ArrayList<DataClass> list) {
            mDiffer.submitList(list);
        }

        @NonNull
        @Override
        public IViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (VIEW_TYPE_TNK_AD == viewType) {
                View view = getLayoutInflater().inflate(R.layout.row_placement_view_ad, parent, false);
                return new TnkPlacementViewHolder(view);
            } else {
                View view = getLayoutInflater().inflate(R.layout.row_placement_view_nor, parent, false);
                return new MyViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull IViewHolder holder, int position) {
            holder.onBind(mDiffer.getCurrentList().get(position));
        }

        @Override
        public int getItemViewType(int position) {
            if (items.get(position).type == VIEW_TYPE_TNK_AD) {
                return VIEW_TYPE_TNK_AD;
            } else {
                return super.getItemViewType(position);
            }
        }
    }

    abstract class IViewHolder extends RecyclerView.ViewHolder {
        public IViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public abstract void onBind(DataClass data);
    }

    class MyViewHolder extends IViewHolder {
        TextView tvRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRow = itemView.findViewById(R.id.tv_test_row);
        }

        @Override
        public void onBind(DataClass data) {
            tvRow.setText("test row " + data.idx);

        }
    }

    class TnkPlacementViewHolder extends IViewHolder {

        FrameLayout contentParent;

        public TnkPlacementViewHolder(@NonNull View itemView) {
            super(itemView);
            contentParent = itemView.findViewById(R.id.row_ad_content);
        }

        @Override
        public void onBind(DataClass data) {
            if (contentParent.getChildCount() < 1) {
                contentParent.addView(adPlacementView);
                adPlacementView.showAdList();
            }
        }
    }


    class DataClass {
        DataClass(int idx, int type) {
            this.idx = idx;
            this.type = type;
        }

        int idx = 0;
        int type = 0;
    }
}

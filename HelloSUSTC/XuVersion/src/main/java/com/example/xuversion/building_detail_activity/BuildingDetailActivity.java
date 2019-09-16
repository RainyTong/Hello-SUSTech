package com.example.xuversion.building_detail_activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.xuversion.MyApplication;
import com.example.xuversion.R;
import com.example.xuversion.RealmHelper;
import com.example.xuversion.model.Building;
import com.example.xuversion.tensorflow_activity.TensorflowActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class BuildingDetailActivity extends AppCompatActivity {

    @BindView(R.id.layout_return)
    ImageView backBtn;
    @BindView(R.id.layout_name)
    TextView buildingTitle;
    @BindView(R.id.detailphoto)
    ImageView buildingPhoto;
    @BindView(R.id.image_bar)
    ProgressBar imageBar;

    private String buildingName;
    private Realm realm;
    private RealmHelper realmHelper;
    /**
     * bound click event
     *
     * @param view the clicked view
     */
    @OnClick({R.id.layout_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_return:
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);
        ButterKnife.bind(this);
        //从父亲activity获取值
        Bundle bundle = getIntent().getExtras();
        buildingName = bundle.getString("buildingName");
        buildingTitle.setText(buildingName);
        realm = Realm.getDefaultInstance();
        realmHelper = new RealmHelper(realm);
        loadImage();

    }



    private void loadImage() {
        Building building = realmHelper.getBuilding(buildingName);
        String url = MyApplication.getUrl()+"/files/"+building.getName()+".jpg";

        Glide.with(this)
                .load(url)
                .error(R.drawable.ic_none)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        //show();
                        imageBar.setVisibility(View.GONE);
                        showToast("图片加载失败");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // Lets attach some listeners, not required though!
                        imageBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(buildingPhoto);
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(BuildingDetailActivity.this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}

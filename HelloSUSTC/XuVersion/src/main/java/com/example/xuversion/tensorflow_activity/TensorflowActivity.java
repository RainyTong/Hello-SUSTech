package com.example.xuversion.tensorflow_activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xuversion.MyApplication;
import com.example.xuversion.R;
import com.example.xuversion.map_activity.MapActivity;
import com.example.xuversion.model.Status;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TensorflowActivity extends AppCompatActivity {

    @BindView(R.id.layout_return)
    ImageView layoutReturn;
    @BindView(R.id.layout_name)
    TextView layoutName;
    @BindView(R.id.detailphoto)
    ImageView detailphoto;
    @BindView(R.id.flow_upload)
    Button flowUpload;
    @BindView(R.id.flow_file)
    Button flowFile;
    @BindView(R.id.flow_camera)
    Button flowCamera;
    @BindView(R.id.flow_process)
    ProgressBar flowProcess;
    @BindView(R.id.flow_main)
    LinearLayout flowMain;
    @BindView(R.id.result_text)
    TextView resultText;
    @BindView(R.id.result_view)
    LinearLayout resultView;

    /**
     * view bind click event
     *
     * @param view view that need to set event
     */
    @OnClick({R.id.layout_return, R.id.flow_upload, R.id.flow_file, R.id.flow_camera,R.id.result_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_return:
                this.finish();
                break;
            case R.id.flow_upload:
                upload();
                break;
            case R.id.flow_file:
                goPhotoAlbum();
                break;
            case R.id.flow_camera:
                goCamera();
                break;
            case R.id.result_view:
                toMap();
                break;
            default:
                break;
        }
    }

    private File cameraSavePath;//拍照照片路径
    private Uri uri;
    private String photoPath;

    /**
     * onCread event
     *
     * @param savedInstanceState the last state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tensorflow);
        ButterKnife.bind(this);
        photoPath = null;
        layoutName.setText("Tensorflow");
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
    }

    private void toMap(){
        Bundle bundle = new Bundle();
        bundle.putString("result",resultText.getText().toString());
        Intent it = new Intent(TensorflowActivity.this, MapActivity.class);
        it.putExtras(bundle);
        startActivity(it);
        finish();
    }
    //激活相册操作
    private void goPhotoAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 2);
    }

    //激活相机操作
    private void goCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(TensorflowActivity.this, "com.example.xuversion.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        TensorflowActivity.this.startActivityForResult(intent, 1);
    }

    private void show(final boolean isok, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flowProcess.setVisibility(View.GONE);
                flowMain.setVisibility(View.VISIBLE);
                if(isok){
                    resultView.setVisibility(View.VISIBLE);
                    resultText.setText(text);
                }
            }
        });
    }

    private void hide() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flowProcess.setVisibility(View.VISIBLE);
                flowMain.setVisibility(View.GONE);
            }
        });
    }

    /**
     * on activity result
     *
     * @param requestCode request code
     * @param resultCode  result code
     * @param data        data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                photoPath = String.valueOf(cameraSavePath);
            } else {
                photoPath = uri.getEncodedPath();
            }
            Log.e("拍照返回图片路径:", photoPath);
            Glide.with(TensorflowActivity.this).load(photoPath).into(detailphoto);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            photoPath = GetPhotoFromPhotoAlbum.getRealPathFromUri(this, data.getData());
            Glide.with(TensorflowActivity.this).load(photoPath).into(detailphoto);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private Status getStatusFromJson(String s) {
        try {
            JSONObject j = new JSONObject(s);
            return new Status(j.getBoolean("result"), j.getString("description"));
        } catch (Exception e) {
            return new Status(false, e.getMessage());
        }
    }


    private void upload() {
        String url = MyApplication.getUrl() + "/";

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new
                MultipartBody.Builder().setType(MultipartBody.FORM);
        if (photoPath == null) {
            showToast("no image");
            return;
        }
        hide();
        Log.e("now", photoPath);
        File file = new File(photoPath);
        Log.e("size", file.length() + "");
        if (cameraSavePath != null) {
            builder.addFormDataPart("uploadfile", file.getName(),
                    RequestBody.create(MediaType.parse("png"), file));
        }
        MultipartBody multipartBody = builder.build();
        //构建请求
        final Request request = new Request.Builder()
                .url(url)//请求地址
                .post(multipartBody)//添加请求体参数
                .build();
        //请求回调
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                showToast("Upload Fail：" + e.getLocalizedMessage());
                show(false,"");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Status status = getStatusFromJson(response.body().string());
                String name = status.getDescription();
                if(status.isResult()){
                    showToast("Upload Success");
                    show(true,name);
                }else{
                    showToast("Result Fail：" + name);
                    show(false,"");
                }
            }
        });

    }


    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(TensorflowActivity.this, text, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }


}

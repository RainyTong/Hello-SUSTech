package com.example.xuversion.login_activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.example.xuversion.MyApplication;
import com.example.xuversion.R;
import com.example.xuversion.SharedUtil;
import com.example.xuversion.login_activity.anim.JellyInterpolator;
import com.example.xuversion.model.Status;
import com.example.xuversion.model.UserModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements RegFragmentListener,
        LoginFragmentListener {

    @BindView(R.id.main_btn_login)
    TextView loginBtn;
    @BindView(R.id.layout_progress)
    View progress;
    @BindView(R.id.input_layout)
    LinearLayout mInputLayout;
    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.login_forget_password)
    TextView forgetPassword;
    @BindView(R.id.login_return)
    ImageView loginReturn;

    /**
     * Bind onClick event
     * @param v view that need to be bound
     */
    @OnClick({R.id.main_btn_login, R.id.login_forget_password, R.id.sign_up, R.id.login_return})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_login:
                startAnim();
                break;
            case R.id.login_forget_password:
                //recoverFromAnim();
                break;
            case R.id.sign_up:
                if (nowMode == LOGIN) {
                    changeToRegister();
                } else {
                    changeToLogin(null, null);
                }
                break;
            case R.id.login_return:
                this.finish();
                break;
            default:
                break;
        }
    }

    private static final int LOGIN = 0;
    private static final int REGISTER = 1;
    private static final String DOLOGIN = "dologin";
    private static final String DOSEND = "dosend";
    private static final String DOREGISTER = "doregister";
    private static final String DOVERIFY = "doverify";

    private float mWidth;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private int nowMode; //0 is login, 1 is Register


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mWidth = loginBtn.getMeasuredWidth();
        //初始化忘记密码下划线
        forgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        forgetPassword.getPaint().setAntiAlias(true);//抗锯齿
        //加载碎片
        changeToLogin(null, null);

    }

    //Turn on animation
    private void startAnim() {
        if (nowMode == LOGIN) {
            loginFragment.setLayoutGone();
        } else {
            registerFragment.setLayoutGone();
        }
        progress.setVisibility(View.VISIBLE);
        setViewEnabled(false);
        inputAnimator(mInputLayout, mWidth);
    }

    private void setViewEnabled(boolean b) {
        signUp.setEnabled(b);
        loginBtn.setEnabled(b);
    }

    private Status getStatusFromJson(String s) {
        try {
            JSONObject j = new JSONObject(s);
            return new Status(j.getBoolean("result"), j.getString("description"));
        } catch (Exception e) {
            return new Status(false, e.getMessage());
        }
    }

    /**
     * when login or sign up fail, need to call this
     * method to reset view from anim
     */
    @Override
    public void recoverFromAnim() {
        setViewEnabled(true);
        mInputLayout.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        setViewMargin(mInputLayout, 0);
        mInputLayout.setScaleX(1);
    }

    /**
     * when click the login button, should use this function to
     * post information to the server
     *
     * @param userModel The information needed to log in
     */
    @Override
    public void doLogin(final UserModel userModel) {
        //Logon network request
        String url = MyApplication.getUrl() + "/user/login";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Status status = getStatusFromJson(s);
                        String name = status.getDescription();
                        if (status.isResult()) {
                            SharedUtil.getIntance(LoginActivity.this).writeShared("name",
                                    name);
                            showToast("hello: "+name);
                            Intent it = new Intent();
                            setResult(RESULT_OK, it);
                            finish();
                        } else {
                            showToast(name);
                            loginFragment.setLayoutRecover();
                            recoverFromAnim();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(volleyError.toString());
                        loginFragment.setLayoutRecover();
                        recoverFromAnim();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if (userModel.getMailbox() != null) {
                    map.put("mailAddress", userModel.getMailbox());
                } else {
                    map.put("username", userModel.getName());
                }
                map.put("password", userModel.getPassword());
                return map;
            }
        };
        //Set the network timeout
        request.setRetryPolicy(new DefaultRetryPolicy(8000, 0, 0f));
        request.setTag(DOLOGIN);
        MyApplication.getInstance().getHttpQueues().add(request);
    }

    /**
     * when click the send buttton, should use this function to
     * post information to the server
     *
     * @param userModel The information needed to send email
     */
    @Override
    public void onSendClick(final UserModel userModel) {

        String url = MyApplication.getUrl() + "/checkcode/get";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Status status = getStatusFromJson(s);
                        showToast(status.getDescription());
                        if (status.isResult()) {
                            registerFragment.setHasCheckTrue();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(volleyError.toString());
                        registerFragment.setLayoutRecover();
                        recoverFromAnim();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", userModel.getName());
                map.put("mailAddress", userModel.getMailbox());
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(25000, 0, 0f));
        request.setTag(DOSEND);
        MyApplication.getInstance().getHttpQueues().add(request);
    }

    /**
     * after verify the check code, will call this function to
     * register an account
     *
     * @param userModel The information needed to sign up
     */
    private void registerContinue(final UserModel userModel) {
        //注册网络请求
        String url = MyApplication.getUrl() + "/user/register";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Status status = getStatusFromJson(s);
                        showToast(status.getDescription());
                        if (status.isResult()) {
                            registerFragment = null;
                            changeToLogin(userModel.getMailbox(), userModel.getPassword());
                        } else {
                            registerFragment.setLayoutRecover();
                            recoverFromAnim();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(volleyError.toString());
                        registerFragment.setLayoutRecover();
                        recoverFromAnim();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", userModel.getName());
                map.put("mailAddress", userModel.getMailbox());
                map.put("password", userModel.getPassword());
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(8000, 0, 0f));
        request.setTag(DOREGISTER);
        MyApplication.getInstance().getHttpQueues().add(request);
    }

    /**
     * when click a sign up button, will call this function
     * to sign up, first need to verify check code
     *
     * @param userModel The information needed to sign up
     */
    @Override
    public void doRegister(final UserModel userModel) {
        //验证验证码
        String url = MyApplication.getUrl() + "/checkcode/verify";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Status status = getStatusFromJson(s);
                        if (status.isResult()) {
                            registerContinue(userModel);
                        } else {
                            showToast(status.getDescription());
                            registerFragment.setLayoutRecover();
                            recoverFromAnim();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        showToast(volleyError.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("mailAddress", userModel.getMailbox());
                map.put("checkCode", userModel.getCheckcode());
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(8000, 0, 0f));
        request.setTag(DOVERIFY);
        MyApplication.getInstance().getHttpQueues().add(request);
    }

    //change to LoginFragment
    private void changeToLogin(String name, String password) {
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance("", "");
            loginFragment.setToActivityListener(this);
        } else if (name != null && password != null) {
            loginFragment = LoginFragment.newInstance(name, password);
            loginFragment.setToActivityListener(this);
        }
        nowMode = LOGIN;
        setViewEnabled(true);
        signUp.setText("Sign up");
        loginBtn.setText("Log in");
        getSupportFragmentManager().beginTransaction().replace(R.id.input_layout, loginFragment,
                null).commit();

    }

    //change to RegisterFragment
    private void changeToRegister() {
        if (registerFragment == null) {
            registerFragment = RegisterFragment.newInstance();
            registerFragment.setToActivityListener(this);
        }
        nowMode = REGISTER;
        setViewEnabled(true);
        signUp.setText("Log in");
        loginBtn.setText("Sign up");
        getSupportFragmentManager().beginTransaction().replace(R.id.input_layout, registerFragment,
                null).commit();
    }


    private void setViewMargin(View view, float value) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = (int) value;
        params.rightMargin = (int) value;
        view.setLayoutParams(params);
    }

    /**
     * The animation of the input box
     *
     * @param view widget
     * @param w    width
     */
    private void inputAnimator(final View view, float w) {

        AnimatorSet set = new AnimatorSet();

        ValueAnimator animator = ValueAnimator.ofFloat(0, w);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                setViewMargin(view, value);
            }
        });

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mInputLayout,
                "scaleX", 1f, 0.5f);
        set.setDuration(1000);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.playTogether(animator, animator2);
        set.start();
        set.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                progress.setVisibility(View.VISIBLE);
                progressAnimator(progress);
                mInputLayout.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void progressAnimator(final View view) {
        PropertyValuesHolder animator = PropertyValuesHolder.ofFloat("scaleX",
                0.5f, 1f);
        PropertyValuesHolder animator2 = PropertyValuesHolder.ofFloat("scaleY",
                0.5f, 1f);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(view,
                animator, animator2);
        animator3.setDuration(1000);
        animator3.setInterpolator(new JellyInterpolator());
        animator3.start();
        animator3.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //finish the anim start request
                if (nowMode == LOGIN) {
                    loginFragment.doLogin();
                } else {
                    registerFragment.doRegister();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getInstance().getHttpQueues().cancelAll(DOLOGIN);
        MyApplication.getInstance().getHttpQueues().cancelAll(DOSEND);
        MyApplication.getInstance().getHttpQueues().cancelAll(DOREGISTER);
        MyApplication.getInstance().getHttpQueues().cancelAll(DOVERIFY);
    }
}

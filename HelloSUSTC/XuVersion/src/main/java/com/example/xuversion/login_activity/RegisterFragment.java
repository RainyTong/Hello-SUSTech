package com.example.xuversion.login_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xuversion.R;
import com.example.xuversion.model.UserModel;
import com.example.xuversion.login_activity.anim.TimeCount;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterFragment extends Fragment {
    protected Context mContext;
    private Unbinder unbinder;
    private boolean hascheck;
    private ButterKnife.Setter<LinearLayout, Boolean> visionSetter;
    private RegFragmentListener regFragmentListener;



    @BindViews({R.id.register_email_layout, R.id.register_password_layout, R.id.register_password_check_layout,
            R.id.register_username_layout, R.id.register_checkcode_layout})
    List<LinearLayout> layouts;

    @BindView(R.id.register_input_username)
    EditText inputUsername;
    @BindView(R.id.register_input_email)
    EditText inputEmail;
    @BindView(R.id.register_input_password)
    EditText inputPassword;
    @BindView(R.id.register_input_password_check)
    EditText inputPasswordCheck;
    @BindView(R.id.register_input_checkcode)
    EditText inputCheckcode;
    @BindView(R.id.send_checkcode_btn)
    TextView sendCheckcodeBtn;

    /**
     * Bind onclick event
     * @param v the bound view
     */
    @OnClick({R.id.send_checkcode_btn})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_checkcode_btn:
                sendCheckCode();
                break;
            default:
                break;
        }
    }

    /**
     *  Use the factory method to return an instance of fragment
     * @return  a fragment
     */
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Log.e("Register", "new");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.e("Register", "OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ui_register_input_layout, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        hascheck = false;
        visionSetter = new ButterKnife.Setter<LinearLayout, Boolean>() {
            @Override
            public void set(@NonNull LinearLayout view, Boolean value, int index) {
                if (value) {
                    if(view != null)
                        view.setVisibility(View.VISIBLE);
                } else {
                    if(view != null)
                        view.setVisibility(View.INVISIBLE);
                }
            }
        };
        return rootView;
    }

    private void sendCheckCode(){
        String name = getEditTextString(inputUsername);
        String mailbox = getEditTextString(inputEmail);
        if(name.equals("") ||mailbox.equals("") ){
            showToast("The username and email cannot be empty");
        }
        else if(!isMailCorrect(mailbox)){
            showToast("Please enter SUSTC email in the correct format");
        }
        else{
            UserModel userModel = new UserModel();
            userModel.setName(name);
            userModel.setMailbox(mailbox);
            regFragmentListener.onSendClick(userModel);
            startTimer();
        }
    }

    private void startTimer(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TimeCount timeCount=new TimeCount(60 * 1000, 1000,sendCheckcodeBtn);
                timeCount.start();
            }
        });
    }

    /**
     * when the server send a check code successfully
     * it should use it to set hascheck to true and allow
     * user continue signing up
     */
    public void setHasCheckTrue(){
        this.hascheck = true;
    }

    /**
     * activity should use it to set a call back listener in this fragment
     * use for communication
     * @param listener the listener
     */
    public void setToActivityListener(RegFragmentListener listener){
        this.regFragmentListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * activity use it to let the layout can not been seen
     * use for anim
     */
    public void setLayoutGone() {
        ButterKnife.apply(layouts, visionSetter, false);
    }

    /**
     * activity use it to let the layout been seen again
     * use for recover from anim
     */
    public void setLayoutRecover() {
        ButterKnife.apply(layouts, visionSetter, true);
    }

    private String getEditTextString(EditText editText) {
        return editText.getText().toString();
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Check email address for legal status
     * @param name  mail address to check
     * @return
     */
    private boolean isMailCorrect(String name) {
        if (name.endsWith("@mail.sustc.edu.cn") || name.endsWith("@sustc.edu.cn")) {
            return true;
        }
        return false;
    }

    /**
     * activity call it for getting the register information
     */
    public void doRegister() {
        String name = getEditTextString(inputUsername);
        String password = getEditTextString(inputPassword);
        String recode = getEditTextString(inputPasswordCheck);
        String mailbox = getEditTextString(inputEmail);
        String checkcodetext = getEditTextString(inputCheckcode);
        boolean isok = true;
        if(name.equals("") || password.equals("")||mailbox.equals("")||recode.equals("")){
            showToast("User name, password, mailbox cannot be empty");
            isok = false;
        }
        else if(!isMailCorrect(mailbox)){
            showToast("Please enter SUSTC email in the correct format");
            isok = false;
        }
        else if(!password.equals(recode)){
            showToast("The password is not the same as the confirmation password");
            isok = false;
        }
        else if(!hascheck){
            showToast("Please send the verification code first");
            isok = false;
        }
        else if(checkcodetext.equals("")){
            showToast("The verification code cannot be empty");
            isok = false;
        }
        if(isok){
            UserModel userModel = new UserModel();
            userModel.setName(name);
            userModel.setMailbox(mailbox);
            userModel.setPassword(password);
            userModel.setCheckcode(checkcodetext);
            regFragmentListener.doRegister(userModel);
        }else{
            setLayoutRecover();
            regFragmentListener.recoverFromAnim();
        }
    }

}

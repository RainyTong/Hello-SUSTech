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
import android.widget.Toast;

import com.example.xuversion.R;
import com.example.xuversion.model.UserModel;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LoginFragment extends Fragment{
    private static final String NAME = "name";
    private static final String PASSWORD = "pass";
    protected Context mContext;
    private Unbinder unbinder;
    private LoginFragmentListener loginFragmentListener;
    private ButterKnife.Setter<LinearLayout,Boolean> visionSetter;

    @BindViews({ R.id.login_username_layout, R.id.login_password_layout })
    List<LinearLayout> layouts;

    @BindView(R.id.login_input_username)
    EditText usernameEditText;

    @BindView(R.id.login_input_password)
    EditText passwordEditText;

    /**
     *  Use the factory method to return an instance of fragment
     * @param name  the text which should in usernameEditText
     * @param password  the text which should in passwordEditText
     * @return  a fragment
     */
    public static LoginFragment newInstance(String name,String password) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(NAME, name);
        args.putString(PASSWORD,password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.e("LoginFragment","OnCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ui_login_input_layout, container, false);
        unbinder = ButterKnife.bind(this,rootView);
        if (getArguments() != null) {
            usernameEditText.setText(getArguments().getString(NAME));
            passwordEditText.setText(getArguments().getString(PASSWORD));
        }
        visionSetter = new ButterKnife.Setter<LinearLayout,Boolean>() {
            @Override
            public void set(@NonNull LinearLayout view, Boolean value, int index) {

                if(value){
                    if(view != null)
                        view.setVisibility(View.VISIBLE);
                }else{
                    if(view != null)
                        view.setVisibility(View.INVISIBLE);
                }
            }
        };
        return rootView;
    }

    /**
     * activity should use it to set a call back listener in this fragment
     * use for communication
     * @param listener the listener
     */
    public void setToActivityListener(LoginFragmentListener listener){
        this.loginFragmentListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        loginFragmentListener.recoverFromAnim();
    }

    /**
     * activity use it to let the layout can not been seen
     * use for anim
     */
    public void setLayoutGone(){
        ButterKnife.apply(layouts, visionSetter,false);
    }

    /**
     * activity use it to let the layout been seen again
     * use for recover from anim
     */
    public void setLayoutRecover(){
        ButterKnife.apply(layouts, visionSetter,true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Log.e("LoginFragment","Destroy");
    }

    private String getEditTextString(EditText editText){
        return editText.getText().toString();
    }

    private void showToast(String text){
        Toast toast =  Toast.makeText(mContext,text,Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * activity call it for getting the login information
     */
    public void doLogin(){
        String username = getEditTextString(usernameEditText);
        String password = getEditTextString(passwordEditText);

        if(username.equals("") || password.equals("")){
            showToast("The username and password cannot be empty");
            setLayoutRecover();
            loginFragmentListener.recoverFromAnim();
            return;
        }
        UserModel userModel = new UserModel();
        if(username.contains("@")){
            userModel.setMailbox(username);
        }else{
            userModel.setName(username);
        }
        userModel.setPassword(password);
        loginFragmentListener.doLogin(userModel);
    }

}

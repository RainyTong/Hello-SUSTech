package com.example.xuversion.login_activity;

import com.example.xuversion.model.UserModel;

public interface RegFragmentListener {
    //用户按发送验证码

    /**
     * on send click event
     * @param userModel the userModel object
     */
    void onSendClick(UserModel userModel);

    /**
     * register event
     * @param userModel the userModel object
     */
    void doRegister(UserModel userModel);

    /**
     * recover from anim
     */
    void recoverFromAnim();
}

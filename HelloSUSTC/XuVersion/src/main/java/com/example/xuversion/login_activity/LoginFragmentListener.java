package com.example.xuversion.login_activity;

import com.example.xuversion.model.UserModel;

public interface LoginFragmentListener {
    /**
     * Login a user
     * @param userModel userModel
     */
    void doLogin(UserModel userModel);

    /**
     * recover from anim
     */
    void recoverFromAnim();
}

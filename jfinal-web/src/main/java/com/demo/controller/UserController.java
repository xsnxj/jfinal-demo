package com.demo.controller;

import com.demo.interceptor.LoginInterceptor;
import com.jfinal.aop.Before;
import com.model.User;

import javax.servlet.http.HttpSession;

/**
 * Created by xxx on 2017/4/24.
 */
public class UserController extends BaseController {
    @Before(LoginInterceptor.class)
    public void index() {
        boolean isMobile = isMobile();
        HttpSession session = getSession();
        String phoneNumber = (String) session.getAttribute("phoneNumber");
        User userInfo = User.dao.getUserInfo(phoneNumber);
        setAttr("userInfo", userInfo);
        if (isMobile) {
            render("/mobile/user/userCenter.html");
        } else {
            render("/pc/user/userCenter.html");
        }
    }

    // user login
    public void login() {
        boolean isMobile = isMobile();
        if (isMobile) {
            render("/mobile/user/login.html");
        } else {
            render("/pc/user/login.html");
        }
    }

    // login check
    public void loginCheck() {
        User user = new User();
        String phoneNumber = getPara("phoneNumber");
        String password = getPara("password");
        setSessionAttr("phoneNumber", phoneNumber);
        setSessionAttr("password", password);
        boolean info = user.isExist(phoneNumber, password);
        if (info) {
            renderText("1");
        } else {
            renderText("0");
        }
    }

    // register
    public void register() {
        boolean isMobile = isMobile();
        if (isMobile) {
            render("/mobile/user/register.html");
        } else {
            render("/pc/user/register.html");
        }
    }

    // ajax register save
    public void registerSave() {
        String phoneNumber = getPara("phoneNumber");
        boolean isExistPhoneNumber = User.dao.isExistPhoneNumber(phoneNumber);
        String userName = getPara("userName");
        boolean isExistUserName = User.dao.isExistUserName(userName);
        if (!isExistPhoneNumber) {
            User info = getModel(User.class, "");
            String now = getNow();
            info.set("createTime", now);
            info.save();
            renderText("1");
        } else {
            renderText("0");
        }
    }

    // ajax update user info
    public void updateUser() {
        String userName = getPara("userName");
        String address = getPara("address");
        String phoneNumber = getPara("phoneNumber");
        String now = getNow();
        String avatarUrl = getPara("avatarUrl");
        String excute = User.dao.updateUser(phoneNumber, userName, avatarUrl, address);
        renderText(excute);
    }
}

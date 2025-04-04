package com.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.login.middleware.LoginMiddleWare;
import com.main_menu.middleware.MainMiddleWare;

public class MiddleWare {

    static List<Class<?>> MidList = new ArrayList<>( Arrays.asList(
        LoginMiddleWare.class,
        MainMiddleWare.class
    ));

}

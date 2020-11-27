package com.AnonymousHippo.Palette;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void test() {
        // 데이터 전송용 배열
        String[] keys = new String[1];
        String[] data = new String[1];

        keys[0] = "email";
        data[0] = "hoeunlee228@gmail.com";

        String result = HttpPostData.POST("account/sendCode/", keys, data);

        System.out.println(result);
    }
}
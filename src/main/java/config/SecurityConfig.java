package config;

import java.util.ArrayList;
import java.util.List;

import static config.Config.WEB_URL_BEGIN;

/**
 * @author 连仕杰
 */
public class SecurityConfig {
    private static List<String> uriList;

    /**
     * 放行的接口：
     * 登录
     * 注册
     * 上传文件
     * */
    static{
        uriList = new ArrayList<>();
        uriList.add(WEB_URL_BEGIN + "/index.html");
        uriList.add(WEB_URL_BEGIN + "/up.html");
        uriList.add(WEB_URL_BEGIN + "/SignUp");
        uriList.add(WEB_URL_BEGIN + "/SignIn");
    }

    public static List<String> getUriList() {
        return uriList;
    }
}

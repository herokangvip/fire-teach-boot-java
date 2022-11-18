package com.example.demo.king.hutool;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import com.example.demo.domain.User;

import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) throws Exception{
        //编码问题：https://zhuanlan.zhihu.com/p/344477237
        String s2 = SecureUtil.md5("Dfsd");
        //key至少16位，或其倍数
        AES aes = SecureUtil.aes("qwertyuiopasdfgh".getBytes(StandardCharsets.UTF_8));
        //Hex编码其实就是Base16编码，速度快占用空间大是源字符的两倍
        String s1 = aes.encryptHex("re",StandardCharsets.UTF_8);
        //Base64,速度稍慢，空间只膨胀1.3倍,会填充一些特殊符号，如果放到url的话需要做一次url编码空间上也并不省
        String s3 = aes.encryptBase64("re", StandardCharsets.UTF_8);
        String encode = URLEncoder.encode(s3, "utf-8");
        System.out.println(s1);//9935fa45373afd1cbc5e169695af9572
        System.out.println(s3);//mTX6RTc6/Ry8XhaWla+Vcg==
        System.out.println(encode);//mTX6RTc6%2FRy8XhaWla%2BVcg%3D%3D

        HashMap<String, String> map = new HashMap<>();
        map.put("a","1");
        map.put("b","2");
        String s = SecureUtil.signParamsMd5(map, "test");

        //Map<String, Object> map1 = BeanUtil.beanToMap(user, false, false); // 不转下划线，不忽略空值
        //Map<String, Object> map2 = BeanUtil.beanToMap(user, true, true); // 转下划线，忽略空值,userName属性key会变成user_name
        //SecureUtil.signParamsMd5等价于&和=都是空，一般会设置分割符&=要不区分不开
        String s5 = MapUtil.sortJoin(map, "&", "=", true, "test");
        System.out.println(s5);//a=1&b=2test
        String s4 = (new Digester(DigestAlgorithm.MD5)).digestHex(s5);
        System.out.println(s+"："+s4);
    }

}

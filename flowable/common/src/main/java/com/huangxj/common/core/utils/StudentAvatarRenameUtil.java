package com.huangxj.common.core.utils;

import cn.hutool.core.img.ImgUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.File;

/**
 * 〈〉
 *
 * @author huangxj
 * @create 2020-09-14
 * @since 1.0.0
 */
public class StudentAvatarRenameUtil {

    public static void main(String[] args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        final String uri = "http://ischool.yunct.net/basic/student/uploadAvatar";
        final String path = "E:\\曲江中学\\高二1班电子班牌相册";
        final String newPath = "E:\\曲江中学\\2019级\\2019级高中1班";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.set("Authorization", "Bearer 661b9208-cfe2-42b8-a8a9-4dbb1d6fee25");

        File file = new File(path);
        File[] tempList = file.listFiles();
        for (File img : tempList) {
            JSONObject json = new JSONObject();
            json.put("gradeName", "2019级高中");
            json.put("className", "1班");
            json.put("studentName", img.getName());
            HttpEntity<String> formEntity = new HttpEntity<String>(json.toString(), headers);
            JSONObject ret = restTemplate.postForEntity(uri, formEntity, JSONObject.class).getBody();
            if ("false".equals(ret.getString("success"))) {
                throw new Exception(ret.toJSONString());
            }
            if (null != ret.getJSONObject("data")) {
                JSONObject student = ret.getJSONObject("data");
                File newImg = new File(newPath + "\\" + student.getString("studentCode") + " " + student.getString("studentName") + ".jpg");
                File fileParent = newImg.getParentFile();
                if (!fileParent.exists()) {
                    fileParent.mkdirs();
                }
                ImgUtil.scale(img, newImg, 0.3f);
//                FileUtils.copyFile(img,newImg);
            }
        }


    }
}

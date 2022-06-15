package com.hahacs.contro;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hahacs.dto.RabbitMqDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 转化mq的字符串成报案号
 */
public class GeneralMqRegistNo {

    //    private final static String CHARSETNAME = "gb2312";
    private final static String CHARSETNAME = "utf-8";

    public static void main(String[] args) {
        String path = "F:\\claim_delegate.txt";

        String json = readTxt(path);
//        System.out.println(json);

        List<RabbitMqDto> rabbitMqDtos = JSON.parseArray(json, RabbitMqDto.class);
        int count = 0;
        for (RabbitMqDto rabbitMqDto : rabbitMqDtos) {
            count = count + 1;
            String payLoad = rabbitMqDto.getPayload();
            if ("application/json".equals(rabbitMqDto.getProperties().getContent_type())) {
                payLoad = payLoad.replaceAll("\\\\", "");
                payLoad = payLoad.substring(1, payLoad.length() - 1);
            }
//            System.out.println(payLoad);
            JSONObject jsonObject = JSON.parseObject(payLoad);
            String registNo = jsonObject.getJSONObject("body").getString("registNo");
            if (registNo == null) {
                registNo = jsonObject.getJSONObject("body").getString("REGISTNO");
            }
//            String registNo = jsonObject.getJSONObject("body").getString("REGISTNO");
            System.out.println(registNo);
        }

        System.out.println(count);
    }

    private static String readTxt(String path) {
        if (path == null || "".equals(path)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        File file = new File(path);
        InputStreamReader read = null;
        BufferedReader reader = null;
        try {
            read = new InputStreamReader(new FileInputStream(file), CHARSETNAME);
            reader = new BufferedReader(read);
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

}

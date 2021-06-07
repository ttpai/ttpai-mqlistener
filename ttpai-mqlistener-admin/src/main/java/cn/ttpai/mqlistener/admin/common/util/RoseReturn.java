package cn.ttpai.mqlistener.admin.common.util;

import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @Description Rose 返回
 * @author: kail
 * Copyright(c)2015/9/1 13:20 ttpai All Rights Reserved.
 */
public class RoseReturn {


    /**
     * 返回视图
     *
     * @param str 视图路径
     * @return
     */
    public static String view(String str) {
        return str;
    }

    /**
     * 【rose 内置】返回字符串
     *
     * @param str 需要返回的字符串
     * @return
     */
    public static String at(String str) {
        return "@" + str;
    }

    /**
     * 【rose 内置】支持跨域JSONP返回字符串
     *
     * @param str      需要返回的字符串
     * @param callback jsonp传来的callback名称
     * @return
     */
    public static String at(String str, String callback) {
        return "@" + callback + "(" + str + ")";
    }


    /**
     * 【rose 内置】返回Json
     *
     * @param str 需要返回的json字符串
     * @return
     */
    public static String json(String str) {
        return "@json:" + str;
    }


    /**
     * 【rose 内置】支持跨域JSONP返回Json
     *
     * @param str      需要返回的json字符串
     * @param callback jsonp传来的callback名称
     * @return
     */
    public static String json(String str, String callback) {
        return "@json:" + callback + "(" + str + ")";
    }


    public static String jsonAuto(Object obj) {
        return "@json:" + JsonUtil.toJsonString(obj);
    }

    public static String jsonAuto(Object obj, String callback) {
        return "@json:" + callback + "(" + JsonUtil.toJsonString(obj) + ")";
    }

    public static String jsonAuto(Object obj, SerializerFeature... features) {
        return "@json:" + JsonUtil.toJsonString(obj, features);
    }


    public static String jsonAuto(Object obj, String callback, SerializerFeature... features) {
        return "@json:" + callback + "(" + JsonUtil.toJsonString(obj, features) + ")";
    }


    /**
     * 【rose 内置】 重定向 redirect
     *
     * @param str 重定向的连接
     * @return
     */
    public static String redirect(String str) {
        return "r:" + str;
    }

    /**
     * 【rose 内置】 转发 forward
     *
     * @param str 转发的连接
     * @return
     */
    public static String forward(String str) {
        return "a:" + str;
    }

    /**
     * 【rose 扩充】返回字符串
     *
     * @param message 需要返回的提示信息
     * @param message 需要返回的错误代码
     * @return
     */
    public static String error(String message, String code) {
        return "@error:" + ResultJson.getInstall().setCode(code).setMessage(message).toString();
    }


    /**
     * 【rose 扩充返回支持jsonp字符串
     *
     * @param message  需要返回的提示信息
     * @param code     需要返回的错误代码
     * @param callback jsonp传来的callback名称
     * @return
     */
    public static String error(String message, String code, String callback) {
        return "@error:" + callback + "(" + ResultJson.getInstall().setCode(code).setMessage(message).toString() + ")";
    }

    /**
     * 返回500
     */
    public static String error(String message) {
        ResultJson j = new ResultJson();
        j.setMessage(message);
        j.setCode("500");
        return "@error:" + j.toString();
    }


    /**
     *返回500支持jsonp
     * @param message 返回的信息
     * @param callback jsonp传来的callback名称
     * @return
     */
    public static String errorJsonp(String message, String callback) {
        ResultJson j = new ResultJson();
        j.setMessage(message);
        j.setCode("500");
        return "@error:" + callback + "(" + j.toString() + ")";
    }

    /**
     * 返回空的json对象
     *
     * @return
     */
    public static String none() {
        return "@json:{}";
    }


    /**
     * 返回支持jsonp空的json对象
     * @param callback jsonp传来的callback名称
     * @return
     */
    public static String none(String callback) {
        return "@json:" + callback + "(" + "{}" + ")";
    }


    /**
     * 成功的信息
     */
    public static String ok() {
        return none();
    }


    /**
     *返回带信息的200
     * @param message 返回的信息
     * @return
     */
    public static String success(String message) {
        ResultJson j = new ResultJson();
        j.setMessage(message);
        return "@json:" + j.toString();
    }

    /**
     *返回支持jsonp带信息的200
     * @param message 返回的信息
     * @param callback jsonp传来的callback名称
     * @return
     */
    public static String success(String message, String callback) {
        ResultJson j = new ResultJson();
        j.setMessage(message);
        return "@json:" + callback + "(" + j.toString() + ")";
    }


    /**
     * 返回空的200
     */
    public static String success() {
        ResultJson j = new ResultJson();
        return "@json:" + j.toString();
    }

    /**
     * 返回支持jsonp空的200
     * @param callback jsonp传来的callback名称
     * @return
     */
    public static String successJsonp(String callback) {
        ResultJson j = new ResultJson();
        return "@json:" + callback + "(" + j.toString() + ")";
    }


}

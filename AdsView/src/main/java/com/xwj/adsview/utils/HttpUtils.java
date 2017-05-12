package com.xwj.adsview.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;


public class HttpUtils {

	/**
	 * Single instance
	 */
	private static AsyncHttpClient client = new AsyncHttpClient();

	private static SyncHttpClient syncHttpClient = new SyncHttpClient();

	public static AsyncHttpClient getClient() {
		return client;
	}

	public static SyncHttpClient getSyncHttpClient() {
		return syncHttpClient;
	}

	/**
	 * while you create StringEntity yourself, please do : entity.setContentType(basicHeader)
	 * but you need not do this by use static method createEntity
	 */
	public static BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8");

	private static PersistentCookieStore persistentCookieStore;
	public static void addCookie(Context context, String value) {
		if (persistentCookieStore == null) persistentCookieStore = new PersistentCookieStore(context);
		BasicClientCookie cookie = new BasicClientCookie("Set-Cookie", value);
		persistentCookieStore.addCookie(cookie);
	}

	public static List<Cookie> getCookie() {
		List<Cookie> cookieList = persistentCookieStore.getCookies();
		return cookieList;
	}

	public static void clearCookies(Context context) {
		if (persistentCookieStore == null) persistentCookieStore = new PersistentCookieStore(context);
		persistentCookieStore.clear();
	}

	public static void addHeaders(Context context) {
		client.addHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
		client.addHeader("Accept", ContentType.APPLICATION_JSON.toString());
		if (persistentCookieStore == null) persistentCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(persistentCookieStore);
	}

	public static void addFormHeaders(Context context) {
		client.addHeader(HTTP.CONTENT_TYPE, ContentType.create(
				"application/x-www-form-urlencoded", Consts.UTF_8).toString());
		if (persistentCookieStore == null) persistentCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(persistentCookieStore);
	}

	public static void addSyncFormHeaders(Context context) {
		syncHttpClient.addHeader(HTTP.CONTENT_TYPE, ContentType.create(
				"application/x-www-form-urlencoded", Consts.UTF_8).toString());
		if (persistentCookieStore == null) persistentCookieStore = new PersistentCookieStore(context);
		syncHttpClient.setCookieStore(persistentCookieStore);
	}

	public static void addSyncJsonHeaders(Context context) {
		syncHttpClient.addHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
		syncHttpClient.addHeader("Accept", ContentType.APPLICATION_JSON.toString());
		if (persistentCookieStore == null) persistentCookieStore = new PersistentCookieStore(context);
		syncHttpClient.setCookieStore(persistentCookieStore);
	}

	/**
	 * create StringEntity with ContentType: application/json; charset=UTF-8
	 * @param string entity string
	 * @return StringEntity
	 */
	public static StringEntity createEntity(String string) {
		return new StringEntity(string, ContentType.APPLICATION_JSON);
	}

	/**
	 * create StringEntity with ContentType: application/json; charset=UTF-8
	 * @param jsonObject entity jsonObject
	 * @return StringEntity
	 */
	public static StringEntity createEntity(JSONObject jsonObject) {
		return new StringEntity(jsonObject.toJSONString(), ContentType.APPLICATION_JSON);
	}

	/**
	 * create StringEntity with ContentType: application/json; charset=UTF-8
	 * @param map entity map
	 * @return StringEntity
	 */
	public static StringEntity createEntity(Map map) {
		return new StringEntity(map.toString(), ContentType.APPLICATION_JSON);
	}

	public static HttpEntity createByteArrayEntity(byte[] binaryBody) {
		return new ByteArrayEntity(binaryBody);
	}

	/**
	 * GET 方法 用一个完整url获取一个string对象
	 * @param url 请求url
	 * @param res AsyncHttpResponseHandler
	 */
	public static void get(Context context, String url, AsyncHttpResponseHandler res) {
		addHeaders(context);
		client.get(url, res);
	}

	/**
	 * GET 方法 用一个带参数的url获取一个string对象
	 * @param url 请求的url
	 * @param params 请求参数
	 * @param res AsyncHttpResponseHandler
	 */
	public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler res) {
		addHeaders(context);
		client.get(url, params, res);
	}

	/**
	 * GET 方法 用一个带参数的url获取一个string对象
	 * @param url 请求的url
	 * @param entity StringEntity body字符串
	 * @param res AsyncHttpResponseHandler
	 */
	public static void get(Context context, String url, StringEntity entity, AsyncHttpResponseHandler res) {
		addHeaders(context);
		client.get(context, url, entity, "application/json", res);
	}

	/**
	 * GET 方法 不带参数，获取json对象或者数组
	 * @param url 请求的url
	 * @param res JsonHttpResponseHandler
	 */
	public static void get(Context context, String url, JsonHttpResponseHandler res) {
		addHeaders(context);
		client.get(url, res);
	}

	/**
	 * GET 方法 不带参数，获取json对象或者数组
	 * @param url 请求的url
	 * @param params 请求参数
	 * @param res JsonHttpResponseHandler
	 */
	public static void get(Context context, String url, RequestParams params, JsonHttpResponseHandler res) {
		addHeaders(context);
		client.get(url, params, res);
	}

	/**
	 * GET 方法 用一个带参数的url获取一个string对象
	 * @param url 请求的url
	 * @param entity StringEntity body字符串
	 * @param res JsonHttpResponseHandler
	 */
	public static void get(Context context, String url, StringEntity entity, JsonHttpResponseHandler res) {
		addHeaders(context);
		client.get(context, url, entity, "application/json", res);
	}

	/**
	 * GET 方法 不带参数 下载数据使用，会返回byte数据
	 * @param url 请求的url
	 * @param res BinaryHttpResponseHandler
	 */
	public static void get(Context context, String url, BinaryHttpResponseHandler res) {
		addHeaders(context);
		client.get(url, res);
	}

	public static void getSync(Context context, String url, AsyncHttpResponseHandler res) {
		addSyncJsonHeaders(context);
		syncHttpClient.get(url, res);
	}


	public static void post(Context context, String url, HttpEntity httpEntity, AsyncHttpResponseHandler res) {
		addHeaders(context);
		client.post(context, url, httpEntity, null, res);
	}


	/**
	 * POST 方法 用一个完整url获取一个string对象
	 * @param url 请求url
	 * @param res AsyncHttpResponseHandler
	 */
	public static void post(Context context, String url, AsyncHttpResponseHandler res) {
		addHeaders(context);
		client.post(url, res);
	}

	/**
	 * POST 方法 用一个带参数的url获取一个string对象
	 * @param url 请求的url
	 * @param params 请求参数
	 * @param res AsyncHttpResponseHandler
	 */
	public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler res) {
		addFormHeaders(context);
		client.post(url, params, res);
	}

	public static void postSync(Context context, String url, RequestParams params, AsyncHttpResponseHandler res) {
		addSyncFormHeaders(context);
		syncHttpClient.post(url, params, res);
	}

	public static void postSync(Context context, String url, HttpEntity entity, AsyncHttpResponseHandler res) {
		syncHttpClient.addHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_OCTET_STREAM.toString());
		if (persistentCookieStore == null) persistentCookieStore = new PersistentCookieStore(context);
		syncHttpClient.setCookieStore(persistentCookieStore);
		syncHttpClient.post(context, url, entity, null, res);
	}

	/**
	 * POST 方法 用一个带参数的url获取一个string对象
	 * @param url 请求的url
	 * @param entity StringEntity body字符串
	 * @param res AsyncHttpResponseHandler
	 */
	public static void post(Context context, String url, StringEntity entity, AsyncHttpResponseHandler res) {
		addHeaders(context);
		client.post(context, url, entity, "application/json", res);
	}

	/**
	 * POST 方法 不带参数，获取json对象或者数组
	 * @param url 请求的url
	 * @param res JsonHttpResponseHandler
	 */
	public static void post(Context context, String url, JsonHttpResponseHandler res) {
		addHeaders(context);
		client.post(url, res);
	}

	/**
	 * POST 方法 不带参数，获取json对象或者数组
	 * @param url 请求的url
	 * @param params 请求参数
	 * @param res JsonHttpResponseHandler
	 */
	public static void post(Context context, String url, RequestParams params, JsonHttpResponseHandler res) {
		addHeaders(context);
		client.post(url, params, res);
	}

	/**
	 * POST 方法 用一个带参数的url获取一个string对象
	 * @param url 请求的url
	 * @param entity StringEntity body字符串
	 * @param res JsonHttpResponseHandler
	 */
	public static void post(Context context, String url, StringEntity entity, JsonHttpResponseHandler res) {
		addHeaders(context);
		client.post(context, url, entity, "application/json", res);
	}

	/**
	 * POST 方法 不带参数 下载数据使用，会返回byte数据
	 * @param url 请求的url
	 * @param res BinaryHttpResponseHandler
	 */
	public static void post(Context context, String url, BinaryHttpResponseHandler res) {
		addHeaders(context);
		client.post(url, res);
	}

	/**
	 * PUT 方法 用一个完整url获取一个string对象
	 * @param url 请求url
	 * @param res AsyncHttpResponseHandler
	 */
	public static void put(String url, AsyncHttpResponseHandler res) {
		client.put(url, res);
	}


}
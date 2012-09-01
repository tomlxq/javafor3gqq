package com.mc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 网络连接底层类，执行get和post请求
 * @author Shine_MuShi
 *
 */
public abstract class WebUtils {

	private static final String DEFAULT_CHARSET ="utf-8";
	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";
	private static final String USER_AGENT="Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.29 Safari/525.13";
	private static final String CTYPE = "application/x-www-form-urlencoded;charset=" + DEFAULT_CHARSET;
	
	public WebUtils() {
		
	}
	
	/**
	 * @parmaַ
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String doPost(String url, Map<String, String> params,int connectTimeout,int readTimeout) throws IOException {
		String query=buildQuery(params, DEFAULT_CHARSET);
		byte[] content={};
		if(query!=null){
			content=query.getBytes(DEFAULT_CHARSET);
		}
		return doPost(url, CTYPE, content, connectTimeout, readTimeout);
	}

	public static String doPost(String url, String ctype, byte[] content,int connectTimeout,int readTimeout) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			try{
				conn = getConnection(new URL(url), METHOD_POST, ctype);	
				conn.setConnectTimeout(connectTimeout);
				conn.setReadTimeout(readTimeout);
			}catch(IOException e){
				throw e;
			}
			try{
				out = conn.getOutputStream();
				out.write(content);
				rsp = getResponseAsString(conn);
			}catch(IOException e){
				throw e;
			}
			
		}finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rsp;
	}
	
	public static String doGet(String url) throws IOException {
		return doGet(url, DEFAULT_CHARSET);
	}

	public static String doGet(String url, String charset)
			throws IOException {
		HttpURLConnection conn = null;
		String rsp = null;
		try {
			try{
				conn = getConnection(new URL(url), METHOD_GET, CTYPE);
			}catch(IOException e){
				throw e;
			}
			try{
				rsp = getResponseAsString(conn);
			}catch(IOException e){
				throw e;
			}
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rsp;
	}
	
	private static HttpURLConnection getConnection(URL url, String action, String ctype)
			throws IOException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(action);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Content-Type", ctype);
		return conn;
	}
	
	private static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if (es == null) {
			return getStreamAsString(conn.getInputStream(), charset);
		}
		return null;
	}
	
	/**
	 * ����Ӧ��ת��Ϊ�ַ�
	 * @param stream
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
			StringWriter writer = new StringWriter();
			char[] chars = new char[256];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}
			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;
		if (ctype!=null) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (pair[1]!=null) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}
		return charset;
	}
	
	public static String buildQuery(Map<String, String> params, String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;
		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// ���Բ���������ֵΪ�յĲ���
			if (name!=null&&value!=null) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				query.append(name).append("=").append(URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
	}


}

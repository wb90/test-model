package org.dubbo.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StrKit;

public class HttpUtils {

	private HttpUtils() {
	}

	/**
	 * https 域名校验
	 */
	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	/**
	 * https 证书管理
	 */
	private class TrustAnyTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	}

	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final String CHARSET = "UTF-8";
	// private static final String byte_stream = "image/jpeg,";

	private static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
	private static final TrustAnyHostnameVerifier trustAnyHostnameVerifier = new HttpUtils().new TrustAnyHostnameVerifier();

	private static SSLSocketFactory initSSLSocketFactory() {
		try {
			TrustManager[] tm = { new HttpUtils().new TrustAnyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers)
			throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		URL _url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
			((HttpsURLConnection) conn).setHostnameVerifier(trustAnyHostnameVerifier);
		}

		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		conn.setDoInput(true);

		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);

		// conn.setRequestProperty("Content-Type",
		// "application/x-www-form-urlencoded");
		conn.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (compatible; MSIE 8.0;Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

		if (headers != null && !headers.isEmpty())
			for (Entry<String, String> entry : headers.entrySet())
				conn.setRequestProperty(entry.getKey(), entry.getValue());
		return conn;
	}

	/**
	 * 发送 get 请求
	 */
	public static HttpReturn get(String url, Map<String, String> queryParas, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), GET, headers);
			System.out.println(conn.getURL());
			conn.connect();
			return readResponseString(conn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	public static HttpReturn get(String url, Map<String, String> queryParas) {
		return get(url, queryParas, null);
	}

	public static HttpReturn get(String url) {
		return get(url, null, null);
	}

	/**
	 * 发送 POST 请求 考虑添加一个参数 Map<String, String> queryParas：
	 * getHttpConnection(buildUrl(url, queryParas), POST, headers);
	 */
	public static HttpReturn post(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), POST, headers);
			conn.connect();

			OutputStream out = conn.getOutputStream();
			// headers.put("Connection", "Keep-Alive");
			// String BOUNDARY = "----------" + System.currentTimeMillis();
			// headers.put("Content-Type", "multipart/form-data; boundary=" +
			// BOUNDARY);
			// StringBuilder sb = new StringBuilder();
			// sb.append("--"); // 必须多两道线
			// sb.append(BOUNDARY);
			// sb.append("\r\n");
			// sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
			// + BOUNDARY + "\"\r\n");
			// sb.append("Content-Type:application/octet-stream\r\n\r\n");
			// sb.append(data);
			// sb.append("\r\n--" + BOUNDARY + "--\r\n");
			out.write(data.getBytes(CHARSET));
			out.flush();
			out.close();

			return readResponseString(conn);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	public static HttpReturn post(String url, Map<String, String> queryParas, String data) {
		return post(url, queryParas, data, null);
	}

	public static HttpReturn post(String url, String data, Map<String, String> headers) {
		return post(url, null, data, headers);
	}

	public static HttpReturn post(String url, String data) {
		return post(url, null, data, null);
	}

	private static HttpReturn binary(String url, String data, String fileName) {
		Map<String, String> headers = new HashMap<String, String>();

		return post(url, null, data, headers);
	}

	public static HttpReturn binary(String url, File file) {

		try {
			FileInputStream in = new FileInputStream(file);
			int byteLength = 0;
			int lineLength = 0;
			byte[] buffer = new byte[1024];
			while ((lineLength = in.read(buffer)) != -1) {
				byteLength = byteLength + lineLength;

			}
			buffer = new byte[byteLength];
			in.read(buffer);
			// System.out.println(ii);
			in.close();
			return binary(url, new String(buffer), file.getName());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static HttpReturn binary(String url, URL uri) {
		HttpReturn hr = get(uri.toString());
		return binary(url, hr.getContent(), System.currentTimeMillis() + "");

	}

	public static HttpReturn binary(String url, String filePath) {
		return binary(url, new File(filePath));

	}

	private static HttpReturn readResponseString(HttpURLConnection conn) {
		// StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		try {
			Charset charset = null;
			String contentType = conn.getContentType();
			inputStream = conn.getInputStream();

			if (contentType != null) {
				String contentTypes[] = contentType.split("=");
				if (contentTypes.length == 2) {
					charset = Charset.forName(contentTypes[1]);
				}
			}

			// inputStream = conn.getInputStream();
			// inputStream.
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			// 创建一个Buffer字符串
			byte[] buffer = new byte[1024];
			// 每次读取的字符串长度，如果为-1，代表全部读取完毕
			// int ii = 0;
			int len = 0;
			// // 使用一个输入流从buffer里把数据读取出来
			while ((len = inputStream.read(buffer)) != -1) {

				// ii = ii + len;
				outStream.write(buffer, 0, len);
			}

			if (charset == null) {
				charset = getFile(outStream.toByteArray());
			}
			String content = new String(outStream.toByteArray(), "gbk");
			outStream.close();
			String cookieVals = "";
			String key = "";
			for (int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++) {
				if (key.equalsIgnoreCase("set-cookie")) {
					String cookieVal = conn.getHeaderField(i);
					cookieVals = cookieVals + ";" + cookieVal.substring(0, cookieVal.indexOf(";"));

				}
			}
			HttpReturn httpRetrun = new HttpReturn();
			if (StrKit.notBlank(cookieVals)) {
				httpRetrun.setCookie(cookieVals.substring(1));
			}

			httpRetrun.setContent(content);
			if (conn.getResponseCode() == 302)
				;
			{
				String url = conn.getHeaderField("Location");
				if (StrKit.isBlank(url)) {
					url = conn.getURL().toString();
				}
				httpRetrun.setUrl(url);
			}

			return httpRetrun;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 在 url 之后构造 queryString
	 */
	private static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
		if (queryParas == null || queryParas.isEmpty())
			return url;

		StringBuilder sb = new StringBuilder(url);
		boolean isFirst;
		if (url.indexOf("?") == -1) {
			isFirst = true;
			sb.append("?");
		} else {
			isFirst = false;
		}

		for (Entry<String, String> entry : queryParas.entrySet()) {
			if (isFirst)
				isFirst = false;
			else
				sb.append("&");

			String key = entry.getKey();
			String value = entry.getValue();
			if (StrKit.notBlank(value))
				try {
					value = URLEncoder.encode(value, CHARSET);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			sb.append(key).append("=").append(value);
		}
		return sb.toString();
	}

	public static String readIncommingRequestData(HttpServletRequest request) {
		BufferedReader br = null;
		try {
			StringBuilder result = new StringBuilder();
			br = request.getReader();
			for (String line = null; (line = br.readLine()) != null;) {
				result.append(line).append("\n");
			}

			return result.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static String readIncommingData(String content) {
		BufferedReader br = null;
		try {
			StringBuilder result = new StringBuilder();
			br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes())));
			for (String line = null; (line = br.readLine()) != null;) {
				if (StrKit.notBlank(line))
					result.append(line).append("\n");
			}

			return result.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static void main(String[] args) throws Exception {
		// Map<String, String> loginMap = new HashMap<String, String>();
		// loginMap.put("userName", "liyunhui1978");
		// loginMap.put("password", "13466703675");
		// post("http://www.guangjiafy.com/Home/LogOn", loginMap,
		// "test content", null);
		//
		System.out.println(get("http://p2.sinaimg.cn/1977639873/180/60731325726113").getContent().length());
		System.out.println(get("http://jxc.cncre.com.cn/1.txt").getContent().length());

		// get(url)
	}

	public static Charset getFile(byte[] bs) throws Exception {

		BigInteger p = new BigInteger(1, Arrays.copyOf(bs, 2));
		// System.out.println(Integer.);
		String code = null;

		switch (p.intValue()) {
		//
		// case 0xefbb:
		// code = "UTF-8";
		// break;
		// case 0xfffe:
		// code = "Unicode";
		// break;
		// case 0xfeff:
		// code = "UTF-16BE";
		// break;
		// case 0x5c75:
		// code = "ANSI|ASCII";
		// break;
		case 0x4749:
			code = "ISO-8859-1";
			break;
		case 0x8950:
			code = "ISO-8859-1";
			break;
		case 0xffd8:
			code = "ISO-8859-1";
			break;
		default:
			code = CHARSET;
		}
		// System.out.println(code);
		return Charset.forName(code);
	}

}

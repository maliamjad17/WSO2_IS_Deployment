/*     */ package org.wso2.carbon.identity.application.authentication.endpoint.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URL;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.net.ssl.HttpsURLConnection;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MutualSSLClient
/*     */ {
/*  34 */   private static final Log log = LogFactory.getLog(MutualSSLClient.class);
/*     */   private static KeyStore keyStore;
/*     */   private static KeyStore trustStore;
/*     */   private static String keyStorePassword;
/*  38 */   private static String KEY_STORE_TYPE = "JKS";
/*  39 */   private static String TRUST_STORE_TYPE = "JKS";
/*  40 */   private static String KEY_MANAGER_TYPE = "SunX509";
/*  41 */   private static String TRUST_MANAGER_TYPE = "SunX509";
/*  42 */   private static String PROTOCOL = "SSLv3";
/*     */   
/*     */ 
/*     */ 
/*     */   private static HttpsURLConnection httpsURLConnection;
/*     */   
/*     */ 
/*     */ 
/*     */   private static SSLSocketFactory sslSocketFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void loadKeyStore(String keyStorePath, String keyStorePassoword)
/*     */     throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException
/*     */   {
/*  58 */     keyStorePassword = keyStorePassoword;
/*  59 */     keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
/*  60 */     keyStore.load(new FileInputStream(keyStorePath), keyStorePassoword.toCharArray());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void loadTrustStore(String trustStorePath, String trustStorePassoword)
/*     */     throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException
/*     */   {
/*  76 */     trustStore = KeyStore.getInstance(TRUST_STORE_TYPE);
/*  77 */     trustStore.load(new FileInputStream(trustStorePath), trustStorePassoword.toCharArray());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void initMutualSSLConnection()
/*     */     throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, UnrecoverableKeyException
/*     */   {
/*  94 */     KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KEY_MANAGER_TYPE);
/*  95 */     keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
/*  96 */     TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TRUST_MANAGER_TYPE);
/*  97 */     trustManagerFactory.init(trustStore);
/*  98 */     SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
/*  99 */     sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
/* 100 */     sslSocketFactory = sslContext.getSocketFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String sendPostRequest(String backendURL, String message, Map<String, String> requestProps)
/*     */     throws IOException
/*     */   {
/* 114 */     URL url = new URL(backendURL);
/* 115 */     httpsURLConnection = (HttpsURLConnection)url.openConnection();
/* 116 */     httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
/* 117 */     httpsURLConnection.setDoOutput(true);
/* 118 */     httpsURLConnection.setDoInput(true);
/* 119 */     httpsURLConnection.setRequestMethod("POST");
/* 120 */     if ((requestProps != null) && (requestProps.size() > 0)) {
/* 121 */       for (Map.Entry<String, String> entry : requestProps.entrySet()) {
/* 122 */         httpsURLConnection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
/*     */       }
/*     */     }
/* 125 */     OutputStream outputStream = null;
/* 126 */     InputStream inputStream = null;
/* 127 */     BufferedReader reader = null;
/* 128 */     StringBuilder response = null;
/*     */     try {
/* 130 */       outputStream = httpsURLConnection.getOutputStream();
/* 131 */       outputStream.write(message.getBytes());
/* 132 */       inputStream = httpsURLConnection.getInputStream();
/* 133 */       reader = new BufferedReader(new InputStreamReader(inputStream));
/* 134 */       response = new StringBuilder();
/*     */       String line;
/* 136 */       while ((line = reader.readLine()) != null) {
/* 137 */         response.append(line);
/*     */       }
/*     */     } catch (IOException e) {
/* 140 */       log.error("Calling url : " + url + "failed. ", e);
/*     */     } finally {
/* 142 */       reader.close();
/* 143 */       inputStream.close();
/* 144 */       outputStream.close();
/*     */     }
/* 146 */     return response.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String sendGetRequest(String backendURL, String message, Map<String, String> requestProps)
/*     */     throws IOException
/*     */   {
/* 159 */     URL url = new URL(backendURL);
/* 160 */     httpsURLConnection = (HttpsURLConnection)url.openConnection();
/* 161 */     httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
/* 162 */     httpsURLConnection.setDoOutput(true);
/* 163 */     httpsURLConnection.setDoInput(true);
/* 164 */     httpsURLConnection.setRequestMethod("GET");
/* 165 */     if ((requestProps != null) && (requestProps.size() > 0)) {
/* 166 */       for (Map.Entry<String, String> entry : requestProps.entrySet()) {
/* 167 */         httpsURLConnection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
/*     */       }
/*     */     }
/* 170 */     OutputStream outputStream = null;
/* 171 */     InputStream inputStream = null;
/* 172 */     BufferedReader reader = null;
/* 173 */     StringBuilder response = null;
/*     */     try {
/* 175 */       outputStream = httpsURLConnection.getOutputStream();
/*     */       
/* 177 */       inputStream = httpsURLConnection.getInputStream();
/* 178 */       reader = new BufferedReader(new InputStreamReader(inputStream));
/* 179 */       response = new StringBuilder();
/*     */       String line;
/* 181 */       while ((line = reader.readLine()) != null) {
/* 182 */         response.append(line);
/*     */       }
/*     */     } catch (IOException e) {
/* 185 */       log.error("Calling url : " + url + "failed. ", e);
/*     */     } finally {
/* 187 */       reader.close();
/* 188 */       inputStream.close();
/* 189 */       outputStream.close();
/*     */     }
/* 191 */     return response.toString();
/*     */   }
/*     */   
/*     */   public static String getKeyStoreType() {
/* 195 */     return KEY_STORE_TYPE;
/*     */   }
/*     */   
/*     */   public static void setKeyStoreType(String KEY_STORE_TYPE) {
/* 199 */     KEY_STORE_TYPE = KEY_STORE_TYPE;
/*     */   }
/*     */   
/*     */   public static String getTrustStoreType() {
/* 203 */     return TRUST_STORE_TYPE;
/*     */   }
/*     */   
/*     */   public static void setTrustStoreType(String TRUST_STORE_TYPE) {
/* 207 */     TRUST_STORE_TYPE = TRUST_STORE_TYPE;
/*     */   }
/*     */   
/*     */   public static String getKeyManagerType() {
/* 211 */     return KEY_MANAGER_TYPE;
/*     */   }
/*     */   
/*     */   public static void settKeyManagerType(String KEY_MANAGER_TYPE) {
/* 215 */     KEY_MANAGER_TYPE = KEY_MANAGER_TYPE;
/*     */   }
/*     */   
/*     */   public static String getTrustManagerType() {
/* 219 */     return TRUST_MANAGER_TYPE;
/*     */   }
/*     */   
/*     */   public static void getTrustManagerType(String TRUST_MANAGER_TYPE) {
/* 223 */     TRUST_MANAGER_TYPE = TRUST_MANAGER_TYPE;
/*     */   }
/*     */   
/*     */   public static HttpsURLConnection getHttpsURLConnection() {
/* 227 */     return httpsURLConnection;
/*     */   }
/*     */   
/*     */   public static void setProtocol(String PROTOCOL) {
/* 231 */     PROTOCOL = PROTOCOL;
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\authenticationendpoint.war!\WEB-INF\classes\org\wso2\carbon\identity\application\authentication\endpoint\util\MutualSSLClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
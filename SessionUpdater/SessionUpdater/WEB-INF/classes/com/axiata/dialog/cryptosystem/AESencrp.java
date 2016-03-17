/*    */ package com.axiata.dialog.cryptosystem;
/*    */ 
/*    */ import com.axiata.dialog.FileUtil;
/*    */ import java.nio.charset.Charset;
/*    */ import java.security.Key;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ import sun.misc.BASE64Decoder;
/*    */ import sun.misc.BASE64Encoder;
/*    */ 
/*    */ 
/*    */ public class AESencrp
/*    */ {
/*    */   private static final String ALGO = "AES";
/* 15 */   private static String key = FileUtil.getApplicationProperty("aeskey");
/* 16 */   private static final byte[] keyValue = key.getBytes(Charset.forName("UTF-8"));
/*    */   
/*    */   public static String encrypt(String Data) throws Exception
/*    */   {
/* 20 */     Key key = generateKey();
/* 21 */     Cipher c = Cipher.getInstance("AES");
/* 22 */     c.init(1, key);
/* 23 */     byte[] encVal = c.doFinal(Data.getBytes());
/* 24 */     String encryptedValue = new BASE64Encoder().encode(encVal);
/* 25 */     return encryptedValue;
/*    */   }
/*    */   
/*    */   public static String decrypt(String encryptedData) throws Exception {
/* 29 */     Key key = generateKey();
/* 30 */     Cipher c = Cipher.getInstance("AES");
/* 31 */     c.init(2, key);
/* 32 */     byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
/* 33 */     byte[] decValue = c.doFinal(decordedValue);
/* 34 */     String decryptedValue = new String(decValue);
/* 35 */     return decryptedValue;
/*    */   }
/*    */   
/* 38 */   private static Key generateKey() throws Exception { Key key = new SecretKeySpec(keyValue, "AES");
/* 39 */     return key;
/*    */   }
/*    */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\cryptosystem\AESencrp.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
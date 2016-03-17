/*     */ package com.axiata.dialog;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileUtil
/*     */ {
/*  36 */   private static Properties props = new Properties();
/*     */   
/*     */   public static boolean createDirectory(String directoryName, String directoryPath) {
/*  39 */     if (new File(directoryPath + "\\" + directoryName).exists()) {
/*  40 */       return true;
/*     */     }
/*     */     
/*  43 */     return new File(directoryPath + "\\" + directoryName).mkdirs();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void deleteFile(String filename)
/*     */   {
/*     */     try
/*     */     {
/*  79 */       File f1 = new File(filename);
/*  80 */       boolean success = f1.delete();
/*  81 */       if (!success) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */       return;
/*     */     } catch (Exception localException) {}
/*     */   }
/*     */   
/*     */   public static String getCorrectFileName(String fileName) {
/*  94 */     fileName = fileName.replaceAll(" ", "_");
/*     */     
/*     */ 
/*  97 */     return fileName;
/*     */   }
/*     */   
/*     */   public static void fileWrite(String filePath, String data) throws IOException {
/* 101 */     BufferedWriter out = null;
/*     */     try {
/* 103 */       out = new BufferedWriter(new FileWriter(filePath));
/* 104 */       out.write(data);
/*     */     } catch (IOException e) {
/* 106 */       e.printStackTrace();
/*     */     } finally {
/* 108 */       out.close();
/*     */     }
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*     */     try {
/* 115 */       props.load(FileUtil.class.getResourceAsStream("/application.properties"));
/*     */     }
/*     */     catch (FileNotFoundException e) {
/* 118 */       System.err.println("Check your Property file, it should be in application home dir, Error:" + e.getCause() + "Cant load APPLICATION.properties");
/*     */ 
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*     */ 
/* 124 */       System.err.println("Check your Property file, it should be in application home dir, Error:" + e.getCause() + "Cant load APPLICATION.properties");
/*     */     }
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
/*     */   public static String getApplicationProperty(String key)
/*     */   {
/* 138 */     return props.getProperty(key);
/*     */   }
/*     */   
/*     */   public static String ReadFullyIntoVar(String fullpath)
/*     */   {
/* 143 */     String result = "";
/*     */     try
/*     */     {
/* 146 */       FileInputStream file = new FileInputStream(fullpath);
/* 147 */       DataInputStream in = new DataInputStream(file);
/* 148 */       byte[] b = new byte[in.available()];
/* 149 */       in.readFully(b);
/* 150 */       in.close();
/* 151 */       result = new String(b, 0, b.length, "Cp850");
/*     */     } catch (Exception e) {
/* 153 */       e.printStackTrace();
/*     */     }
/* 155 */     return result;
/*     */   }
/*     */   
/*     */   public static void copy(String src, String dst) throws IOException
/*     */   {
/* 160 */     String fileName = src.substring(src.lastIndexOf("/") + 1);
/*     */     
/* 162 */     File fsrc = new File(src);
/* 163 */     File fdst = new File(dst + "/" + fileName);
/*     */     
/* 165 */     InputStream in = new FileInputStream(fsrc);
/* 166 */     OutputStream out = new FileOutputStream(fdst);
/*     */     
/*     */ 
/* 169 */     byte[] buf = new byte['Ѐ'];
/*     */     int len;
/* 171 */     while ((len = in.read(buf)) > 0) {
/* 172 */       out.write(buf, 0, len);
/*     */     }
/* 174 */     in.close();
/* 175 */     out.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\FileUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
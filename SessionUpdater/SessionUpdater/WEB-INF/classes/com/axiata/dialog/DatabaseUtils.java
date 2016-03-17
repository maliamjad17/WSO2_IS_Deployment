/*     */ package com.axiata.dialog;
/*     */ 
/*     */ import com.axiata.dialog.entity.LoginHistory;
/*     */ import java.io.PrintStream;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public class DatabaseUtils
/*     */ {
/*  35 */   private static volatile DataSource ussdDatasource = null;
/*     */   
/*     */ 
/*     */ 
/*  39 */   private static Log log = LogFactory.getLog(DatabaseUtils.class);
/*     */   
/*     */   public static void initializeDataSource() throws NamingException {
/*  42 */     if (ussdDatasource != null) {
/*  43 */       return;
/*     */     }
/*     */     
/*  46 */     String statdataSourceName = "jdbc/CONNECT_DB";
/*     */     
/*  48 */     if (statdataSourceName != null) {
/*     */       try {
/*  50 */         Context ctx = new InitialContext();
/*  51 */         ussdDatasource = (DataSource)ctx.lookup(statdataSourceName);
/*     */       }
/*     */       catch (NamingException e) {
/*  54 */         throw e;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateUSerStatus(String sessionID, String status) throws SQLException
/*     */   {
/*  61 */     Connection connection = null;
/*  62 */     PreparedStatement ps = null;
/*     */     
/*  64 */     String sql = "INSERT INTO `clientstatus` (`SessionID`, `Status`) VALUES (?, ?);";
/*     */     try
/*     */     {
/*  67 */       connection = getUssdDBConnection();
/*     */       
/*  69 */       ps = connection.prepareStatement(sql);
/*     */       
/*  71 */       ps.setString(1, sessionID);
/*  72 */       ps.setString(2, status);
/*     */       
/*     */ 
/*  75 */       ps.execute();
/*     */     }
/*     */     catch (NamingException ex) {
/*  78 */       log.error("Naming Error occurred: " + ex);
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/*  82 */       log.error("SQL Error occurred: " + e);
/*  83 */       System.out.print(e.getMessage());
/*     */     } finally {
/*  85 */       connection.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateStatus(String sessionID, String status) throws SQLException
/*     */   {
/*  91 */     Connection connection = null;
/*  92 */     PreparedStatement ps = null;
/*     */     
/*  94 */     String sql = "update `clientstatus` set Status=? where SessionID=?;";
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 100 */       connection = getUssdDBConnection();
/*     */       
/* 102 */       ps = connection.prepareStatement(sql);
/*     */       
/* 104 */       ps.setString(1, status);
/* 105 */       ps.setString(2, sessionID);
/*     */       
/* 107 */       ps.execute();
/*     */     }
/*     */     catch (NamingException ex)
/*     */     {
/* 111 */       log.error("Naming Error occurred: " + ex);
/*     */     }
/*     */     catch (SQLException e)
/*     */     {
/* 115 */       log.error("SQL Error occurred: " + e);
/* 116 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 118 */       connection.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updatePinStatus(String sessionID, String status, String userpin) throws SQLException
/*     */   {
/* 124 */     Connection connection = null;
/* 125 */     PreparedStatement ps = null;
/*     */     
/* 127 */     String sql = "update `clientstatus` set Status=? , pin = ? where SessionID=?;";
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 133 */       connection = getUssdDBConnection();
/*     */       
/* 135 */       ps = connection.prepareStatement(sql);
/*     */       
/* 137 */       ps.setString(1, status);
/* 138 */       ps.setString(2, userpin);
/* 139 */       ps.setString(3, sessionID);
/*     */       
/* 141 */       ps.execute();
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/*     */ 
/* 148 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 150 */       connection.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updatePinStatus(String sessionID, String status, String userpin, String ussdSessionID)
/*     */     throws SQLException
/*     */   {
/* 157 */     Connection connection = null;
/*     */     
/* 159 */     PreparedStatement ps = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 165 */     String sql = "update `clientstatus` set  Status=? , pin = ?, ussdsessionid=? where SessionID=?;";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 173 */       connection = getUssdDBConnection();
/*     */       
/*     */ 
/*     */ 
/* 177 */       ps = connection.prepareStatement(sql);
/*     */       
/*     */ 
/*     */ 
/* 181 */       ps.setString(1, status);
/*     */       
/* 183 */       ps.setString(2, userpin);
/*     */       
/* 185 */       ps.setString(3, ussdSessionID);
/*     */       
/* 187 */       ps.setString(4, sessionID);
/*     */       
/*     */ 
/*     */ 
/* 191 */       ps.execute();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 205 */       System.out.print(e.getMessage());
/*     */     }
/*     */     finally
/*     */     {
/* 209 */       connection.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getUSerStatus(String sessionID)
/*     */     throws SQLException
/*     */   {
/* 218 */     Connection connection = null;
/* 219 */     PreparedStatement ps = null;
/* 220 */     String userStatus = null;
/* 221 */     ResultSet rs = null;
/*     */     
/* 223 */     String sql = "select Status from `clientstatus` where SessionID=?;";
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 228 */       connection = getUssdDBConnection();
/*     */       
/* 230 */       ps = connection.prepareStatement(sql);
/*     */       
/* 232 */       ps.setString(1, sessionID);
/*     */       
/* 234 */       rs = ps.executeQuery();
/*     */       
/* 236 */       while (rs.next()) {
/* 237 */         userStatus = rs.getString("Status");
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/* 244 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 246 */       connection.close();
/*     */     }
/*     */     
/*     */ 
/* 250 */     return userStatus;
/*     */   }
/*     */   
/*     */   public static Connection getUssdDBConnection()
/*     */     throws SQLException, NamingException
/*     */   {
/*     */     
/* 257 */     if (ussdDatasource != null) {
/* 258 */       return ussdDatasource.getConnection();
/*     */     }
/* 260 */     throw new SQLException("USSD Datasource not initialized properly");
/*     */   }
/*     */   
/*     */   public static int readMultiplePasswordNoOfAttempts(String username) throws SQLException
/*     */   {
/* 265 */     Connection connection = null;
/* 266 */     PreparedStatement ps = null;
/* 267 */     int noOfAttempts = 0;
/* 268 */     ResultSet rs = null;
/*     */     
/* 270 */     String sql = "select attempts from `multiplepasswords` where username=?;";
/*     */     try {
/* 272 */       connection = getUssdDBConnection();
/* 273 */       ps = connection.prepareStatement(sql);
/* 274 */       ps.setString(1, username);
/* 275 */       rs = ps.executeQuery();
/* 276 */       while (rs.next()) {
/* 277 */         noOfAttempts = rs.getInt("attempts");
/*     */       }
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/* 282 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 284 */       connection.close();
/*     */     }
/* 286 */     return noOfAttempts;
/*     */   }
/*     */   
/*     */   private static boolean isFirstPinRequest(String username) throws SQLException {
/* 290 */     Connection connection = null;
/* 291 */     PreparedStatement ps = null;
/* 292 */     int count = 0;
/* 293 */     ResultSet rs = null;
/*     */     
/* 295 */     String sql = "select count(*) as total from `multiplepasswords` where username=?;";
/*     */     try {
/* 297 */       connection = getUssdDBConnection();
/* 298 */       ps = connection.prepareStatement(sql);
/* 299 */       ps.setString(1, username);
/* 300 */       rs = ps.executeQuery();
/* 301 */       while (rs.next()) {
/* 302 */         count = rs.getInt("total");
/*     */       }
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/* 307 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 309 */       connection.close();
/*     */     }
/* 311 */     return count == 0;
/*     */   }
/*     */   
/*     */   public static void updateMultiplePasswordNoOfAttempts(String username, int attempts) throws SQLException
/*     */   {
/* 316 */     Connection connection = null;
/* 317 */     PreparedStatement ps = null;
/* 318 */     String sql = null;
/* 319 */     boolean isFirstPinRequest = isFirstPinRequest(username);
/* 320 */     if (isFirstPinRequest) {
/* 321 */       sql = "INSERT INTO `multiplepasswords` set attempts=?, username=?;";
/*     */     }
/*     */     else
/*     */     {
/* 325 */       sql = "update `multiplepasswords` set attempts=? where username=?;";
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 330 */       connection = getUssdDBConnection();
/* 331 */       ps = connection.prepareStatement(sql);
/* 332 */       ps.setInt(1, attempts);
/* 333 */       ps.setString(2, username);
/* 334 */       ps.execute();
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/* 338 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 340 */       connection.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void deleteUser(String username) throws SQLException
/*     */   {
/* 346 */     Connection connection = null;
/* 347 */     String sql = "delete from `multiplepasswords` where username=?;";
/*     */     try {
/* 349 */       connection = getUssdDBConnection();
/* 350 */       PreparedStatement ps = connection.prepareStatement(sql);
/* 351 */       ps.setString(1, username);
/* 352 */       ps.execute();
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/* 356 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 358 */       connection.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public static List<LoginHistory> getLoginHistory(String userId, String application, java.sql.Date fromDate, java.sql.Date toDate) throws SQLException {
/* 363 */     Connection connection = null;
/* 364 */     PreparedStatement ps = null;
/* 365 */     String userStatus = null;
/* 366 */     ResultSet rs = null;
/*     */     
/* 368 */     if (application.equalsIgnoreCase("__ALL__")) {
/* 369 */       application = "%";
/*     */     }
/*     */     
/* 372 */     List<LoginHistory> loghistory = new ArrayList();
/*     */     
/* 374 */     String sql = "SELECT id, reqtype, application_id, authenticated_user, isauthenticated, authenticators, ipaddress, created_date FROM sp_login_history WHERE application_id like ? AND authenticated_user = ? AND created_date between ? and ? order by id desc";
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 381 */     if ((application == null) || (application.isEmpty())) {
/* 382 */       application = "%";
/*     */     }
/*     */     try
/*     */     {
/* 386 */       connection = getUssdDBConnection();
/* 387 */       ps = connection.prepareStatement(sql);
/* 388 */       ps.setString(1, application);
/* 389 */       ps.setString(2, userId);
/* 390 */       ps.setTimestamp(3, new Timestamp(fromDate.getTime()));
/* 391 */       ps.setTimestamp(4, getEndOfDay(toDate));
/*     */       
/* 393 */       rs = ps.executeQuery();
/*     */       
/* 395 */       while (rs.next()) {
/* 396 */         SimpleDateFormat sdf = new SimpleDateFormat("yyy-MMM-dd HH:mm:ss z");
/* 397 */         sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 398 */         String creationDate = sdf.format(rs.getTimestamp("created_date"));
/* 399 */         loghistory.add(new LoginHistory(rs.getString("application_id"), rs.getString("authenticated_user"), rs.getInt("isauthenticated") == 1 ? "SUCCESS" : "FAILED", rs.getString("ipaddress"), creationDate));
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/* 406 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 408 */       connection.close();
/*     */     }
/*     */     
/*     */ 
/* 412 */     return loghistory;
/*     */   }
/*     */   
/*     */   public static List<String> getLoginApps(String userId) throws SQLException {
/* 416 */     Connection connection = null;
/* 417 */     PreparedStatement ps = null;
/* 418 */     List<String> apps = new ArrayList();
/* 419 */     ResultSet rs = null;
/*     */     
/* 421 */     String sql = "SELECT distinct application_id FROM sp_login_history WHERE authenticated_user = ?";
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 426 */       connection = getUssdDBConnection();
/* 427 */       ps = connection.prepareStatement(sql);
/* 428 */       ps.setString(1, userId);
/* 429 */       rs = ps.executeQuery();
/*     */       
/* 431 */       while (rs.next()) {
/* 432 */         apps.add(rs.getString(1));
/*     */       }
/*     */       
/*     */     }
/*     */     catch (NamingException localNamingException) {}catch (SQLException e)
/*     */     {
/* 438 */       System.out.print(e.getMessage());
/*     */     } finally {
/* 440 */       connection.close();
/*     */     }
/*     */     
/*     */ 
/* 444 */     return apps;
/*     */   }
/*     */   
/*     */   public static Timestamp getEndOfDay(java.sql.Date date) {
/* 448 */     Calendar calendar = Calendar.getInstance();
/* 449 */     calendar.setTime(date);
/* 450 */     calendar.set(11, 23);
/* 451 */     calendar.set(12, 59);
/* 452 */     calendar.set(13, 59);
/* 453 */     calendar.set(14, 999);
/* 454 */     return new Timestamp(calendar.getTime().getTime());
/*     */   }
/*     */   
/*     */   public static String getMePinSessionID(String transactionId) throws SQLException {
/* 458 */     String sessionID = null;
/* 459 */     Connection connection = null;
/* 460 */     PreparedStatement ps = null;
/* 461 */     ResultSet rs = null;
/*     */     
/* 463 */     String sql = "SELECT session_id FROM mepin_transactions WHERE transaction_id = ?";
/*     */     try
/*     */     {
/* 466 */       connection = getUssdDBConnection();
/* 467 */       ps = connection.prepareStatement(sql);
/* 468 */       ps.setString(1, transactionId);
/* 469 */       rs = ps.executeQuery();
/*     */       
/* 471 */       while (rs.next()) {
/* 472 */         sessionID = rs.getString(1);
/*     */       }
/*     */     }
/*     */     catch (NamingException ex) {
/* 476 */       log.error("Error while connecting to DB", ex);
/*     */     } catch (SQLException e) {
/* 478 */       log.error("Error in querying DB", e);
/*     */     } finally {
/* 480 */       if (connection != null) {
/* 481 */         connection.close();
/*     */       }
/*     */     }
/* 484 */     return sessionID;
/*     */   }
/*     */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\SessionUpdater.war!\WEB-INF\classes\com\axiata\dialog\DatabaseUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
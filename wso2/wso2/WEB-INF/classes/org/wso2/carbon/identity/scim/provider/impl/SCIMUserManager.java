/*      */ package org.wso2.carbon.identity.scim.provider.impl;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.wso2.carbon.identity.application.common.IdentityApplicationManagementException;
/*      */ import org.wso2.carbon.identity.application.common.model.InboundProvisioningConfig;
/*      */ import org.wso2.carbon.identity.application.common.model.ProvisioningServiceProviderType;
/*      */ import org.wso2.carbon.identity.application.common.model.ServiceProvider;
/*      */ import org.wso2.carbon.identity.application.common.model.ThreadLocalProvisioningServiceProvider;
/*      */ import org.wso2.carbon.identity.application.common.util.IdentityApplicationManagementUtil;
/*      */ import org.wso2.carbon.identity.application.mgt.ApplicationInfoProvider;
/*      */ import org.wso2.carbon.identity.scim.common.config.SCIMProvisioningConfigManager;
/*      */ import org.wso2.carbon.identity.scim.common.group.SCIMGroupHandler;
/*      */ import org.wso2.carbon.identity.scim.common.utils.AttributeMapper;
/*      */ import org.wso2.carbon.identity.scim.common.utils.IdentitySCIMException;
/*      */ import org.wso2.carbon.identity.scim.common.utils.SCIMCommonUtils;
/*      */ import org.wso2.carbon.user.api.Claim;
/*      */ import org.wso2.carbon.user.api.ClaimMapping;
/*      */ import org.wso2.carbon.user.core.UserStoreManager;
/*      */ import org.wso2.carbon.user.core.claim.ClaimManager;
/*      */ import org.wso2.carbon.user.core.util.UserCoreUtil;
/*      */ import org.wso2.charon.core.attributes.Attribute;
/*      */ import org.wso2.charon.core.exceptions.CharonException;
/*      */ import org.wso2.charon.core.exceptions.DuplicateResourceException;
/*      */ import org.wso2.charon.core.exceptions.NotFoundException;
/*      */ import org.wso2.charon.core.extensions.UserManager;
/*      */ import org.wso2.charon.core.objects.Group;
/*      */ import org.wso2.charon.core.objects.SCIMObject;
/*      */ import org.wso2.charon.core.objects.User;
/*      */ import org.wso2.charon.core.provisioning.ProvisioningHandler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SCIMUserManager
/*      */   implements UserManager
/*      */ {
/*   57 */   private UserStoreManager carbonUM = null;
/*   58 */   private ClaimManager carbonClaimManager = null;
/*      */   
/*      */   private String consumerName;
/*   61 */   private static Log log = LogFactory.getLog(SCIMUserManager.class);
/*      */   
/*      */ 
/*   64 */   private ExecutorService provisioningThreadPool = Executors.newCachedThreadPool();
/*      */   
/*      */   public SCIMUserManager(UserStoreManager carbonUserStoreManager, String userName, ClaimManager claimManager)
/*      */   {
/*   68 */     this.carbonUM = carbonUserStoreManager;
/*   69 */     this.consumerName = userName;
/*   70 */     this.carbonClaimManager = claimManager;
/*      */   }
/*      */   
/*      */   public User createUser(User user) throws CharonException, DuplicateResourceException
/*      */   {
/*      */     try
/*      */     {
/*   77 */       ThreadLocalProvisioningServiceProvider threadLocalSP = IdentityApplicationManagementUtil.getThreadLocalProvisioningServiceProvider();
/*      */       
/*      */ 
/*   80 */       ServiceProvider serviceProvider = null;
/*   81 */       if (threadLocalSP.getServiceProviderType() == ProvisioningServiceProviderType.OAUTH) {
/*   82 */         serviceProvider = ApplicationInfoProvider.getInstance().getServiceProviderByClienId(threadLocalSP.getServiceProviderName(), "oauth2", threadLocalSP.getTenantDomain());
/*      */       }
/*      */       else
/*      */       {
/*   86 */         serviceProvider = ApplicationInfoProvider.getInstance().getServiceProvider(threadLocalSP.getServiceProviderName(), threadLocalSP.getTenantDomain());
/*      */       }
/*      */       
/*      */ 
/*   90 */       String userStoreName = null;
/*      */       
/*   92 */       if ((serviceProvider != null) && (serviceProvider.getInboundProvisioningConfig() != null)) {
/*   93 */         userStoreName = serviceProvider.getInboundProvisioningConfig().getProvisioningUserStore();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*   98 */       StringBuilder userName = new StringBuilder();
/*      */       
/*  100 */       if ((userStoreName != null) && (userStoreName.trim().length() > 0))
/*      */       {
/*      */ 
/*  103 */         String currentUserName = user.getUserName();
/*  104 */         currentUserName = UserCoreUtil.removeDomainFromName(currentUserName);
/*  105 */         user.setUserName(userStoreName + "/" + currentUserName);
/*      */       }
/*      */       
/*      */     }
/*      */     catch (IdentityApplicationManagementException e)
/*      */     {
/*  111 */       throw new CharonException("Error retrieving User Store name. ", e);
/*      */     }
/*      */     
/*  114 */     SCIMProvisioningConfigManager provisioningConfigManager = SCIMProvisioningConfigManager.getInstance();
/*      */     
/*      */ 
/*  117 */     if (SCIMProvisioningConfigManager.isDumbMode())
/*      */     {
/*  119 */       if (log.isDebugEnabled()) {
/*  120 */         log.debug("This instance is operating in dumb mode. Hence, operation is not persisted, it will only be provisioned.");
/*      */       }
/*      */       
/*  123 */       provisionSCIMOperation(2, user, 1, null);
/*  124 */       return user;
/*      */     }
/*      */     
/*      */ 
/*  128 */     if (log.isDebugEnabled()) {
/*  129 */       log.debug("Creating user: " + user.getUserName());
/*      */     }
/*      */     
/*      */ 
/*  133 */     SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*  134 */     Map<String, String> claimsMap = AttributeMapper.getClaimsMap(user);
/*      */     
/*      */ 
/*      */ 
/*  138 */     if (claimsMap.containsKey("urn:scim:schemas:core:1.0:groups")) {
/*  139 */       claimsMap.remove("urn:scim:schemas:core:1.0:groups");
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  144 */       if (this.carbonUM.isExistingUser(user.getUserName())) {
/*  145 */         String error = "User with the name: " + user.getUserName() + " already exists in the system.";
/*  146 */         throw new DuplicateResourceException(error);
/*      */       }
/*  148 */       if (claimsMap.containsKey("urn:scim:schemas:core:1.0:userName")) {
/*  149 */         claimsMap.remove("urn:scim:schemas:core:1.0:userName");
/*      */       }
/*  151 */       this.carbonUM.addUser(user.getUserName(), user.getPassword(), null, claimsMap, null);
/*  152 */       log.info("User: " + user.getUserName() + " is created through SCIM.");
/*      */     }
/*      */     catch (org.wso2.carbon.user.api.UserStoreException e) {
/*  155 */       String errMsg = e.getMessage() + " ";
/*  156 */       errMsg = errMsg + "Error in adding the user: " + user.getUserName() + " to the user store..";
/*      */       
/*  158 */       throw new CharonException(errMsg, e);
/*      */     }
/*  160 */     return user;
/*      */   }
/*      */   
/*      */   public User getUser(String userId) throws CharonException
/*      */   {
/*  165 */     if (log.isDebugEnabled()) {
/*  166 */       log.debug("Retrieving user: " + userId);
/*      */     }
/*  168 */     User scimUser = null;
/*      */     try
/*      */     {
/*  171 */       String[] userNames = this.carbonUM.getUserList("urn:scim:schemas:core:1.0:id", userId, "default");
/*      */       
/*      */ 
/*  174 */       if ((userNames == null) || (userNames.length == 0)) {
/*  175 */         if (log.isDebugEnabled()) {
/*  176 */           log.debug("User with SCIM id: " + userId + " does not exist in the system.");
/*      */         }
/*  178 */         return null; }
/*  179 */       if ((userNames != null) && (userNames.length == 0)) {
/*  180 */         if (log.isDebugEnabled()) {
/*  181 */           log.debug("User with SCIM id: " + userId + " does not exist in the system.");
/*      */         }
/*  183 */         return null;
/*      */       }
/*      */       
/*  186 */       scimUser = getSCIMUser(userNames[0]);
/*      */       
/*  188 */       log.info("User: " + scimUser.getUserName() + " is retrieved through SCIM.");
/*      */     }
/*      */     catch (org.wso2.carbon.user.api.UserStoreException e)
/*      */     {
/*  192 */       throw new CharonException("Error in getting user information from Carbon User Store foruser: " + userId, e);
/*      */     }
/*      */     
/*  195 */     return scimUser;
/*      */   }
/*      */   
/*      */   public List<User> listUsers() throws CharonException {
/*  199 */     List<User> users = new ArrayList();
/*      */     try {
/*  201 */       String[] userNames = this.carbonUM.getUserList("urn:scim:schemas:core:1.0:id", "*", null);
/*  202 */       if ((userNames != null) && (userNames.length != 0)) {
/*  203 */         for (String userName : userNames) {
/*  204 */           if (userName.contains("|")) {
/*  205 */             userName = userName.split("\\|")[0];
/*      */           }
/*  207 */           User scimUser = getSCIMUser(userName);
/*  208 */           Map<String, Attribute> attrMap = scimUser.getAttributeList();
/*  209 */           if ((attrMap != null) && (!attrMap.isEmpty())) {
/*  210 */             users.add(scimUser);
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (org.wso2.carbon.user.core.UserStoreException e) {
/*  215 */       throw new CharonException("Error while retrieving users from user store..", e);
/*      */     }
/*  217 */     return users;
/*      */   }
/*      */   
/*      */   public List<User> listUsersByAttribute(Attribute attribute) {
/*  221 */     return null;
/*      */   }
/*      */   
/*      */   public List<User> listUsersByFilter(String attributeName, String filterOperation, String attributeValue)
/*      */     throws CharonException
/*      */   {
/*  227 */     if (log.isDebugEnabled()) {
/*  228 */       log.debug("Listing users by filter: " + attributeName + filterOperation + attributeValue);
/*      */     }
/*      */     
/*  231 */     List<User> filteredUsers = new ArrayList();
/*  232 */     User scimUser = null;
/*      */     try
/*      */     {
/*  235 */       String[] userNames = null;
/*  236 */       if (attributeName.equals("urn:scim:schemas:core:1.0:userName")) {
/*  237 */         if (this.carbonUM.isExistingUser(attributeValue)) {
/*  238 */           userNames = new String[] { attributeValue };
/*      */         }
/*      */       } else {
/*  241 */         userNames = this.carbonUM.getUserList(attributeName, attributeValue, "default");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  246 */       if ((userNames == null) || (userNames.length == 0)) {
/*  247 */         if (log.isDebugEnabled()) {
/*  248 */           log.debug("Users with filter: " + attributeName + filterOperation + attributeValue + " does not exist in the system.");
/*      */         }
/*      */         
/*  251 */         return null;
/*      */       }
/*  253 */       for (String userName : userNames) {
/*  254 */         if (!"wso2.anonymous.user".equals(userName))
/*      */         {
/*      */ 
/*  257 */           scimUser = getSCIMUser(userName);
/*      */           
/*  259 */           if (scimUser.getId() != null)
/*      */           {
/*      */ 
/*  262 */             filteredUsers.add(scimUser); }
/*      */         }
/*      */       }
/*  265 */       log.info("Users filtered through SCIM for the filter: " + attributeName + filterOperation + attributeValue);
/*      */ 
/*      */     }
/*      */     catch (org.wso2.carbon.user.api.UserStoreException e)
/*      */     {
/*  270 */       String errMsg = "Error in getting user information from Carbon User Store forusers:" + attributeValue + " ";
/*      */       
/*  272 */       errMsg = errMsg + e.getMessage();
/*  273 */       throw new CharonException(errMsg, e);
/*      */     }
/*  275 */     return filteredUsers;
/*      */   }
/*      */   
/*      */   public List<User> listUsersBySort(String s, String s1) {
/*  279 */     return null;
/*      */   }
/*      */   
/*      */   public List<User> listUsersWithPagination(int i, int i1) {
/*  283 */     return null;
/*      */   }
/*      */   
/*      */   public User updateUser(User user) throws CharonException {
/*  287 */     SCIMProvisioningConfigManager provisioningConfigManager = SCIMProvisioningConfigManager.getInstance();
/*      */     
/*      */ 
/*  290 */     if (SCIMProvisioningConfigManager.isDumbMode())
/*      */     {
/*  292 */       if (log.isDebugEnabled()) {
/*  293 */         log.debug("This instance is operating in dumb mode. Hence, operation is not persisted, it will only be provisioned.");
/*      */       }
/*      */       
/*  296 */       provisionSCIMOperation(4, user, 1, null);
/*  297 */       return user;
/*      */     }
/*      */     
/*  300 */     if (log.isDebugEnabled()) {
/*  301 */       log.debug("Updating user: " + user.getUserName());
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  306 */       SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*      */       
/*  308 */       Map<String, String> claims = AttributeMapper.getClaimsMap(user);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  315 */       if (!this.carbonUM.isExistingUser(user.getUserName())) {
/*  316 */         throw new CharonException("User name is immutable in carbon user store.");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  321 */       if (claims.containsKey("urn:scim:schemas:core:1.0:groups")) {
/*  322 */         claims.remove("urn:scim:schemas:core:1.0:groups");
/*      */       }
/*      */       
/*  325 */       if (claims.containsKey("urn:scim:schemas:core:1.0:userName")) {
/*  326 */         claims.remove("urn:scim:schemas:core:1.0:userName");
/*      */       }
/*      */       
/*      */ 
/*  330 */       this.carbonUM.setUserClaimValues(user.getUserName(), claims, null);
/*      */       
/*  332 */       if (user.getPassword() != null) {
/*  333 */         this.carbonUM.updateCredentialByAdmin(user.getUserName(), user.getPassword());
/*      */       }
/*  335 */       log.info("User: " + user.getUserName() + " updated updated through SCIM.");
/*      */     } catch (org.wso2.carbon.user.core.UserStoreException e) {
/*  337 */       String errMsg = "Error while updating attributes of user: " + user.getUserName();
/*  338 */       errMsg = errMsg + " " + e.getMessage();
/*  339 */       throw new CharonException(errMsg, e);
/*      */     }
/*      */     
/*  342 */     return user;
/*      */   }
/*      */   
/*      */   public User updateUser(List<Attribute> attributes)
/*      */   {
/*  347 */     return null;
/*      */   }
/*      */   
/*      */   public void deleteUser(String userId) throws NotFoundException, CharonException {
/*  351 */     SCIMProvisioningConfigManager provisioningConfigManager = SCIMProvisioningConfigManager.getInstance();
/*      */     
/*      */ 
/*      */ 
/*  355 */     if (SCIMProvisioningConfigManager.isDumbMode()) {
/*  356 */       if (log.isDebugEnabled()) {
/*  357 */         log.debug("This instance is operating in dumb mode. Hence, operation is not persisted, it will only be provisioned.");
/*      */       }
/*      */       
/*  360 */       User user = new User();
/*  361 */       user.setUserName(userId);
/*  362 */       provisionSCIMOperation(3, user, 1, null);
/*      */     } else {
/*  364 */       if (log.isDebugEnabled()) {
/*  365 */         log.debug("Deleting user: " + userId);
/*      */       }
/*      */       
/*  368 */       String[] userNames = null;
/*  369 */       String userName = null;
/*      */       
/*      */       try
/*      */       {
/*  373 */         SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*  374 */         userNames = this.carbonUM.getUserList("urn:scim:schemas:core:1.0:id", userId, "default");
/*      */         
/*  376 */         if ((userNames == null) && (userNames.length == 0))
/*      */         {
/*  378 */           if (log.isDebugEnabled()) {
/*  379 */             log.debug("User with id: " + userId + " not found.");
/*      */           }
/*  381 */           throw new NotFoundException(); }
/*  382 */         if ((userNames != null) && (userNames.length == 0))
/*      */         {
/*  384 */           if (log.isDebugEnabled()) {
/*  385 */             log.debug("User with id: " + userId + " not found.");
/*      */           }
/*  387 */           throw new NotFoundException();
/*      */         }
/*      */         
/*  390 */         userName = userNames[0];
/*  391 */         this.carbonUM.deleteUser(userName);
/*  392 */         log.info("User: " + userName + " is deleted through SCIM.");
/*      */ 
/*      */       }
/*      */       catch (org.wso2.carbon.user.core.UserStoreException e)
/*      */       {
/*  397 */         String errMsg = "Error in deleting user: " + userName + " ";
/*  398 */         errMsg = errMsg + e.getMessage();
/*  399 */         throw new CharonException(errMsg, e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Group createGroup(Group group) throws CharonException, DuplicateResourceException {
/*  405 */     SCIMProvisioningConfigManager provisioningConfigManager = SCIMProvisioningConfigManager.getInstance();
/*      */     
/*      */ 
/*      */ 
/*  409 */     if (SCIMProvisioningConfigManager.isDumbMode()) {
/*  410 */       if (log.isDebugEnabled()) {
/*  411 */         log.debug("This instance is operating in dumb mode. Hence, operation is not persisted, it will only be provisioned.");
/*      */       }
/*      */       
/*  414 */       provisionSCIMOperation(2, group, 2, null);
/*  415 */       return group;
/*      */     }
/*  417 */     if (log.isDebugEnabled()) {
/*  418 */       log.debug("Creating group: " + group.getDisplayName());
/*      */     }
/*      */     try
/*      */     {
/*  422 */       String originalName = group.getDisplayName();
/*  423 */       String roleNameWithDomain = null;
/*  424 */       String domainName = "";
/*  425 */       if (originalName.indexOf("/") > 0) {
/*  426 */         roleNameWithDomain = originalName;
/*  427 */         domainName = originalName.split("/")[0];
/*      */       } else {
/*  429 */         roleNameWithDomain = "PRIMARY/" + originalName;
/*      */         
/*  431 */         domainName = "PRIMARY";
/*      */       }
/*  433 */       group.setDisplayName(roleNameWithDomain);
/*      */       
/*  435 */       if (this.carbonUM.isExistingRole(group.getDisplayName(), false)) {
/*  436 */         String error = "Group with name: " + group.getDisplayName() + " already exists in the system.";
/*      */         
/*  438 */         throw new DuplicateResourceException(error);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  443 */       SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*      */       
/*      */ 
/*  446 */       List<String> userIds = group.getMembers();
/*  447 */       List<String> userDisplayNames = group.getMembersWithDisplayName();
/*  448 */       if ((userIds != null) && (userIds.size() != 0)) {
/*  449 */         List<String> members = new ArrayList();
/*  450 */         for (String userId : userIds) {
/*  451 */           String[] userNames = this.carbonUM.getUserList("urn:scim:schemas:core:1.0:id", userId, "default");
/*      */           
/*  453 */           if ((userNames == null) || (userNames.length == 0)) {
/*  454 */             String error = "User: " + userId + " doesn't exist in the user store. " + "Hence, can not create the group: " + group.getDisplayName();
/*      */             
/*  456 */             throw new IdentitySCIMException(error);
/*      */           }
/*  458 */           if ((userNames[0].indexOf("/") > 0) && (!userNames[0].contains(domainName))) {
/*  459 */             String error = "User: " + userId + " doesn't exist in the same user store. " + "Hence, can not create the group: " + group.getDisplayName();
/*      */             
/*  461 */             throw new IdentitySCIMException(error);
/*      */           }
/*      */           
/*  464 */           members.add(userNames[0]);
/*  465 */           if ((userDisplayNames != null) && (userDisplayNames.size() != 0)) {
/*  466 */             boolean userContains = false;
/*  467 */             for (String user : userDisplayNames) {
/*  468 */               user = user.indexOf("/") > 0 ? user.split("/")[1] : user;
/*      */               
/*      */ 
/*      */ 
/*  472 */               if (user.equalsIgnoreCase(userNames[0].indexOf("/") > 0 ? userNames[0].split("/")[1] : userNames[0]))
/*      */               {
/*      */ 
/*  475 */                 userContains = true;
/*  476 */                 break;
/*      */               }
/*      */             }
/*  479 */             if (!userContains) {
/*  480 */               throw new IdentitySCIMException("Given SCIM user Id and name not matching..");
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  488 */         SCIMGroupHandler scimGroupHandler = new SCIMGroupHandler(this.carbonUM.getTenantId());
/*      */         
/*      */ 
/*  491 */         scimGroupHandler.createSCIMAttributes(group);
/*  492 */         this.carbonUM.addRole(group.getDisplayName(), (String[])members.toArray(new String[members.size()]), null, false);
/*      */         
/*  494 */         log.info("Group: " + group.getDisplayName() + " is created through SCIM.");
/*      */       }
/*      */       else {
/*  497 */         SCIMGroupHandler scimGroupHandler = new SCIMGroupHandler(this.carbonUM.getTenantId());
/*  498 */         scimGroupHandler.createSCIMAttributes(group);
/*  499 */         this.carbonUM.addRole(group.getDisplayName(), null, null, false);
/*  500 */         log.info("Group: " + group.getDisplayName() + " is created through SCIM.");
/*      */       }
/*      */     } catch (org.wso2.carbon.user.api.UserStoreException e) {
/*  503 */       throw new CharonException(e.getMessage(), e);
/*      */     } catch (IdentitySCIMException e) {
/*  505 */       throw new CharonException(e.getMessage(), e);
/*      */     }
/*      */     
/*  508 */     return group;
/*      */   }
/*      */   
/*      */   public Group getGroup(String id) throws CharonException
/*      */   {
/*  513 */     if (log.isDebugEnabled()) {
/*  514 */       log.debug("Retrieving group with id: " + id);
/*      */     }
/*  516 */     Group group = null;
/*      */     try {
/*  518 */       SCIMGroupHandler groupHandler = new SCIMGroupHandler(this.carbonUM.getTenantId());
/*      */       
/*  520 */       String groupName = groupHandler.getGroupName(id);
/*      */       
/*  522 */       if (groupName != null) {
/*  523 */         group = getGroupWithName(groupName);
/*      */       }
/*      */       else {
/*  526 */         return null;
/*      */       }
/*      */     } catch (org.wso2.carbon.user.core.UserStoreException e) {
/*  529 */       String errMsg = "Error in retrieving group: " + id + " ";
/*  530 */       errMsg = errMsg + e.getMessage();
/*  531 */       throw new CharonException(errMsg, e);
/*      */     } catch (IdentitySCIMException e) {
/*  533 */       throw new CharonException("Error in retrieving SCIM Group information from database.", e);
/*      */     }
/*  535 */     return group;
/*      */   }
/*      */   
/*      */   public List<Group> listGroups() throws CharonException {
/*  539 */     List<Group> groupList = new ArrayList();
/*      */     try {
/*  541 */       SCIMGroupHandler groupHandler = new SCIMGroupHandler(this.carbonUM.getTenantId());
/*  542 */       Set<String> roleNames = groupHandler.listSCIMRoles();
/*      */       
/*  544 */       for (String roleName : roleNames)
/*      */       {
/*  546 */         Group group = getGroupWithName(roleName);
/*  547 */         groupList.add(group);
/*      */       }
/*      */     } catch (org.wso2.carbon.user.core.UserStoreException e) {
/*  550 */       String errMsg = "Error in obtaining role names from user store.";
/*  551 */       errMsg = errMsg + e.getMessage();
/*  552 */       throw new CharonException(errMsg, e);
/*      */     } catch (IdentitySCIMException e) {
/*  554 */       throw new CharonException("Error in retrieving SCIM Group information from database.", e);
/*      */     }
/*  556 */     return groupList;
/*      */   }
/*      */   
/*      */   public List<Group> listGroupsByAttribute(Attribute attribute) throws CharonException {
/*  560 */     return null;
/*      */   }
/*      */   
/*      */   public List<Group> listGroupsByFilter(String filterAttribute, String filterOperation, String attributeValue)
/*      */     throws CharonException
/*      */   {
/*  566 */     if (log.isDebugEnabled()) {
/*  567 */       log.debug("Listing groups with filter: " + filterAttribute + filterOperation + attributeValue);
/*      */     }
/*      */     
/*  570 */     List<Group> filteredGroups = new ArrayList();
/*  571 */     Group group = null;
/*      */     try {
/*  573 */       if ((attributeValue != null) && (this.carbonUM.isExistingRole(attributeValue, false)))
/*      */       {
/*  575 */         if (("system/wso2.anonymous.role".equals(attributeValue)) || (UserCoreUtil.isEveryoneRole(attributeValue, this.carbonUM.getRealmConfiguration())) || (UserCoreUtil.isPrimaryAdminRole(attributeValue, this.carbonUM.getRealmConfiguration())))
/*      */         {
/*      */ 
/*  578 */           throw new IdentitySCIMException("Internal roles do not support SCIM.");
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  583 */         String groupNameWithDomain = null;
/*  584 */         if (attributeValue.indexOf("/") > 0) {
/*  585 */           groupNameWithDomain = attributeValue;
/*      */         } else {
/*  587 */           groupNameWithDomain = "PRIMARY/" + attributeValue;
/*      */         }
/*      */         
/*  590 */         group = getGroupWithName(groupNameWithDomain);
/*  591 */         filteredGroups.add(group);
/*      */       }
/*      */       else {
/*  594 */         return null;
/*      */       }
/*      */     } catch (org.wso2.carbon.user.core.UserStoreException e) {
/*  597 */       String errMsg = "Error in filtering group with filter: " + filterAttribute + filterOperation + attributeValue;
/*      */       
/*  599 */       errMsg = errMsg + e.getMessage();
/*  600 */       throw new CharonException(errMsg, e);
/*      */     } catch (org.wso2.carbon.user.api.UserStoreException e) {
/*  602 */       throw new CharonException("Error in filtering group with filter: " + filterAttribute + filterOperation + attributeValue, e);
/*      */     }
/*      */     catch (IdentitySCIMException e) {
/*  605 */       throw new CharonException("Error in retrieving SCIM Group information from database.", e);
/*      */     }
/*  607 */     return filteredGroups;
/*      */   }
/*      */   
/*      */   public List<Group> listGroupsBySort(String s, String s1) throws CharonException {
/*  611 */     return null;
/*      */   }
/*      */   
/*      */   public List<Group> listGroupsWithPagination(int i, int i1) {
/*  615 */     return null;
/*      */   }
/*      */   
/*      */   public Group updateGroup(Group oldGroup, Group newGroup) throws CharonException {
/*  619 */     SCIMProvisioningConfigManager provisioningConfigManager = SCIMProvisioningConfigManager.getInstance();
/*      */     
/*      */ 
/*  622 */     newGroup.setDisplayName(SCIMCommonUtils.getGroupNameWithDomain(newGroup.getDisplayName()));
/*  623 */     oldGroup.setDisplayName(SCIMCommonUtils.getGroupNameWithDomain(oldGroup.getDisplayName()));
/*      */     
/*      */ 
/*      */ 
/*  627 */     if (SCIMProvisioningConfigManager.isDumbMode()) {
/*  628 */       if (log.isDebugEnabled()) {
/*  629 */         log.debug("This instance is operating in dumb mode. Hence, operation is not persisted, it will only be provisioned.");
/*      */       }
/*      */       
/*      */ 
/*  633 */       Map<String, Object> additionalInformation = new HashMap();
/*  634 */       additionalInformation.put("ISRoleNameChangedOnUpdate", Boolean.valueOf(true));
/*  635 */       additionalInformation.put("OldGroupName", oldGroup.getDisplayName());
/*      */       
/*  637 */       provisionSCIMOperation(4, newGroup, 2, additionalInformation);
/*      */       
/*  639 */       return newGroup;
/*      */     }
/*      */     
/*  642 */     if (log.isDebugEnabled()) {
/*  643 */       log.debug("Updating group: " + oldGroup.getDisplayName());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  653 */       SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*      */       
/*  655 */       boolean updated = false;
/*      */       
/*      */ 
/*  658 */       SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*      */       
/*      */ 
/*  661 */       List<String> userIds = newGroup.getMembers();
/*  662 */       List<String> userDisplayNames = newGroup.getMembersWithDisplayName();
/*      */       
/*  664 */       String groupName = newGroup.getDisplayName();
/*  665 */       String userStoreDomainForGroup = null;
/*      */       
/*  667 */       int domainSeparatorIndexForGroup = groupName.indexOf("/");
/*      */       
/*  669 */       if (domainSeparatorIndexForGroup > 0) {
/*  670 */         userStoreDomainForGroup = groupName.substring(0, domainSeparatorIndexForGroup);
/*      */         
/*      */ 
/*  673 */         for (int i = 0; i < userDisplayNames.size(); i++) {
/*  674 */           String userDisplayName = (String)userDisplayNames.get(i);
/*  675 */           int userDomainSeparatorIndex = userDisplayName.indexOf("/");
/*      */           
/*  677 */           if (userDomainSeparatorIndex > 0) {
/*  678 */             String userStoreDomainForUser = groupName.substring(0, userDomainSeparatorIndex);
/*      */             
/*  680 */             if (!userStoreDomainForGroup.equals(userStoreDomainForUser))
/*      */             {
/*      */ 
/*  683 */               throw new IdentitySCIMException(userDisplayName + " does not " + "belongs to user store " + userStoreDomainForGroup);
/*      */             }
/*      */           }
/*      */           else
/*      */           {
/*  688 */             throw new IdentitySCIMException(userDisplayName + " does not " + "belongs to user store " + userStoreDomainForGroup);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */       String[] userNames;
/*  694 */       if ((userIds != null) && (userIds.size() != 0)) {
/*  695 */         userNames = null;
/*  696 */         for (String userId : userIds) {
/*  697 */           userNames = this.carbonUM.getUserList("urn:scim:schemas:core:1.0:id", userId, "default");
/*      */           
/*  699 */           if (userStoreDomainForGroup != null) {
/*  700 */             userNames = this.carbonUM.getUserList("urn:scim:schemas:core:1.0:id", userStoreDomainForGroup + "/" + userId, "default");
/*      */           }
/*      */           else
/*      */           {
/*  704 */             userNames = this.carbonUM.getUserList("urn:scim:schemas:core:1.0:id", userId, "default");
/*      */           }
/*      */           
/*  707 */           if ((userNames == null) || (userNames.length == 0)) {
/*  708 */             String error = "User: " + userId + " doesn't exist in the user store. " + "Hence, can not update the group: " + oldGroup.getDisplayName();
/*      */             
/*  710 */             throw new IdentitySCIMException(error);
/*      */           }
/*  712 */           if (!userDisplayNames.contains(userNames[0])) {
/*  713 */             throw new IdentitySCIMException("Given SCIM user Id and name not matching..");
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  721 */       if (!oldGroup.getDisplayName().equals(newGroup.getDisplayName()))
/*      */       {
/*  723 */         this.carbonUM.updateRoleName(oldGroup.getDisplayName(), newGroup.getDisplayName());
/*      */         
/*  725 */         updated = true;
/*      */       }
/*      */       
/*      */ 
/*  729 */       List<String> oldMembers = oldGroup.getMembersWithDisplayName();
/*  730 */       List<String> newMembers = newGroup.getMembersWithDisplayName();
/*  731 */       if (newMembers != null)
/*      */       {
/*  733 */         List<String> addedMembers = new ArrayList();
/*  734 */         List<String> deletedMembers = new ArrayList();
/*      */         
/*      */ 
/*  737 */         if ((oldMembers != null) && (oldMembers.size() != 0)) {
/*  738 */           for (String oldMember : oldMembers) {
/*  739 */             if ((newMembers == null) || (!newMembers.contains(oldMember)))
/*      */             {
/*      */ 
/*  742 */               deletedMembers.add(oldMember);
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  747 */         if ((newMembers != null) && (newMembers.size() != 0)) {
/*  748 */           for (String newMember : newMembers) {
/*  749 */             if ((oldMembers == null) || (!oldMembers.contains(newMember)))
/*      */             {
/*      */ 
/*  752 */               addedMembers.add(newMember);
/*      */             }
/*      */           }
/*      */         }
/*  756 */         if ((addedMembers.size() != 0) || (deletedMembers.size() != 0)) {
/*  757 */           this.carbonUM.updateUserListOfRole(newGroup.getDisplayName(), (String[])deletedMembers.toArray(new String[deletedMembers.size()]), (String[])addedMembers.toArray(new String[addedMembers.size()]));
/*      */           
/*      */ 
/*  760 */           updated = true;
/*      */         }
/*      */       }
/*  763 */       if (updated) {
/*  764 */         log.info("Group: " + newGroup.getDisplayName() + " is updated through SCIM.");
/*      */       } else {
/*  766 */         log.warn("There is no updated field in the group: " + oldGroup.getDisplayName() + ". Therefore ignoring the provisioning.");
/*      */       }
/*      */     }
/*      */     catch (org.wso2.carbon.user.api.UserStoreException e)
/*      */     {
/*  771 */       throw new CharonException(e.getMessage());
/*      */     } catch (IdentitySCIMException e) {
/*  773 */       throw new CharonException(e.getMessage());
/*      */     }
/*  775 */     return newGroup;
/*      */   }
/*      */   
/*      */   public Group updateGroup(List<Attribute> attributes) throws CharonException
/*      */   {
/*  780 */     return null;
/*      */   }
/*      */   
/*      */   public Group patchGroup(Group oldGroup, Group newGroup) throws CharonException {
/*  784 */     SCIMProvisioningConfigManager provisioningConfigManager = SCIMProvisioningConfigManager.getInstance();
/*      */     
/*      */ 
/*  787 */     newGroup.setDisplayName(SCIMCommonUtils.getGroupNameWithDomain(newGroup.getDisplayName()));
/*  788 */     oldGroup.setDisplayName(SCIMCommonUtils.getGroupNameWithDomain(oldGroup.getDisplayName()));
/*      */     
/*      */ 
/*  791 */     if (SCIMProvisioningConfigManager.isDumbMode()) {
/*  792 */       if (log.isDebugEnabled()) {
/*  793 */         log.debug("This instance is operating in dumb mode. Hence, operation is not persisted, it will only be provisioned.");
/*      */       }
/*      */       
/*      */ 
/*  797 */       Map<String, Object> additionalInformation = new HashMap();
/*  798 */       additionalInformation.put("ISRoleNameChangedOnUpdate", Boolean.valueOf(true));
/*  799 */       additionalInformation.put("OldGroupName", oldGroup.getDisplayName());
/*      */       
/*  801 */       provisionSCIMOperation(4, newGroup, 2, additionalInformation);
/*      */       
/*  803 */       return newGroup;
/*      */     }
/*      */     
/*  806 */     if (log.isDebugEnabled()) {
/*  807 */       log.debug("Updating group: " + oldGroup.getDisplayName());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  816 */       SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*      */       
/*  818 */       boolean updated = false;
/*      */       
/*      */ 
/*  821 */       SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*      */       List<String> userDisplayNames;
/*      */       String[] userNames;
/*  824 */       if ((newGroup != null) && (newGroup.getMembers().size() != 0)) {
/*  825 */         List<String> userIds = newGroup.getMembers();
/*  826 */         userDisplayNames = newGroup.getMembersWithDisplayName();
/*  827 */         if ((userIds != null) && (userIds.size() != 0)) {
/*  828 */           userNames = null;
/*  829 */           for (String userId : userIds) {
/*  830 */             userNames = this.carbonUM.getUserList("urn:scim:schemas:core:1.0:id", userId, "default");
/*      */             
/*  832 */             if ((userNames == null) || (userNames.length == 0)) {
/*  833 */               String error = "User: " + userId + "+ doesn't exist in the user store. " + "Hence, can not update the group: " + oldGroup.getDisplayName();
/*      */               
/*  835 */               throw new IdentitySCIMException(error);
/*      */             }
/*  837 */             if (!userDisplayNames.contains(userNames[0])) {
/*  838 */               throw new IdentitySCIMException("Given SCIM user Id and name not matching..");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  847 */       if (!oldGroup.getDisplayName().equals(newGroup.getDisplayName()))
/*      */       {
/*  849 */         this.carbonUM.updateRoleName(oldGroup.getDisplayName(), newGroup.getDisplayName());
/*      */         
/*  851 */         updated = true;
/*      */       }
/*      */       
/*      */ 
/*  855 */       List<String> oldMembers = oldGroup.getMembersWithDisplayName();
/*  856 */       List<String> addRequestedMembers = null;
/*  857 */       List<String> deleteRequestedMembers = null;
/*  858 */       if ((newGroup != null) && (newGroup.getMembers().size() != 0)) {
/*  859 */         addRequestedMembers = newGroup.getMembersWithDisplayName(null);
/*  860 */         deleteRequestedMembers = newGroup.getMembersWithDisplayName("delete");
/*      */       }
/*      */       
/*  863 */       List<String> addedMembers = new ArrayList();
/*  864 */       List<String> deletedMembers = new ArrayList();
/*      */       
/*  866 */       if ((addRequestedMembers == null) && (deleteRequestedMembers == null)) {
/*  867 */         String[] users = this.carbonUM.getUserListOfRole(newGroup.getDisplayName());
/*  868 */         deletedMembers = Arrays.asList(users);
/*      */       }
/*      */       
/*  871 */       if ((addRequestedMembers != null) && (addRequestedMembers.size() != 0)) {
/*  872 */         for (String addRequestedMember : addRequestedMembers) {
/*  873 */           if ((oldMembers == null) || (!oldMembers.contains(addRequestedMember)))
/*      */           {
/*      */ 
/*  876 */             addedMembers.add(addRequestedMember);
/*      */           }
/*      */         }
/*      */       }
/*  880 */       if ((deleteRequestedMembers != null) && (deleteRequestedMembers.size() != 0)) {
/*  881 */         for (String deleteRequestedMember : deleteRequestedMembers) {
/*  882 */           if ((oldMembers != null) && (oldMembers.contains(deleteRequestedMember))) {
/*  883 */             deletedMembers.add(deleteRequestedMember);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  890 */       if ((newGroup.getDisplayName() != null) && (
/*  891 */         (addedMembers.size() != 0) || (deletedMembers.size() != 0))) {
/*  892 */         this.carbonUM.updateUserListOfRole(newGroup.getDisplayName(), (String[])deletedMembers.toArray(new String[deletedMembers.size()]), (String[])addedMembers.toArray(new String[addedMembers.size()]));
/*      */         
/*      */ 
/*  895 */         updated = true;
/*      */       }
/*      */       
/*      */ 
/*  899 */       if (updated) {
/*  900 */         log.info("Group: " + newGroup.getDisplayName() + " is updated through SCIM.");
/*      */       } else {
/*  902 */         log.warn("There is no updated field in the group: " + oldGroup.getDisplayName() + ". Therefore ignoring the provisioning.");
/*      */       }
/*      */     }
/*      */     catch (org.wso2.carbon.user.api.UserStoreException e)
/*      */     {
/*  907 */       throw new CharonException(e.getMessage());
/*      */     } catch (IdentitySCIMException e) {
/*  909 */       throw new CharonException(e.getMessage());
/*      */     }
/*  911 */     return newGroup;
/*      */   }
/*      */   
/*      */   public void deleteGroup(String groupId)
/*      */     throws NotFoundException, CharonException
/*      */   {
/*  917 */     SCIMProvisioningConfigManager provisioningConfigManager = SCIMProvisioningConfigManager.getInstance();
/*      */     
/*      */ 
/*  920 */     if (SCIMProvisioningConfigManager.isDumbMode()) {
/*  921 */       if (log.isDebugEnabled()) {
/*  922 */         log.debug("This instance is operating in dumb mode. Hence, operation is not persisted, it will only be provisioned.");
/*      */       }
/*      */       
/*  925 */       Group group = new Group();
/*  926 */       group.setDisplayName(groupId);
/*  927 */       provisionSCIMOperation(3, group, 2, null);
/*      */     } else {
/*  929 */       if (log.isDebugEnabled()) {
/*  930 */         log.debug("Deleting group: " + groupId);
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  935 */         SCIMCommonUtils.setThreadLocalIsManagedThroughSCIMEP(Boolean.valueOf(true));
/*      */         
/*      */ 
/*  938 */         SCIMGroupHandler groupHandler = new SCIMGroupHandler(this.carbonUM.getTenantId());
/*  939 */         String groupName = groupHandler.getGroupName(groupId);
/*      */         
/*  941 */         if (groupName != null)
/*      */         {
/*  943 */           this.carbonUM.deleteRole(groupName);
/*      */           
/*      */ 
/*  946 */           log.info("Group: " + groupName + " is deleted through SCIM.");
/*      */         }
/*      */         else {
/*  949 */           if (log.isDebugEnabled()) {
/*  950 */             log.debug("Group with SCIM id: " + groupId + " doesn't exist in the system.");
/*      */           }
/*  952 */           throw new NotFoundException();
/*      */         }
/*      */       } catch (org.wso2.carbon.user.api.UserStoreException e) {
/*  955 */         throw new CharonException(e.getMessage(), e);
/*      */       } catch (IdentitySCIMException e) {
/*  957 */         throw new CharonException(e.getMessage(), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private User getSCIMUser(String userName) throws CharonException {
/*  963 */     User scimUser = null;
/*      */     try
/*      */     {
/*  966 */       ClaimMapping[] claims = this.carbonClaimManager.getAllClaimMappings("urn:scim:schemas:core:1.0");
/*      */       
/*  968 */       List<String> claimURIList = new ArrayList();
/*  969 */       for (ClaimMapping claim : claims) {
/*  970 */         claimURIList.add(claim.getClaim().getClaimUri());
/*      */       }
/*      */       
/*  973 */       Map<String, String> attributes = this.carbonUM.getUserClaimValues(userName, (String[])claimURIList.toArray(new String[claimURIList.size()]), null);
/*      */       
/*      */ 
/*  976 */       if (attributes.containsKey("urn:scim:schemas:core:1.0:addresses")) {
/*  977 */         attributes.remove("urn:scim:schemas:core:1.0:addresses");
/*      */       }
/*      */       
/*      */ 
/*  981 */       attributes.put("urn:scim:schemas:core:1.0:userName", userName);
/*      */       
/*      */ 
/*  984 */       String[] roles = this.carbonUM.getRoleListOfUser(userName);
/*      */       
/*  986 */       scimUser = (User)AttributeMapper.constructSCIMObjectFromAttributes(attributes, 1);
/*      */       
/*      */ 
/*  989 */       for (String role : roles) {
/*  990 */         if ((!UserCoreUtil.isEveryoneRole(role, this.carbonUM.getRealmConfiguration())) && (!UserCoreUtil.isPrimaryAdminRole(role, this.carbonUM.getRealmConfiguration())) && (!"system/wso2.anonymous.role".equalsIgnoreCase(role)) && (!role.toLowerCase().startsWith("Internal/".toLowerCase())))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1000 */           Group group = getGroupOnlyWithMetaAttributes(role);
/* 1001 */           if (group != null)
/* 1002 */             scimUser.setGroup(null, group.getId(), role);
/*      */         }
/*      */       }
/*      */     } catch (org.wso2.carbon.user.api.UserStoreException e) {
/* 1006 */       String errMsg = "Error in getting user information from Carbon User Store for user: " + userName + " ";
/*      */       
/* 1008 */       errMsg = errMsg + e.getMessage();
/* 1009 */       throw new CharonException(errMsg, e);
/*      */     } catch (CharonException e) {
/* 1011 */       throw new CharonException("Error in getting user information from Carbon User Store for user: " + userName, e);
/*      */     }
/*      */     catch (NotFoundException e) {
/* 1014 */       throw new CharonException("Error in getting user information from Carbon User Store for user: " + userName, e);
/*      */     }
/*      */     catch (IdentitySCIMException e) {
/* 1017 */       throw new CharonException("Error in getting group information from Identity DB for user: " + userName, e);
/*      */     }
/*      */     
/* 1020 */     return scimUser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Group getGroupWithName(String groupName)
/*      */     throws CharonException, org.wso2.carbon.user.core.UserStoreException, IdentitySCIMException
/*      */   {
/* 1036 */     Group group = new Group();
/* 1037 */     group.setDisplayName(groupName);
/* 1038 */     String[] userNames = this.carbonUM.getUserListOfRole(groupName);
/*      */     
/*      */ 
/* 1041 */     if ((userNames != null) && (userNames.length != 0)) {
/* 1042 */       for (String userName : userNames) {
/* 1043 */         User user = getSCIMUser(userName);
/* 1044 */         if (user != null) {
/* 1045 */           group.setMember(user.getId(), userName);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1050 */     SCIMGroupHandler groupHandler = new SCIMGroupHandler(this.carbonUM.getTenantId());
/* 1051 */     group = groupHandler.getGroupWithAttributes(group, groupName);
/* 1052 */     return group;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Group getGroupOnlyWithMetaAttributes(String groupName)
/*      */     throws CharonException, IdentitySCIMException, org.wso2.carbon.user.core.UserStoreException
/*      */   {
/* 1069 */     Group group = new Group();
/* 1070 */     group.setDisplayName(groupName);
/* 1071 */     SCIMGroupHandler groupHandler = new SCIMGroupHandler(this.carbonUM.getTenantId());
/* 1072 */     return groupHandler.getGroupWithAttributes(group, groupName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void provisionSCIMOperation(int provisioningMethod, SCIMObject provisioningObject, int provisioningObjectType, Map<String, Object> properties)
/*      */     throws CharonException
/*      */   {
/*      */     try
/*      */     {
/* 1089 */       if (log.isDebugEnabled()) {
/* 1090 */         log.debug("Server is operating in dumb mode. Hence, operation is not persisted, it will only be provisioned.");
/*      */       }
/*      */       
/* 1093 */       SCIMProvisioningConfigManager provisioningConfigManager = SCIMProvisioningConfigManager.getInstance();
/*      */       
/*      */ 
/* 1096 */       String[] provisioningHandlers = SCIMProvisioningConfigManager.getProvisioningHandlers();
/* 1097 */       if ((provisioningHandlers != null) && (provisioningHandlers.length != 0))
/*      */       {
/* 1099 */         for (String provisioningHandler : provisioningHandlers) {
/* 1100 */           Class provisioningClass = Class.forName(provisioningHandler);
/* 1101 */           ProvisioningHandler provisioningAgent = (ProvisioningHandler)provisioningClass.newInstance();
/* 1102 */           provisioningAgent.setProvisioningConsumer(this.consumerName);
/* 1103 */           provisioningAgent.setProvisioningMethod(provisioningMethod);
/* 1104 */           provisioningAgent.setProvisioningObject(provisioningObject);
/* 1105 */           provisioningAgent.setProvisioningObjectType(provisioningObjectType);
/* 1106 */           provisioningAgent.setProperties(properties);
/* 1107 */           this.provisioningThreadPool.submit(provisioningAgent);
/*      */         }
/*      */       } else {
/* 1110 */         throw new CharonException("Server is operating in dumb mode, but no provisioning connectors are registered.");
/*      */       }
/*      */     }
/*      */     catch (ClassNotFoundException e) {
/* 1114 */       throw new CharonException("Error in initializing provisioning handler", e);
/*      */     } catch (InstantiationException e) {
/* 1116 */       throw new CharonException("Error in initializing provisioning handler", e);
/*      */     } catch (IllegalAccessException e) {
/* 1118 */       throw new CharonException("Error in initializing provisioning handler", e);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\MuhammadAli\Desktop\MC\ISCODECOMPARE\wso2is-5.0.0\repository\deployment\server\webapps\wso2.war!\WEB-INF\classes\org\wso2\carbon\identity\scim\provider\impl\SCIMUserManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
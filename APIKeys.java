package com.hhly.member.client;/**
 * Created by dell on 2018/2/26.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hhly.member.client.entity.MemberUser;
import com.hhly.member.client.entity.ResponseT;
import com.hhly.member.client.exception.MemberException;
import com.hhly.member.client.util.Okhttp;
import com.hhly.member.client.util.ReqContextUtil;
import com.hhly.member.client.util.SignUtil;

/**
 * api接口入口
 *
 * @author jiasx
 * @create 2018-02-26 17:25
 **/
public class Member {

    private static class Builder {
        private static final Member INSTANCE = new Member();
    }

    public static final Member builder() {
        return Member.Builder.INSTANCE;
    }

    private Member(){
    }

    /**
     * 获取当前请求的cookie
     * @return
     */
    public String getCookie(){
        return ReqContextUtil.getCookie();
    }

    /**
     * 获取登录后返回的用户token
     * @return
     */
    public String getToken(){
        return ReqContextUtil.getToken();
    }


    /**
     * 用验证码登录，手机/邮箱，服务器不验证
     * @param account 手机/邮箱
     * @param tokenExpireTime token过期时间，单位为秒
     * @return
     */
    public MemberUser loginWithVerifyCode(String account,long tokenExpireTime){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("account","admin");
        paramsMap.put("apiKey","6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2");

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("tokenExpireTime", String.valueOf(tokenExpireTime));
        headersMap.put("sign", SignUtil.sign(Constant.APISECRET,"f6e0a1e2ac41945a9aa7ff8a8aaa0cebc12a3b"));

        String result = Okhttp.builder().post(Constant.LOGIN_WITH_VERIFY_CODE_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }
        ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 登录，手机/邮箱/用户名
     * @param account 手机/邮箱/用户名
     * @param password 密码，md5和PBKDF2(明文，固定盐)，中间用分号隔开
     * @param tokenExpireTime token过期时间，单位为秒
     * @return
     */
    public MemberUser login(String account, String password,long tokenExpireTime){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("account",account);
        paramsMap.put("password",password);
        paramsMap.put("apiKey",Constant.API_KEY);

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("tokenExpireTime", String.valueOf(tokenExpireTime));
        headersMap.put("sign", SignUtil.sign(paramsMap,Constant.API_SECRET));

        String result = Okhttp.builder().post(Constant.LOGIN_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }
        ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 登录，针对微步在线对邮箱、手机登录。
     * 如果用账号登录，那么email，phone为空
     * 如果用邮箱登录，account，phone为空
     * 如果用手机号登录，account，email为空
     * @param account 手机/邮箱/用户名
     * @param accountType 账户类型，1 手机 2 邮箱 3 用户名,如果不传则按照账号匹配
     * @param password 密码，md5和PBKDF2(明文，固定盐)，中间用分号隔开
     * @param tokenExpireTime token过期时间，单位为秒
     * @return
     */
    public MemberUser login(String account, int accountType, String password,long tokenExpireTime){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("account",account);
        paramsMap.put("accountType",String.valueOf(accountType));
        paramsMap.put("password",password);
        paramsMap.put("apiKey",Constant.API_KEY);

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("tokenExpireTime", String.valueOf(tokenExpireTime));
        headersMap.put("sign", SignUtil.sign(paramsMap,Constant.API_SECRET));

        String result = Okhttp.builder().post(Constant.LOGIN_DIFF_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }
        ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 注册
     * @param user 用户对象
     * @return
     */
    public MemberUser register(MemberUser user){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("account",user.getAccount());
        paramsMap.put("email",user.getEmail());
        paramsMap.put("phone",user.getPhone());
        paramsMap.put("password",user.getPassword());
        paramsMap.put("salt",user.getSalt());
        paramsMap.put("nickName",user.getNickName());
        paramsMap.put("sex",String.valueOf(user.getSex()));
        paramsMap.put("image",user.getImage());
        paramsMap.put("name",user.getName());
        paramsMap.put("idNumber",user.getIdNumber());
        paramsMap.put("apiKey",Constant.API_KEY);

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("sign", SignUtil.sign(paramsMap,Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.REGISTER_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }
        ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 注册确认
     * @param id 用户id
     * @param state 1 成功 0 失败
     * @return
     */
    public String registerConfirm(long id,int state){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("id",String.valueOf(id));
        paramsMap.put("state",String.valueOf(state));
        paramsMap.put("apiKey",Constant.API_KEY);

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("sign", SignUtil.sign(paramsMap,Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.REGISTER_CONFIRM_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }
        ResponseT<String> response = JSON.parseObject(result, new TypeReference<ResponseT<String>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 查看登录状态。
     * @param token 用户登录后的令牌
     * @return
     */
    public String checkLoginStatus(String token){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey",Constant.API_KEY);

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("Cookie", "token=" + token);
        headersMap.put("sign", SignUtil.sign(paramsMap,Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.CHECK_LOGIN_STATUS_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<String> response = JSON.parseObject(result, new TypeReference<ResponseT<String>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 根据token获取当前登录的用户信息
     * @param token 用户登录后的令牌
     * @return
     */
    public MemberUser getCurLoginUser(String token){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey",Constant.API_KEY);

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("Cookie", "token=" + token);
        headersMap.put("sign", SignUtil.sign(paramsMap,Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.GET_CUR_LOGIN_USER_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 修改当前用户的token失效时间
     * @param token 用户登录后的令牌
     * @param expireTime 超时时间，单位秒
     * @return
     */
    public String modifyTokenExpireTime(String token,long expireTime){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey",Constant.API_KEY);
        paramsMap.put("expireTime",String.valueOf(expireTime));

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("Cookie", "token=" + token);
        headersMap.put("sign", SignUtil.sign(paramsMap,Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.MODIFY_TOKEN_EXPIRE_TIME_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<String> response = JSON.parseObject(result, new TypeReference<ResponseT<String>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 登出。
     * @param token 用户登录后的令牌
     * @return
     */
    public String loginOut(String token){
        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey",Constant.API_KEY);

        Map<String,String> headersMap = new HashMap<>();
        headersMap.put("Cookie", "token=" + token);
        headersMap.put("sign", SignUtil.sign(paramsMap,Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.LOGINOUT_URL,headersMap,paramsMap);
        if(StringUtils.isEmpty(result)){
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<String> response = JSON.parseObject(result, new TypeReference<ResponseT<String>>(){});
        if(!Constant.SUCCESS_CODE.equals(response.getCode())){
            throw new MemberException(response.getMsg());
        }
        return response.getData();

    }

    /**
     * 修改个人资料
     * 可修改除账号外的所有信息，新的手机号和邮箱号唯一
     *
     * @param token 用户登录后存放在cookie中的令牌
     * @param user  个人资料
     * @return 个人资料
     * @author yangzhen
     */
    public MemberUser updateUserInfo(String token, MemberUser user) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey", Constant.API_KEY);

        paramsMap.put("id", String.valueOf(user.getId()));
        paramsMap.put("phone", user.getPhone());
        paramsMap.put("email", user.getEmail());
        paramsMap.put("nickName", user.getNickName());
        paramsMap.put("image", user.getImage());
        paramsMap.put("sex", String.valueOf(user.getSex()));
        paramsMap.put("name", String.valueOf(user.getName()));
        paramsMap.put("idNumber", user.getIdNumber());
        paramsMap.put("idNumberState", String.valueOf(user.getIdNumberState()));

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Cookie", "token=" + token);
        headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.UPDATE_USER_URL, headersMap, paramsMap);
        if (StringUtils.isEmpty(result)) {
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>() {
        });
        if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 账号关联接口
     * 当前登录的账号可关联手机号或者邮箱匹配的账号
     *
     * @param token       用户登录后存放在cookie中的令牌
     * @param curUserId   当前用户ID
     * @param bindUserIds 绑定的用户ID集合
     * @return 用户信息
     * @author yangzhen
     */
    public String bindAccounts(String token, Long curUserId, String bindUserIds) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey", Constant.API_KEY);

        paramsMap.put("curUserId", String.valueOf(curUserId));
        paramsMap.put("bindUserIds", bindUserIds);

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Cookie", "token=" + token);
        headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.ACCOUNTS_BIND_URL, headersMap, paramsMap);
        if (StringUtils.isEmpty(result)) {
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<String> response = JSON.parseObject(result, new TypeReference<ResponseT<String>>() {
        });
        if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 修改密码
     *
     * @param id               用户ID
     * @param sourcePassword   原密码,md5和PBKDF2(明文，固定盐)，中间用分号隔开，MD5在前，PBDF2在后
     * @param modifiedPassword 新密码
     * @return 用户信息
     * @author yangzhen
     */
    public MemberUser modifyPassword(Long id, String sourcePassword, String modifiedPassword) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey", Constant.API_KEY);

        paramsMap.put("id", String.valueOf(id));
        paramsMap.put("sourcePassword", sourcePassword);
        paramsMap.put("modifiedPassword", modifiedPassword);

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.ACCOUNT_MODIFY_PASSWORD, headersMap, paramsMap);
        if (StringUtils.isEmpty(result)) {
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>() {
        });
        if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 重置密码
     * @param id 用户ID
     * @param newPassword 新密码
     * @return 用户信息
     */
    public MemberUser resetPassword(Long id, String newPassword) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey", Constant.API_KEY);

        paramsMap.put("id", String.valueOf(id));
        paramsMap.put("newPassword", newPassword);

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.ACCOUNT_RESET_PASSWORD, headersMap, paramsMap);
        if (StringUtils.isEmpty(result)) {
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>() {
        });
        if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }
   
    /**
     * 查询用户信息
     *@author wangh
     *@date 2018年3月15日
     * @param account   账户
     * @param accountType  账户类型  1 手机号 2 邮箱 3 账号
     * @return  用户信息
     */
    public List<MemberUser> queryUserInfo( String account,    Integer  accountType) {
    	Map<String, String> paramsMap = new HashMap<>();
    	paramsMap.put("apiKey", Constant.API_KEY);
    	
    	paramsMap.put("account",account);
    	paramsMap.put("accountType", accountType+"");
    	
    	Map<String, String> headersMap = new HashMap<>();
    	headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
    	String result = Okhttp.builder().post(Constant.QUERY_URL, headersMap, paramsMap);
    	if (StringUtils.isEmpty(result)) {
    		throw new MemberException("请求失败，未知原因！");
    	}
    	
        ResponseT<List<MemberUser>> response = JSON.parseObject(result, new TypeReference<ResponseT<List<MemberUser>>>(){});
    	if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
    		throw new MemberException(response.getMsg());
    	}
    	return response.getData();
    }

    /**
     * 根据用户id查询用户详情
     * @param id 用户id
     * @return
     */
    public MemberUser queryUserInfoById(Long id ) {
    	Map<String, String> paramsMap = new HashMap<>();
    	paramsMap.put("apiKey", Constant.API_KEY);
    	
    	paramsMap.put("id",id+"");
    	
    	Map<String, String> headersMap = new HashMap<>();
    	headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
    	String result = Okhttp.builder().post(Constant.QUERY_BY_ID_URL, headersMap, paramsMap);
    	if (StringUtils.isEmpty(result)) {
    		throw new MemberException("请求失败，未知原因！");
    	}
    	
    	 ResponseT<MemberUser> response = JSON.parseObject(result, new TypeReference<ResponseT<MemberUser>>() {
         });
    	if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
    		throw new MemberException(response.getMsg());
    	}
    	return response.getData();
    }
    
    /**
     * 批量查询用户信息
     *
     * @param accounts    账户列表  多个以“，”分隔
     * @param accountType 账户类型  1:手机号  2：邮箱  3： 用户名
     * @return 用户信息
     * @author wangh
     * @date 2018年2月28日
     */
    public Map batchQueryUserInfo(String  accounts ,Integer accountType) {
    	Map<String, String> paramsMap = new HashMap<>();
    	paramsMap.put("apiKey", Constant.API_KEY);
    	
    	paramsMap.put("accountType",accountType+"");
    	paramsMap.put("accounts", accounts);
    	
    	Map<String, String> headersMap = new HashMap<>();
    	headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
    	String result = Okhttp.builder().post(Constant.BATCH_QUERY_URL, headersMap, paramsMap);
    	if (StringUtils.isEmpty(result)) {
    		throw new MemberException("请求失败，未知原因！");
    	}
    	
    	ResponseT<Map> response = JSON.parseObject(result, new TypeReference<ResponseT<Map>>() {
        });
    	if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
    		throw new MemberException(response.getMsg());
    	}
    	return response.getData();
    }
    
    /**
     * 查询用户是否存在
     *
     * @param account 用户名
     *  @param accountType       账户类型  1:手机号  2：邮箱  3： 用户名
     * @return
     * @author wangh
     * @date 2018年2月28日
     */
    public boolean exsistUser( String account ,Integer accountType) {
    	Map<String, String> paramsMap = new HashMap<>();
    	paramsMap.put("apiKey", Constant.API_KEY);
    	
    	paramsMap.put("account",account);
    	paramsMap.put("accountType", accountType+"");
    	
    	Map<String, String> headersMap = new HashMap<>();
    	headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
    	String result = Okhttp.builder().post(Constant.EXSIST_URL, headersMap, paramsMap);
    	if (StringUtils.isEmpty(result)) {
    		throw new MemberException("请求失败，未知原因！");
    	}
    	
    	ResponseT<Boolean> response = JSON.parseObject(result,  new TypeReference<ResponseT<Boolean>>() {
        });
    	if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
    		throw new MemberException(response.getMsg());
    	}
    	return response.getData();
    }

    /**
     * 重置密码（根据手机号码）
     * @param phone 手机号码
     * @param newPassword 新密码
     * @return 用户信息列表
     */
    public List<MemberUser> resetPasswordByPhone(String phone, String newPassword) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey", Constant.API_KEY);

        paramsMap.put("phone", String.valueOf(phone));
        paramsMap.put("newPassword", newPassword);

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.ACCOUNT_RESET_PASSWORD_BY_PHONE, headersMap, paramsMap);
        if (StringUtils.isEmpty(result)) {
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<List<MemberUser>> response = JSON.parseObject(result, new TypeReference<ResponseT<List<MemberUser>>>() {
        });
        if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }

    /**
     * 重置密码（根据邮箱）
     * @param email 邮箱
     * @param newPassword 新密码
     * @return 用户信息列表
     */
    public List<MemberUser> resetPasswordByEmail(String email, String newPassword) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("apiKey", Constant.API_KEY);

        paramsMap.put("email", String.valueOf(email));
        paramsMap.put("newPassword", newPassword);

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("sign", SignUtil.sign(paramsMap, Constant.API_SECRET));
        String result = Okhttp.builder().post(Constant.ACCOUNT_RESET_PASSWORD_BY_EMAIL, headersMap, paramsMap);
        if (StringUtils.isEmpty(result)) {
            throw new MemberException("请求失败，未知原因！");
        }

        ResponseT<List<MemberUser>> response = JSON.parseObject(result, new TypeReference<ResponseT<List<MemberUser>>>() {
        });
        if (!Constant.SUCCESS_CODE.equals(response.getCode())) {
            throw new MemberException(response.getMsg());
        }
        return response.getData();
    }
}

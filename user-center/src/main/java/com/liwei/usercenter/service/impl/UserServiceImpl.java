package com.liwei.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liwei.usercenter.common.ErrorCode;
import com.liwei.usercenter.exception.BusinessException;
import com.liwei.usercenter.model.domain.User;
import com.liwei.usercenter.service.UserService;
import com.liwei.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.liwei.usercenter.common.ErrorCode.NULL_ERROR;
import static com.liwei.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author Macro-Laplace
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2022-12-04 18:43:58
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    /**
     * 盐
     */
    private static final  String SALT="liwei";


    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        //校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode))
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");

        if(userAccount.length()<4)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号长度小于4");

        if(userPassword.length()<8)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码长度小于8");

        if(planetCode.length()>10)
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球号码长度大于10");
        //userAccount不能包含特殊字符
        String validPattern ="[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find())
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号包含特殊字符");
        //账户不可重复,需要查数据库的放到后面做
        QueryWrapper<User> userQW = new QueryWrapper<>();
        userQW.eq("userAccount",userAccount);
        long count = userMapper.selectCount(userQW);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        //星球不可重复
        QueryWrapper<User> planetCodeQW = new QueryWrapper<>();
        planetCodeQW.eq("planetCode",planetCode);
        count = userMapper.selectCount(planetCodeQW);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"星球编号重复");
        }
        //对密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);

        //User那边id是Long包装类，这边返回long,所以判断一下防止拆箱失败
        if(!saveResult)
            return -1;
        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword,HttpServletRequest request) {
        //校验
        if(StringUtils.isAnyBlank(userAccount,userPassword))
            return null;

        if(userAccount.length()<4)
            return null;

        if(userPassword.length()<8)
            return null;
        //userAccount不能包含特殊字符
        String validPattern ="[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find())
            return null;

        //对密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查用户
        QueryWrapper<User> userQW = new QueryWrapper<>();
        userQW.eq("userAccount",userAccount);
        userQW.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(userQW);
        //用户不存在
        if(user == null){
            StringBuilder logInfo = new StringBuilder().append("userAccount: ")
                    .append(userAccount).append(" does not matcher").append(userPassword);
            log.info(logInfo.toString());
            return null;
        }
        //脱敏
        User safetyUser = getSafetyUser(user);
        //记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safetyUser);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if(originUser == null)
            return null;
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setUserRole(originUser.getUserRole());

        return safetyUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}





package com.github.wxiaoqi.security.dzsw.sys.vo;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class JwtUser implements UserDetails {
    private Integer id;
    private String name;
    private String account; // 用户名
    private String sysName;
    private String status;
    //  private String password;
    private String phone;
    private String targetSystem;
    private Integer isVerify;
    private String photo;
    private Date regTime;
    private Integer isDeleted;
    //权限
    private Collection<? extends GrantedAuthority> authorities;

    public JwtUser() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    // 账户是否未过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账户是否未锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 密码是否未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账户是否激活
    @Override
    public boolean isEnabled() {
        return true;
    }

    //获得当前登陆用户对应的对象。
    public static JwtUser getCurUser() {
        JwtUser userDetails = null;
        Object obj = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        if (obj instanceof JwtUser) {
            userDetails = (JwtUser) obj;
        }
        return userDetails;
    }

}

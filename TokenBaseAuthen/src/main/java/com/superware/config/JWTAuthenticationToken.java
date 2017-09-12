package com.superware.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JWTAuthenticationToken extends AbstractAuthenticationToken{
    private static final long serialVersionUID = 1L;
    private final Object principal;
 
    private Collection<GrantedAuthority>  authorities;
    /*@Autowired
    private JwtUtil jwtUtil;*/
    private JwtUtil jwtUtil = new JwtUtil();
    public JWTAuthenticationToken( String jwtToken) {
        super(null);
        super.setAuthenticated(true); // must use super, as we override
        //JWTParser parser = new JWTParser(jwtToken);
         
        //this.principal=parser.getSub();
        UserByToken userByToken = jwtUtil.parseToken(jwtToken);
        principal = userByToken.getId();
         
        this.setDetailsAuthorities(userByToken);
    }
 
    @Override
    public Object getCredentials() {
        return "";
    }
 
    @Override
    public Object getPrincipal() {
        return principal;
    }
    
    private void setDetailsAuthorities(UserByToken token) {
        //String username = principal.toString();
        SpringUserDetailsAdapter adapter = new SpringUserDetailsAdapter(token);
        super.setDetails(adapter);
        authorities=(Collection<GrantedAuthority>) adapter.getAuthorities();    
    }
 
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }
}

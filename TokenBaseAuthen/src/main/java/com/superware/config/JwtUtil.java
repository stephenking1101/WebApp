package com.superware.config;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Andrii on 14.03.2016.
 */
@Component
public class JwtUtil {

    //@Value("${jwt.secret}")
    private String secret="test";

    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     *
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or null if a token is invalid.
     */
    public UserByToken parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            UserByToken u = new UserByToken();
            u.setLogin(body.getSubject());
            u.setId(Integer.valueOf((String) body.get(UserByToken.USER_ID)));
            u.setPassword((String)body.get(UserByToken.PASSWORD));
            u.setAuthorId(Integer.valueOf((String) body.get(UserByToken.AUTHOR_ID)));
            u.setPublisherId(Integer.valueOf((String) body.get(UserByToken.PUBLISHER_ID)));
            boolean isActive = Boolean.valueOf((String)body.get(UserByToken.IS_ACTIVE));
            u.setActive(isActive);
            ObjectMapper mapper = new ObjectMapper();
            RoleEntity roleEntity = mapper.convertValue(body.get(UserByToken.ROLE),RoleEntity.class);
            u.setRole(roleEntity);
            long time = Long.valueOf((String)body.get(UserByToken.DATE));
            Date tokenDate = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTime(tokenDate);
            c.add(Calendar.HOUR, 1);
            Date maxLive = c.getTime();
            Date now = new Date();
            if (now.after(maxLive))
                return null;
            return u;

        } catch (JwtException e) {
            return null;
        } catch (ClassCastException e){
            return null;
        }
    }

    public String generateToken(UserByToken u) {
        Claims claims = Jwts.claims().setSubject(u.getLogin());
        claims.put(UserByToken.USER_ID, u.getId() + "");
        claims.put(UserByToken.ROLE, u.getRoleEntity());
        claims.put(UserByToken.PASSWORD,u.getPassword());
        claims.put(UserByToken.AUTHOR_ID, u.getAuthorId() + "");
        claims.put(UserByToken.PUBLISHER_ID, u.getPublisherId() + "");
        claims.put(UserByToken.DATE,System.currentTimeMillis()+"");
        if (u.isActive())
            claims.put(UserByToken.IS_ACTIVE,"true");
        else
            claims.put(UserByToken.IS_ACTIVE,"false");

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}

package com.superware.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SpringUserDetailsAdapter implements UserDetails{

	private static final long serialVersionUID = 1L;
	private UserByToken target;

    public SpringUserDetailsAdapter(UserByToken target) {
        this.target = target;
    }

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authoritySet = new HashSet<GrantedAuthority>();
        for (PermissionEntity perm:target.getRole().getPermissions()){
            authoritySet.add(new SpringGrantedAuthorityAdapter(perm));
        }
        //authoritySet.add(new SpringGrantedAuthorityAdapter(target.getRole()));
        return authoritySet;
    }

    public UserByToken getUser() {
        return target;
    }

    public String getPassword() {
        return target.getPassword();
    }

    public String getUsername() {
        return target.getLogin();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return target.isActive();
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return target.isActive();
    }

    @Override
    public String toString() {
        return target.getLogin();
    }

    public static class SpringGrantedAuthorityAdapter implements GrantedAuthority, Serializable {

		private static final long serialVersionUID = 1L;
		/*private RoleEntity target;
        public SpringGrantedAuthorityAdapter(RoleEntity role) {
            this.target = role;
        }*/

        private PermissionEntity target;

        public SpringGrantedAuthorityAdapter(PermissionEntity role) {
            this.target = role;
        }

        public String getAuthority() {
            return target.getName().toString();
        }

        @Override
        public String toString() {
            return target.getName().toString();
        }

    }
}
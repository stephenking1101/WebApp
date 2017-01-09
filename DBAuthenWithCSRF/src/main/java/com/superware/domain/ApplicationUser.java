package com.superware.domain;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * <b> We use this class to represent a user to the application. The information should be pulled from LDAP and populated. </b>
 * </p>
 */
public class ApplicationUser implements UserDetails {

    /**
     * <p>
     * <b>Ditto </b>
     * </p>
     */
    private static final long serialVersionUID = 2225844057439002042L;

    private String id;

    private String username;

    private String password;

    private long expires;

    private boolean accountExpired = false;

    private boolean accountLocked = false;

    private boolean credentialsExpired = false;

    private boolean accountEnabled = true;

    private String newPassword;
    
    private String internalEmail;
    
    private String externalEmail;
    
    private String jobLevel;
    
    private String gender;
    
    private String conutry;
    
    private String jobTitle;

    private Collection<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;

    private List<String> roles;
    
    public ApplicationUser() {}

    public ApplicationUser(final String username) {
        this.username = username;
    }

    public ApplicationUser(final String username, final Date expires) {
        this.username = username;
        this.expires = expires.getTime();
    }


    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }

    @JsonProperty
    public void setPassword(final String password) {
        this.password = password;
    }

    @JsonIgnore
    public String getNewPassword() {
        return this.newPassword;
    }

    @JsonProperty
    public void setNewPassword(final String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
    	this.authorities = new ArrayList<GrantedAuthority>();
    	this.authorities.addAll(authorities);
	}

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.accountEnabled;
    }

    public long getExpires() {
        return this.expires;
    }

    public void setExpires(final long expires) {
        this.expires = expires;
    }

    public String getInternalEmail() {
		return internalEmail;
	}

	public void setInternalEmail(String internalEmail) {
		this.internalEmail = internalEmail;
	}

	public String getExternalEmail() {
		return externalEmail;
	}

	public void setExternalEmail(String externalEmail) {
		this.externalEmail = externalEmail;
	}

	public String getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(String jobLevel) {
		this.jobLevel = jobLevel;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getConutry() {
		return conutry;
	}

	public void setConutry(String conutry) {
		this.conutry = conutry;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getUsername();
    }

}

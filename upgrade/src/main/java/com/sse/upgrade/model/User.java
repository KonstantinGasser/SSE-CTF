package com.sse.upgrade.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;

public class User implements UserDetails {
    private Collection<Role> roles;
    private String username;
    private String password;
    private int id;

    public User(int id, String username, String pwhash, Role role) {
        this.id = id;
        this.roles = new LinkedList<>();
        this.roles.add(role);
        this.username = username;
        this.password = pwhash;
    }

    public User(int id, String username, String pwHash, Collection<Role> roles) {
        this.password = pwHash;
        this.id = id;
        this.roles = roles;
        this.username = username;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public int getId() { return id; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Role implements GrantedAuthority {
        STUDENT,
        PROFESSOR,
        PRUEFUNGSAMT;

        @Override
        public String getAuthority() {
            return name();
        }
    }

    @Override
    public String toString() {
        return "Name: "+ this.username + " | Role: "+ this.roles.toString();
    }
}

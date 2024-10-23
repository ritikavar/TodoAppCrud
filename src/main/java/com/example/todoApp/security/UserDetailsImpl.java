package com.example.todoApp.security;

import com.example.todoApp.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private String email;

  @JsonIgnore
  private String password;

  // You can add roles or authorities if needed
  private Collection<? extends GrantedAuthority> authorities;

  public UserDetailsImpl(Long id, String name, String email, String password,
                         Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  /**
   * Builds a UserDetailsImpl object from a User entity.
   * @param user the User entity.
   * @return a UserDetailsImpl object.
   */
  public static UserDetailsImpl build(User user) {
    // Example: Assign a default role. Adjust as per your requirements.
    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    return new UserDetailsImpl(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            authorities);
  }

  // Getters
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public String getUsername() {
    return email; // Using email as the username
  }

  @Override
  public String getPassword() {
    return password;
  }

  // Granted Authorities
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  // Account Status Flags
  @Override
  public boolean isAccountNonExpired() {
    return true; // Adjust as needed
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // Adjust as needed
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // Adjust as needed
  }

  @Override
  public boolean isEnabled() {
    return true; // Adjust as needed
  }

  // Optional: Override equals and hashCode for better security practices
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserDetailsImpl user = (UserDetailsImpl) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

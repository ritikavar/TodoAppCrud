package com.example.todoApp.security;



import com.example.todoApp.model.User;
import com.example.todoApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Locates the user based on the username (in this case, email).
   * @param email the email identifying the user whose data is required.
   * @return a fully populated UserDetails object (never null).
   * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority.
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // Fetch user from the database using email
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

    // Convert your User entity to Spring Security's UserDetails
    return UserDetailsImpl.build(user);
  }
}


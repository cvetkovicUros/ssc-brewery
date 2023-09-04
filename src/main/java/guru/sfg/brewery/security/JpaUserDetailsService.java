package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        guru.sfg.brewery.domain.security.User user = userRepository.findByUsername(username).orElseThrow(() -> {
            return new UsernameNotFoundException(String.format("Username %s not found", username));
        });
        return new User(user.getUsername(),user.getPassword(),user.getEnabled(),user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),user.getAccountNonLocked(),mapToSpringAuthorities(user.getAuthorities()));
    }

    private Collection<? extends GrantedAuthority> mapToSpringAuthorities(Set<Authority> authorities) {
        if(CollectionUtils.isEmpty(authorities)){
            return Collections.emptySet();
        }
        return authorities.stream().map(Authority::getRole).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }
}

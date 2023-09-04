package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0){
            log.info("DB connection established");
            loadData();
        }
    }

    private void loadData() {
        Authority admin = authorityRepository.save(Authority.builder().role("ADMIN").build());
        Authority user = authorityRepository.save(Authority.builder().role("USER").build());
        Authority customer = authorityRepository.save(Authority.builder().role("CUSTOM").build());

        userRepository.save(User.builder().username("uros").password(passwordEncoder.encode("urospass")).authority(admin).build());
        userRepository.save(User.builder().username("user").password(passwordEncoder.encode("password")).authority(user).build());
        userRepository.save(User.builder().username("scott").password(passwordEncoder.encode("tiger")).authority(customer).build());

        log.info("Users loaded: "+userRepository.count());
    }
}

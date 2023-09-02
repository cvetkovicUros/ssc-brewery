package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {


    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager){
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }


    /** In Memory Auth **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http.addFilterBefore(restUrlAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeRequests(authorize -> {
                    authorize.antMatchers("/","/webjars/**","/beers/find").permitAll()
                            .antMatchers("/beers*").permitAll()
                            .antMatchers(HttpMethod.GET,"/api/v1/beer/**").permitAll() // ogranicava se samo na get pozive
                            .mvcMatchers(HttpMethod.GET,"/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }
/** User Details Service **/
    /*@Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("uros")
                .password("urospass")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin,user);
    }*/

    /** NoOp **/

    /*@Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance(); //spring po default-u uzima instancu i uvodi u context
    }*/


   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("uros")
                .password("urospass")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("password")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("tiger")
                .roles("CUSTOMER");
    }*/

    /** LDAP **/
  /*  @Bean
    PasswordEncoder passwordEncoder(){
        return new LdapShaPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("uros")
                .password("{SSHA}VQgsu3yYyZW5vJShtolZmlK2AmkhGPVXJ+rKWw==")
                .roles("CUSTOMER");
    }*/

    /**SHA-256**/
    /*@Bean
    PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("uros")
                .roles("ADMIN")
                .password("3e559f1f8fad3cc3093a309b594edaca7e9db4d80a12baaae324abb21ab077b360d98cea54946f86");
    }*/

    /**Bcrypt**/
    /*@Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("uros")
                .roles("ADMIN")
                .password("$2a$12$cdR9fh8.B4MXS08FMlfWDuLrXJe7Pzo7dgcorkUx9SekRUiToA07S");
    }*/

    /**Delegating password encoder **/
    /*@Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("uros").password("{bcrypt}$2a$10$c.3UThreJplNwfUqI0iUo.EEbVNPf2koeafQ2R.n2gFK/kxjZJFs6").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("user").password("{sha256}ae92c62cf34af5bfa2157874afedc087614397c6cb49ec5c663ad096a1bc08b2aa7a962f872f3da4").roles("USER");
        auth.inMemoryAuthentication().withUser("scott").password("{ldap}{SSHA}eI0/tNWJCa6G612n7ZYhXT0bgfMH0OKi069wSg==").roles("CUSTOMER");
    }*/

    /** Custom delegating password encoder
     *
     * Created custom encoder configuration in ssc-brewery/src/main/java/guru/sfg/brewery/security/SfgPasswordEncoderFactories.java
     */

    @Bean
    PasswordEncoder passwordEncoder(){
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("uros").password("{bcrypt}$2a$10$c.3UThreJplNwfUqI0iUo.EEbVNPf2koeafQ2R.n2gFK/kxjZJFs6").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("user").password("{sha256}ae92c62cf34af5bfa2157874afedc087614397c6cb49ec5c663ad096a1bc08b2aa7a962f872f3da4").roles("USER");
        auth.inMemoryAuthentication().withUser("scott").password("{ldap}{SSHA}eI0/tNWJCa6G612n7ZYhXT0bgfMH0OKi069wSg==").roles("CUSTOMER");
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("uros").password("{bcrypt10}$2a$10$c.3UThreJplNwfUqI0iUo.EEbVNPf2koeafQ2R.n2gFK/kxjZJFs6").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("user").password("{sha256}ae92c62cf34af5bfa2157874afedc087614397c6cb49ec5c663ad096a1bc08b2aa7a962f872f3da4").roles("USER");
        auth.inMemoryAuthentication().withUser("scott").password("{bcrypt15}$2a$15$gYHViD.2BRegysNhl6KnZOGtmeQe6UvE6eaTj2uyvzUDxPRBWNG76").roles("CUSTOMER");
    }



}

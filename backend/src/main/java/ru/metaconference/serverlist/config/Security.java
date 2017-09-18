package ru.metaconference.serverlist.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import ru.metaconference.serverlist.data.entity.Group;
import ru.metaconference.serverlist.data.entity.RememberMeToken;
import ru.metaconference.serverlist.data.repo.RememberMeTokenRepository;
import ru.metaconference.serverlist.data.repo.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by user on 2017-09-03.
 */
@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;
    private final RememberMeTokenRepository rememberMeTokenRepository;

    @Autowired
    public Security(UserRepository userRepository, RememberMeTokenRepository rememberMeTokenRepository) {
        this.userRepository = userRepository;
        this.rememberMeTokenRepository = rememberMeTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder()).and()
                .authenticationProvider(preAuthProvider());
    }

    private AuthenticationProvider preAuthProvider() {
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(token -> new User(token.getName(), String.valueOf(token.getCredentials()), token.getAuthorities()));
        return preAuthenticatedAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DelegatingSecurityContextExecutorService delegatingSecurityContextExecutor() throws Exception {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("admin"));

        PreAuthenticatedAuthenticationToken authToken = new PreAuthenticatedAuthenticationToken(new User("root", "", authorities), "root", authorities);
        Authentication auth = authenticationManager().authenticate(authToken);
        context.setAuthentication(auth);
        return new DelegatingSecurityContextExecutorService(Executors.newSingleThreadExecutor(), context);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .anonymous().authorities("anonymous").and()
                .rememberMe().alwaysRemember(true).tokenRepository(rememberMeTokenRepository()).and()
                .httpBasic().and()
                .csrf().ignoringAntMatchers("/api/**");
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                ru.metaconference.serverlist.data.entity.User user = userRepository.findByName(username);
                if (user == null) {
                    throw new UsernameNotFoundException(username);
                }
                List<GrantedAuthority> authorities = new ArrayList<>();
                for (Group group : user.getGroups()) {
                    authorities.add(new SimpleGrantedAuthority(group.getName()));
                }
                return new User(user.getName(), user.getPasswordHash(), authorities);
            }
        };
    }

    private PersistentTokenRepository rememberMeTokenRepository() {
        return new PersistentTokenRepository() {
            @Override
            public void createNewToken(PersistentRememberMeToken token) {
                rememberMeTokenRepository.save(new RememberMeToken(token));
            }

            @Override
            public void updateToken(String series, String tokenValue, Date lastUsed) {
                RememberMeToken token = rememberMeTokenRepository.findOne(series);
                token.setValue(tokenValue);
                token.setDate(lastUsed);
            }

            @Override
            public PersistentRememberMeToken getTokenForSeries(String seriesId) {
                RememberMeToken token = rememberMeTokenRepository.findOne(seriesId);
                return new PersistentRememberMeToken(token.getUsername(), token.getSeries(), token.getValue(), token.getDate());
            }

            @Override
            public void removeUserTokens(String username) {
                rememberMeTokenRepository.deleteAllByUsername(username);
            }
        };
    }
}

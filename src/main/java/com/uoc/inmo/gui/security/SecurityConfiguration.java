package com.uoc.inmo.gui.security;

import com.uoc.inmo.query.entity.user.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity 
@Configuration 
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_PROCESSING_URL = "/ui/login";
    private static final String LOGIN_FAILURE_URL = "/ui/login?error";
    private static final String LOGIN_URL = LOGIN_PROCESSING_URL;
    
    public static final String LOGOUT_PROCESSING_URL = "/ui/logout";
    private static final String LOGOUT_SUCCESS_URL = LOGIN_PROCESSING_URL;
    private static final String INDEX_URL = "/ui/";

    private final UserDetailsService userDetailsService;

    @Autowired
	public SecurityConfiguration(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

    @Autowired
	private PasswordEncoder passwordEncoder;

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    /**
	 * Registers our UserDetailsService and the password encoder to be used on login attempts.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        System.out.println("User password encoded: "+passwordEncoder.encode("user"));
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  
            .requestCache().requestCache(new CustomRequestCache()) 
            
            // Restrict access to our application.
            .and().authorizeRequests().antMatchers("/ui/signup").permitAll()

            // Allow all flow internal requests.
            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

            // Allow all requests by logged in users.
            .anyRequest().hasAnyAuthority(Role.getAllRoles())
            .and().formLogin()  
            .loginPage(LOGIN_URL).permitAll()
            .loginProcessingUrl(LOGIN_PROCESSING_URL)
            .successForwardUrl(INDEX_URL)
            .failureUrl(LOGIN_FAILURE_URL)
            .and().logout().logoutUrl(LOGOUT_PROCESSING_URL)
            .logoutSuccessUrl(LOGOUT_SUCCESS_URL); 

    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
            "/VAADIN/**",
            "/favicon.ico",
            "/robots.txt",
            "/manifest.webmanifest",
            "/sw.js",
            "/offline.html",
            "/icons/**",
            "/images/**",
            "/styles/**",
            "/h2-console/**");
    }
}

package com.uoc.inmo.gui.security;

import com.uoc.inmo.gui.GuiConst;
import com.uoc.inmo.query.entity.user.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
	}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  
            .requestCache().requestCache(new CustomRequestCache()) 
            
            // Restrict access to our application.
            .and().authorizeRequests().antMatchers("/ui/signup", "/ui/inmuebles", "/ui/inmuebles/*", "/swagger-ui/*", "/api/*").permitAll()

            // Allow all flow internal requests.
            .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

            // Allow all requests by logged in users.
            .anyRequest().hasAnyAuthority(Role.getAllRoles())
            .and().formLogin()  
            .loginPage(GuiConst.LOGIN_URL).permitAll()
            .loginProcessingUrl(GuiConst.LOGIN_PROCESSING_URL)
            .successForwardUrl(GuiConst.INDEX_URL)
            .failureUrl(GuiConst.LOGIN_FAILURE_URL)
            .and().logout().logoutUrl(GuiConst.LOGOUT_PROCESSING_URL)
            .logoutSuccessUrl(GuiConst.LOGOUT_SUCCESS_URL); 

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
            "/js/**",
            "/images/**",
            "/styles/**",
            "/h2-console/**");
    }
}

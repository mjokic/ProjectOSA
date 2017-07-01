package sf282015.osa.projectOSA.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import sf282015.osa.projectOSA.security.JWTAuthenticationFilter;
import sf282015.osa.projectOSA.security.JWTLoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.jdbcAuthentication()
						.usersByUsernameQuery("select email,password,active from user where email=?")
						.authoritiesByUsernameQuery("select email,role from user where email=?")
						.dataSource(dataSource);
		
	}
	
	
	@Override
	public void configure(HttpSecurity http) throws Exception{
		http.csrf().disable().authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/upload/**").permitAll()
		.antMatchers("/images/**").permitAll()
		.antMatchers(HttpMethod.POST, "/login").permitAll()
		.antMatchers(HttpMethod.POST, "/register").permitAll()
		.antMatchers("/users/**").hasAuthority("ADMIN")
		
		.antMatchers(HttpMethod.DELETE, "/bids/**").hasAuthority("ADMIN")
		.antMatchers(HttpMethod.PUT, "/bids/**").hasAuthority("ADMIN")
		.anyRequest().authenticated()
        .and()
        // We filter the /login requests
        .addFilterBefore(
        		new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
        
        // And filter other requests to check the presence of JWT in header
        .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		http.headers().cacheControl();
		
	}
	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    // Create a default account
//    auth.inMemoryAuthentication()
//        .withUser("admin")
//        .password("admin")
//        .roles("ADMIN")
//        .and()
//        .withUser("test")
//        .password("test")
//        .roles("USER");
//  }
	
}

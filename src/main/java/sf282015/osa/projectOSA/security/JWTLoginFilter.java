package sf282015.osa.projectOSA.security;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import sf282015.osa.projectOSA.service.UserService;

@ComponentScan("sf282015.osa.projectOSA")
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

	UserService userService;
	
	public JWTLoginFilter(String url, AuthenticationManager authManager) {
	    super(new AntPathRequestMatcher(url));
	    setAuthenticationManager(authManager);
	  }
	

	  @Override
	  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
	      throws AuthenticationException, IOException, ServletException {
		  
	    AccountCredentials creds = 
	    		new ObjectMapper().readValue(req.getInputStream(), AccountCredentials.class);
	    
//	    System.out.println(creds);
	    
	    AuthenticationManager authManager = getAuthenticationManager();
	    
	    UsernamePasswordAuthenticationToken authToken = 
	    		new UsernamePasswordAuthenticationToken(
	    				creds.getEmail(), creds.getPassword(), Collections.emptyList());
	    
	    authToken.setDetails(creds.getDevice_token());
	    Authentication auth = authManager.authenticate(authToken);
	    
//	    System.err.println("AUTORITIES:");
//	    System.out.println(auth.getAuthorities());
	    
	    return auth;

	  }

	  @Override
	  protected void successfulAuthentication(
			  HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) 
					  throws IOException, ServletException {
		  
		  // izmuljano nesto jer se ne moze autowire-ovati service u filter klasi
		  // ako se pokvari nesto pogledaj ispod
		  if(userService==null){
	            ServletContext servletContext = req.getServletContext();
	            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	            userService = webApplicationContext.getBean(UserService.class);
	        }

//		  System.out.println("PRINCIPAL: " + auth.getPrincipal());
	      SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		  Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		  
		  String authority = authorities.toString().substring(1, authorities.toString().length()-1);
		  String email = auth.getName();
		  String device_token = (String) auth.getDetails();
		  
		  userService.addDeviceToken(email, device_token);
		  
	    TokenAuthenticationService.addAuthentication(res, auth.getName(), authority);
	  }
	
}

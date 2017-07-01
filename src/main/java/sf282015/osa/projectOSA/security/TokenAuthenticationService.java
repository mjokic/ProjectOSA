package sf282015.osa.projectOSA.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sf282015.osa.projectOSA.service.UserService;

public class TokenAuthenticationService {
	
	  static final long EXPIRATIONTIME = 864_000_000; // 10 days
	  static final String SECRET = "cVTZMcLOZ1w7RWTVWdKl";
	  static final String TOKEN_PREFIX = "Bearer";
	  static final String HEADER_STRING = "Authorization";
	  
	
	  static void addAuthentication(HttpServletResponse res, String email, String autho) {
		  String JWT = Jwts.builder()
	        .setSubject(email)
	        .claim("role", autho)
	        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	        .signWith(SignatureAlgorithm.HS512, SECRET)
	        .compact();
		  
	    res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	    
	  }
	
	  static Authentication getAuthentication(HttpServletRequest request) {
	    String token = request.getHeader(HEADER_STRING);
	    if (token != null) {
	      // parse the token.
//		  String user = Jwts.parser()
//		      .setSigningKey(SECRET)
//		      .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
//	          .getBody()
//	          .getSubject();
	    	
	    	Jws<Claims> claims = Jwts.parser()
	  		      .setSigningKey(SECRET)
	  		      .parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
	    	
	    	Claims body = claims.getBody();
	    	String authority = body.get("role").toString();
	    	
	    	String user = body.getSubject();
		  
		    Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		    authorities.add(new SimpleGrantedAuthority(authority));
		  
		  UsernamePasswordAuthenticationToken upAuthToken = null; 
		  if(user != null){
//			  upAuthToken = new UsernamePasswordAuthenticationToken(user, null, emptyList());
			  upAuthToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
		  }
		  return upAuthToken;
		  
	    }
	    return null;
	  }

}

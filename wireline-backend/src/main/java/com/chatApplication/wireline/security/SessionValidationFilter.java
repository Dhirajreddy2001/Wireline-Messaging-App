package com.chatApplication.wireline.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
@Order(1)
public class SessionValidationFilter extends OncePerRequestFilter {

   private final StringRedisTemplate redisTemplate;

   public SessionValidationFilter(StringRedisTemplate redisTemplate) {
       
    this.redisTemplate = redisTemplate;
    
   }
   System.Logger logger = System.getLogger(SessionValidationFilter.class.getName());

    @Override
    protected void doFilterInternal( HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {
                                        
                    String path = request.getRequestURI();
                    if (path.startsWith("/api/auth/") ||
                        path.startsWith("/api/users") ||
                        path.startsWith("/swagger") ||
                        path.startsWith("/v3/api-docs") ||
                        path.startsWith("/swagger-ui") ||
                        path.startsWith("/webjars/") || 
                        path.startsWith("/favicon.ico") || 
                        path.endsWith(".html") || 
                        path.endsWith(".css") || 
                        path.endsWith(".js")) {

                        // Skip filter for public, swagger, and static paths
                        filterChain.doFilter(request, response);
                        return;
                    }


            Cookie[] cookies = request.getCookies();

            if(cookies == null)
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Misssing session");
                return;
            }


            String sessionId = Arrays.stream(cookies)
                                            .filter(cookie -> cookie.getName().equals("sessionId"))
                                            .map(Cookie::getValue)
                                            .findFirst()
                                            .orElse(null);

              
                                            
            if(sessionId == null )
            {
            
                logger.log(System.Logger.Level.DEBUG, " sessionId not null [FROM SESSION Validator] Session ID: " + sessionId);   
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing session");
                return;
            }

            try{
                if(!redisTemplate.hasKey(sessionId))
                {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired session");
                    return;
                }

                Authentication authentication = new UsernamePasswordAuthenticationToken(sessionId, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }catch(Exception e)
            {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Session Validation Error");
                return;

            }

            filterChain.doFilter(request, response);

         }                               
    
}

package com.example.maintenance.config;

import com.example.maintenance.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    public JwtAuthenticationFilter(JwtService jwtService)
    {
        this.jwtService=jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
   throws ServletException, IOException
    {
    String authHeader=request.getHeader("Authorization");
   if(authHeader == null || !authHeader.startsWith("Bearer "))
    {
    filterChain.doFilter(request, response);
    return;
      }
     String token=authHeader.substring(7);
     try{
     if(jwtService.isTokenValid(token))
     {
         Long userId= jwtService.extractUserId(token);
         String userRoll= jwtService.extractRole(token);

         SimpleGrantedAuthority authority=new SimpleGrantedAuthority("ROLE_"+userRoll);
         UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken( userId, null, List.of(authority));
         SecurityContextHolder.getContext().setAuthentication(authenticationToken);


     }

     }catch (Exception ex){
         SecurityContextHolder.clearContext();
     };
filterChain.doFilter(request, response);
    }
}

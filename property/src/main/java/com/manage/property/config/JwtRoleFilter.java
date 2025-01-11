package com.manage.property.config;

import com.manage.property.util.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtRoleFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        if (header != null && header.startsWith("Bearer ")
                && !requestURI.contains("/api/v1/login")) {
            try {
                String token = header.substring(7);
                List<String> roles = jwtUtils.extractRoles(token);
                String username = jwtUtils.extractUsername(token);

                if(jwtUtils.validateToken(token, username)) {
                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, "",
                            roles.stream().map(SimpleGrantedAuthority::new).toList());

                    PreAuthenticatedAuthenticationToken authentication =
                            new PreAuthenticatedAuthenticationToken(userDetails, "", userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("token expired");
                    return;
                }



            } catch (ExpiredJwtException | SignatureException | MalformedJwtException |
                     UnsupportedJwtException e) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(e.getMessage());
                return;
            }

        }

        filterChain.doFilter(request, response);
    }
}
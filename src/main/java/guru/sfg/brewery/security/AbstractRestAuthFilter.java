package guru.sfg.brewery.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public abstract class AbstractRestAuthFilter extends AbstractAuthenticationProcessingFilter {
    protected AbstractRestAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if(logger.isDebugEnabled()){
            logger.debug("Request is to process authentication");
        }

        try {
            Authentication authResult = attemptAuthentication(request,response);
            if(Objects.nonNull(authResult)){
                successfulAuthentication(request,response,chain,authResult);
            }else {
                chain.doFilter(request,response);
            }
        } catch (AuthenticationException ex){
            logger.error("Authentication failed",ex);
            unsuccessfulAuthentication(request,response,ex);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String username = getUsername(httpServletRequest);
        String password = getPassword(httpServletRequest);

        logger.debug("Authenticating User: "+ username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username,password);

        if(!StringUtils.isEmpty(username)){
            return this.getAuthenticationManager().authenticate(token);
        }else{
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if(logger.isDebugEnabled()){
            logger.debug("Authentication success. Updating SpringSecurityContextHolder to contain:" + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        if (logger.isDebugEnabled()) {
            logger.debug("Authentication request failed: " + failed.toString(), failed);
            logger.debug("Updated SecurityContextHolder to contain null Authentication");
        }
        response.sendError(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase());

    }
    protected abstract String getUsername(HttpServletRequest request);
    protected abstract String getPassword(HttpServletRequest request);
}

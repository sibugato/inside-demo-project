package com.sibgatullinvv.insidedemoproject.utils;

import com.sibgatullinvv.insidedemoproject.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
Фильтр запросов, который первым будет проверять переданный в заголовках JWT токен.
Наследование от OncePerRequestFilter позволяет Spring-у убедиться что проверка токена происходит при каждом HTTP запросе.
 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /*
    Реализация метода взята из свободного доступа.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Сначала получаем токен из соответствующего заголовка запроса
        final String requestTokenHeader = request.getHeader("Authorization");

        /*
         Если токен начинается с "Bearer" отсеиваем первые 7 символов. Это позволит принимать конструкцию аргументов обоих видов
         "Bearer токен" и "Bearer_токен".
         */
        if (StringUtils.startsWith(requestTokenHeader,"Bearer")) {
            String jwtToken = requestTokenHeader.substring(7);

            // Далее идёт валидация информации из токена и запись лога в случае ошибок
            try {
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                if (StringUtils.isNotEmpty(username)
                        && null == SecurityContextHolder.getContext().getAuthentication()) {
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext()
                                .setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                logger.error("Unable to fetch JWT Token");
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token is expired");
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        // Если с токеном всё в порядке - запрос передаётся в изначальный эндпоинт.
        chain.doFilter(request, response);
    }

}
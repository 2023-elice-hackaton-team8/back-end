package com.elice.hackathon.security;

import com.elice.hackathon.global.error.BusinessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.elice.hackathon.global.common.ErrorMessage.INVAILID_USERID_JWT_TOKEN;
import static com.elice.hackathon.global.common.ErrorMessage.NOT_FIND_JWT_TOKEN;


@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        log.info("req URI : " + request.getRequestURI() + "   :: client IP : " + request.getRemoteAddr());

        // 1. 토큰이 필요하지 않은 API URL에 대해서 배열로 구성합니다.
        List<String> list = Arrays.asList(
                "/users", "/users/login"
//                "api/v1/code/codeList"
        );
        if(request.getRequestURI().equals("/"))
            return;
        if(request.getRequestURI().split("/")[1].equals("swagger-ui") || request.getRequestURI().split("/")[1].equals("v3") || request.getRequestURI().split("/")[1].equals("crawling")){
            chain.doFilter(request, response);
            return;
        }
        // 2. 토큰이 필요하지 않은 API URL의 경우 => 로직 처리 없이 다음 필터로 이동
        if (list.contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // 3. OPTIONS 요청일 경우 => 로직 처리 없이 다음 필터로 이동
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }

        // [STEP1] Client에서 API를 요청할때 Header를 확인합니다.
        String header = request.getHeader(AuthConstants.AUTH_HEADER);

        try {
            // [STEP2-1] Header 내에 토큰이 존재하는 경우
            if (header != null && !header.equalsIgnoreCase("")) {

                // [STEP2] Header 내에 토큰을 추출합니다.
                String token = header.split(" ")[1];

                // [STEP4] 토큰을 기반으로 사용자 아이디를 반환 받는 메서드
                String userId = TokenProvider.getUserIdFromToken(token);

                // [STEP5] 사용자 아이디가 존재하는지 여부 체크
                if (userId != null && !userId.equalsIgnoreCase("")) {
                    User user = parseUserSpecification(token);
                    AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
                    authenticated.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticated);

                    chain.doFilter(request, response);
                } else {
                    throw new BusinessException(INVAILID_USERID_JWT_TOKEN);
                }

            }
            // [STEP2-1] 토큰이 존재하지 않는 경우
            else {
                throw new BusinessException(NOT_FIND_JWT_TOKEN);
            }
        } catch (BusinessException e) {
            // Token 내에 Exception이 발생 하였을 경우 => 클라이언트에 응답값을 반환하고 종료합니다.
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = jsonResponseWrapper(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
//
//        String token = parseBearerToken(request);
//        User user = parseUserSpecification(token);
//        AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
//        authenticated.setDetails(new WebAuthenticationDetails(request));
//        SecurityContextHolder.getContext().setAuthentication(authenticated);

//        chain.doFilter(request, response);
    }
    /**
     * 토큰 관련 Exception 발생 시 예외 응답값 구성
     *
     * @param e Exception
     * @return JSONObject
     */
    private JSONObject jsonResponseWrapper(BusinessException e) {

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("code", e.getErrorMessage().getCode());
        jsonMap.put("isSuccess", e.getErrorMessage().isSuccess());
        jsonMap.put("message", e.getErrorMessage().getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);

        return jsonObject;
    }

    private String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    private User parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(tokenProvider::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":");

        return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
    }
}


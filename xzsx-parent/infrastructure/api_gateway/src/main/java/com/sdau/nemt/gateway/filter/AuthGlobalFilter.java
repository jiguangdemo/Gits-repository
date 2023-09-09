package com.sdau.nemt.gateway.filter;

/**
 * @Date: 2023-8-17
 * @Author:
 * @Description:
 */

import com.alibaba.fastjson.JSON;
//import com.sdau.nemt.service.user.util.JwtTokenUtils;
import com.sdau.nemt.gateway.util.JwtTokenUtils;
//import com.sdau.nemt.service.user.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String PATH = "/user/login";
    private static final String PATH2 = "/user/verifyCode";
    private static final String PATH3 = "/user/phone-code";
    private static final String PATH4 = "/user/email";
    private static final String PATH5 = "/user/register";
    private static final String PATH6 = "/user/find-password";
    private static final String CHARSET_NAME = "utf-8";
    private static final String ADMIN = "admin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 获取请求路径，
        String path = request.getPath().toString();
        // 获取请求参数中token的值
        String token = request.getQueryParams().getFirst("token");
        HttpHeaders headers = exchange.getRequest().getHeaders();
        token = String.valueOf(headers.get("token"));
        String tokenJwt = "";
        System.out.println(token);
        for (int i = 1; i < token.length() -1; i++) {
            tokenJwt = tokenJwt + token.charAt(i);
        }
        System.out.println(tokenJwt);

        // 判断是否请求的login页面
        if (path.contains(PATH)||path.contains(PATH2)||path.contains(PATH3)||path.contains(PATH4)||path.contains(PATH5)||path.contains(PATH6)) {
            return chain.filter(exchange);
            // 没有访问登录页面，判断用户是否有token
        } else if(!tokenJwt.isEmpty()){
            String username = JwtTokenUtils.getUsername(tokenJwt);
            String user = redisTemplate.opsForValue().get("user" + username);
            String userToken = redisTemplate.opsForValue().get("token" + username);
            if(user!=null&&!user.equals("")) {
                if (tokenJwt.equals(userToken)||tokenJwt==userToken) {
                    return chain.filter(exchange);
                }else {
                    // 响应结果，
                    HashMap data = new HashMap();
                    DataBuffer buffer = null;
                    try {
                        data.put("code",410);
                        data.put("msg","账号在其他平台登录，您这边被迫下线");

                        byte[] bytes = JSON.toJSONString(data).getBytes(CHARSET_NAME);
                        buffer = response.bufferFactory().wrap(bytes);
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    return response.writeWith(Mono.just(buffer));
                }
            }else {
                // 响应结果，
                HashMap data = new HashMap();
                DataBuffer buffer = null;
                try {
                    data.put("code",404);
                    data.put("msg","请先登录");

                    byte[] bytes = JSON.toJSONString(data).getBytes(CHARSET_NAME);
                    buffer = response.bufferFactory().wrap(bytes);
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
                }catch (Exception e) {
                    e.printStackTrace();
                }
                return response.writeWith(Mono.just(buffer));
            }
        } else {
            // 响应结果，
            HashMap data = new HashMap();
            DataBuffer buffer = null;
            try {
                data.put("code",404);
                data.put("msg","请先登录");

                byte[] bytes = JSON.toJSONString(data).getBytes(CHARSET_NAME);
                buffer = response.bufferFactory().wrap(bytes);
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Content-Type","application/json;charset=UTF-8");
            }catch (Exception e) {
                e.printStackTrace();
            }
            return response.writeWith(Mono.just(buffer));
        }

    }

    /**
     * 权重，值越小优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }

}


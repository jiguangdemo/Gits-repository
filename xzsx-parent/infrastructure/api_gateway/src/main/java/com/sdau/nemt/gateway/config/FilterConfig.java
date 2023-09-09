package com.sdau.nemt.gateway.config;

/**
 * @Date: 2023-8-17
 * @Author:
 * @Description:
 */
import com.sdau.nemt.gateway.filter.AuthGlobalFilter;
import com.sdau.nemt.gateway.filter.TokenFilter;
import com.sdau.nemt.gateway.filter.UrlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {

    Logger logger = LoggerFactory.getLogger(FilterConfig.class);

//    @Bean
//    public GlobalFilter tokenFilter() {
//        return new TokenFilter();
//    }


    @Bean
    public GlobalFilter AuthGlobalFilter() {
        return new AuthGlobalFilter();
    }



//    @Bean
//    public RouteLocator urlFilterRouteLocator(RouteLocatorBuilder builder) {
//        logger.info("FilterConfig---urlFilterRouteLocator---");
//        return builder.routes()
//                .route(r -> r.path("/user/**")
//                        .filters(f -> f.stripPrefix (1).filter(new UrlFilter())
//                                .addResponseHeader("urlFilterFlag", "pass"))
//                        .uri("lb://service-user")
//                        .order(0)
//                        .id("service-user")
//                )
//                .build();
//    }



}
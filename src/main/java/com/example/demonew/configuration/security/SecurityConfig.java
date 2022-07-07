package com.example.demonew.configuration.security;

import com.example.demonew.configuration.CustomAccessDeniedHandler;
import com.example.demonew.configuration.JwtAuthenticationFilter;
import com.example.demonew.configuration.JwtProvider;
import com.example.demonew.configuration.RestAuthenticationEntryPoint;
import com.example.demonew.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() { //bean mã hóa pass người dùng
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {  //Cấu hình lại lỗi không có quyền truy cập
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/**"); // vô hiệu hóa csrf cho 1 số đường dẫn nhất định
        http.httpBasic().authenticationEntryPoint(jwtAuthenticationEntryPoint);//Tùy chỉnh lại thông báo 401 thông qua class restEntryPoint
        http.authorizeRequests()
                .antMatchers("/login",
                        "/register", "/**").permitAll() // tất cả truy cập được
                .anyRequest().authenticated()  //các request còn lại cần xác thực
                .and().csrf().disable(); // vô hiệu hóa bảo vệ của csrf (kiểm soát quyền truy cập)
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class) // lớp filter kiểm tra chuỗi jwt
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler()); //xử lý ngoaoj lệ khi không có quyền truy cập
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.cors();// ngăn chăn truy cập từ miền khác
    }
}
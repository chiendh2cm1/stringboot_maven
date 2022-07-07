package com.example.demonew.configuration;

import com.example.demonew.model.entity.User;
import com.example.demonew.service.user.UserService;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {   //class có nhiệm vụ mã hóa thông tin người dùng thành chuỗi jwt.
    private static final String SECRET_KEY = "123456789"; //mã số bí mật
    private static final long EXPIRE_TIME = 86400000000L;
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class.getName()); // thời gian sống

    @Autowired
    private UserService userService;
    public String generateTokenLogin(String username) { //tạo ra jwt từ thông tin user
        return Jwts.builder()
                .setSubject((username))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e); //kiểm tra phần chỹ ký điện tử
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);  //kiểm tra định dạng
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);  //kiểm tra hạn chuỗi jwt
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);  //kiểm tra chuỗi jwt được hỗ trợ không
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e); // kiểm tra rỗng
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token) { // lấy thông tin user từ chuỗi jwt
        String userName = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return userName;
    }

    public User getUserByUsername(String username){
        return userService.findByUsername(username);
    }
}
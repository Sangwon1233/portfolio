// package com.sangwon97.portfolio.user;

// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.sangwon97.portfolio.domain.User;
// import com.sangwon97.portfolio.repository.UserRepository;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


// @Configuration
// public class IdCreateTest {
    
//     @Bean
//     public CommandLineRunner initAdmin(UserRepository UserRepository) {
//         return args -> {
//             // 초기 관리자 계정 생성 (1회 실행 후 주석 처리!)
//             if (!UserRepository.existsById("")) { //사용할 아이디
//                 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//                 String encodedPassword = encoder.encode(""); //사용할 비밀번호

//                 User admin = new User();
//                 admin.setId("");
//                 admin.setPassword(encodedPassword);

//                 UserRepository.save(admin);
//                 System.out.println("✅ 관리자 계정 생성 완료");
//             }
//         };
//     }
// }

package kitchenpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@EnableJpaRepositories
// @WebMVCTest와 함께 사용시 오류발생
// 참고 : https://swampwar.github.io/2020/02/12/WebMvcTest%EC%99%80-EnableJpaRepositories-%EC%82%AC%EC%9A%A9%EC%8B%9C-%EC%98%A4%EB%A5%98%ED%95%B4%EA%B2%B0.html
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

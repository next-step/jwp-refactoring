package kitchenpos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MenuApplication {
    public static void main(String[] args) {
        System.out.println("MENU-APPLICATION-START");
        SpringApplication.run(MenuApplication.class, args);
    }

}

package kioskapp;

import org.springframework.boot.SpringApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableJpaAuditing
@SpringBootApplication
public class CafeKioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafeKioskApplication.class, args);
    }

}
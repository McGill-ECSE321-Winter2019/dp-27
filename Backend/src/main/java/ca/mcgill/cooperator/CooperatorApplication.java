package ca.mcgill.cooperator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CooperatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CooperatorApplication.class, args);
    }
}

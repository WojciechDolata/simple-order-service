package pl.wdolata.simpleorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SimpleOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleOrderServiceApplication.class, args);
    }
}

package projectone.enhanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/*
 * Entry point.
 * The original Contact, Task, and Appointment services are now exposed as a secured REST API.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Cs320Application {

    public static void main(String[] args) {
        SpringApplication.run(Cs320Application.class, args);
    }
}

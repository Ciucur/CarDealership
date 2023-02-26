package ro.itschool.CarDealership;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CarDealershipApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarDealershipApplication.class, args);
	}

}

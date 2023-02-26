package ro.itschool.CarDealership.scheduler;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.itschool.CarDealership.entity.Product;
import ro.itschool.CarDealership.repository.ProductRepository;

@Service
public class PokemonScheduler {

    @Autowired
    private ProductRepository productRepository;


    @Scheduled(cron = "*/30 * * * * *")
    public void insertPokemonEveryMinute() {
        System.out.println("hello");
        Faker faker = new Faker();

        Product product = productRepository.save(
                new Product(
                        faker.pokemon().name(),
                        faker.number().numberBetween(1, 200),
                        faker.number().numberBetween(0, 20)));
        productRepository.save(product);
    }
}

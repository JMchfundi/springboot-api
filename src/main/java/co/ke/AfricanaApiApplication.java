package co.ke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AfricanaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AfricanaApiApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(
								"http://localhost:3000",
								"https://www.boreshamaisha.tucode.co.ke");
								// "https://www.capdo.org");
				// registry.addMapping("/**").allowedOrigins("https://www.housing.tucode.co.ke");
			}
		};
	}
}
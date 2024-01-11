package br.com.flores.screenmetch;

import br.com.flores.screenmetch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmetchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmetchApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}

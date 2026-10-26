package com.habito_controle;

import com.habito_controle.Configs.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Program {

	public static void main(String[] args) {
		DotenvConfig.load();
		SpringApplication.run(Program.class, args);
	}

}

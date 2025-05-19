package com.bank.banksystem;

import org.springframework.boot.SpringApplication;

public class TestBanksystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(BanksystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

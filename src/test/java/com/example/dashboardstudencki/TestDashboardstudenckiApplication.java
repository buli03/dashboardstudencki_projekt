package com.example.dashboardstudencki;

import org.springframework.boot.SpringApplication;

public class TestDashboardstudenckiApplication {

	public static void main(String[] args) {
		SpringApplication.from(DashboardstudenckiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

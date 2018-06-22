package com.launch.management.launchmanagementprocessor;

import com.launch.management.launchmanagementprocessor.application.stream.Channels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(Channels.class)
public class LaunchManagementProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaunchManagementProcessorApplication.class, args);
	}
}

package com.jeet.chatconsumer;

import org.springframework.boot.SpringApplication;

public class TestChatConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.from(ChatConsumerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

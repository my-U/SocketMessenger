package com.example.socketmessenger;

import com.example.socketmessenger.auth.service.JwtService;
import com.example.socketmessenger.config.RedisPublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SocketMessengerApplication {

	private static NettyServer nettyServer;

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(SocketMessengerApplication.class, args);

		JwtService jwtService = context.getBean(JwtService.class);
		RedisPublisher redisPublisher = context.getBean(RedisPublisher.class);

		nettyServer = new NettyServer(jwtService, redisPublisher);
		nettyServer.start();
	}
}
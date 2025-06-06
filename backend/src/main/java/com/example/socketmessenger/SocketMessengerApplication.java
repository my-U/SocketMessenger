package com.example.socketmessenger;

import com.example.socketmessenger.auth.service.JwtService;
import com.example.socketmessenger.chat.ChatRoomService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SocketMessengerApplication {

	private static NettyServer nettyServer;

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(SocketMessengerApplication.class, args);

		JwtService jwtService = context.getBean(JwtService.class);
		ChatRoomService chatRoomService = context.getBean(ChatRoomService.class);

		nettyServer = new NettyServer(jwtService, chatRoomService);
		nettyServer.start();
	}
}
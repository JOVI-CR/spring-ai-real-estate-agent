package com.adriani.imoveis_bot;

import com.adriani.imoveis_bot.model.ImovelDTO;
import com.adriani.imoveis_bot.service.AgenteService;
import com.adriani.imoveis_bot.service.ImobiliariaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ImoveisBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImoveisBotApplication.class, args);
	}

//	@Bean
	public CommandLineRunner testarIntegracao(AgenteService agentService) {
		return args -> {
			System.out.println("🤖 --- TESTE DE TRANSBORDO HUMANO (OPÇÃO 5) ---");

			String pergunta = "5";

			System.out.println("CLIENTE: " + pergunta);

			String resposta = agentService.responderCliente(pergunta);

			System.out.println("AGENTE: " + resposta);

			System.out.println("--------------------------------------------------");
			System.out.println("✅ Se você viu a mensagem de 'ALERTA' vermelha acima, o sistema funcionou!");
		};
	}
}
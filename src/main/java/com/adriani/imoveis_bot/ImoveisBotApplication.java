package com.adriani.imoveis_bot;

import com.adriani.imoveis_bot.model.ImovelDTO;
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

	// Isso aqui roda assim que o app inicia, só pra gente testar
	@Bean
	public CommandLineRunner testarIntegracao(ImobiliariaService service) {
		return args -> {
			System.out.println("🚀 Iniciando busca de imóveis...");
			List<ImovelDTO> imoveis = service.buscarImoveis();

			System.out.println("✅ Encontrados: " + imoveis.size() + " imóveis!");

			// Imprime os 3 primeiros só pra gente ver se funcionou
			imoveis.stream().limit(3).forEach(i -> {
				System.out.println("--------------------------------------------------");
				System.out.println("🏠 " + i.getTitulo());
				System.out.println("💰 Preço: " + i.getPrecoFormatado());
				System.out.println("📍 Endereço: " + i.getEndereco());
			});

//			System.out.println("🐛 Iniciando Debug...");
//
//			service.debugJson();
		};
	}
}
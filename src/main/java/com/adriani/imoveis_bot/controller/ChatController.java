package com.adriani.imoveis_bot.controller;

import com.adriani.imoveis_bot.service.AgenteService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bot")
@CrossOrigin(origins = "*") // Permite testes fáceis via navegador
public class ChatController {

    private final AgenteService agenteService;

    public ChatController(AgenteService agenteService) {
        this.agenteService = agenteService;
    }

    // Exemplo de uso: POST http://localhost:8080/api/bot/chat
    // Body (JSON): { "mensagem": "Olá, quero uma casa" }
    @PostMapping("/chat")
    public Map<String, String> conversar(@RequestBody Map<String, String> payload) {
        String mensagemUsuario = payload.get("mensagem");

        // Chama nosso serviço inteligente
        String respostaIA = agenteService.responderCliente(mensagemUsuario);

        // Retorna um JSON bonitinho
        return Map.of("resposta", respostaIA);
    }

    // Para teste rápido no navegador: GET http://localhost:8080/api/bot/teste?msg=Oi
    @GetMapping("/teste")
    public String testeRapido(@RequestParam(value = "msg", defaultValue = "Olá") String mensagem) {
        return agenteService.responderCliente(mensagem);
    }
}
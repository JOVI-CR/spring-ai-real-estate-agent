package com.adriani.imoveis_bot.service;

import com.adriani.imoveis_bot.model.ImovelDTO;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgenteService {

    private final ImobiliariaService imobiliariaService;
    private final ChatClient chatClient;

    public AgenteService(ImobiliariaService imobiliariaService, ChatClient.Builder chatClientBuilder) {
        this.imobiliariaService = imobiliariaService;
        this.chatClient = chatClientBuilder.build();
    }

    public String responderCliente(String mensagemUsuario) {
        List<ImovelDTO> todosImoveis = imobiliariaService.buscarImoveis();

        List<ImovelDTO> imoveisFiltrados = filtrarImoveis(todosImoveis, mensagemUsuario);

        String contextoImoveis = formatarParaIA(imoveisFiltrados);

        String promptSistema = """
            Você é a **Assistente Virtual da Adriani Imóveis** 🤖.
            Sua função é atender clientes de forma automatizada, simpática e eficiente.
            
            **IMPORTANTE:**
            1. Em sua PRIMEIRA resposta, apresente-se como uma Inteligência Artificial.
            2. Se o cliente disser "Olá" ou algo genérico, ofereça este MENU numerado:
               1. 🏖️ Aluguel de Temporada
               2. 🏠 Comprar Imóvel (Venda)
               3. 🔑 Aluguel Fixo (Anual)
               4. 📝 Anunciar meu Imóvel (Proprietário)
               5. 🗣️ Falar com um Humano
            
            **SUAS FONTES DE DADOS (Imóveis Reais):**
            %s
            
            ---
            **REGRAS DE NEGÓCIO:**
            
            * **OPÇÃO 4 (ANUNCIAR):**
              Se o cliente quiser cadastrar um imóvel, diga:
              "Que ótimo! Adoraríamos ter seu imóvel conosco. Baixe nossa ficha de cadastro no link abaixo, preencha e me envie pelo WhatsApp."
              Link da Ficha: [lembrete para inserir o link]
            
            * **OPÇÃO 5 (HUMANO) ou PEDIDO DE AJUDA:**
              Se o cliente escolher a opção 5 ou pedir para falar com atendente/humano/pessoa:
              1. NÃO envie links de WhatsApp externos.
              2. Responda EXATAMENTE e APENAS: "Entendido! Já notifiquei a Adriani e ela (ou um assistente humano disponível) vai assumir essa conversa aqui mesmo em instantes. Por favor, aguarde um momento. 🔔"
              3. Não diga mais nada depois disso.
            
            * **VENDA/ALUGUEL FIXO:** Objetivo: AGENDAR VISITA. Nunca dê o endereço exato (apenas Bairro).
            
            * **TEMPORADA:** Objetivo: FECHAR DATAS. Pergunte os dias desejados.
            
            Seja breve. Use emojis.
            """.formatted(contextoImoveis);

        String respostaIA = chatClient.prompt()
                .system(promptSistema)
                .user(mensagemUsuario)
                .call()
                .content();

        if (respostaIA.contains("Já notifiquei a Adriani")) {
            System.out.println("🚨 [ALERTA DE SISTEMA] O cliente pediu um humano! O bot deve ser PAUSADO agora.");
            System.out.println("📨 Enviando notificação para o celular da Adriani...");
        }

        return respostaIA;
    }

    private List<ImovelDTO> filtrarImoveis(List<ImovelDTO> imoveis, String pergunta) {
        String msgLower = pergunta.toLowerCase();

        return imoveis.stream()
                .filter(i -> {
                    if (msgLower.contains("venda") || msgLower.contains("comprar")) {
                        return i.getTitulo().toLowerCase().contains("venda");
                    }
                    if (msgLower.contains("aluguel") || msgLower.contains("locação")) {
                        return i.getTitulo().toLowerCase().contains("aluguel") || i.getTitulo().toLowerCase().contains("locação");
                    }
                    if (msgLower.contains("temporada") || msgLower.contains("diária") || msgLower.contains("feriado")) {
                        return i.getTitulo().toLowerCase().contains("temporada");
                    }
                    return true;
                })
                .limit(15)
                .collect(Collectors.toList());
    }

    private String formatarParaIA(List<ImovelDTO> imoveis) {
        if (imoveis.isEmpty()) return "Nenhum imóvel encontrado no banco de dados com essas características.";

        StringBuilder sb = new StringBuilder();
        for (ImovelDTO i : imoveis) {
            sb.append(String.format("""
                ---
                [IMÓVEL ID: %s]
                Título: %s
                Preço: %s
                Quartos: %s
                Localização (Bairro/Cidade): %s
                Link: %s
                Descrição Completa: %s
                ---
                """,
                    i.getCodigo(),
                    i.getTitulo(),
                    i.getPrecoFormatado(),
                    i.getQuartos(),
                    i.getEndereco(),
                    i.link(),
                    i.descricaoObj() != null ? i.descricaoObj().rendered() : "Sem descrição"
            ));
        }
        return sb.toString();
    }
}
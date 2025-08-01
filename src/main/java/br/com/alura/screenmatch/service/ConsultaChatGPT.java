package br.com.alura.screenmatch.service;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService("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");

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(150)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}

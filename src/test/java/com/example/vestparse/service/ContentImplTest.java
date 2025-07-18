package com.example.vestparse.service;

import com.example.vestparse.model.*;
import com.example.vestparse.util.GeminiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentImplTest {

    @Mock
    private GeminiClient geminiClient;

    @Mock
    private QuestionsRepository questionRelationRepository;

    private static final String RAW_DATA_PROVA = """
             O que teria eu de fazer, até o fim da vida, era o “teatro
            desagradável”. Brecht inventou a “distância crítica” entre o
            espectador e a peça. Era uma maneira de isolar a emoção.
            Não me parece que tenha sido bem sucedido em tal experiência.
            O que se verifica, inversamente, é que ele faz toda a sorte de
            concessões ao patético. Ao passo que eu, na minha infinita
            modéstia, queria anular qualquer distância. A plateia sofreria
            tanto quanto o personagem e como se fosse também personagem.
            A partir do momento em que a plateia deixa de existir como
            plateia, está realizado o mistério teatral.
             O “teatro desagradável” ofende e humilha e com o
            sofrimento está criada a relação mágica. Não há distância. O
            espectador subiu ao palco e não tem a noção da própria
            identidade. Está ali como o homem. E, depois, quando acaba
            tudo, só então é que se faz a “distância crítica”. A grande vida da
            boa peça só começa quando baixa o pano. É o momento de fazer
            nossa meditação sobre o amor e sobre a morte.
             Depois viriam Anjo Negro, Senhora dos Afogados,
            Doroteia, Perdoa-me por me Traíres. Esta última estreou no
            Teatro Municipal. Mas ao baixar o pano, no terceiro ato, o teatro
            veio abaixo. Explodiu uma vaia jamais concebida. Senhoras,
            grã-finérrimas, subiam nas cadeiras e assoviavam como apaches.
            Meu texto não tinha um mísero palavrão. Quem dizia os
            palavrões era a plateia. No camarote, o então vereador Wilson
            Leite Passos puxou um revólver. E, como um Tom Mix, queria,
            decerto, fuzilar o meu texto.
            Nelson Rodrigues. A verdadeira apoteose é a vaia.
            Internet:<www.nelsonrodrigues.com.br> (com adaptações).
            Tendo como referência o texto precedente, a peça Perdoa-me
            por me traíres, de Nelson Rodrigues, e a obra A exceção e a
            regra, de Bertold Brecht, bem como aspectos a eles relacionados,
            julgue os itens a seguir.
            11 No universo de significado criado no primeiro parágrafo, as
            expressões ‘teatro desagradável’ e ‘distância crítica’ se
            complementam no que diz respeito à relação entre o público,
            a peça e a emoção que esta desencadeia.
            12 No trecho “só então é que se faz a ‘distância crítica’” (quinto
            período do segundo parágrafo), o vocábulo “se” indica que o
            sujeito da oração é indeterminado.
            13 O teatro épico de Bertold Brecht caracterizava-se pelo
            distanciamento crítico e pela negação de qualquer emoção
            em cena, de forma que a plateia se envolvesse apenas de
            maneira crítica com o que assistia.
            14 As obras Perdoa-me por me traíres e A exceção e a regra
            evidenciam estruturas de poder e fazem o público refletir
            sobre questões sociais e políticas, ainda que proponham
            diferentes modos de envolvimento afetivo entre público e
            obra.
            15 É possível realizar uma montagem de Perdoa-me por me
            traíres conforme o modelo do teatro épico, por meio, por
            exemplo, de procedimentos que façam o público refletir
            sobre a obra enquanto a assiste, tal como a quebra da quarta
            parede e uma interpretação e encenação (cenografia,
            iluminação, figurino) que provoquem o distanciamento
            crítico do público, equipe artística e técnica.
            16 No século XX, o avanço tecnológico na iluminação e o uso
            de projeção de imagens, assim como os novos materiais para
            cenários e figurinos, diversificaram as possibilidades de
            encenação e relação com o público, mas não afetaram a
            escrita das peças, como se pode evidenciar pelas rubricas dos
            textos, que ignoram qualquer indicação desse tipo.
            """;

    @Test
    void processarProvaCompleta_comDadosReais_deveEstruturarCorretamente() throws Exception {
        ExamParserService parserService = new ExamParserService();
        ContentGrouperImpl grouperService = new ContentGrouperImpl();
        ContentImpl contentImpl = new ContentImpl(parserService, grouperService, geminiClient, questionRelationRepository);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Liga a formatação com indentação

        when(geminiClient.setThemeBasic(anyString())).thenReturn("Título Gerado pela IA");
        when(geminiClient.classificationTypeCategory(anyString())).thenReturn(Category.OUTRA);
        when(questionRelationRepository.saveAll(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));




        List<QuestionRelation> atomicRelations = parserService.parseText(RAW_DATA_PROVA);
        System.out.println("\n---   APÓS O PARSING (Lista Plana de Elementos) ---");
        System.out.println(mapper.writeValueAsString(atomicRelations));

        List<QuestionRelation> groupedRelations = grouperService.groupContent(atomicRelations);
        System.out.println("\n---   APÓS O AGRUPAMENTO (Estrutura Hierárquica) ---");
        System.out.println(mapper.writeValueAsString(groupedRelations));

        for (QuestionRelation relation : groupedRelations) {
            if (relation.getBaseText() != null) {
                String title = geminiClient.setThemeBasic(relation.getBaseText().getContent());
                relation.getBaseText().setTitle(title);
            }
            for (Question question : relation.getQuestions()) {
                String contextText = (relation.getBaseText() != null) ? relation.getBaseText().getContent() : "";
                Category category = geminiClient.classificationTypeCategory(question.getContent());
                question.setCategory(category);
            }
        }
        System.out.println("\n---  3. APÓS O ENRIQUECIMENTO (Com Dados da IA) ---");
        System.out.println(mapper.writeValueAsString(groupedRelations));

        List<QuestionRelation> savedRelations = questionRelationRepository.saveAll(groupedRelations);
        System.out.println("\n---  4. RESULTADO FINAL (Após Salvar no Banco) ---");
        System.out.println(mapper.writeValueAsString(savedRelations));


        assertNotNull(savedRelations);
        assertEquals(1, savedRelations.size());
    }
}
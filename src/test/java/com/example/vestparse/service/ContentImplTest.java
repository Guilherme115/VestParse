package com.example.vestparse.service;

import com.example.vestparse.model.Category;
import com.example.vestparse.model.QuestionRelation;
import com.example.vestparse.util.GeminiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContentImplTest {

        @Mock
        private QuestionImpl question;

        @Mock
        private GeminiClient geminiClient;

        @InjectMocks
        private ContentImpl contentService;

        @Test
        void deveMontarListaComQuestoesEBaseText() throws Exception {
            String[] textos = {
                    "1 Quanto é 2 + 2?",
                    "Texto base sobre matemática"
            };

            Mockito.when(question.isQuestion("1 Quanto é 2 + 2?")).thenReturn(true);
            Mockito.when(question.isQuestion("Texto base sobre matemática")).thenReturn(false);

            Mockito.when(geminiClient.classificationTypeCategory("Quanto é 2 + 2?")).thenReturn(Category.MATEMATICA);
            Mockito.when(geminiClient.setThemeBasic("Texto base sobre matemática")).thenReturn("Matemática");


            List<QuestionRelation> resultado = contentService.contentList(textos);

            assertEquals(2, resultado.size());

            QuestionRelation questao = resultado.get(0);
            assertNotNull(questao.getQuestion());
            assertEquals("Quanto é 2 + 2?", questao.getQuestion().getContent());
            assertEquals(1, questao.getQuestion().getNumber());
            assertEquals(Category.MATEMATICA, questao.getCategory());
            assertNull(questao.getBaseText());

            QuestionRelation base = resultado.get(1);
            assertNull(base.getQuestion());
            assertNotNull(base.getBaseText());
            assertEquals("Texto base sobre matemática", base.getBaseText().getContent());
            assertEquals("Matemática", base.getBaseText().getTitle());
        }
    }

package com.example.vestparse.service;

import com.example.vestparse.model.AnswerKey;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnswerKeyServiceTest {

    private final AnswerKeyService service = new AnswerKeyService();

    @Test
    void testParseAnswerKeyText() {
        String gabaritoTexto = """
                 Gabarito
                                                                    
                                                                     Item
                                                                     PAS 1 - SUBPROGRAMA 2022 (TRIÊNIO 2022/2024)
                                                                     Gabarito
                                                                     Item
                                                                     Gabarito
                                                                     Item
                                                                     Gabarito
                                                                     Língua Espanhola
                                                                     Língua Francesa
                                                                     Língua Inglesa
                1 2 3 4 5 6 7 8 9 10 1 2 3 4 5 6 7 8 9 10
                                                                     EC C E E C E C C D C E C E E C C C E D
                                                                     1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20
                                                                     EE E C C E C C C D E C E E E A C E E D
                                                                     21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40
                                                                     EC C C E E C C E E C E C X C E E C D C
                                                                     41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60
                                                                     C 194 E C C E C E D X C E C X C C C E E C
                                                                     61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80
                                                                     DE E C C E E C C E C C C E E C C E E E
                                                                     81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99 100
                                                                     B C E Tipo D E C E C E C C E 018 320 D E C E E 030
                                                                     0 0 0 0 0 0 0 0 0 0 000 000 0
                                                                     0 0 0 0 0 0 0 0 0 0 000 000 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                     00 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
                                                                    
                """;

        List<AnswerKey> resposta = service.parseAnswerKeyText(gabaritoTexto);

        // Descomente a linha abaixo para ver a saída completa para debug
        resposta.forEach(a -> System.out.println(a.getQuestionNumber() + ": " + a.getAnswer()));

        assertEquals(120, resposta.size(), "O número total de questões deve ser 100.");


    }
}
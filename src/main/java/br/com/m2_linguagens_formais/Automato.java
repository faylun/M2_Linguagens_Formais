package br.com.m2_linguagens_formais;

import java.util.ArrayList;
import java.util.List;

public class Automato {

    // Índices de estado
    private static final int Q0   = 0;
    private static final int Q1   = 1;
    private static final int Q2   = 2;  // estado inicial
    private static final int Q3   = 3;
    private static final int Q4   = 4;
    private static final int Q5   = 5;
    private static final int Q6   = 6;
    private static final int Q7   = 7;
    private static final int Q8   = 8;
    private static final int Q9   = 9;
    private static final int ERRO = 10;

    // Índice de colunas da tabela de transição
    private static final int COL_A    = 0;
    private static final int COL_B    = 1;
    private static final int COL_C    = 2;
    private static final int COL_D    = 3;
    private static final int COL_E    = 4;
    private static final int COL_ERRO = 5;  // símbolo fora do alfabeto

    private static final String CARACTERES_DE_CONTROLE = " \t\n\r";

    private static final String OPERADORES = "+-*/";

    private static final int[][] tabela = {
            //         A      B      C      D      E    outro
            /* Q0  */ { ERRO,  ERRO,  Q3,    Q6,    ERRO,  ERRO },
            /* Q1  */ { ERRO,  ERRO,  ERRO,  Q6,    ERRO,  ERRO },
            /* Q2  */ { Q4,    ERRO,  Q0,    Q9,    ERRO,  ERRO },
            /* Q3  */ { ERRO,  ERRO,  Q0,    Q9,    ERRO,  ERRO },
            /* Q4  */ { ERRO,  Q5,    ERRO,  ERRO,  ERRO,  ERRO },
            /* Q5  */ { Q7,    ERRO,  ERRO,  ERRO,  ERRO,  ERRO },
            /* Q6  */ { ERRO,  ERRO,  ERRO,  ERRO,  Q8,    ERRO },
            /* Q7  */ { ERRO,  Q2,    ERRO,  ERRO,  ERRO,  ERRO },
            /* Q8  */ { ERRO,  ERRO,  ERRO,  Q9,    ERRO,  ERRO },
            /* Q9  */ { ERRO,  ERRO,  ERRO,  ERRO,  Q1,    ERRO },
            /* ERR */ { ERRO,  ERRO,  ERRO,  ERRO,  ERRO,  ERRO },
    };

    private static final boolean[] EF = {
            /* Q0  */ true,
            /* Q1  */ true,
            /* Q2  */ false,
            /* Q3  */ false,
            /* Q4  */ false,
            /* Q5  */ false,
            /* Q6  */ false,
            /* Q7  */ false,
            /* Q8  */ false,
            /* Q9  */ false,
            /* ERR */ false,
    };

    private static int coluna(char c) {
        return switch (Character.toUpperCase(c)) {
            case 'A' -> COL_A;
            case 'B' -> COL_B;
            case 'C' -> COL_C;
            case 'D' -> COL_D;
            case 'E' -> COL_E;
            default  -> COL_ERRO;
        };
    }


    public record Resultado(String categoria, String valor, Tipo tipo) {
        public enum Tipo { VALIDA, OPERADOR, ERRO_INVALIDA, ERRO_SIMBOLOS }
    }

    public List<Resultado> processarEntrada(String entrada) {
        List<Resultado> resultados = new ArrayList<>();
        StringBuilder   acumulador = new StringBuilder();

        for (int i = 0; i < entrada.length(); i++) {
            char simbolo = entrada.charAt(i);   // leitura símbolo a símbolo

            if (ehCaractereDeControle(simbolo)) {
                finalizarSentenca(acumulador, resultados);

            } else if (ehOperador(simbolo)) {
                finalizarSentenca(acumulador, resultados);
                resultados.add(new Resultado(
                        "operador aritmético",
                        String.valueOf(simbolo),
                        Resultado.Tipo.OPERADOR
                ));

            } else {
                acumulador.append(simbolo);
            }
        }

        finalizarSentenca(acumulador, resultados);   // token pendente no fim

        return resultados;
    }

    private boolean reconhece(String sentenca) {
        int estado = Q2;   // estado inicial

        for (char simbolo : sentenca.toCharArray()) {
            estado = tabela[estado][coluna(simbolo)];
        }

        return EF[estado];
    }

    private Resultado classificarSentenca(String sentenca) {
        boolean iniciaNoAlfabeto = coluna(sentenca.charAt(0)) != COL_ERRO;

        if (reconhece(sentenca)) {
            return new Resultado("sentença válida", sentenca, Resultado.Tipo.VALIDA);

        } else if (iniciaNoAlfabeto) {
            return new Resultado("ERRO: sentença inválida", sentenca, Resultado.Tipo.ERRO_INVALIDA);

        } else {
            return new Resultado("ERRO: símbolo(s) inválido(s)", sentenca, Resultado.Tipo.ERRO_SIMBOLOS);
        }
    }

    private void finalizarSentenca(StringBuilder acumulador, List<Resultado> resultados) {
        if (!acumulador.isEmpty()) {
            resultados.add(classificarSentenca(acumulador.toString()));
            acumulador.setLength(0);
        }
    }

    private static boolean ehCaractereDeControle(char c) {
        return CARACTERES_DE_CONTROLE.indexOf(c) >= 0;
    }

    private static boolean ehOperador(char c) {
        return OPERADORES.indexOf(c) >= 0;
    }
}
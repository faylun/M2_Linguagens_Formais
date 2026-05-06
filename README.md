# Reconhecedor de Sentenças — Autômato Finito Determinístico

## Descrição

Este programa implementa um **Autômato Finito Determinístico (AFD) mínimo** para reconhecimento de sentenças pertencentes a uma linguagem formal, conforme os conceitos de Linguagens Formais e Autômatos. A implementação utiliza **exclusivamente a forma genérica** de AFD (matriz de transição e vetor de estados finais) lendo a entrada símbolo a símbolo.


---

## Como rodar?


---

## Linguagem Reconhecida

### Definição Formal

$$L = \{ w \mid w \in \{a, b, c, d, e\}^+ \text{ e } w \text{ segue o padrão } (ab)^n c^m (de)^p \}$$

onde:
- **n** é um número par (n ≥ 0, ou seja, n ∈ {0, 2, 4, ...})
- **m + p** é ímpar (m ≥ 0, p ≥ 0, m + p ≥ 1)
- A palavra deve ter pelo menos um símbolo (w ∈ Σ⁺)
### Alfabeto

$$\Sigma = \{a, b, c, d, e\}$$


---

## Autômato Finito Determinístico

### Estados Finais e Estado Inicial

$$F = \{Q0, Q1\}$$
$$e0 = \{Q2 \}$$


## Tabela de Transição (Matriz)

As colunas representam os símbolos do alfabeto: **A, B, C, D, E**

|    |        | A  | B  | C  | D  | E  |
|----|--------|----|----|----|----|----|
| *  | **Q0** | -  | -  | Q3 | Q6 | -  |
| *  | **Q1** | -  | -  | -  | Q6 | -  |
| →  | **Q2** | Q4 | -  | Q0 | Q9 | -  |
|    | **Q3** | -  | -  | Q0 | Q9 | -  |
|    | **Q4** | -  | Q5 | -  | -  | -  |
|    | **Q5** | Q7 | -  | -  | -  | -  |
|    | **Q6** | -  | -  | -  | -  | Q8 |
|    | **Q7** | -  | Q2 | -  | -  | -  |
|    | **Q8** | -  | -  | -  | Q9 | -  |
|    | **Q9** | -  | -  | -  | -  | Q1 |


### Representação como Matriz (índices 0–10)
Símbolo **V** representa qualquer símbolo não pertencente a Σ nem aos separadores. Estado de erro.

```
           A    B    C    D     E    V
Q0   0  [  10,  10,   3,   6,   10,  10 ]
Q1   1  [  10,  10,  10,   6,   10,  10 ]
Q2   2  [   4,  10,   0,   9,   10,  10 ]
Q3   3  [  10,  10,   0,   9,   10,  10 ]
Q4   4  [  10,   5,  10,  10,   10,  10 ]
Q5   5  [   7,  10,  10,  10,   10,  10 ]
Q6   6  [  10,  10,  10,  10,    8,  10 ]
Q7   7  [  10,   2,  10,  10,   10,  10 ]
Q8   8  [  10,  10,  10,   9,   10,  10 ]
Q9   9  [  10,  10,  10,  10,    1,  10 ]
ERRO 10 [  10,  10,  10,  10,   10,  10 ]
```

### Vetor de Estados Finais

```

```
 
---
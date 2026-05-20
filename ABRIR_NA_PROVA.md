# Abrir Na Prova

Se estiver nervoso, comece aqui.

## Decisao Rapida

| Problema parece com... | Use |
|---|---|
| variavel compartilhada dando resultado errado | `cenario01` |
| estoque/ingressos/saldo | `cenario01` ou `cenario16` |
| fila com produtor e consumidor | `cenario02` |
| fila usando `wait`/`notifyAll` | `cenario03` |
| varias pessoas usando recurso limitado | `cenario06` ou `cenario11` |
| todo mundo precisa esperar todo mundo | `cenario08` |
| uma thread espera outra chegar num ponto | `cenario09` |
| fila de tarefas com varios trabalhadores | `cenario15` |
| processamento paralelo com resultado final | `cenario17` |
| duas contas/dois garfos/dois recursos | `cenario12` ou `cenario20` |

## Ordem Para Implementar

1. Crie os `static` compartilhados.
2. Crie as classes `Runnable`.
3. Dentro do `run`, coloque o loop.
4. Proteja a parte compartilhada.
5. No `main`, crie as threads.
6. De `start` em todas.
7. De `join` em todas se precisar de resultado final.

## Se Nao Souber Qual Usar

Use estas perguntas:

```text
Tem limite de quantidade?
Sim -> Semaphore(n)

Tem dado compartilhado simples?
Sim -> Semaphore(1) ou synchronized

Tem fila cheia/vazia?
Sim -> produtor-consumidor

Tem condicao para dormir?
Sim -> synchronized + while + wait + notifyAll

Tem que esperar todo mundo terminar?
Sim -> join, latch ou barreira

Tem duas travas?
Sim -> cuidado com deadlock, use ordem fixa
```

## Coisas Que Nao Pode Esquecer

```java
import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;
```

`wait` precisa estar dentro de `synchronized`.

Use `while`, nao `if`, antes de `wait`.

Se usar `acquire`, precisa ter `release`.

Se a main imprime resultado final, precisa ter `join`.

Se mudar uma condicao que outra thread espera, chame `notifyAll`.


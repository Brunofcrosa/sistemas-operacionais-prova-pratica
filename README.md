# Concorrencia na Pratica - Guia de Prova

Este material e para ajudar a sair do enunciado e chegar no codigo.

A ideia nao e decorar teoria. A ideia e bater o olho no problema, descobrir qual padrao de concorrencia ele pede e adaptar um dos arquivos em `cenarios/`.

## Como Usar Amanhã

1. Leia a questao e nao comece codando ainda.
2. Descubra quem sao as threads.
3. Descubra qual recurso ou dado e compartilhado.
4. Descubra quando alguem precisa esperar.
5. Escolha o cenario mais parecido.
6. Copie/adapte a estrutura.
7. Compile cedo, mesmo incompleto.

Arquivos uteis:

```text
ABRIR_NA_PROVA.md       guia curto para decidir rapido
TEMPLATES_RAPIDOS.md    blocos pequenos para copiar
CHECKLIST_FINAL.md      coisas para conferir antes de entregar
PROVA_SIMULADA.md       treino com enunciados
REVISAO_VESPERA.md      revisao final
COMO_COMPILAR.md        comandos de javac/java
cenarios/               20 exemplos Java prontos
```

## A Pergunta Mais Importante

Quando aparecer um problema de concorrencia, pergunte:

```text
Quem esta disputando o que?
```

Quase todo problema cai em uma dessas formas:

| Historia do enunciado | Traducao mental | Ferramenta comum |
|---|---|---|
| varios alunos usam poucas impressoras | muitas threads disputam recurso limitado | `Semaphore(n)` |
| produtor coloca itens e consumidor retira | fila/buffer compartilhado | semaforos ou monitor |
| varios vendedores vendem ingressos | varias threads alteram mesmo contador | mutex |
| todos terminam fase 1 antes da fase 2 | sincronizacao coletiva | barreira |
| duas contas transferem dinheiro | duas travas ao mesmo tempo | ordem fixa para evitar deadlock |
| fila de tarefas com trabalhadores | workers consumindo trabalho | monitor com fila |
| pacientes urgentes antes dos comuns | fila com prioridade | `PriorityQueue` protegida |

## O Metodo Dos 4 Blocos

Antes de escrever codigo, preencha mentalmente:

1. **Threads:** quem executa ao mesmo tempo?
2. **Estado compartilhado:** qual variavel, fila, lista, saldo, contador ou recurso todos acessam?
3. **Regra de espera:** quando uma thread deve parar e esperar?
4. **Regra de acordar:** quando uma thread muda algo que libera outra?

Exemplo: deposito com capacidade 5.

```text
Threads: produtores e consumidores.
Estado compartilhado: fila de caixas.
Espera: produtor espera se cheio, consumidor espera se vazio.
Acorda: produtor acorda consumidor quando coloca; consumidor acorda produtor quando remove.
```

Depois disso, a solucao praticamente vira produtor-consumidor.

## Como Escolher a Primitiva

| Sinal no enunciado | Escolha | Por que |
|---|---|---|
| "no maximo N ao mesmo tempo" | `Semaphore(N)` | o semaforo conta as permissoes |
| "varias threads incrementam/alteram" | `Semaphore(1)` ou `synchronized` | precisa exclusao mutua |
| "dormir ate ter item/espaco" | `synchronized + wait + notifyAll` | espera uma condicao |
| "cheio e vazio" | `vazio`, `cheio`, `mutex` | produtor-consumidor classico |
| "esperar todo mundo" | `join`, latch ou barreira | depende de finalizacao coletiva |
| "dois recursos ao mesmo tempo" | ordem fixa de travas | evita deadlock |

## Pensando Como Codigo

Depois de entender a historia, traduza para esta estrutura:

```java
public class Prova {
    static recursoCompartilhado;
    static primitivasDeSincronizacao;

    public static void main(String[] args) throws Exception {
        criar threads
        start em todas
        join se precisar esperar resultado
    }

    static class AlgumaThread implements Runnable {
        public void run() {
            loop
            sincronizacao
            trabalho
        }
    }
}
```

O segredo e separar trabalho normal de parte compartilhada. A parte compartilhada deve ficar pequena e protegida.

## Exemplo Guiado: Impressoras

Enunciado: existem 3 impressoras e 10 alunos. No maximo 3 imprimem ao mesmo tempo.

Raciocinio:

- Threads: alunos.
- Recurso compartilhado: impressoras.
- Limite: 3.
- Espera: aluno espera se nao houver impressora livre.
- Ferramenta: `Semaphore(3)`.

Forma do codigo:

```java
static Semaphore impressoras = new Semaphore(3, true);

impressoras.acquire();
System.out.println("imprimindo");
Thread.sleep(tempo);
impressoras.release();
```

Esse mesmo raciocinio serve para estacionamento, API limitada, conexoes, caixas, cabines e laboratorios.

Base para adaptar: `cenario06.java` ou `cenario11.java`.

## Exemplo Guiado: Deposito

Enunciado: produtores colocam caixas em um deposito limitado; consumidores retiram.

Raciocinio:

- Threads: produtores e consumidores.
- Estado compartilhado: buffer/fila.
- Condicoes: cheio e vazio.
- Ferramentas: `vazio`, `cheio`, `mutex`.

Ordem que precisa lembrar:

```text
produtor:
1. espera espaco vazio
2. trava buffer
3. adiciona item
4. solta buffer
5. avisa que tem item cheio

consumidor:
1. espera item cheio
2. trava buffer
3. remove item
4. solta buffer
5. avisa que tem espaco vazio
```

Base para adaptar: `cenario02.java`.

## Exemplo Guiado: Fila Com Monitor

Quando o enunciado fala que uma thread deve dormir enquanto uma condicao nao acontece, pense em monitor.

```java
synchronized (fila) {
    while (fila.isEmpty()) {
        fila.wait();
    }
    int item = fila.remove();
}
```

Regra de prova: antes de `wait`, use `while`. Nao use `if`.

Depois que alguem muda a condicao, acorda os outros:

```java
synchronized (fila) {
    fila.add(item);
    fila.notifyAll();
}
```

Base para adaptar: `cenario03.java` ou `cenario15.java`.

## Exemplo Guiado: Banco e Deadlock

Problemas com duas contas, dois arquivos, dois recursos ou dois objetos compartilhados tem cheiro de deadlock.

Erro classico:

```text
Thread 1: trava A, depois tenta B
Thread 2: trava B, depois tenta A
```

Solucao pratica: criar uma ordem global. Por exemplo, conta com menor id sempre trava primeiro.

```java
Conta primeira = origem.id < destino.id ? origem : destino;
Conta segunda = origem.id < destino.id ? destino : origem;

synchronized (primeira) {
    synchronized (segunda) {
        transferir
    }
}
```

Base para adaptar: `cenario12.java` ou `cenario20.java`.

## O Que Escrever Primeiro Se Travar

Se voce nao souber por onde comecar, escreva o esqueleto:

```java
public class Prova {
    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[quantidade];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new Trabalho(i));
            threads[i].start();
        }
        for (Thread t : threads) t.join();
    }

    static class Trabalho implements Runnable {
        int id;

        Trabalho(int id) {
            this.id = id;
        }

        public void run() {
        }
    }
}
```

Depois coloque o recurso compartilhado e a sincronizacao.

## Como Testar Mentalmente

Antes de executar, simule com duas ou tres threads:

1. Thread A entra primeiro.
2. Thread B tenta entrar ao mesmo tempo.
3. Quem deve esperar?
4. Quem acorda quem?
5. Existe caminho em que todo mundo dorme para sempre?

Se existir um caminho onde todos ficam esperando, provavelmente tem deadlock ou faltou `release`/`notifyAll`.

## Erros Que Mais Acontecem

| Erro | Como perceber | Conserto |
|---|---|---|
| esquecer `join` | main imprime antes das threads terminarem | dar `join` depois de iniciar todas |
| esquecer `release` | programa roda um pouco e trava | todo `acquire` precisa liberar |
| `wait` fora de `synchronized` | `IllegalMonitorStateException` | usar `synchronized` no mesmo objeto |
| usar `if` antes de `wait` | acorda em estado errado | trocar por `while` |
| proteger so metade da operacao | contador, saldo ou fila inconsistente | proteger leitura/escrita compartilhada |
| usar `sleep` como sincronizacao | funciona por sorte | usar semaforo, monitor ou `join` |
| travar A/B numa thread e B/A em outra | deadlock | ordem fixa de travas |

## Indice Dos Cenarios

| Arquivo | Tema | Quando usar |
|---|---|---|
| `cenario01.java` | Exclusao mutua com semaforo | contador compartilhado, regiao critica |
| `cenario02.java` | Produtor-consumidor com semaforos | buffer limitado, cheio/vazio/mutex |
| `cenario03.java` | Produtor-consumidor com monitor | `synchronized`, `wait`, `notifyAll` |
| `cenario04.java` | Leitores e escritores | muitos leitores, escritor exclusivo |
| `cenario05.java` | Jantar dos filosofos | evitar deadlock com recursos multiplos |
| `cenario06.java` | Estacionamento limitado | recurso contado por semaforo |
| `cenario07.java` | Ponte de mao unica | sentido unico e capacidade |
| `cenario08.java` | Barreira reutilizavel | todos terminam uma fase antes de continuar |
| `cenario09.java` | Rendezvous | uma thread espera ponto especifico da outra |
| `cenario10.java` | Pipeline | etapas encadeadas |
| `cenario11.java` | API limitada | maximo de acessos simultaneos |
| `cenario12.java` | Transferencia bancaria | duas travas sem deadlock |
| `cenario13.java` | Barbeiro dorminhoco | clientes, cadeiras e barbeiro dormindo |
| `cenario14.java` | Fumantes | agente libera recursos e fumantes sincronizam |
| `cenario15.java` | Fila de tarefas | workers consumindo tarefas |
| `cenario16.java` | Venda de ingressos | estoque compartilhado |
| `cenario17.java` | Soma paralela | dividir trabalho, `join`, combinar resultado |
| `cenario18.java` | Latch manual | main espera varios servicos |
| `cenario19.java` | Fila com prioridade | prioridade com `PriorityQueue` |
| `cenario20.java` | Deadlock e correcao | ordem fixa de travas |

## Mapa Final

| Se cair... | Pense... | Arquivo base |
|---|---|---|
| API, impressora, estacionamento | recurso limitado | `cenario06`, `cenario11` |
| deposito, buffer, fila cheia/vazia | produtor-consumidor | `cenario02`, `cenario03` |
| saldo, ingresso, contador | regiao critica | `cenario01`, `cenario16` |
| todos esperam todos | barreira | `cenario08` |
| fila de trabalho | workers consumindo tarefas | `cenario15` |
| prioridade | `PriorityQueue` protegida | `cenario19` |
| duas travas | deadlock e ordem fixa | `cenario12`, `cenario20` |

## Comandos Basicos

```powershell
javac cenarios\cenario02.java
java -cp cenarios cenario02
```

Se `javac` nao estiver no PATH neste computador, consulte `COMO_COMPILAR.md`.

## Resumo Para Levar Na Cabeca

Concorrencia pratica e descobrir:

```text
qual estado compartilhado precisa ser protegido
qual condicao faz uma thread esperar
quem acorda quem
```

Depois disso, o codigo vira escolha de padrao.


# Guia de Prova Pratica - Concorrencia em Java

Este arquivo e para bater o olho durante a prova e escolher rapidamente qual padrao implementar.

## Roteiro de 30 Segundos

1. Identifique quais sao as threads.
2. Identifique qual dado ou recurso e compartilhado.
3. Pergunte: existe limite de quantidade?
4. Pergunte: alguem precisa dormir esperando uma condicao?
5. Pergunte: a main precisa esperar o fim de todo mundo?
6. Escolha o padrao mais parecido nos cenarios.

## Como Escolher a Solucao

| Se o enunciado fala... | Pense em... | Olhe |
|---|---|---|
| contador, saldo, estoque, variavel compartilhada | regiao critica | `cenario01`, `cenario16` |
| buffer, fila, produzir e consumir | produtor-consumidor | `cenario02`, `cenario03` |
| cheio e vazio | dois semaforos contadores + mutex | `cenario02` |
| thread dorme ate ter item/espaco/condicao | `wait` dentro de `while` | `cenario03`, `cenario15` |
| vagas, conexoes, acessos simultaneos | `Semaphore(n)` | `cenario06`, `cenario11` |
| todos precisam chegar antes de continuar | barreira | `cenario08` |
| main espera varios servicos terminarem | latch ou `join` | `cenario18`, `cenario17` |
| uma thread so continua depois da outra chegar em um ponto | rendezvous | `cenario09` |
| leitores podem entrar juntos, escritor sozinho | leitores-escritores | `cenario04` |
| dois recursos ao mesmo tempo | risco de deadlock | `cenario12`, `cenario20` |
| ponte, estrada, sentido, capacidade | monitor com condicao + limite | `cenario07` |
| barbeiro, clientes, cadeiras, sala de espera | barbeiro dorminhoco | `cenario13` |
| varias etapas encadeadas | pipeline | `cenario10` |
| prioridade | `PriorityQueue` protegida por monitor | `cenario19` |

## Templates Minimos

### 1. Regiao Critica com Semaforo

```java
static Semaphore mutex = new Semaphore(1);
static int x = 0;

mutex.acquire();
x++;
mutex.release();
```

Use quando varias threads alteram a mesma variavel.

Se tiver erro ou `return` no meio, uma versao mais segura e:

```java
mutex.acquire();
try {
    x++;
} finally {
    mutex.release();
}
```

### 2. Recurso Limitado

```java
static Semaphore recurso = new Semaphore(5, true);

recurso.acquire();
System.out.println("usando recurso");
Thread.sleep(100);
recurso.release();
```

Use para API limitada, estacionamento, impressoras, conexoes, caixas, pontes com capacidade.

### 3. Produtor-Consumidor com Semaforos

```java
static Queue<Integer> buffer = new LinkedList<>();
static Semaphore mutex = new Semaphore(1);
static Semaphore vazio = new Semaphore(5);
static Semaphore cheio = new Semaphore(0);

vazio.acquire();
mutex.acquire();
buffer.add(valor);
mutex.release();
cheio.release();

cheio.acquire();
mutex.acquire();
int valor = buffer.remove();
mutex.release();
vazio.release();
```

Ordem mental:

```text
produtor: espera vazio -> trava buffer -> coloca -> solta buffer -> avisa cheio
consumidor: espera cheio -> trava buffer -> retira -> solta buffer -> avisa vazio
```

### 4. Monitor com `wait` e `notifyAll`

```java
synchronized (objeto) {
    while (!condicao) {
        objeto.wait();
    }
    alteraEstadoCompartilhado();
    objeto.notifyAll();
}
```

Regra de ouro: `wait` sempre dentro de `synchronized`, e quase sempre dentro de `while`.

### 5. Esperar Threads Terminarem

```java
Thread t1 = new Thread(new MinhaThread());
Thread t2 = new Thread(new MinhaThread());
t1.start();
t2.start();
t1.join();
t2.join();
System.out.println("resultado final");
```

Comece todas as threads antes de chamar `join`.

### 6. Evitar Deadlock com Duas Travas

```java
Object primeira = idA < idB ? lockA : lockB;
Object segunda = idA < idB ? lockB : lockA;

synchronized (primeira) {
    synchronized (segunda) {
        // usa os dois recursos
    }
}
```

Ideia: todas as threads pegam as travas na mesma ordem global.

## Erros Classicos Que Derrubam Nota

| Erro | Sintoma | Correcao |
|---|---|---|
| usar `if` antes de `wait` | acorda sem condicao verdadeira | usar `while` |
| chamar `wait` fora de `synchronized` | `IllegalMonitorStateException` | sincronizar no mesmo objeto |
| esquecer `release` | programa trava depois de um tempo | liberar sempre depois de usar |
| esquecer `notifyAll` | thread fica dormindo para sempre | chamar apos mudar a condicao |
| dar `join` logo depois de cada `start` no mesmo loop | perde paralelismo | primeiro inicia todas, depois faz join |
| acessar lista/fila sem trava | resultado inconsistente | proteger com mutex ou monitor |
| travar A depois B numa thread e B depois A em outra | deadlock | ordem fixa de travas |
| usar `Thread.sleep` como sincronizacao | funciona por sorte | usar semaforo, wait/notify ou join |
| soltar semaforo antes de alterar o estado | outra thread ve estado errado | altere estado dentro da regiao critica |

## Mapa de Adaptacao Rapida

### Se cair "banheiro/estacionamento/elevador/API com limite"

Use `Semaphore(n)`.

Base: `cenario06` ou `cenario11`.

Troque:

```text
Carro -> Usuario/Requisicao/Cliente
vagas -> capacidade/conexoes/permissoes
```

### Se cair "deposito com produtor e consumidor"

Use `cenario02` se o professor pediu semaforo.

Use `cenario03` se o professor pediu monitor, `synchronized`, `wait`, `notify`.

### Se cair "fila de atendimento"

Use `cenario15`.

Se tiver prioridade, use `cenario19`.

### Se cair "banco, transferencia, duas contas"

Use `cenario12`.

Ponto mais importante: travar as contas sempre na mesma ordem.

### Se cair "processamento em paralelo e resultado final"

Use `cenario17`.

Padrao:

```text
divide vetor -> cada thread calcula um pedaco -> join -> soma parciais
```

### Se cair "todos devem esperar todos"

Use `cenario08`.

Palavras do enunciado: fase, rodada, etapa, so pode continuar quando todos terminarem.

### Se cair "um espera sinal do outro"

Use `cenario09`.

Palavras do enunciado: A so pode continuar depois que B fizer X.

## Plano Para Resolver Qualquer Questao

1. Escreva as classes internas `Runnable`.
2. Crie os recursos compartilhados como `static`.
3. Proteja cada recurso compartilhado.
4. Inicie todas as threads.
5. Use `join` se precisa de resultado final.
6. Teste com poucos valores.
7. Aumente os valores se quiser demonstrar concorrencia.

## Frases Para Pensar Na Hora

Regiao critica e tudo que le ou altera estado compartilhado.

Semaforo com valor 1 e mutex.

Semaforo com valor N e limite de recursos.

`wait` libera o monitor e dorme.

`notifyAll` acorda, mas a thread so continua quando recuperar o monitor.

`sleep` nao resolve concorrencia, so atrasa.

Deadlock normalmente aparece quando duas threads pegam recursos em ordens diferentes.

Se a prova estiver dificil, implemente primeiro uma versao correta e simples. Depois melhore justica, capacidade, prioridade ou logs.


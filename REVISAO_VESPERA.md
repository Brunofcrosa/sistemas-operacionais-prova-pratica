# Revisao de Vespera

Use este arquivo para treinar a cabeca para reconhecer padroes.

## Ordem Recomendada de Estudo

1. Leia `GUIA_PROVA.md`.
2. Abra `cenario01`, `cenario02`, `cenario03`, `cenario06`, `cenario08`, `cenario12`, `cenario15`, `cenario17`.
3. Rode mentalmente cada codigo: quem espera, quem acorda, quem protege o que.
4. Tente resolver os mini-enunciados abaixo escolhendo um cenario base.

## Mini-Enunciados Possiveis

### 1. Impressoras compartilhadas

Um laboratorio possui 3 impressoras. Dez alunos tentam imprimir trabalhos. No maximo 3 podem imprimir ao mesmo tempo.

Padrao: recurso limitado.

Base: `cenario06` ou `cenario11`.

Ferramenta: `Semaphore(3)`.

### 2. Padaria com fila de pedidos

Clientes colocam pedidos em uma fila. Funcionarios retiram pedidos e preparam. A fila tem tamanho maximo 5.

Padrao: produtor-consumidor.

Base: `cenario02`.

Ferramentas: `vazio`, `cheio`, `mutex`.

### 3. Chat com mensagens

Uma thread produz mensagens e outra exibe. Se nao houver mensagem, a exibidora deve dormir.

Padrao: monitor.

Base: `cenario03`.

Ferramentas: `synchronized`, `while`, `wait`, `notifyAll`.

### 4. Arquivo compartilhado

Varios usuarios podem ler ao mesmo tempo, mas quando alguem escreve, ninguem mais pode ler nem escrever.

Padrao: leitores e escritores.

Base: `cenario04`.

Ferramentas: contador de leitores, mutex dos leitores, semaforo da escrita.

### 5. Corrida com largada simultanea

Todos os corredores treinam individualmente. A corrida so comeca quando todos estiverem prontos.

Padrao: barreira.

Base: `cenario08`.

Ferramenta: contador de chegadas + `wait/notifyAll`.

### 6. Banco com transferencias

Duas threads fazem transferencias entre as mesmas contas em direcoes opostas.

Padrao: duas travas com risco de deadlock.

Base: `cenario12`.

Ideia principal: sempre travar na mesma ordem.

### 7. Caixa de supermercado

Varios caixas atendem clientes de uma fila. Quando a fila acaba, os caixas encerram.

Padrao: fila de tarefas.

Base: `cenario15`.

Ferramenta: fila protegida por monitor e flag `terminou`.

### 8. Calculo de media

Um vetor grande precisa ser processado por 4 threads. Cada uma soma uma parte e a main calcula a media final.

Padrao: divisao de trabalho.

Base: `cenario17`.

Ferramentas: vetor de parciais + `join`.

### 9. Ponte estreita

Carros dos dois lados querem atravessar. So pode ter carro em um sentido por vez, e no maximo 3 carros na ponte.

Padrao: monitor com condicao e capacidade.

Base: `cenario07`.

Ferramentas: `sentidoAtual`, `naPonte`, `capacidade`, `wait/notifyAll`.

### 10. Sistema com login prioritario

Ha tarefas normais e urgentes. Os consumidores devem pegar primeiro as urgentes.

Padrao: fila com prioridade.

Base: `cenario19`.

Ferramenta: `PriorityQueue`.

## Checklist Antes de Entregar

Marque mentalmente:

- Todas as threads foram iniciadas com `start`?
- A main precisa esperar? Se sim, tem `join`?
- Toda variavel compartilhada esta protegida?
- Todo `wait` esta dentro de `while`?
- Todo `wait` esta dentro de `synchronized`?
- Depois de mudar uma condicao, tem `notifyAll`?
- Todo `acquire` tem `release` correspondente?
- Se pega duas travas, a ordem e sempre a mesma?
- O programa termina ou o enunciado permite loop infinito?
- Os nomes impressos ajudam a mostrar que a sincronizacao funcionou?

## Como Adaptar Um Cenario Rapido

1. Copie o arquivo mais parecido.
2. Renomeie a classe para o nome pedido pelo professor.
3. Troque os nomes das entidades.
4. Ajuste quantidades: numero de threads, capacidade, tamanho da fila.
5. Mantenha a estrutura de sincronizacao.
6. Teste com numeros pequenos.

## Padroes Que Mais Valem Memorizar

### Mutex

```java
mutex.acquire();
// regiao critica
mutex.release();
```

### Fila Vazia

```java
synchronized (fila) {
    while (fila.isEmpty()) fila.wait();
    int item = fila.remove();
}
```

### Fila Recebe Item

```java
synchronized (fila) {
    fila.add(item);
    fila.notifyAll();
}
```

### Esperar Todo Mundo

```java
for (Thread t : threads) t.start();
for (Thread t : threads) t.join();
```

### Semaforo Contador

```java
Semaphore limite = new Semaphore(quantidade);
limite.acquire();
// usa recurso
limite.release();
```


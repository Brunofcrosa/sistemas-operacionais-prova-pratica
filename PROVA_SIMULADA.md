# Prova Simulada

Use assim:

1. Leia o enunciado.
2. Antes de olhar o gabarito, escreva qual padrao usaria.
3. Abra o cenario indicado e tente adaptar mentalmente.
4. So depois confira o gabarito no final.

## Enunciados

### Questao 1 - Laboratorio de Impressao

Um laboratorio possui 4 impressoras. Existem 12 alunos tentando imprimir trabalhos. Cada impressao demora um tempo. No maximo 4 alunos podem imprimir ao mesmo tempo.

O programa deve mostrar quando um aluno chega, quando comeca a imprimir e quando termina.

### Questao 2 - Deposito

Dois produtores colocam caixas em um deposito com capacidade maxima 6. Tres consumidores retiram caixas. Produtor nao pode colocar se o deposito estiver cheio. Consumidor nao pode retirar se estiver vazio.

### Questao 3 - Sistema de Logs

Varias threads geram mensagens de log. Uma thread gravadora retira mensagens da fila e imprime. Se nao houver mensagem, a gravadora deve dormir ate chegar uma nova mensagem.

### Questao 4 - Banco

Duas contas bancarias recebem transferencias simultaneas em sentidos opostos. O total de dinheiro do sistema nao pode mudar. O programa nao pode entrar em deadlock.

### Questao 5 - Arquivo Compartilhado

Varios usuarios podem ler um arquivo ao mesmo tempo. Quando um usuario estiver escrevendo, nenhum leitor nem outro escritor pode acessar o arquivo.

### Questao 6 - Largada de Corrida

Cinco corredores se preparam em tempos diferentes. A corrida so pode comecar quando todos estiverem prontos.

### Questao 7 - Servidor Com Limite

Um servidor externo so aceita 3 requisicoes simultaneas. Vinte threads tentam consultar esse servidor.

### Questao 8 - Ponte Estreita

Uma ponte permite carros em apenas um sentido por vez. No maximo 2 carros podem estar na ponte ao mesmo tempo. Carros chegam alternando norte e sul.

### Questao 9 - Processamento Paralelo

Um vetor com muitos numeros deve ser somado por 4 threads. Cada thread soma um trecho. A main deve imprimir a soma total.

### Questao 10 - Fila de Atendimento

Um conjunto de tarefas chega antes do inicio do atendimento. Tres trabalhadores retiram tarefas da fila ate acabar tudo.

### Questao 11 - Atendimento Com Prioridade

Um hospital recebe pacientes comuns e urgentes. Os atendentes devem atender primeiro os mais urgentes.

### Questao 12 - Ordem Entre Threads

O processo B so pode executar sua segunda etapa depois que A terminar a primeira etapa. O processo A tambem so pode executar sua segunda etapa depois que B terminar a primeira etapa.

## Gabarito

| Questao | Padrao | Base |
|---|---|---|
| 1 | Recurso limitado com semaforo contador | `cenario06` ou `cenario11` |
| 2 | Produtor-consumidor com buffer limitado | `cenario02` |
| 3 | Monitor com fila, `wait` e `notifyAll` | `cenario03` ou `cenario15` |
| 4 | Duas travas com ordem fixa | `cenario12` |
| 5 | Leitores e escritores | `cenario04` |
| 6 | Barreira | `cenario08` |
| 7 | Recurso limitado com `Semaphore(3)` | `cenario11` |
| 8 | Ponte com sentido e capacidade | `cenario07` |
| 9 | Divisao de trabalho + `join` | `cenario17` |
| 10 | Fila de tarefas | `cenario15` |
| 11 | Fila com prioridade | `cenario19` |
| 12 | Rendezvous | `cenario09` |

## Como Se Corrigir

Para cada resposta, veja se voce identificou:

- quais sao as threads
- qual e o recurso compartilhado
- qual e a condicao de espera
- qual primitiva resolve melhor
- onde precisa de `join`
- onde pode acontecer deadlock


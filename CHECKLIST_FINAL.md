# Checklist Final

Leia isto antes de entregar a prova.

## Compilacao

- O nome do arquivo e igual ao nome da classe `public`.
- Todos os imports necessarios estao no topo.
- Nao tem `package` se o professor pediu arquivo unico simples.
- O codigo compila com `javac Nome.java`.

## Threads

- Todas as threads recebem `start`.
- O `join` vem depois de todos os `start`, quando precisa esperar resultado final.
- O programa termina se o enunciado pede termino.
- Se for simulacao infinita, isso esta coerente com o enunciado.

## Dados Compartilhados

- Variavel compartilhada alterada por varias threads esta protegida.
- Lista, fila, vetor de escrita compartilhada ou contador estao protegidos.
- Regiao critica esta pequena e clara.

## Semaforos

- `Semaphore(1)` para mutex.
- `Semaphore(n)` para recurso limitado.
- Todo `acquire` tem um `release`.
- No produtor-consumidor, a ordem esta correta:

```text
produtor: vazio -> mutex -> add -> mutex release -> cheio release
consumidor: cheio -> mutex -> remove -> mutex release -> vazio release
```

## Monitores

- `wait` esta dentro de `synchronized`.
- A condicao antes do `wait` usa `while`.
- Depois de mudar estado compartilhado, chama `notifyAll`.
- O objeto usado no `synchronized` e o mesmo usado no `wait/notifyAll`.

## Deadlock

- Se duas travas sao usadas, todas as threads pegam na mesma ordem.
- Nenhuma thread segura uma trava enquanto espera algo que depende de outra thread segurando outra trava em ordem inversa.
- `sleep` nao esta sendo usado como se fosse sincronizacao.

## Saida

- Os `System.out.println` mostram que a sincronizacao funcionou.
- A saida nao precisa ser sempre na mesma ordem.
- O resultado final confere quando existe contador, soma, saldo ou estoque.

## Ultima Decisao

Se estiver com pouco tempo, priorize:

1. Compilar.
2. Nao ter deadlock.
3. Proteger dado compartilhado.
4. Mostrar no console que funcionou.
5. Deixar bonito so se sobrar tempo.


# prototipo-prova

Projeto Maven Java 21 no mesmo estilo do `so-threads`, pronto para abrir no IntelliJ e clicar em `Run`.

Fluxo pensado para prova:

1. Abra a pasta `prototipo-prova`.
2. Aguarde o IntelliJ importar o `pom.xml`.
3. Edite apenas `src/main/java/br/ufsm/politecnico/csi/prototipo/Main.java`.
4. Execute a configuracao `Main` no topo da IDE.

Tudo importante esta dentro de `Main.java`:

- `main`: ponto de entrada
- `executarBasico`: base de criacao de threads e `join`
- `executarSemaforo`: secao critica com `Semaphore`
- `executarMonitor`: produtor/consumidor com `wait` e `notifyAll`
- `executarBarreira`: sincronizacao por barreira

Se quiser testar sem alterar codigo, use argumentos:

- vazio: `basico`
- `semaforo`
- `monitor`
- `barreira`

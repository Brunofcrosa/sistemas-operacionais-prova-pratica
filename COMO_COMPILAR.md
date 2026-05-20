# Como Compilar e Rodar Sem Internet

## Jeito Normal

Se `javac` e `java` estiverem no PATH:

```powershell
javac MinhaClasse.java
java MinhaClasse
```

Para os cenarios desta pasta:

```powershell
javac cenarios\cenario02.java
java -cp cenarios cenario02
```

## Se O Java Nao Estiver No PATH

Neste computador foi encontrado um `javac` aqui:

```powershell
C:\Programas\IntelliJ IDEA 2026.1.1\jbr\bin\javac.exe
```

Exemplo:

```powershell
& 'C:\Programas\IntelliJ IDEA 2026.1.1\jbr\bin\javac.exe' MinhaClasse.java
& 'C:\Programas\IntelliJ IDEA 2026.1.1\jbr\bin\java.exe' MinhaClasse
```

Para compilar os cenarios:

```powershell
& 'C:\Programas\IntelliJ IDEA 2026.1.1\jbr\bin\javac.exe' cenarios\*.java
& 'C:\Programas\IntelliJ IDEA 2026.1.1\jbr\bin\java.exe' -cp cenarios cenario02
```

## Erros Comuns

### Erro: class X is public, should be declared in a file named X.java

O nome do arquivo precisa ser igual ao nome da classe `public`.

Se o arquivo chama `Prova.java`, a classe deve ser:

```java
public class Prova {
}
```

### Erro: cannot find symbol Semaphore

Faltou import:

```java
import java.util.concurrent.Semaphore;
```

### Erro: IllegalMonitorStateException

Voce chamou `wait`, `notify` ou `notifyAll` fora de `synchronized`, ou sincronizou em um objeto e chamou `wait` em outro.

### O programa nao termina

Possibilidades:

- faltou `release`
- faltou `notifyAll`
- alguma thread esta esperando mais itens do que foram produzidos
- o loop e infinito
- deu deadlock com duas travas


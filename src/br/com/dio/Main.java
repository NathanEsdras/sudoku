package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public class Main {
    private static final int BOARD_LIMIT = 9;
    private static Board board;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n==== MENU ====");
            System.out.println("1 - Iniciar Jogo");
            System.out.println("2 - Inserir número");
            System.out.println("3 - Remover número");
            System.out.println("4 - Mostrar tabuleiro atual");
            System.out.println("5 - Mostrar status do jogo");
            System.out.println("6 - Limpar tabuleiro");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Dica automática");
            System.out.println("9 - Sair");
            System.out.print("Escolha uma opção: ");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame();
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> giveHint();
                case 9 -> System.out.println("Saindo do jogo...");
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }

        } while (option != 9);

        scanner.close();
    }

    private static void startGame() {
        board = new Board(BOARD_LIMIT);
        System.out.println("Novo jogo iniciado!");
    }

    private static void inputNumber() {
        if (Objects.isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a linha (0-8): ");
        int row = scanner.nextInt();
        System.out.print("Digite a coluna (0-8): ");
        int col = scanner.nextInt();
        System.out.print("Digite o número (1-9): ");
        int number = scanner.nextInt();

        var space = board.getSpaces().get(row).get(col);
        if (Objects.nonNull(space.getInitial())) {
            System.out.println("Não é possível alterar um número inicial.");
            return;
        }

        space.setActual(number);
        System.out.println("Número inserido com sucesso!");
    }

    private static void removeNumber() {
        if (Objects.isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a linha (0-8): ");
        int row = scanner.nextInt();
        System.out.print("Digite a coluna (0-8): ");
        int col = scanner.nextInt();

        var space = board.getSpaces().get(row).get(col);
        if (Objects.nonNull(space.getInitial())) {
            System.out.println("Não é possível remover um número inicial.");
            return;
        }

        space.setActual(null);
        System.out.println("Número removido!");
    }

    private static void showCurrentGame() {
        if (Objects.isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        for (int row = 0; row < BOARD_LIMIT; row++) {
            for (int col = 0; col < BOARD_LIMIT; col++) {
                var space = board.getSpaces().get(row).get(col);
                Integer value = space.getActual();
                if (Objects.isNull(value)) {
                    System.out.print(". ");
                } else {
                    System.out.print(value + " ");
                }
            }
            System.out.println();
        }
    }

    private static void showGameStatus() {
        if (Objects.isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        boolean complete = true;
        for (int row = 0; row < BOARD_LIMIT; row++) {
            for (int col = 0; col < BOARD_LIMIT; col++) {
                var space = board.getSpaces().get(row).get(col);
                if (Objects.isNull(space.getActual()) || !space.getActual().equals(space.getExpected())) {
                    complete = false;
                    break;
                }
            }
        }

        if (complete) {
            System.out.println("Parabéns! Você completou o jogo!");
        } else {
            System.out.println("O jogo ainda não foi finalizado.");
        }
    }

    private static void clearGame() {
        if (Objects.isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        for (int row = 0; row < BOARD_LIMIT; row++) {
            for (int col = 0; col < BOARD_LIMIT; col++) {
                var space = board.getSpaces().get(row).get(col);
                if (Objects.isNull(space.getInitial())) {
                    space.setActual(null);
                }
            }
        }
        System.out.println("Tabuleiro limpo!");
    }

    private static void finishGame() {
        board = null;
        System.out.println("Jogo finalizado.");
    }

    private static void giveHint() {
        if (Objects.isNull(board)) {
            System.out.println("O jogo ainda não foi iniciado");
            return;
        }

        List<int[]> emptySpaces = new ArrayList<>();

        for (int row = 0; row < BOARD_LIMIT; row++) {
            for (int col = 0; col < BOARD_LIMIT; col++) {
                var space = board.getSpaces().get(row).get(col);
                if (Objects.isNull(space.getActual())) {
                    emptySpaces.add(new int[]{row, col});
                }
            }
        }

        if (emptySpaces.isEmpty()) {
            System.out.println("Não há espaços vazios para dar dica!");
            return;
        }

        var random = new Random();
        var pos = emptySpaces.get(random.nextInt(emptySpaces.size()));
        var row = pos[0];
        var col = pos[1];
        var space = board.getSpaces().get(row).get(col);

        System.out.printf(
            "Dica: na posição [%d,%d] o valor correto é %d\n",
            row, col, space.getExpected()
        );
    }
}

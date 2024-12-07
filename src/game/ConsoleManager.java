package game;

import staticClasses.Colors;
import staticClasses.Symbols;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleManager {
    public static ConsoleManager Instance = new ConsoleManager();

    private final Scanner sc = new Scanner(System.in);

    public void EmptyLine() {
        System.out.println();
    }

    public void Title(String text) {
        printColoredText("      ---\\\\ " + text + " //---\n", Colors.CYAN);
    }

    public void SubTitle(String text) {
        printColoredText("   --- " + text + " ---\n", Colors.GREEN);
    }

    public void Paragraph(String text) {
        final int LINE_SIZE = 40;

        List<String> lines = splitEqually(text, LINE_SIZE);

        for (String line : lines) {
            System.out.printf("  - %s\n", line);
        }
    }

    public void Error(String text) {
        printColoredText(" -{ " + text + " }-\n", Colors.RED);
    }

    public void List(String listTitle, String[] listItems) {
        printColoredText("\\ " + listTitle + " /\n", Colors.CYAN);

        for (String item : listItems) {
            System.out.printf(" * %s\n", item);
        }
    }

    public int Selection(String listTitle, String[] listItems, String prompt, int selectionRange) {
        printColoredText("\\ " + listTitle + " /\n", Colors.CYAN);

        for (int i = 1; i <= listItems.length; i++) {
            System.out.printf(" %d %s\n", i, listItems[i - 1]);
        }

        int choice;
        while (true) {
            String input = GetInput(prompt);
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                Error("Invalid input format");
                continue;
            }

            if (choice < 1 || choice > selectionRange) {
                Error("Number out of range");
                EmptyLine();
            } else {
                return choice;
            }
        }
    }

    public String GetInput(String prompt) {
        if (!prompt.isEmpty()) {
            printColoredText(prompt + "\n", Colors.YELLOW);
        }
        printColoredText("<< ", Colors.YELLOW);
        return sc.nextLine();
    }

    public void FieldInterface(int width, Field field, Player player1, Player player2) {
        final int NICKNAME_HEIGHT = 2;
        final int NUMERATION_HEIGHT = 1;
        final int SYMBOLS_WIDTH = 3;

        StringBuilder separator = new StringBuilder();

        String lowerSeparator = String.valueOf(Symbols.BORDER_FORMAT_2.charAt(2)).repeat(width / 2);
        separator.append(lowerSeparator);
        separator.append(String.valueOf(Symbols.BORDER_FORMAT_2.charAt(6)));
        separator.append(lowerSeparator);

        String[][] entitySymbols = field.GetField();

        String[] content = new String[NICKNAME_HEIGHT + NUMERATION_HEIGHT + entitySymbols.length];

        content[0] = String.format(player1 + "%-" + (width / 2 - player1.Name.length())
                + "s│%" + (width / 2 - player2.Name.length()) + "s" + player2, "", "");
        content[1] = separator.toString();

        int emptySpace = (width - field.getWidth() * SYMBOLS_WIDTH - 6) / 2;

        StringBuilder numerationLine = new StringBuilder();

        numerationLine.append(" ".repeat(emptySpace + 3));
        for (int i = 0; i < field.getWidth(); i++) {
            numerationLine.append(Symbols.BORDER_FORMAT_2.charAt(3));
            numerationLine.append(Symbols.LOWER_ALPHABET.charAt(i));
            numerationLine.append(Symbols.BORDER_FORMAT_2.charAt(3));
        }

        content[NICKNAME_HEIGHT + NUMERATION_HEIGHT - 1] = numerationLine.toString();

        for (int i = 0; i < entitySymbols.length; i++) {
            StringBuilder line = new StringBuilder();


            if (i < player1.characters.length) {
                if (player1.characters[i].IsActionable()) {
                    line.append(String.format("%-" + (emptySpace - 5) + "s", String.valueOf(i + 1) + ' ' + player1.characters[i].GetName()));
                } else {
                    line.append(Colors.Format(
                            String.format("%-" + (emptySpace - 5) + "s", String.valueOf(i + 1) + ' ' + player1.characters[i].GetName()),
                            Colors.YELLOW));
                }

                String hpStatus = String.format("%2s HP", player1.characters[i].getHealth());
                line.append(player1.characters[i].IsPoisoned ? Colors.Format(hpStatus, Colors.GREEN) : hpStatus);
            } else {
                line.append(" ".repeat(emptySpace));
            }


            line.append(Symbols.BORDER_FORMAT_2.charAt(3));
            line.append(i + 1);
            line.append(Symbols.BORDER_FORMAT_2.charAt(3));

            for (String c : entitySymbols[i]) {
                line.append(" ".repeat(SYMBOLS_WIDTH / 2));
                line.append(c);
                line.append(" ".repeat(SYMBOLS_WIDTH / 2));
            }

            line.append(Symbols.BORDER_FORMAT_2.charAt(3));
            line.append(i + 1);
            line.append(Symbols.BORDER_FORMAT_2.charAt(3));


            if (i < player2.characters.length) {
//                String hpStatus = String.format("%2s HP", player2.characters[i].getHealth());
//                line.append(player2.characters[i].IsPoisoned ? Colors.Format(hpStatus, Colors.GREEN) : hpStatus);
                if (player2.characters[i].IsPoisoned) {
                    line.append(Colors.Format(
                            String.format("%2s HP", player2.characters[i].getHealth()), Colors.GREEN));
                } else {
                    line.append(String.format("%2s HP", player2.characters[i].getHealth()));
                }

                if (player2.characters[i].IsActionable()) {
                    line.append(String.format("%" + (emptySpace - 5) + "s", player2.characters[i].GetName() + ' ' + (i + 1)));
                } else {
                    line.append(Colors.Format(
                            String.format("%" + (emptySpace - 5) + "s", player2.characters[i].GetName() + ' ' + (i + 1)),
                            Colors.YELLOW));
                }
            } else {
                line.append(" ".repeat(emptySpace));
            }

            content[NICKNAME_HEIGHT + NUMERATION_HEIGHT + i] = line.toString();
        }

        Border(width, content.length, Symbols.BORDER_FORMAT_1, content);
    }

    public void Border(int width, int height, String borderFormat, String[] content) {
        if (width > 1 && height > 1) {
            System.out.print(borderFormat.charAt(0));
            System.out.print(String.valueOf(borderFormat.charAt(2)).repeat(width));
            System.out.println(borderFormat.charAt(1));

            for (int i = 0; i < height; i++) {
                System.out.print(borderFormat.charAt(3));
                System.out.printf("%-" + width + "s", i < content.length ? content[i] : "");
                System.out.println(borderFormat.charAt(3));
            }

            System.out.print(borderFormat.charAt(4));
            for (int i = 0; i < width; i++) {
                System.out.print(borderFormat.charAt(2));
            }
            System.out.println(borderFormat.charAt(5));
        }
    }

    private void printColoredText(String text, String color) {
        System.out.print(color + text + Colors.RESET);
    }

    private void printColoredText(String text, String mainColor, String bgColor) {
        System.out.print(bgColor + mainColor + text + Colors.RESET);
    }

    private List<String> splitEqually(String text, int size) {
        List<String> res = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            res.add(text.substring(start, Math.min(text.length(), start + size)));
        }

        return res;
    }
}

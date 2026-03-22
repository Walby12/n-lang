import java.io.File;
import java.util.Scanner;
import java.util.Stack;
import javax.sound.sampled.*;
import javax.swing.*;

enum Tokens {
    Number,
    Plus,
    Minus,
    Star,
    Slash,
    Dot,
    Exit,
    Dup,
    Pinodaniele,
    Help,
    Hi,
    Dump,
    EOF,
}

public class Main {

    private static class Comp {

        String src;
        int i = 0;
        Tokens cur_tok;
        String cur_val;
    }

    static Stack<Integer> stack = new Stack<>();

    // --- LEXER ---
    static Tokens lexer(Comp comp) {
        if (comp.i >= comp.src.length()) return Tokens.EOF;

        char c = comp.src.charAt(comp.i);

        if (Character.isWhitespace(c)) {
            comp.i++;
            return lexer(comp);
        }

        switch (c) {
            case '+':
                comp.i++;
                return Tokens.Plus;
            case '-':
                comp.i++;
                return Tokens.Minus;
            case '*':
                comp.i++;
                return Tokens.Star;
            case '/':
                comp.i++;
                return Tokens.Slash;
            case '.':
                comp.i++;
                return Tokens.Dot;
            default:
                StringBuilder buff = new StringBuilder();
                while (comp.i < comp.src.length()) {
                    char current = comp.src.charAt(comp.i);
                    if (
                        Character.isWhitespace(current) ||
                        current == '+' ||
                        current == '.' ||
                        current == '*' ||
                        current == '/' ||
                        current == '-'
                    ) break;
                    buff.append(current);
                    comp.i++;
                }
                comp.cur_val = buff.toString();
                try {
                    Integer.parseInt(comp.cur_val);
                    return Tokens.Number;
                } catch (NumberFormatException e) {
                    switch (comp.cur_val) {
                        case "dup":
                            return Tokens.Dup;
                        case "dump":
                            return Tokens.Dump;
                        case "exit":
                            return Tokens.Exit;
                        case "pinodaniele":
                            return Tokens.Pinodaniele;
                        case "help":
                            return Tokens.Help;
                        case "hi":
                            return Tokens.Hi;
                        default:
                            System.out.printf(
                                "error: Unknown insturction: %s\n",
                                comp.cur_val
                            );
                            report_error();
                    }
                }
        }
        return Tokens.EOF;
    }

    // --- PARSER ---
    static void parser(Comp comp) {
        comp.cur_tok = lexer(comp);

        while (comp.cur_tok != Tokens.EOF) {
            switch (comp.cur_tok) {
                case Number:
                    stack.push(Integer.parseInt(comp.cur_val));
                    break;
                case Plus:
                    if (stack.size() < 2) {
                        System.out.printf(
                            "error: '+' requires two numbers on the stack\n"
                        );
                        report_error();
                    }
                    stack.push(stack.pop() + stack.pop());
                    break;
                case Minus:
                    if (stack.size() < 2) {
                        System.out.printf(
                            "error: '-' requires two numbers on the stack\n"
                        );
                        report_error();
                    }
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a - b);
                    break;
                case Star:
                    if (stack.size() < 2) {
                        System.out.printf(
                            "error: '*' requires two numbers on the stack\n"
                        );
                        report_error();
                    }
                    stack.push(stack.pop() * stack.pop());
                    break;
                case Slash:
                    if (stack.size() < 2) {
                        System.out.printf(
                            "error: '/' requires two numbers on the stack\n"
                        );
                        report_error();
                    }
                    int c = stack.pop();
                    if (c == 0) {
                        System.out.printf("error: division by zero\n");
                        report_error();
                    } else {
                        int d = stack.pop();
                        stack.push(d / c);
                    }
                    break;
                case Dot:
                    if (stack.isEmpty()) {
                        System.out.printf(
                            "error: '.' requires one number on the stack to print\n"
                        );
                        report_error();
                    }
                    System.out.println("> " + stack.pop());
                    break;
                case Dup:
                    if (stack.isEmpty()) {
                        System.out.printf(
                            "error: 'dup' requires one number on the stack du duplicate it\n"
                        );
                        report_error();
                    }
                    stack.push(stack.peek());
                    break;
                case Exit:
                    System.out.printf("Goodbye");
                    System.exit(0);
                    break;
                case Dump:
                    System.out.println(stack);
                    break;
                case Pinodaniele:
                    easter_egg();
                    break;
                case Help:
                    System.out.printf(
                        "List of available commands:\n\tdump: prints all of the stack\n\tdup: duplicates the top of the stack\n\texit: exits the repl\n\thi: prints a nice message\n\tpinodaniele: type and find out\n"
                    );
                    break;
                case Hi:
                    System.out.printf("Hello\n");
                    greeting();
                    break;
            }
            comp.cur_tok = lexer(comp);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Comp comp = new Comp();
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(
            "                                                               _.--,-```-.    \n" +
                "         ,--.                                                 /    /      '.  \n" +
                "       ,--.'|                                                /  ../         ; \n" +
                "   ,--,:  : |              ,---,  ,--,                       \\  ``\\  .``-    '\n" +
                ",`--.'`|  ' :            ,---.'|,--.'|                        \\ ___\\/    \\   :\n" +
                "|   :  :  | |            |   | :|  |,                               \\    :   |\n" +
                ":   |   \\ | :            |   | |`--'_                               |    ;  . \n" +
                "|   : '  '; |          ,--.__| |,' ,'|                             ;   ;   :  \n" +
                "'   ' ;.    ;         /   ,'   |'  | |                            /   :   :   \n" +
                "|   | | \\   |        .   '  /  ||  | :                            `---'.  |   \n" +
                "'   : |  ; .'        '   ; |:  |'  : |__                           `--..`;    \n" +
                "|   | '`--'          |   | '/  '|  | '.'|         ___  ___  ___  .--,_        \n" +
                "'   : |              |   :    :|;  :    ;        /  .\\/  .\\/  .\\ |    |`.     \n" +
                ";   |.'               \\   \\  /  |  ,   /         \\  ; \\  ; \\  ; |`-- -`, ;    \n" +
                "'---'                  `----'    ---`-'           `--\" `--\" `--\"   '---`\"\n"
        );

        System.out.println("Type help for the list of available commands");

        while (true) {
            System.out.print("shell >> ");
            comp.src = input.nextLine();
            comp.i = 0;
            parser(comp);
        }
    }

    static void easter_egg() {
        // --- Window + GIF ---
        JFrame frame = new JFrame("Gross king");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        ImageIcon gif = new ImageIcon("random/makesweet-zrxxeu.gif");
        JLabel label = new JLabel(gif);
        frame.add(label);
        frame.setVisible(true);

        // --- Sound ---
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(
                new File("random/fahh-but-louder-very-loud.wav")
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            frame.addWindowListener(
                new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        clip.stop();
                    }
                }
            );
        } catch (Exception e) {
            System.out.println(
                "error: could not play sound: " + e.getMessage()
            );
        }
    }

    static void greeting() {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(
                new File("random/metal-pipe.wav")
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            System.out.println(
                "error: could not play sound: " + e.getMessage()
            );
        }
    }

    static void report_error() {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(
                new File("random/bird.wav")
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            System.out.println(
                "error: could not play sound: " + e.getMessage()
            );
        }
    }
}

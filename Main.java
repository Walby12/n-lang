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
                        default:
                            System.out.printf(
                                "error: Unknown token: %s\n",
                                comp.cur_val
                            );
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
                    }
                    stack.push(stack.pop() + stack.pop());
                    break;
                case Minus:
                    if (stack.size() < 2) {
                        System.out.printf(
                            "error: '-' requires two numbers on the stack\n"
                        );
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
                    }
                    stack.push(stack.pop() * stack.pop());
                    break;
                case Slash:
                    if (stack.size() < 2) {
                        System.out.printf(
                            "error: '/' requires two numbers on the stack\n"
                        );
                    }
                    int c = stack.pop();
                    if (c == 0) {
                        System.out.printf("error: division by zero\n");
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
                    }
                    System.out.println("> " + stack.pop());
                    break;
                case Dup:
                    if (stack.isEmpty()) {
                        System.out.printf(
                            "error: 'dup' requires one number on the stack du duplicate it\n"
                        );
                    }
                    stack.push(stack.peek());
                    break;
                case Exit:
                    System.out.printf("Goodbye from cianobatteri corp");
                    System.exit(0);
                    break;
                case Dump:
                    System.out.println(stack);
                    break;
                case Pinodaniele:
                    easter_egg();
                    break;
            }
            comp.cur_tok = lexer(comp);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Comp comp = new Comp();

        System.out.println(
            " ██████╗ ██╗ █████╗ ███╗  ██╗ ██████╗ ██████╗  █████╗ ████████╗████████╗███████╗██████╗ ██╗\n" +
                "██╔════╝ ██║██╔══██╗████╗ ██║██╔═══██╗██╔══██╗██╔══██╗╚══██╔══╝╚══██╔══╝██╔════╝██╔══██╗██║\n" +
                "██║      ██║███████║██╔██╗██║██║   ██║██████╔╝███████║   ██║      ██║   █████╗  ██████╔╝██║\n" +
                "██║      ██║██╔══██║██║╚████║██║   ██║██╔══██╗██╔══██║   ██║      ██║   ██╔══╝  ██╔══██╗██║\n" +
                "╚██████╗ ██║██║  ██║██║ ╚███║╚██████╔╝██████╔╝██║  ██║   ██║      ██║   ███████╗██║  ██║██║\n" +
                " ╚═════╝ ╚═╝╚═╝  ╚═╝╚═╝  ╚══╝ ╚═════╝ ╚═════╝ ╚═╝  ╚═╝  ╚═╝      ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝\n" +
                "\n" +
                "                         ██████╗ ██████╗ ██████╗ ██████╗ \n" +
                "                        ██╔════╝██╔═══██╗██╔══██╗██╔══██╗\n" +
                "                        ██║     ██║   ██║██████╔╝██████╔╝\n" +
                "                        ██║     ██║   ██║██╔══██╗██╔═══╝ \n" +
                "                        ╚██████╗╚██████╔╝██║  ██║██║     \n" +
                "                         ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚═╝     \n"
        );

        while (true) {
            System.out.print("n lang shell: ");
            comp.src = input.nextLine();
            comp.i = 0;
            parser(comp);
        }
    }

    static void easter_egg() {
        // --- Window + GIF ---
        JFrame frame = new JFrame("Gross king");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
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
}

package org.lazaro.rt5e.utility;

import java.io.PrintStream;

/**
 * @author Lazaro
 */
public class Logger {
    protected static int indentationLevel = 0;

    public static void setupLogging() {
        System.setOut(new PrintStream(System.out) {
            @Override
            public void print(String string) {
                for (int i = 0; i < indentationLevel; i++) {
                    super.print(" ");
                }
                super.print(string);
            }

            @Override
            public void println(String string) {
                print(string + "\n");
            }

            @Override
            public void println() {
                print("\n");
            }
        });
        System.setErr(new PrintStream(System.err) {
            @Override
            public void print(String string) {
                for (int i = 0; i < indentationLevel; i++) {
                    super.print(" ");
                }
                super.print(string);
            }

            @Override
            public void println(String string) {
                print(string + "\n");
            }

            @Override
            public void println() {
                print("\n");
            }
        });
    }

    public static void printInfo() {
        System.out.println("//****************************************************\\\\");
        Logger.incrementIndentation();
        System.out.println("RT5E (RuneTek 5 Emulator)");
        System.out.println("Copyright (C) Lazaro Brito 2010");
        System.out.println("Running " + System.getProperty("os.name") + " on a(n) " + System.getProperty("os.arch") + " architecture");
        System.out.println("Java version is " + System.getProperty("java.version"));
        Logger.decrementIndentation();
        System.out.println("\\\\****************************************************//");
        System.out.println();
    }

    public static void incrementIndentation() {
        indentationLevel++;
    }

    public static void decrementIndentation() {
        indentationLevel--;
    }

    public static void incrementIndentationTab() {
        indentationLevel += 3;
    }

    public static void decrementIndentationTab() {
        indentationLevel -= 3;
    }

    public static void resetIndentation() {
        indentationLevel = 0;
    }

}

/**
 * Copyright (C) 2010 Lazaro Brito
 *
 * This file is part of RT5E.
 *
 * RT5E is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RT5E is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RT5E.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.lazaro.rt5e.utility;

import java.io.PrintStream;

/**
 * @author Lazaro
 */
public class Logger {
    protected static Object lock = new Object();
    protected static int indentationLevel = 0;
    protected static boolean textOnLine = false;


    public static void setupLogging() {
        System.setOut(new PrintStream(System.out) {
            @Override
            public void print(String string) {
                if(string.length() > 0) {
                    synchronized(lock) {
                        if(!textOnLine) {
                            for (int i = 0; i < indentationLevel; i++) {
                                super.print(" ");
                            }
                            if(string.charAt(string.length() - 1) != '\n') {
                                textOnLine = true;
                            }
                        } else if(string.charAt(string.length() - 1) == '\n') {
                            textOnLine = false;
                        }
                        super.print(string);
                    }
                }
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
                if(string.length() > 0) {
                    synchronized(lock) {
                        if(!textOnLine) {
                            for (int i = 0; i < indentationLevel; i++) {
                                super.print(" ");
                            }
                            if(string.charAt(string.length() - 1) != '\n') {
                                textOnLine = true;
                            }
                        } else if(string.charAt(string.length() - 1) == '\n') {
                            textOnLine = false;
                        }
                        super.print(string);
                    }
                }
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

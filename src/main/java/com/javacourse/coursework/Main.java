package com.javacourse.coursework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        for(;;) {
            System.out.println("enter command (type help to get commands list):");
            String[] rawCommand = scanner.nextLine().trim().split(" ");
            if(rawCommand.length > 0) {
                ArrayList<String> arguments = new ArrayList<>(Arrays.asList(rawCommand).subList(1, rawCommand.length));
                CommandHandler.handle(rawCommand[0], arguments);
            }
        }
    }

}
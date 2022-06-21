package com.example.regexvisualizer.regexutils;

import java.util.*;

public final class ParseHelper {

    // @purpose: Makes concatanation explicit
    // @example: stringToConvert = "abc" -> "a@b@c"
    public static String addConcatOperators(String stringToConvert) {
        StringBuilder returnString = new StringBuilder();

        // Begin by removing all whitespaces in the string
        stringToConvert = stringToConvert.replaceAll(" ", "");

        for(int i = 0; i < stringToConvert.length(); i++) {
            if(i + 1 < stringToConvert.length()) {

                boolean leftCharFlag = Character.isLetterOrDigit(stringToConvert.charAt(i)) || stringToConvert.charAt(i) == ')'
                        || stringToConvert.charAt(i) == '*';
                boolean letterOrDigit = Character.isLetterOrDigit(stringToConvert.charAt(i + 1));
                boolean rightCharFlag = letterOrDigit || (stringToConvert.charAt(i + 1) == '(');

                if(leftCharFlag && rightCharFlag) {
                    returnString.append(stringToConvert.charAt(i)).append(".");
                } else {
                    if(letterOrDigit) {
                        returnString.append(stringToConvert.charAt(i));
                    } else {
                        returnString.append(stringToConvert.charAt(i));
                    }
                }
            }
        }

        returnString.append(stringToConvert.charAt(stringToConvert.length() - 1));
        return returnString.toString();
    }

    // Compares two operators and determines if op1 has higher precedence than op2
    public static boolean higherOrEqualPrec(char op1, char op2, HashMap<Character, Integer> precedence) {
        return precedence.get(op1) >= precedence.get(op2);
    }

    public static boolean isOperator(char charToTest) {
        return charToTest == '*' || charToTest == '+' || charToTest == '.';
    }

    public static String convertToPostfix(String stringToConvert) {
        LinkedList<Character> outputQueue = new LinkedList<>();
        Stack<Character> operatorStack = new Stack<>();

        HashMap<Character, Integer> precedence = new HashMap<>();

        precedence.put('*', 3);
        precedence.put('.', 2);
        precedence.put('+', 1);
        precedence.put('(', 0);
        precedence.put(')', 0);

        for(int i = 0; i < stringToConvert.length(); i++) {
            char currentChar = stringToConvert.charAt(i);

            if(Character.isLetterOrDigit(currentChar)) {
                outputQueue.add(currentChar);
            }
            else if(currentChar == '(') {
                operatorStack.push(currentChar);
            }
            else if(currentChar == ')') {

                while(!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    outputQueue.add(operatorStack.pop());
                }

                operatorStack.pop();
            }
            else if(isOperator(currentChar) && !operatorStack.isEmpty() && operatorStack.peek() == '(') {
                operatorStack.push(currentChar);
            }
            else if(isOperator(currentChar)) {
                if(operatorStack.isEmpty()) {
                    operatorStack.push(currentChar);
                    continue;
                }

                while(!operatorStack.isEmpty()) {
                    if(higherOrEqualPrec(operatorStack.peek(), currentChar, precedence)) {
                        outputQueue.add(operatorStack.pop());
                    } else {
                        operatorStack.push(currentChar);
                        break;
                    }
                }


                if(operatorStack.isEmpty()) {
                    operatorStack.push(currentChar);
                }

            }
        }

        while(!operatorStack.isEmpty()) {
            outputQueue.add(operatorStack.pop());
        }

        return outputQueue.toString();
    }


    public static NFA parseRegex(String regex) {
        regex = addConcatOperators(regex);
        regex = convertToPostfix(regex);

        System.out.println("Regex: " + regex);

        Stack<NFA> nfaStack = new Stack<>();

        for(int i = 0; i < regex.length(); i++) {
            char currentChar = regex.charAt(i);

            if(Character.isLetterOrDigit(currentChar)) {
                NFA symbolNFA = NFA.symbolNFA(currentChar);
                nfaStack.push(symbolNFA);
            }
            else if(isOperator(currentChar)) {
                switch (currentChar) {
                    case '*' -> {
                        NFA topNFA = nfaStack.pop();
                        NFA kleeneNFA = NFA.kleeneNFA(topNFA);
                        nfaStack.push(kleeneNFA);
                    }
                    case '.' -> {
                        NFA concatNFA1 = nfaStack.pop();
                        NFA concatNFA2 = nfaStack.pop();
                        NFA concatNFA = NFA.concatenatedNFA(concatNFA2, concatNFA1);
                        nfaStack.push(concatNFA);
                    }
                    case '+' -> {
                        NFA unionNFA1 = nfaStack.pop();
                        NFA unionNFA2 = nfaStack.pop();
                        NFA unionedNFA = NFA.unionNFA(unionNFA1, unionNFA2);
                        nfaStack.push(unionedNFA);
                    }
                }
            }
        }

        return nfaStack.pop();
    }
}

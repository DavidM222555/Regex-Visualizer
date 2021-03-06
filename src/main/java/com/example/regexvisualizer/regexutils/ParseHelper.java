package com.example.regexvisualizer.regexutils;

import java.util.*;

public final class ParseHelper {

    /**
     * @return Make concatenation explicit in stringToConvert and then return it
     */
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

    /**
     * @param precedence Hashmap of operators to their precedence value
     * @return Returns whether op1 has higher or equal precedence to op2
     */
    public static boolean higherOrEqualPrec(char op1, char op2, HashMap<Character, Integer> precedence) {
        return precedence.get(op1) >= precedence.get(op2);
    }

    /**
     * @return Returns whether or not charToTest is a valid operator according to our regex specification
     */
    public static boolean isOperator(char charToTest) {
        return charToTest == '*' || charToTest == '+' || charToTest == '.';
    }


    /**
     * Converts a string in infix notation to a string in postfix notation using the Shunting-Yard algorithm.
     * This is primarily done because
     * @param stringToConvert A string in infix notation
     * @return A string in postfix notation representing stringToConvert
     */
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


    /**
     * Converts a regex to a complete NFA that can be used to parse a text.
     * @param regex String for our regex
     * @return The complete NFA for recognizing strings in our regex
     */
    public static NFA parseRegex(String regex) {
        regex = addConcatOperators(regex);
        regex = convertToPostfix(regex);

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

    /**
     * Find all indices in text where regex matches.
     * @return Set of indices that correspond to places in text where regex matched.
     */
    public static Set<Integer> getWordsThatMatchRegex(String text, String regex) {
        List<Integer> highlightIndices = new ArrayList<>();
        NFA nfaForRegex = parseRegex(regex);

        Set<Integer> highlightedIndices = new HashSet<>();

        for(int startIndex = 0; startIndex <= text.length(); startIndex++) {
            for(int endIndex = startIndex; endIndex <= text.length(); endIndex++) {
                String substring = text.substring(startIndex, endIndex);

                // Does our regex match the string?
                if(nfaForRegex.readString(substring)) {
                    for(int i = startIndex; i < endIndex; i++) {
                        highlightedIndices.add(i);
                    }
                }
            }
        }

        return highlightedIndices;
    }
}

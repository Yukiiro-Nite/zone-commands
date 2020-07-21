package me.yukiironite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class Solver {
  public static Map<String, Integer> operatorPrecedence;

  static {
    operatorPrecedence = new HashMap<String, Integer>();

    // Arithmetic Operators
    operatorPrecedence.put("^", 7);
    operatorPrecedence.put("*", 6);
    operatorPrecedence.put("/", 6);
    operatorPrecedence.put("+", 5);
    operatorPrecedence.put("-", 5);

    // Binary Operators
    operatorPrecedence.put(">", 4);
    operatorPrecedence.put(">=", 4);
    operatorPrecedence.put("<", 4);
    operatorPrecedence.put("<=", 4);
    operatorPrecedence.put("==", 3);
    operatorPrecedence.put("!=", 3);
    operatorPrecedence.put("&&", 2);
    operatorPrecedence.put("||", 1);
  }

  public static double expandAndSolve(String script, Player player, double defaultValue) {
    if(script == null) {
      return defaultValue;
    } else {
      String expandedScript = expand(script, player);

      return solve(expandedScript);
    }
  }

  public static boolean expandAndSolveBool(String script, Player player) {
    if(script == null) {
      return false;
    } else {
      String expandedScript = expand(script, player);

      return solve(expandedScript) != 0;
    }
  }

  public static String expand(String script, Player player) {
    Pattern pattern = Pattern.compile("(\\{\\S+\\})");
    Matcher matcher = pattern.matcher(script);
    ArrayList<String> captures = new ArrayList<String>();
    
    while(matcher.find()) {
      captures.add(matcher.group());
    }

    for(int i = 0; i < captures.size(); i++) {
      String playerValue = getPlayerValue(captures.get(i), player);
      script = script.replaceAll(Pattern.quote(captures.get(i)), playerValue);
    }

    return script;
  }

  public static String getPlayerValue(String capture, Player player) {
    switch(capture) {
      case "{player}": return player.getDisplayName();
      case "{player.x}": return "" + player.getLocation().getX();
      case "{player.y}": return "" + player.getLocation().getY();
      case "{player.z}": return "" + player.getLocation().getZ();
      default: return "";
    }
  }

  // Thank you wikipedia: https://en.wikipedia.org/wiki/Shunting-yard_algorithm
  public static double solve(String script) {
    Queue<String> input = new LinkedList<String>(Arrays.asList(script.split(" ")));
    Queue<String> output = new LinkedList<String>();
    Stack<String> operatorStack = new Stack<String>();

    while(input.peek() != null) {
      String token = input.poll();
      Double num = tryParseDouble(token);

      if(num != null) {
        output.add("" + num);
      } else if(isOperator(token)) {
        while(shouldExpelOperator(operatorStack, token)) {
          output.add(operatorStack.pop());
        }
        operatorStack.push(token);
      } else if(token.equals("(")) {
        operatorStack.push(token);
      } else if(token.equals(")")) {
        while(!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
          output.add(operatorStack.pop());
        }
        if(!operatorStack.isEmpty() && operatorStack.peek().equals("(")) {
          operatorStack.pop();
        }
      }
    }

    while(!operatorStack.isEmpty()) {
      String token = operatorStack.pop();
      if(isOperator(token)) {
        output.add(token);
      }
    }

    double response = calculateFromPostfix(output);

    return response;
  }

  public static Double tryParseDouble(String str) {
    try {
      double num = Double.parseDouble(str);
      return num;
    } catch(NumberFormatException e) {
      return null;
    }
  }

  public static boolean isOperator(String str) {
    return operatorPrecedence.containsKey(str);
  }

  public static boolean shouldExpelOperator(Stack<String> operatorStack, String current) {
    if(operatorStack.isEmpty()) {
      return false;
    } else {
      String top = operatorStack.peek();
      return getPrecedence(top) > getPrecedence(current)
        || (
          getPrecedence(top) == getPrecedence(current)
          && isLeftAssociative(current)
        ) 
        && !top.equals("(");
    }
  }

  public static int getPrecedence(String operator) {
    return operatorPrecedence.getOrDefault(operator, 0);
  }

  public static boolean isLeftAssociative(String operator) {
    return operator.equals("-")
      || operator.equals("/");
  }

  public static double calculateFromPostfix(Queue<String> input) {
    Stack<Double> output = new Stack<Double>();

    while(input.peek() != null) {
      String token = input.poll();
      Double num = tryParseDouble(token);
      if(num != null) {
        output.push(num);
      } else if(isOperator(token)) {
        Double arg2 = output.pop();
        Double arg1 = output.pop();
        Double result = doOperation(arg1, arg2, token);

        output.push(result);
      }
    }

    return output.pop();
  }

  public static double doOperation(double arg1, double arg2, String operator) {
    switch(operator) {
      case "^": return Math.pow(arg1, arg2);
      case "/": return arg1 / arg2;
      case "*": return arg1 * arg2;
      case "-": return arg1 - arg2;
      case "+": return arg1 + arg2;
      case ">": return arg1 > arg2 ? 1.0 : 0.0;
      case ">=": return arg1 >= arg2 ? 1.0 : 0.0;
      case "<": return arg1 < arg2 ? 1.0 : 0.0;
      case "<=": return arg1 <= arg2 ? 1.0 : 0.0;
      case "==": return arg1 == arg2 ? 1.0 : 0.0;
      case "!=": return arg1 != arg2 ? 1.0 : 0.0;
      case "&&": return (arg1 != 0.0) && (arg2 != 0.0) ? 1.0 : 0.0;
      case "||": return (arg1 != 0.0) || (arg2 != 0.0) ? 1.0 : 0.0;
      default: return 0.0;
    }
  }
}
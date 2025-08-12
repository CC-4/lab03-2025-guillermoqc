/*
    Laboratorio No. 3 - Recursive Descent Parsing
    CC4 - Compiladores

    Clase que representa el parser

    Actualizado: agosto de 2021, Luis Cu
*/
import java.util.LinkedList;
import java.util.Stack;

public class Parser {

    // Puntero next que apunta al siguiente token
    private int next;
    // Stacks para evaluar en el momento
    private Stack<Double> operandos;
    private Stack<Token> operadores;
    // LinkedList de tokens
    private LinkedList<Token> tokens;

    // Funcion que manda a llamar main para parsear la expresion
    public boolean parse(LinkedList<Token> tokens) {
        this.tokens = tokens;
        this.next = 0;
        this.operandos = new Stack<Double>();
        this.operadores = new Stack<Token>();

        // Recursive Descent Parser
        // Imprime si el input fue aceptado
        System.out.println("Aceptada? " + S());

        // Shunting Yard Algorithm
        // Imprime el resultado de operar el input
        System.out.println("Resultado: " + this.operandos.peek());

        // Verifica si terminamos de consumir el input
        if(this.next != this.tokens.size()) {
            return false;
        }
        return true;
    }

    // Verifica que el id sea igual que el id del token al que apunta next
    // Si si avanza el puntero es decir lo consume.
    private boolean term(int id) {
        if(this.next < this.tokens.size() && this.tokens.get(this.next).equals(id)) {
            
            // Codigo para el Shunting Yard Algorithm
            /*
            if (id == Token.NUMBER) {
                // Encontramos un numero
                // Debemos guardarlo en el stack de operandos
                operandos.push( this.tokens.get(this.next).getVal() );

            } else if (id == Token.SEMI) {
                // Encontramos un punto y coma
                // Debemos operar todo lo que quedo pendiente
                while (!this.operadores.empty()) {
                    popOp();
                }
                
            } else {
                // Encontramos algun otro token, es decir un operador
                // Lo guardamos en el stack de operadores
                // Que pushOp haga el trabajo, no quiero hacerlo yo aqui
                pushOp( this.tokens.get(this.next) );
            }
            */

            this.next++;
            return true;
        }
        return false;
    }

    // Funcion que verifica la precedencia de un operador
    private int pre(Token op) {
        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        switch(op.getId()) {
            case Token.PLUS:
            case Token.MINUS:
                return 1;
            case Token.MULT:
            case Token.DIV:
            case Token.MOD:
                return 2;
            case Token.EXP:
                return 3;
            default:
                return -1;
        }
    }

    private void popOp() {
        Token op = this.operadores.pop();

        /* TODO: Su codigo aqui */

        /* El codigo de esta seccion se explicara en clase */

        if (op.equals(Token.PLUS)) {
            double b = this.operandos.pop();
            double a = this.operandos.pop();
            // print para debug, quitarlo al terminar
            System.out.println("suma " + a + " + " + b);
            this.operandos.push(a + b);
        } else if (op.equals(Token.MINUS)) {
            double b = this.operandos.pop();
            double a = this.operandos.pop();
            // print para debug, quitarlo al terminar
            System.out.println("resta " + a + " - " + b);
            this.operandos.push(a - b);
        } else if (op.equals(Token.MULT)) {
            double b = this.operandos.pop();
            double a = this.operandos.pop();
            // print para debug, quitarlo al terminar
            System.out.println("mult " + a + " * " + b);
            this.operandos.push(a * b);
        } else if (op.equals(Token.DIV)) {
            double b = this.operandos.pop();
            double a = this.operandos.pop();
            // print para debug, quitarlo al terminar
            System.out.println("div " + a + " / " + b);
            this.operandos.push(a / b);
        } else if (op.equals(Token.MOD)) {
            double b = this.operandos.pop();
            double a = this.operandos.pop();
            // print para debug, quitarlo al terminar
            System.out.println("mod " + a + " % " + b);
            this.operandos.push(a % b);
        } else if (op.equals(Token.EXP)) {
            double b = this.operandos.pop();
            double a = this.operandos.pop();
            // print para debug, quitarlo al terminar
            System.out.println("exp " + a + " ^ " + b);
            this.operandos.push(Math.pow(a, b));
        }
    }

    private void pushOp(Token op) {
        /* TODO: Su codigo aqui */

        /* Casi todo el codigo para esta seccion se vera en clase */
        
        // Si no hay operandos automaticamente ingresamos op al stack

        // Si si hay operandos:
            // Obtenemos la precedencia de op
            // Obtenemos la precedencia de quien ya estaba en el stack
            // Comparamos las precedencias y decidimos si hay que operar
            // Es posible que necesitemos un ciclo aqui, una vez tengamos varios niveles de precedencia
            // Al terminar operaciones pendientes, guardamos op en stack

        while (!this.operadores.isEmpty() && pre(this.operadores.peek()) >= pre(op)) {
            popOp();
        }
        this.operadores.push(op);
    }

    private boolean S() {
        return E() && term(Token.SEMI);
    }

    private boolean E() {
        // Aquí parseamos la expresión E según la gramática dada

        // Caso base: número, paréntesis o menos unario

        if (next >= tokens.size()) return false;

        Token tokenActual = tokens.get(next);

        if (tokenActual.equals(Token.NUMBER)) {
            // Consumimos un número
            operandos.push(tokenActual.getVal());
            next++;
        } else if (tokenActual.equals(Token.LPAREN)) {
            // Consumimos '('
            next++;
            if (!E()) return false;
            if (!term(Token.RPAREN)) return false;
        } else if (tokenActual.equals(Token.MINUS)) {
            // Menos unario
            next++;
            if (!E()) return false;
            // Aplicamos el menos unario
            double val = operandos.pop();
            operandos.push(-val);
            return true;
        } else {
            return false;
        }

        // Mientras haya operadores válidos, seguimos aplicando Shunting Yard

        while (next < tokens.size()) {
            Token op = tokens.get(next);
            if (op.equals(Token.PLUS) || op.equals(Token.MINUS) ||
                op.equals(Token.MULT) || op.equals(Token.DIV) ||
                op.equals(Token.MOD) || op.equals(Token.EXP)) {
                
                pushOp(op);
                next++;

                if (!E()) return false;
            } else {
                break;
            }
        }

        // Procesamos operadores pendientes

        while (!operadores.isEmpty()) {
            popOp();
        }

        return true;
    }
}

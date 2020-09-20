package compiler.parser;
import compiler.scanner.Scanner;
import compiler.lib.Debug;

public class Parser {

    public Debug debug;

    public Parser(Scanner scan)
    {
        System.out.println("Stage: parser");
    }

    public void setDebuger(Debug d)
    {
        debug = d;
        debug.println("debugging: Parser");
    }

}

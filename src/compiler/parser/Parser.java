package compiler.parser;
import compiler.scanner.Scanner;
import compiler.lib.Debug;
import compiler.lib.OutputFile;

public class Parser {

    public Debug debug;
    public OutputFile of;

    public Parser(Scanner scan) throws Exception
    {
        //System.out.println("Stage: parser");
        String msg = "stage: Parser";
        of = scan.getOutFile();
        System.out.println(msg);
        of.writeln(msg);
    }

    public void setDebuger(Debug d)
    {
        debug = d;
        debug.println("debugging: Parser");
    }

    public OutputFile getOutFile()
    {
        return of;
    }

}

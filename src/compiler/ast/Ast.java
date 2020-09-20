package compiler.ast;
import compiler.parser.Parser;
import compiler.lib.Debug;

public class Ast {

    public Debug debug;

    public Ast(Parser parser)
    {
        System.out.println("Stage: ast");
    }

    public void setDebuger(Debug d)
    {
        debug = d;
        debug.println("Debugging: Ast");
    }
}

package compiler.ast;
import compiler.parser.Parser;
import compiler.lib.Debug;
import compiler.lib.OutputFile;

public class Ast {

    public Debug debug;
    public OutputFile of;

    public Ast(Parser parser) throws Exception
    {
        //System.out.println("Stage: ast");
        of = parser.getOutFile();
        String msg = "Stage: Ast";
        System.out.println(msg);
        of.writeln(msg);
    }

    public void setDebuger(Debug d)
    {
        debug = d;
        debug.println("Debugging: Ast");
    }

    public OutputFile getOutFile()
    {
        return of;
    }
}

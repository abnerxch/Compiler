package compiler;

import java.util.regex.Pattern;
import compiler.scanner.Scanner;
import compiler.parser.Parser;
import compiler.ast.Ast;
import compiler.codegen.Codegen;
import compiler.irt.Irt;
import compiler.semantic.Semantic;

public class Compiler {
    public static void main(String[] args) {
        //System.out.println("Test");
        String inputFilename = "";
        String outputFilename = "";
        String target = "";
        String opt = "";
        String debug = "";
        String option = "";
        String option2 = "";
        /*String filename = "";
        if(args.length>2)
        {
            for (int i=0; i<args.length; i++)
            {
                if(i==args.length-3)
                {
                    option = args[i];
                }else if(i==args.length-2){
                    if(args[i].substring(0,1).equals("-"))
                    {
                        option = args[i];
                    }
                    else
                        {
                        option2 = args[i];*/
        if(args.length > 0){
            for(int i = 0; i < args.length; i++)
            {
                switch (args[i])
                {
                    case "-o":
                        outputFilename = args[++i];
                        break;
                    case "-target":
                        target = args[++i];
                        break;
                    case "-opt":
                        opt = args[++i];
                        System.out.println("Optimización " + opt);
                        break;
                    case "-debug":
                        debug = args[++i];
                        System.out.println("Debug " + args[++i]);
                        break;
                    case "-h":
                        printHelp();
                        exit(0);
                        break;
                    default:
                        if(Pattern.matches("[^\\-].*\\.[d][c][f]", args[i]) && args.length-1 == i)
                        {
                            inputFilename = args[i];
                        }
                        else
                            {
                                System.err.println("Opción " + args[i] + " no reconocida");
                                System.exit(1);
                            }
                        break;
                }
            }

            Scanner scan;
            Parser parser;
            Ast ast;
            Semantic semantic;
            Irt irt;
            Codegen codegen;

            if(!target.equals(""))
            {
                if(!inputFilename.equals(""))
                {
                    if(!outputFilename.equals(""))
                    {
                        outputFilename = inputFilename.substring(0, inputFilename.lastIndexOf('.')) + ".s";
                }
            //}
                    //}
                /*}
                else if(i==args.length-1)
                {
                    if(args[i].substring(0,1).equals("-"))
                    {
                        option = args[i];
                    }
                    else
                        {
                        filename = args[i];*/

                System.out.println("Input: " + inputFilename);
                System.out.println("Output: " + outputFilename);

                // IMPORTANTE

                if (target.equals("scan") ||
                        target.equals("parse") ||
                        target.equals("ast") ||
                        target.equals("semantic") ||
                        target.equals("irt") ||
                        target.equals("codegen"))
                    {
                    scan = new Scanner(inputFilename);

                    if (target.equals("scan")) exit(0);
                    parser = new Parser(scan);

                    if(target.equals("parse")) exit(0);
                    ast = new Ast(parser);

                    if(target.equals("ast")) exit(0);
                    semantic = new Semantic(ast);

                    if(target.equals("semantic")) exit(0);
                    irt = new Irt(semantic);

                    if(target.equals("irt")) exit(0);
                    codegen = new Codegen(irt);

                }

            }

            else
            {
                System.err.println("Error: no se indicó el archivo");
            }
        }
            else {
            System.err.println("Error: no se indicó el target");
        }
    }

        else
        {
            printHelp();
        }
    }



    public static void printHelp()
        {
            System.out.println("-o <outname>	Escribir el output a un archivo de texto llamado <outname>.\n");
            System.out.println("-target <stage>	Donde <stage> es uno de: scan, parse, ast, semantic, irt, codegen; la compilación debe proceder hasta la etapa indicada.");
            System.out.println("Por ejemplo, si <stage> es scan, una instancia de scan debe ser creada imprimiendo en el archivo de salida \"stage: scanning\".");
            System.out.println("Si es parse una instancia de parser debe ser creada a partir de la instancia de scanner imprimiendo \"stage: parsing\",");
            System.out.println("además del mensaje de scanner y así sucesivamente.\n");
            System.out.println("-opt <optimzation>	<optimization> es uno de: constant, algebraic; la compilación debe hacer solo la optimización que se le pida,");
            System.out.println("y debe imprimir como en -target \"optimizing: constant folding\" o \"optimizing: algebraic simplification\".\n");
            System.out.println("-debug <stage>	Imprimir información de debugging. Debe haber un mensaje por cada etapa listada en <stage> de la forma \"Debugging <stage\".");
            System.out.println("<stage> tiene las mismas opciones de -target, con la diferencia que se pueden \"debuggear\" varias etapas,");
            System.out.println("separandolas con ':' de la forma scan:parse:etc.\n");
            System.out.println("-h		Muestra esta ayuda al usuario.");
        }

    public static void exit(int i)
    {
        System.exit(i);
    }
}
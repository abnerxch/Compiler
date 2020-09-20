package compiler;

import java.util.regex.Pattern;
import compiler.scanner.Scanner;
import compiler.parser.Parser;
import compiler.ast.Ast;
import compiler.codegen.Codegen;
import compiler.irt.Irt;
import compiler.semantic.Semantic;
import compiler.opt.Algebraic;
import compiler.opt.ConstantFolding;
import compiler.lib.Debug;
import compiler.lib.ErrorHandler;
import compiler.lib.OutputFile;

public class Compiler
{
    public static void main(String[] args) throws Exception {
        try
        {
        //System.out.println("Test");
            String inputFilename = "";
            String outputFilename = "";
            String target = "";
            String opt = "";
            String debug[] = null;
            String option = "";
            String option2 = "";

            if (args.length > 1) {
                for (int i = 0; i < args.length; i++) {
                    switch (args[i]) {
                        case "-o":
                            outputFilename = args[++i];
                            break;
                        case "-target":
                            target = args[++i];
                            break;
                        case "-opt":
                            opt = args[++i];
                            //System.out.println("Optimización " + opt);
                            break;
                        case "-debug":
                            debug = args[++i].split(":");
                            //System.out.println("Debug " + args[++i]);
                            break;
                        case "-h":
                            printHelp();
                            exit(0);
                            break;
                        default:
                            if (Pattern.matches("[^\\-].*\\.dcf", args[i]) && args.length - 1 == i)
                            {
                                inputFilename = args[i];
                            }
                            else if (Pattern.matches("[^\\-].*(^\\.)", args[i]))
                            {
                                throw new ErrorHandler("Error en la opción: " + args[i]);
                            }
                            else if (!Pattern.matches("[^\\-].*\\.dcf", args[i]) && args.length - 1 == i)
                            {
                                throw new ErrorHandler("Error en archivo: " + args[i]);
                            }
                            else
                                {
                                    throw new ErrorHandler("Error en la opción: " + args[i]);
                            }
                            break;
                    }
                }

            }
            else if (args.length == 0)
            {
                printHelp();
            }
            else if ((args[0].equals("-h")) && (args.length == 1))
            {
                printHelp();
            }

            Scanner scan;
            Parser parse;
            Ast ast;
            Semantic semantic;
            Irt irt;
            Codegen codegen;
            ConstantFolding cf;
            Algebraic algebraic;
            Debug deb = new Debug();
            OutputFile outFile;

            if ((args.length > 0) && (!args[0].equals("-h")))
            {
                if (!target.equals(""))
                {
                    if (!inputFilename.equals(""))
                    {
                        if (!outputFilename.equals(""))
                        {
                            outputFilename = inputFilename.substring(0, inputFilename.lastIndexOf('.')) + ".s";
                        }

                        outFile = new OutputFile( outputFilename );

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
                            scan = new Scanner(inputFilename, outFile);

                            if (buscarString(debug, "scan")) scan.setDebuger(deb);

                            if (target.equals("scan")) exit(0);
                            parse = new Parser(scan);

                            if (buscarString(debug, "parse")) parse.setDebuger(deb);

                            if (target.equals("parse")) exit(0);
                            ast = new Ast(parse);

                            if (buscarString(debug, "ast")) ast.setDebuger(deb);

                            if (target.equals("ast")) exit(0);
                            semantic = new Semantic(ast);

                            if (buscarString(debug, "semantic")) semantic.setDebuger(deb);

                            if (target.equals("semantic")) exit(0);
                            irt = new Irt(semantic);

                            if (buscarString(debug, "irt")) irt.setDebuger(deb);

                            if (target.equals("irt")) exit(0);
                            codegen = new Codegen(irt);

                            if (buscarString(debug, "codegen")) codegen.setDebuger(deb);

                            if (!opt.equals(""))
                            {
                                if (!inputFilename.equals(""))
                                {
                                    if (outputFilename.equals(""))
                                    {
                                        outputFilename = inputFilename.substring(0, inputFilename.lastIndexOf('.')) + ".s";
                                    }
                                    //outFile = new OutputFile(outputFilename);
                                    System.out.println("Input: " + inputFilename);
                                    System.out.println("Output: " + outputFilename);

                                    if (opt.equals("constant"))
                                    {
                                        cf = new ConstantFolding(inputFilename);
                                        exit(0);
                                    }
                                    else if (opt.equals("algebraic"))
                                    {
                                        algebraic = new Algebraic(inputFilename);
                                        exit(0);
                                    }
                                    else
                                        {
                                            throw new ErrorHandler("Error de optimización no válida: " + opt);
                                    }
                                }
                                else
                                    {
                                        throw new ErrorHandler("Error: no indicó archivo, opción u opción inválida");
                                }
                            }
                            else
                                {
                                    throw new ErrorHandler("Error: no indicó archivo, opción u opción inválida");
                            }

                        }
                        else
                            {
                                throw new ErrorHandler("Opción de target no válido: " + target);
                        }

                    }
                    else
                        {
                            throw new ErrorHandler("Error: no indicó archivo, opción u opción inválida");
                    }

                }
                else
                    {
                        throw new ErrorHandler("Error: no indicó archivo, opción u opción inválida");

                }
            }
        }

        catch(ErrorHandler e)
        {
            System.err.println(e.getMessage());
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

    public static boolean buscarString(String[] array, String str)
    {
        if (array != null)
        {
            for (String e : array)
            {
                if (e.equals(str))
                {
                    return true;
                }
            }
        }
        return false;
    }
}
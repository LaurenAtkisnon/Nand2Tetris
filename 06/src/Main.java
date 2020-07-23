import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static Map<String, Integer> symbols = new HashMap<>();
    private static Map<String, String> comp = new HashMap<>();
    private static Map<String, String> dest = new HashMap<>();
    private static Map<String, String> jump = new HashMap<>();
    private static Map<String, Integer> vars = new HashMap<>();


     static {
    	 //symbol table 
    	 
        symbols.put("R0", 0);
        symbols.put("R1", 1);
        symbols.put("R2", 2);
        symbols.put("R3", 3);
        symbols.put("R4", 4);
        symbols.put("R5", 5);
        symbols.put("R6", 6);
        symbols.put("R7", 7);
        symbols.put("R8", 8);
        symbols.put("R9", 9);
        symbols.put("R10", 10);
        symbols.put("R11", 11);
        symbols.put("R12", 12);
        symbols.put("R13", 13);
        symbols.put("R14", 14);
        symbols.put("R15", 15);
        symbols.put("SCREEN", 16384);
        symbols.put("KBD", 24576);
        symbols.put("SP", 0);
        symbols.put("LCL", 1);
        symbols.put("ARG", 2);
        symbols.put("THIS", 3);
        symbols.put("THAT", 4);

        //comp bits 
        comp.put("0", "0101010");
        comp.put("1", "0111111");
        comp.put("-1", "0111010");
        comp.put("D", "0001100");
        comp.put("A", "0110000");
        comp.put("M", "1110000");
        comp.put("!D", "0001101");
        comp.put("!A", "0110001");
        comp.put("!M", "1110001");
        comp.put("-D", "0001111");
        comp.put("-A", "0110011");
        comp.put("-D", "1110011");
        comp.put("D+1", "0011111");
        comp.put("A+1", "0110111");
        comp.put("M+1", "1110111");
        comp.put("D-1", "0001110");
        comp.put("A-1", "0110010");
        comp.put("M-1", "1110010");
        comp.put("D+A", "0000010");
        comp.put("D+M", "1000010");
        comp.put("D-A", "0010011");
        comp.put("D-M", "1010011");
        comp.put("A-D", "0000111");
        comp.put("M-D", "1000111");
        comp.put("D&A", "0000000");
        comp.put("D&M", "1000000");
        comp.put("D|A", "0010101");
        comp.put("D|M", "1010101");

        //dest bits 
        dest.put("", "000");
        dest.put("M", "001");
        dest.put("D", "010");
        dest.put("MD", "011");
        dest.put("A", "100");
        dest.put("AM", "101");
        dest.put("AD", "110");
        dest.put("AMD", "111");

        //jump bits 
        jump.put("", "000");
        jump.put("JGT", "001");
        jump.put("JEQ", "010");
        jump.put("JGE", "011");
        jump.put("JLT", "100");
        jump.put("JNE", "101");
        jump.put("JLE", "110");
        jump.put("JMP", "111");

    }

    public static void main(String[] args) {
        List<String> asmlist = new LinkedList<>();
        List<String> machinelist = new LinkedList<>();
        String infile = args[0];
        String outfile = args[0].replaceFirst("[.]", ".hack"); //should create a the new file

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(infile));
            reader.close();
        } catch (IOException e) {
            System.err.println("Error, somethings not working! Go back to the drawing board!");
            e.printStackTrace();
            System.exit(1);
        }

        for (int i = 0; i < asmlist.size(); i++) {
            if (asmlist.get(i).startsWith("(")) {
                Matcher m = Pattern.compile("\\((.*?)\\)").matcher(asmlist.get(i));
                while(m.find()) {
                    symbols.put(m.group(1), i);
                }
                asmlist.remove(i);
                i--;
            }
        }  
        

        String result;
        for (String line : asmlist) {
            if (line.startsWith("@")) {
                result = InstructionA(line); //a instruction
            } else {
                result = InstructionC(line); //c instruction
            }

            machinelist.add(result);
        }
    }

    private static String InstructionC(String line) {
        String prefix = "111";
        String dest_part = "";
        String comp_part = "";
        String jump_part = "";

        if (line.contains("=")) {
            dest_part = line.split("=")[0];
            comp_part = line.split("=")[1];
        }

        if (line.contains(";")) {
            if (line.contains("=")) {
                comp_part = comp_part.split(";")[0];
                jump_part = comp_part.split(";")[1];
            } else {
                comp_part = line.split(";")[0];
                jump_part = line.split(";")[1];
            }
        }

        return prefix + comp.get(comp_part) + dest.get(dest_part) + jump.get(jump_part); //should get the perfect 16 bit 
    }

    private static String InstructionA(String line) {
        String address;
        String a;

        a = line.replaceFirst("@", "");
        if (isNumeric(a)) {
            address = a;
        } else if (symbols.containsKey(a)) {
            address = String.valueOf(symbols.get(a));
        } else if (vars.containsKey(a)) {
            address = String.valueOf(vars.get(a));
        } else {
            int var_address = 16; // or priv static Integer var_address = 1 at the top if doesnt run
			vars.put(a, var_address);
            var_address++;
            address = String.valueOf(vars.get(a));
        }

        return mach2bin(address);
    }

    private static String mach2bin(String addr) {
    	
    	//parseInt is the representation of a decimal as a string just makes it look cleaner
        String bin = Integer.toBinaryString(Integer.parseInt(addr)); 

        for (int i = bin.length(); i < 16; i++) {
            bin = 0 + bin;
        }

        return bin;
    }
    public static boolean isNumeric(String str) {
        return str.matches("-?\\\\d+(\\\\.\\\\d+)?"); //guy from work helped me figure it out 
    }

   
}
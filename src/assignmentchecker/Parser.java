/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentchecker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sazzad
 */
public class Parser {
    
    public String getCode(String fileName) {
        String code = "";
        Scanner sc = null;
        try {
             sc = new Scanner(new File(fileName));
            for(int i=0;sc.hasNext(); i++)
            {
                String s = sc.nextLine();
                if(s.startsWith("//")) continue;        //single comment
                else if(s.startsWith("/*"))             //multiple line comment
                    while(sc.hasNext()){
                        s = sc.nextLine();
                        if(s.endsWith("*/")) break;
                    }
                
                else if(s.startsWith("#")) continue;            //header file  &  define
                else 
                {
                    String[] sp = s.split(" ");
                    
                    for (String st: sp) 
            
                if(st.length()>0) code += st+" ";
                if(code.contains("//"))code = code.substring(0,code.indexOf("//"));
//                System.out.println(code);
                }
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            sc.close();
        }
        return functionProcess(code);
        }
    
    private String functionProcess(String code)
    {
        String fcode="", fname="";
        int bracks=0, start=0, end=0, main = 0;
        ArrayList<Functions> functions = new ArrayList<>();
        String[] sp = code.split(" ");
        for(int i=0;i<sp.length;i++)
        {
//            System.out.println(i+"------->"+sp[i]);
            if(sp[i].contains("(") && (sp[i-1].contains("int") || sp[i-1].contains("float") || sp[i-1].contains("double") || sp[i-1].contains("long")  || sp[i-1].contains("char") || sp[i-1].contains("void") )) fname = sp[i].substring(0, sp[i].indexOf("("));
            if(sp[i].contains("{")) bracks++;
            if(sp[i].contains("}")) {
                bracks--;
                if(bracks == 0) 
                {
                    end = i;
                    for(int j= start; j<=end; j++) fcode += sp[j]+ " ";
                    Functions f = new Functions(fname, start, end);
                    f.setCode(fcode);
                    functions.add(f);
                    fcode = "";
                    start = i+1;
                    if(fname.equals("main")) main = functions.size()-1;
//                    System.out.println("function: "+ fname);
                }
                 continue;
            }
            
        }
        String mainCode = functions.get(main).getCode();
        sp = mainCode.split(" ");
        for(String s : sp)
        {
            if(s.contains("(")) 
            {
                fname = s.substring(0, s.indexOf("("));
//                System.out.println(fname);
                for(Functions fn : functions) if(fn.getName().equals(fname)) fn.foundf();
            }
        }
        
        String modCode = "";
        for(Functions fn : functions) 
            if(fn.found) modCode += fn.getCode();
        
//            System.out.println(fn.getName()+"------->"+fn.found);
            
        return modCode;
    }
    
    class Functions{
        private String name;
        private int start;
        private int end;
        private String code;
        private boolean found=false;
        
        public void foundf()
        {
            found = true;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Functions(String name, int start, int end) {
            this.name = name;
            this.start = start;
            this.end = end;
        }
        
        

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }
    }
    
}

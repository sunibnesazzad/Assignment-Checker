/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignmentchecker;

/**
 *
 * @author Sazzad
 */
public class C_File {
    
    String fileName = null;
	
	public C_File(String name){
		this.fileName = name;
	}
	
	public String getName(){
		int len = fileName.length();
		String name = "";
		char[] tempName = fileName.toCharArray();
		for(int i = len-1; i >= 0; i--){
			char c = fileName.charAt(i);
			if(c == '\\') break;
			else
				name += String.valueOf(c);
		}		
		StringBuffer buffer = new StringBuffer(name);
		return buffer.reverse().toString();
	}
    
    
}

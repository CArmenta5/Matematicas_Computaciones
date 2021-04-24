import java.util.Stack;

public class AnalizadorParentesis {	
	public static void analizar(String conjunto) {
		boolean flag = true;
		Stack <String> pila = new Stack<String>();
		for(int i = 0; i < conjunto.length(); i++) {
			String parentesis = conjunto.substring(i,i+1);
			if(parentesis.equals("{") || parentesis.equals("(") || parentesis.equals("[")) {
				pila.push(parentesis);
			}else if(parentesis.equals("}") || parentesis.equals(")") || parentesis.equals("]")){
				// Contiene elementos a comparar
				if(pila.isEmpty()) {
					flag = false;
					break;	
				}else {
					// existe la parentesis de cerradura
					if(pila.peek().equals("{") && parentesis.equals("}")) {
						pila.pop();
					}else if(pila.peek().equals("(") && parentesis.equals(")")){
						pila.pop();
					}else if(pila.peek().equals("[") && parentesis.equals("]")){
						pila.pop();
					}else{
						// no existe parentesis de cerradura
						flag = false;
						break;
					}
				}	
			}
		}
		if(!pila.isEmpty()) {
			// checar remanentes
			flag = false;
		}
		if(flag) {
			System.out.println("Válido: Parentesis bien balanceados");
		}else {
			System.out.println("Inválido: La cadena no cumplió");	
		}		
	}
	
	
	public static void main(String[] args) {
		// Para utilizar el programa ponga sus parentesis en formato de String dentro de la variable parentesis
		String parentesis = "{{[({})}}}";
		analizar(parentesis);



	}
}


public class Reemplazador {
	private String cadenaOriginal ,
					patronER,
					cadenaReemplazadora,
					resultado;
	private String[] arrayER;
	private int indexInicio,
				indexFinal;	
	
	public Reemplazador(String cadenaOriginal, String patronER, String cadenaReemplazadora) {
		this.cadenaOriginal = this.resultado= cadenaOriginal;
		this.patronER = patronER;
		this.cadenaReemplazadora = cadenaReemplazadora;
		this.indexInicio = this.indexFinal = 0;
		int cantUnion = contadorCoincidencias(patronER);
		this.arrayER = new String[cantUnion + 1];
		fillArray();	
	}
	
	public void fillArray() {
		int cont = 0;
		int cont2 = 0;
		String cadena="";
		while(cont <= patronER.length()-1) {
			if(patronER.charAt(cont) != '+') {
				cadena += patronER.charAt(cont);
			}else {
				this.arrayER[cont2] = cadena;
				cadena = "";
				cont2++;
			}
			cont++;
		}
		this.arrayER[cont2] = cadena;
	}
	
	//Checa las ocurrencias dentro de una palabra
	public int contadorCoincidencias(String cadena) {
		if(cadena.length() == 1){
			if(cadena.equals("+")){
				return 1;
			}else {
				return 0;	
			}
		}else {
			if(cadena.charAt(0)  == '+') {
				return contadorCoincidencias(cadena.substring(1)) + 1;
			}else {
				return contadorCoincidencias(cadena.substring(1));	
			}
		}
	}
	
	public void reemplazador() {
		int i, j, contER, tries, pointerA,pointerB,pointerC;
		this.indexInicio = this.indexFinal = i = j = contER = tries= pointerA = pointerB = pointerC = 0;
		boolean flag = false;
		boolean flagStar = false;
		/*Limite de chequeo */
		while(this.indexInicio < this.cadenaOriginal.length()) {
			tries = 0;
			/*Partes de la ER de la unión*/
			while(tries < this.arrayER.length) {
				/*Se encontró match o llegó al límite*/
				if(flag==true) {
					//Reiniciar los indeces
					j = this.indexFinal;
					i = this.indexInicio;
					flag=false;
				/*Se reinicia el chequeo por otra parte del ER*/
				}else {
					this.indexFinal = j;
					this.indexInicio = i;
				}
				System.out.println("Parte de la expresión #: "+tries+", i = "+i+" j = "+j);
				//Se checa la ER de concateniación o cerradura de Kleene
				while(pointerA < this.arrayER[tries].length() && flag == false && this.indexFinal <this.cadenaOriginal.length()) {	
					
					/*Inicializar a,b,c dependeiendo del length*/
					
					// Se valida que c llegue hasta al final y que a agregue hasta el ultimo elemento posible
					
					if(pointerC != this.arrayER[tries].length() && pointerA != pointerC) {
						
						System.out.println(this.cadenaOriginal.charAt(this.indexFinal)+"-"+this.arrayER[tries].charAt(pointerA));
						
						//checo que la derecha del simbolo se encuentre esta operación
						if(this.arrayER[tries].charAt(pointerB)=='*') {
							// CERRADURA DE  KLEENE
							// CASO 1: Elemento igual a la derecha (a*a) -> a/aa/aaa/aaa...a en donde no la 2da a no es cerradura manda a hacer bandera especial
							if(this.arrayER[tries].charAt(pointerA) == this.arrayER[tries].charAt(pointerA+2) && this.arrayER[tries].charAt(pointerA+2) != '*') {
								//validar al menos exista una a
								if(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA)) {
									this.indexFinal++;
									flag = true; //Se ha validado que exista al menos una vez
								}
								//Agregamos hasta todas la posibles
								while(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA)) {
									this.indexFinal++;
								}
								if(flag) {
									System.out.println("Se ha llegado a un match dentro del CASO 1");
								}
							}							
							//CASO 2: Elemento diferente a la derecha (a*b) -> b/ab/aab/aaa...b en donde no la 2da a no es cerradura manda a hacer bandera especial
							else if(this.arrayER[tries].charAt(pointerA) != this.arrayER[tries].charAt(pointerA+2) && this.arrayER[tries].charAt(pointerA+2) != '*') {
						    	//Si existe podemos dejarle el trabajo a la concatenación para que continue...
							  //CASO 2.1: No existe a*, pero sí b
							   if(this.arrayER[tries].charAt(pointerC) == this.cadenaOriginal.charAt(this.indexFinal)) {
									this.indexFinal++;
									flag = true; //Se ha validado que exista al menos b, pero no a*
									System.out.println("Se ha llegado a un match dentro del CASO 2.1");
							   }
							   //CASO 2.2: Sí existe a*b
							   else if(this.arrayER[tries].charAt(pointerA) == this.cadenaOriginal.charAt(this.indexFinal)) {
								   this.indexFinal++;
								   while(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA)) {
										this.indexFinal++;
									}
								    if(this.cadenaOriginal.charAt(this.indexFinal)==this.arrayER[tries].charAt(pointerC)) {
										flag = true; //Se ha validado que exista al menos b, pero no a*
										System.out.println("Se ha llegado a un match dentro del CASO 2.2");
								    }
							   } 			   
						  		}
							//Caso 3: Cerradura de kleene individual
							 else {
								  // Casp 3.1: Producto de la palabra vacía
								  if(this.arrayER[tries].charAt(pointerA) != this.cadenaOriginal.charAt(this.indexFinal)) {
									  flag =true;
       								  System.out.println("Se ha llegado a un match dentro del CASO 3.1");
								  }
								  // CASO 3.2: Sí existe a*
								  while(this.arrayER[tries].charAt(pointerA) == this.cadenaOriginal.charAt(this.indexFinal) ) {
									  this.indexFinal++;
									  flag = true;
										System.out.println("Se ha llegado a un match dentro del CASO 3.1");
								  }
							  }
							
						}else {
							// CONCATENACIÓN
							if(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA)) {
								this.indexFinal++;
								flag = true;
								System.out.println("Se ha llegado a un match de concatenación");
								
							}	
							pointerA++;//contador dentro de los ciclos de la ER
						}
					}
				}
				/********/
				pointerA = 0;
				if(this.indexFinal == this.indexInicio && flagStar == true) {
					//hacer el cambio en la copia 
					System.out.println("Inicio: "+this.indexInicio+" Final: "+this.indexFinal);
					//this.indexFinal++;
					break;
				}
				if(this.indexFinal != this.indexInicio && flag ==true) {
					//hacer el cambio en la copia 
					System.out.println("Inicio: "+this.indexInicio+" Final: "+this.indexFinal);
					this.indexInicio = this.indexFinal ;
					//this.indexFinal++;
				}
				if(flag == true) {
					break;
				}
				if(flag == false && this.arrayER.length-1==tries) {
					flag = true;
					this.indexInicio++;
					this.indexFinal = this.indexInicio ;
				}
				tries++;
			}
		}
		
	}
	public static void main(String[] args) {
		Reemplazador rempExa = new Reemplazador("bba", "ba*b","ccc");
		rempExa.reemplazador();
		
		System.out.println("aabaaabaaabaaabbba".substring(2, 7));
		System.out.println("aabaaabaaabaaabbba".substring(10, 15));
		System.out.println("aabaaabaaabaaabbba".substring(16, 18));

	}
}
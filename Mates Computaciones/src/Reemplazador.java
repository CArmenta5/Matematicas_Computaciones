import java.util.ArrayList;

public class Reemplazador {
	private String cadenaOriginal ,
	patronER,
	cadenaReemplazadora,
	resultado;
	private String[] arrayER;
	private ArrayList<Integer> indexAL ;
	private int indexInicio,
	indexFinal;	

	public Reemplazador(String cadenaOriginal, String patronER, String cadenaReemplazadora) {
		this.cadenaOriginal = this.resultado= cadenaOriginal;
		this.patronER = patronER;
		this.cadenaReemplazadora = cadenaReemplazadora;
		this.indexInicio = this.indexFinal = 0;
		this.indexAL = new ArrayList<Integer>();
		if(!this.patronER.equals("")) {
			this.arrayER = new String[countMatches(patronER) + 1];
			fillArrayER();	
		}else {
			this.arrayER = new String[0];
		}
	}
	//Rellena el array de las parte de la ER
	public void fillArrayER() {
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
	public int countMatches(String cadena) {
		if(cadena.length() == 1){
			if(cadena.equals("+")){
				return 1;
			}else {
				return 0;	
			}
		}else {
			if(cadena.charAt(0)  == '+') {
				return countMatches(cadena.substring(1)) + 1;
			}else {
				return countMatches(cadena.substring(1));	
			}
		}
	}
	//Hace el intercambio dentro de los index de final a inicio para no preocuparnos de calculos e imprime el resultado
	public String useSwap() {
		String left, right;
		for(int i = this.indexAL.size()-1; i >0 ; i=i-2) {
			left =this.resultado.substring(0, this.indexAL.get(i-1));
			right =this.resultado.substring(this.indexAL.get(i),this.resultado.length());
			this.resultado =left+this.cadenaReemplazadora+right;
		}
		return this.resultado;
	}
	//M�todo que consigue los indices de las coincidencias dentro de la cadena original
	public void replacer() {
		int i, j, tries;
		int pointerA, // Pointer dentro de la ER, es el simbolo actual que se estar� checando
		pointerB, // Pointer delante del PointerA el cual nos avisa cu�l ser� la operaci�n a seguir si es CERRRADURA DE KLEENE o CONCATENACI�N
		pointerC; // Pointer delante del PointerB que nos avisa el fin de la ER
		this.indexInicio = this.indexFinal = i = j = tries= pointerA = pointerB = pointerC = 0;
		boolean flag = false; //Una expresi�n es correcta
		boolean flagSpecialConc1 = false; //Caso especial dentro de concatenaci�n
		boolean situation = false; //La ER ya acab� y es igual a cadena vac�a
		boolean primerVuelta =false; //Realiza dos vueltas dentro de la ER
		/*Limite de chequeo */
		while(this.indexInicio < this.cadenaOriginal.length()) {
			tries = 0;
			/*Partes de la ER de la uni�n*/
			while(tries < this.arrayER.length) {
				/*Se encontr� match o lleg� al l�mite*/
				if(flag==true) {
					//Reiniciar los indeces
					j = this.indexFinal;
					i = this.indexInicio;
					/*Se reinicia el chequeo por otra parte del ER*/
				}else {
					this.indexFinal = j;
					this.indexInicio = i;
					flag =true;
				}
				//Se checa la ER de concateniaci�n o cerradura de Kleene
				//Hasta que el pointer sea menor o que el indexFinal sea menor a la cadenaoriginal
				/*Inicializar a,b,c dependeiendo del length*/
				//CASO 1: Solo existe un simbolo
				if(this.arrayER[tries].length() == 1) {
					pointerA = pointerB = pointerC = 0;
				}
				//CASO 2: Existe un simbolo concatenado o simbolo con cerradura
				if(this.arrayER[tries].length() == 2) {
					pointerA = 0;
					pointerB = pointerC = 1;

				}
				//CASO 3: GENERAL
				if(this.arrayER[tries].length() >= 3) {
					pointerA = 0;
					pointerB = 1;
					pointerC = 2;
				}

				while(pointerA < this.arrayER[tries].length() && this.indexFinal <this.cadenaOriginal.length()  && flag && !flagSpecialConc1) {	

					// Se valida que c llegue hasta al final y que a agregue hasta el ultimo elemento posible

					if(pointerC <= this.arrayER[tries].length() && pointerA <= pointerC) {

						System.out.println(this.cadenaOriginal.charAt(this.indexFinal)+"-"+this.arrayER[tries].charAt(pointerA));

						//checo que la derecha del simbolo se encuentre esta operaci�n
						if(pointerB==pointerC && this.arrayER[tries].length() != 2) {
							pointerB = pointerA;
						}

						if(this.arrayER[tries].charAt(pointerB)=='*') {
							// CERRADURA DE  KLEENE
							boolean extraFlag =false; //Checa que si haya otra estrella
							//Verificamos que no nos pasemos
							//PointerA->a PointerB->* PointerC->x o |
							//PointerA->a PointerB->* PointerC->x o | PointerA+3 ->*
							//Primera condicion posible si jala 
							if(pointerA+2 < this.arrayER[tries].length() || pointerA+3 < this.arrayER[tries].length() ) {
								//Checa si no nos hemos pasamos del limite
								if(pointerA+3<this.arrayER[tries].length()) {
									//Caso de que pointerA+3 sea *
									if(this.arrayER[tries].charAt(pointerA+3)=='*') {
										//tenemos el caso a*x*
										extraFlag = true;
									}//s� no entra ser� concatenaci�n
									
								}else {
									//caso donde a*x
									// CASO 1: Elemento igual a la derecha (a*a) -> a/aa/aaa/aaa...a en donde no la 2da a no es cerradura manda a hacer bandera especial
									if(this.arrayER[tries].charAt(pointerA) == this.arrayER[tries].charAt(pointerA+2) ) {
										//validar al menos exista una a
										if(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA)) {
											this.indexFinal++;
											flag = true; //Se ha validado que exista al menos una vez
										}else {
											flag = false;
										}
										//Agregamos hasta todas la posibles
										if(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA)) {
											while(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA)) {
												this.indexFinal++;
												if(this.indexFinal >= this.cadenaOriginal.length()) {
													break;
												}
											}
										}
										
										if(pointerA < pointerC) {
											pointerA+=3;
										}
										if(pointerB < pointerC) {
											pointerB+=3;	
										}
										if(pointerC < this.arrayER[tries].length()) {
											pointerC+=3;
										}
										if(pointerA<this.arrayER[tries].length()) {
											if(this.arrayER[tries].charAt(pointerA+1)=='*' ||this.arrayER[tries].charAt(pointerA-1)==this.arrayER[tries].charAt(pointerA)||this.arrayER[tries].charAt(pointerA-1)!=this.arrayER[tries].charAt(pointerA)) {
												pointerA--;	
												pointerB--;	
												pointerC--;													
											}
										}
										if(flag) {
											System.out.println("Se ha llegado a un match dentro del CASO 1");
										}
									}							
									//CASO 2: Elemento diferente a la derecha (a*b) -> b/ab/aab/aaa...b en donde no la 2da a no es cerradura manda a hacer bandera especial
									else if(this.arrayER[tries].charAt(pointerA) != this.arrayER[tries].charAt(pointerA+2)) {
										//Si existe podemos dejarle el trabajo a la concatenaci�n para que continue...
										//CASO 2.1: No existe a*, pero s� b
										//Apuntamos a *
										//si hay un chingo o al menos uno
										if(this.arrayER[tries].charAt(pointerA) == this.cadenaOriginal.charAt(this.indexFinal)) {											
											this.indexFinal++;
											flag = true; //Se ha validado que exista al menos una vez
											if(this.indexFinal< this.cadenaOriginal.length()) {
												while(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA)) {
													this.indexFinal++;
													if(this.indexFinal >= this.cadenaOriginal.length()-1) {
														break;
													}
												}
											}else {
												flag = false;
											}
											
											
											
										}//si es null 
										else if(this.arrayER[tries].charAt(pointerA) != this.cadenaOriginal.charAt(this.indexFinal)) {
											flag = true;
											
										}
										if(flag){
											//Se evalu� a*, pero no a�n b
											if(this.indexFinal>=this.cadenaOriginal.length()) {
												flag = false;
											}else if(this.arrayER[tries].charAt(pointerA+2) == this.cadenaOriginal.charAt(this.indexFinal)) {
												this.indexFinal++;

												flag =true;
												if(pointerA < pointerC) {
													//if(pointerA+3 < this.arrayER[tries].length()-1 && !(pointerA+3 >= this.arrayER[tries].length())) {
													pointerA+=3;															
												}
												if(pointerB < pointerC) {
													pointerB+=3;
												}
												if(pointerC < this.arrayER[tries].length()) {
													pointerC+=3;
												}
												
											}else {
												flag = false;
												if(pointerA <= pointerC) {
													pointerA+=2;//contador dentro de los ciclos de la ER
												}
												if(pointerB <= pointerC) {
													pointerB+=2;	
												}
												if(pointerC < this.arrayER[tries].length()) {
													pointerC+=2;	
												}
											}
										}				   
									}
								
								}
							}else {
								extraFlag =true;
							}
/*							if(pointerA != pointerC && !(pointerA+2 < this.arrayER[tries].length() && pointerA+3 < this.arrayER[tries].length())) {
								extraFlag = true;
							}
	*/						//Caso 3: Cerradura de kleene individual
							if(extraFlag) {
								// CASO 3.1: Producto de la palabra vac�a
								if(this.arrayER[tries].charAt(pointerA) != this.cadenaOriginal.charAt(this.indexFinal)) {
									//Aunque se vea tonto este valida si tomaremos en cuenta la palabra vacia o no
									if(pointerA+3 <this.arrayER[tries].length() ) {
										if( this.arrayER[tries].charAt(pointerA+3) == '*' ) {
											if(flag){
												flag = true;

											}else{
												flag = false;  
												if(pointerA <= pointerC) {
													pointerA++;//contador dentro de los ciclos de la ER
												}
												if(pointerB <= pointerC) {
													pointerB++;	
												}
												if(pointerC < this.arrayER[tries].length()) {
													pointerC++;	
												}
												if(pointerA+3>=this.arrayER[tries].length()) {
													break;
												}
											}

											System.out.println("Se ha llegado a un match dentro del CASO 3.1");
										}
									}else {
										if(flag) {
											if(pointerA+1<this.arrayER[tries].length()) {
												if(this.arrayER[tries].charAt(pointerA)!=this.cadenaOriginal.charAt(this.indexFinal) && this.arrayER[tries].charAt(pointerA+1)=='*' && (!(pointerA<this.arrayER[tries].length())||this.arrayER[tries].length()==2)) {
													flag=true;	
													situation = true;
												}else if(this.arrayER[tries].charAt(pointerA)!=this.cadenaOriginal.charAt(this.indexFinal) && this.arrayER[tries].charAt(pointerA+1)!='*'){
													flag=true;	

												}else 	if(this.arrayER[tries].charAt(pointerA+1)=='*') {
													flag = true;
													if(pointerA <= pointerC) {
														pointerA++;//contador dentro de los ciclos de la ER
													}
													if(pointerB <= pointerC) {
														pointerB++;	
													}
													if(pointerC < this.arrayER[tries].length()) {
														pointerC++;	
													}
													if(pointerA+3>=this.arrayER[tries].length()) {
														if(pointerA==this.arrayER[tries].length()-1) {
															situation = true;
														}
														break;
													}
												}else if(this.arrayER[tries].charAt(pointerA)!=this.cadenaOriginal.charAt(this.indexFinal)) {
													flag=false;	

												}
												else if(pointerC == this.arrayER[tries].length() && flag && pointerA+2 ==pointerC && primerVuelta) {
													situation = true;

												}
												if(!primerVuelta && this.arrayER[tries].length() ==pointerC && this.arrayER[tries].length() > pointerA) {
													primerVuelta = true;
												}
												
											}else {
												if(this.arrayER[tries].charAt(pointerA+1)=='*') {
													flag = true;
													if(pointerA <= pointerC) {
														pointerA++;//contador dentro de los ciclos de la ER
													}
													if(pointerB <= pointerC) {
														pointerB++;	
													}
													if(pointerC < this.arrayER[tries].length()) {
														pointerC++;	
													}
													if(pointerA+3>=this.arrayER[tries].length()) {

														break;
													}
												}else if(this.arrayER[tries].charAt(pointerA)!=this.cadenaOriginal.charAt(this.indexFinal)) {
													flag=false;	

												}	
											}

										}
										if(pointerA <= pointerC) {
											pointerA++;//contador dentro de los ciclos de la ER
										}
										if(pointerB <= pointerC) {
											pointerB++;	
										}
										if(pointerC < this.arrayER[tries].length()) {
											pointerC++;	
										}
										if(pointerA+3>=this.arrayER[tries].length()) {
											if(pointerA+3==pointerC) {
												break;
											}
										}
										System.out.println("Se ha llegado a un match dentro del CASO 3.1");

									}
									//A pesar de que esta se encuentre en todas partes, se tiene que validar.
								}
								// CASO 3.2: S� existe a*
								if(this.cadenaOriginal.length()!=this.indexFinal && flag) {
									while(this.arrayER[tries].charAt(pointerA) == this.cadenaOriginal.charAt(this.indexFinal) ) {
										this.indexFinal++;
										flag = true; //Se ha validado que exista al menos una vez

										System.out.println("Se ha llegado a un match dentro del CASO 3.2");
										if(this.cadenaOriginal.length()==this.indexFinal) {

											break;
										}
									}
								}//CASO 3.3
								else {
									System.out.println("Se ha llegado a un match dentro del CASO 3.2");

								}
								if(pointerA <= pointerC) {
									pointerA++;//contador dentro de los ciclos de la ER
								}
								if(pointerB <= pointerC) {
									pointerB++;	
								}
								if(pointerC < this.arrayER[tries].length()) {
									pointerC++;	
								}
							}

						}
						else {
							// CONCATENACI�N
							//CASO 1: Solo existe un simbolo dentro de la ER
							if(this.arrayER[tries].length()==1) {
								if(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA) ) {
									this.indexFinal++;
									flag = true; //Se ha validado que exista al menos una vez
									flagSpecialConc1 = true;
									System.out.println("Se ha llegado a un match de concatenaci�n del CASO 1");
								}else {
									flag = false;
									flagSpecialConc1 = false;
								}	
							}//CASO 2: Existen m�s de dos simbolos en la ER
							else {
								if(this.cadenaOriginal.charAt(this.indexFinal) == this.arrayER[tries].charAt(pointerA) ) {
									this.indexFinal++;
									flag = true; //Se ha validado que exista al menos una vez

									System.out.println("Se ha llegado a un match de concatenaci�n CASO 2");
								}else {
									flag = false;
								}	
							}						
						}
						if(pointerA < pointerC) {
							pointerA++;//contador dentro de los ciclos de la ER
						}
						if(pointerB < pointerC) {
							pointerB++;	
						}
						if(pointerC < this.arrayER[tries].length()) {
							pointerC++;	
						}
					}
					if(flag == true && this.arrayER[tries].length() <= pointerA && this.indexFinal!=this.indexInicio) {
						break;
					}
					if(flag == true && (this.cadenaOriginal.length() == this.indexFinal && pointerA!=pointerC) ) {
						//Todos son asteriscos
						boolean demente=false;
						while(pointerA+1<this.arrayER[tries].length()) {
							if(this.arrayER[tries].charAt(pointerA+1)=='*') {
								pointerA+=2;
							}else {
								break;
							}
						}
						if(pointerA%2==0 && pointerA==this.arrayER[tries].length()) {
							flag = true; //Se ha validado que exista al menos una vez

						}else {
							System.out.println("No se ha llegado a un match");
							flag = false;
							break;
						}
					}if(flag == false) {
						System.out.println("No se ha llegado a un match");
						flag = false;
						break;
					}
					if(pointerA == pointerC) {
						flag = true; //Se ha validado que exista al menos una vez
						//situation =true;
					}	
				}
				/********/
				pointerA = 0;
				
				
				if(this.indexFinal != this.indexInicio && flag ==true) {
					//hacer el cambio en la copia 
					this.indexAL.add(this.indexInicio);
					this.indexAL.add(this.indexFinal);
					if(pointerB==this.arrayER[tries].length()){}
					System.out.println("Inicio: "+this.indexInicio+" Final: "+this.indexFinal);
					if(situation) {
						this.indexInicio = this.indexFinal=this.indexFinal+1;
						situation=false;
					}else {
						this.indexInicio = this.indexFinal ;
					}
					flagSpecialConc1 = false;
					//this.indexFinal++;
				}else if(this.indexFinal == this.indexInicio && flag ==true) {
					if(situation) {
						this.indexInicio = this.indexFinal=this.indexFinal+1;
						situation=false;
						flagSpecialConc1 = false;
					}
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
		if(patronER.length() == 0) {
			System.out.println("El patr�n no puede ser la cadena vac�a");
		}
	}
	public static void main(String[] args) {
		Reemplazador rempExa = new Reemplazador("abaa aab aaanbbb", "a*","ccc");
		rempExa.replacer();
		System.out.println(rempExa.useSwap());
	}
}
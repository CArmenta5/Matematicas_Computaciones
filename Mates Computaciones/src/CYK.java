import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CYK {
	private Queue<String> cola;
	private Queue<String> cola2;

	private String cadena;
	private int n;
	private String[] memoizacion;
	private String gramatica;
	private ArrayList<ArrayList<String>> gramaticaDistr;
	private int index;
	private String[] alfabeto = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","Ñ","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private ArrayList<String> disponibles;
	private Map<String, String> mapa = new HashMap<String, String>();
	private String fileName = "my-file.txt";
    private String encoding = "UTF-8";
    private PrintWriter writer;
    private HashMap<String,String> abc = new HashMap<String,String>();

	public CYK(String cadena, String gramatica) {
		try{
			this.writer = new PrintWriter(fileName, encoding);

		    }
		    catch (IOException e){
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		this.cola = new LinkedList<String>();
		this.cadena = cadena;
		this.cola2 = new LinkedList<String>();
		System.out.println("Cadena: "+this.cadena);

		this.gramatica = gramatica;
		this.n = this.cadena.length();
		this.memoizacion = new String[(this.n * this.n - this.n) / 2 + this.n];
		this.gramaticaDistr = new ArrayList<ArrayList<String>>();
		this.index = 0;
		int pointer = 0;
		ArrayList<String> internal;
		String produccion;
		System.out.println("Gramatica -> "+this.gramatica);
		while (pointer < this.gramatica.length()) {
			internal = new ArrayList<String>();
			internal.add(this.gramatica.substring(pointer, pointer + 1));
			pointer++;
			produccion = "";
			// S=AB|SS|AC|BD|BA,A=a,B=b,C=SB,D=SA
			while (!this.gramatica.substring(pointer, pointer + 1).equals(",")) {
				if (!this.gramatica.substring(pointer, pointer + 1).equals("|")
						&& !this.gramatica.substring(pointer, pointer + 1).equals("=")) {
					produccion = produccion + this.gramatica.substring(pointer, pointer + 1);
				} else if (this.gramatica.substring(pointer, pointer + 1).equals("|") && produccion.length() > 0) {
					internal.add(produccion);
					produccion = "";
				}
				pointer++;
				if (pointer == this.gramatica.length()) {
					break;
				}
			}
			if (produccion.length() > 0) {
				internal.add(produccion);
			}
			this.gramaticaDistr.add(internal);
			pointer++;
		}
		if(!this.isFNCh()) {
			this.gramaticaAFNCh();
		}
		System.out.print("FNCh -> ");

		for(int i = 0; i < this.gramaticaDistr.size(); i++) {
			for(int j = 0; j < this.gramaticaDistr.get(i).size(); j++) {
				if(j==0) {
					System.out.print(this.gramaticaDistr.get(i).get(0)+"=");
				}else if(j <this.gramaticaDistr.get(i).size()-1){
					System.out.print(this.gramaticaDistr.get(i).get(j)+"|");
				}else{
					System.out.print(this.gramaticaDistr.get(i).get(j)+",");	
				}
			}

		}
		System.out.println();	
	}

	public boolean algoritmoCYK() throws IOException {
		int cont = 0;
		 writer.println("digraph G {");
		 writer.println("ordering = out");

		for (int i = 0; i < this.n; i++) {
			for (int j = i; j < this.n; j++) {
				String tmp;
				if (i == 0) {
					tmp = terminales(this.cadena.substring(j, j + 1));
					memoizacion[cont] = tmp;

					cont++;
				} else {
					// polea
					tmp = polea(cont);
					memoizacion[cont] = tmp;

					cont++;
				}
				if (tmp.length() != 0) {
					 writer.println((cont - 1) + " " + "[ label=<" + tmp + "> ]");
				}
			}
		}
		ArrayList<String> aux = backtracking();

		
		for (String elemet : cola) {
			for(String aceptables: aux) {
				String[] a = elemet.split(",");				
				if(aceptables.equals(a[0])) {
					 writer.println(a[0] + "->" + a[1]);
				}
			}
		}
		 writer.println("}");

		writer.close();

		if(this.memoizacion[this.memoizacion.length-1].contains("S")) {
			for (int i = 0; i < this.n; i++) {

				int rest = this.n;
				int acum = rest;
				for (int j = i; j < this.n; j++) {
					if(this.memoizacion[j+(acum-this.n)].length()!=0){
						System.out.print(this.memoizacion[j+(acum-this.n)]+" ");

					}else {
						System.out.print("-"+" ");
						
					}
					rest--;
					acum +=rest;
				}
				
				
				System.out.println();
					
				}
			
			
			return true;	
		}else {
			return false;
		}
	}

	public String terminales(String s) {
		String cadena = "";
		for (int x = 0; x < this.gramaticaDistr.size(); x++) {
			String simboloTerminal = this.gramaticaDistr.get(x).get(0);
			for (int y = 0; y < this.gramaticaDistr.get(x).size(); y++) {
				if (this.gramaticaDistr.get(x).get(y).equals(s)) {
					cadena =  cadena+simboloTerminal;
				}
			}
		}
		return cadena;
	}

	// Recorrer la matriz para insertar datos a partidr de comparaciones
	public String polea(int indexInsert) {
		int top = 0;
		int nivel = 0;
		for (int i = 1; i < this.n; i++) {
			top += this.n - i + 1;
			if (indexInsert >= top) {
				nivel++;
			}
		}
		String cadena = "";
		if (nivel == 1) {
			int x = indexInsert % (this.n - (nivel - 1));
			String tmp = extraerGeneradores(memoizacion[x], memoizacion[x + 1]);

			cadena = tmp;
			if (tmp.length() != 0) {
				cola2.add(indexInsert + "->" + x);
				
				cola2.add(indexInsert + "->" + (x + 1));

				cola.add(indexInsert + "," + x);
				cola.add(indexInsert + "," + (x + 1));

			}
		} else {
			int aux = indexInsert;
			for (int k = 0; k < nivel; k++) {
				aux = aux - (this.n - k);
			}

			int aux2 = aux + 1;
			int z = 0;
			for (int k = 0; k < nivel - 1; k++) {
				aux2 = aux2 + (this.n - k);
				z = (this.n - k) - 1;
			}
			for (int i = 0; i < nivel; i++) {
				String tmp = "";
				// for para sacar mínimo de nivel
				for(int j =0; j < memoizacion[aux].length();j++) {
					for(int k =0; k < memoizacion[aux2].length();k++) {
						tmp = extraerGeneradores(memoizacion[aux].charAt(j)+"", memoizacion[aux2].charAt(k)+"");
						if (!tmp.equals("")) {
							cola2.add(indexInsert + "->" + aux);
							
							cola2.add(indexInsert + "->" + aux2);
							cola.add(indexInsert + "," + aux);
							cola.add(indexInsert + "," + aux2);

						}
					}	
				}
				

				cadena = cadena + tmp;
				aux += (this.n - i);
				aux2 -= z;
				z++;
			}
		}

		return cadena;
	}

	// Sacar cuales generadores dan esa combinacion, recorriendo toda la gramatica
	public String extraerGeneradores(String s, String s2) {
		String cadena = "";
		if (s.length() == 0 || s2.length() == 0) {
			return cadena;
		}
		for (int x = 0; x < this.gramaticaDistr.size(); x++) {
			String simboloTerminal = this.gramaticaDistr.get(x).get(0);
			for (int y = 1; y < this.gramaticaDistr.get(x).size(); y++) {
				for(int i = 0; i < s.length(); i++) {
					for(int j = 0; j < s2.length(); j++) {
			
						if (this.gramaticaDistr.get(x).get(y).equals(s.substring(i, i+1) + s2.substring(j, j+1))) {

							cadena = cadena+simboloTerminal;
						}
					}
				}
			}
		}
		return cadena;
	}

	public String gramaticaAFNCh() {
		// A -> BC | A -> a
		// Eliminar epsilon-producciones y producciones unitarias
		// Identificar ambas
		ArrayList<ArrayList<String>> epsilonProducciones = new ArrayList<>();
		ArrayList<ArrayList<String>> produccionesUnitarias = new ArrayList<>();
		boolean flag = true;
		flag = false;
		int cont = 0;
		// while(cont<this.gramaticaDistr.size()) {
		for (int j = 1; j < this.gramaticaDistr.size(); j++) {
			boolean flag1 = false;
			boolean flag2 = false;
			for (int i = 1; i < this.gramaticaDistr.get(j).size(); i++) {
				if (this.gramaticaDistr.get(j).get(i).equals("[") && !flag1) {
					epsilonProducciones.add(this.gramaticaDistr.get(j));
					flag1 = true;

				} else if (this.gramaticaDistr.get(j).get(i).length() == 1
						&& Character.isLowerCase(this.gramaticaDistr.get(j).get(1).charAt(0)) && !flag2) {
					produccionesUnitarias.add(this.gramaticaDistr.get(j));
					flag2 = true;
				}
			}
		}
		// Regla 1
		for (int j = 0; j < this.gramaticaDistr.size(); j++) {
			for (int i = 1; i < this.gramaticaDistr.get(j).size(); i++) {
				// [ - Epsiolon
				int index;
				for (ArrayList<String> ele : epsilonProducciones) {
					if (this.gramaticaDistr.get(j).get(i).indexOf(ele.get(0)) != -1) {
						index = this.gramaticaDistr.get(j).get(i).indexOf(ele.get(0));
						String g1 = this.gramaticaDistr.get(j).get(i).substring(0, index) + this.gramaticaDistr.get(j)
								.get(i).substring(index + 1, this.gramaticaDistr.get(j).get(i).length());
						if (g1.length() == 0) {
							this.gramaticaDistr.get(j).add("[");

						} else {
							this.gramaticaDistr.get(j).add(g1);

						}
					}

				}
			}
		}
		// Regla 2
		for (int j = 0; j < this.gramaticaDistr.size(); j++) {
			for (int i = 1; i < this.gramaticaDistr.get(j).size(); i++) {
				// [ - Epsiolon
				for (ArrayList<String> ele : produccionesUnitarias) {
					if (this.gramaticaDistr.get(j).get(i).indexOf(ele.get(0)) != -1) {
						for (int k = 1; k < ele.size(); k++) {
							if (ele.get(k).length() == 1 && Character.isLowerCase(ele.get(k).charAt(0))
									|| ele.get(k).equals("[")) {
								String g1;
								if (ele.get(k).equals("[")) {
									index = this.gramaticaDistr.get(j).get(i).indexOf(ele.get(0));
									g1 = this.gramaticaDistr.get(j).get(i).substring(0, index)
											+ this.gramaticaDistr.get(j).get(i).substring(index + 1,
													this.gramaticaDistr.get(j).get(i).length());
								} else {
									g1 = ele.get(k);
								}
								if (g1.length() != 0) {
									if (!isThere(this.gramaticaDistr.get(j), g1)) {
										this.gramaticaDistr.get(j).add(g1);

									}
									if (this.gramaticaDistr.get(j).get(0).equals(ele.get(0))) {
										break;
									}
								}
							}
						}
					}
				}
			}
		}

		// cont++;

		// }
		// quitamos simbolos inutiles
		for (ArrayList<String> array : this.gramaticaDistr) {
			for (int i = 1; i < array.size(); i++) {

				int c = 0;
				for (ArrayList<String> ele : epsilonProducciones) {
					if (array.get(i).equals(ele.get(0)) || array.get(i).equals("[") || array.get(i).equals("")) {
						array.remove(i);
						i--;
					}
					c++;
				}
				c = 0;
				for (ArrayList<String> ele : produccionesUnitarias) {
					if (array.get(i).equals(ele.get(0))) {
						array.remove(i);
						i--;
					}
					

					c++;
				}
			}
		}
		int co=0;
		for (int i = 0; i< this.gramaticaDistr.size(); i++) {
			
			if(this.gramaticaDistr.get(i).size()==2) {
				this.gramaticaDistr.remove(i);
				i--;
			}
		}
		
		this.disponibles = new ArrayList<String>();
		for(String ele: this.alfabeto) {
			boolean flag1 = false;
			String str ="";
			for(int i = 0; i < this.gramaticaDistr.size(); i++) {
				str = this.gramaticaDistr.get(i).get(0);
				if(ele.equals(this.gramaticaDistr.get(i).get(0))) {
					flag1 = true;
					str ="";
					break;
				}
			}
			if(!flag1) {
				this.disponibles.add(ele);

			}
		}
		/*Para cada simbolo terminal Zalpha -> alpha*/
			/*Creamos un array de A-Z (disponibles para hacer los cambios de alpha)*/
			/*Al asignar usamor lo del diccionario*/
		int cant =this.gramaticaDistr.size();
		for(int i =0; i< cant;i++) {
			for(int j =1; j< this.gramaticaDistr.get(i).size();j++) {
				for(int k =0; k < this.gramaticaDistr.get(i).get(j).length(); k++) {
					if( Character.isLowerCase(this.gramaticaDistr.get(i).get(j).charAt(k))) {
						// no tiene ya asignado
						if(this.mapa.get(this.gramaticaDistr.get(i).get(j).charAt(k)+"") != null) {
							//se le asigna
							String p1=this.gramaticaDistr.get(i).get(j).substring(0,k);
							String p2=this.gramaticaDistr.get(i).get(j).substring(k+1,this.gramaticaDistr.get(i).get(j).length());
							this.gramaticaDistr.get(i).set(j, p1+this.mapa.get(this.gramaticaDistr.get(i).get(j).charAt(k)+"")+p2);
						}else {
							//se le asigna
							String p1=this.gramaticaDistr.get(i).get(j).substring(0,k);
							String p2=this.gramaticaDistr.get(i).get(j).substring(k+1,this.gramaticaDistr.get(i).get(j).length());
							String key= this.gramaticaDistr.get(i).get(j).charAt(k)+"";
							String value = this.disponibles.remove(0);
							this.mapa.put(key,value);
							this.gramaticaDistr.get(i).set(j, p1+value+p2);
							ArrayList<String> nvo = new ArrayList();
							nvo.add(value); // gramatica
							nvo.add(key); // produccion

							this.gramaticaDistr.add(nvo);;
						}
					}
				}
			}	
		}
		// Recorrer igual pero para terminales dentro
		for(int i =0; i< cant;i++) {
			for(int j =1; j< this.gramaticaDistr.get(i).size();j++) {
				for(int k =0; k < this.gramaticaDistr.get(i).get(j).length();k++) {
					char letra=this.gramaticaDistr.get(i).get(j).charAt(k);
					if(Character.isLowerCase(letra)) {
						// no tiene ya asignado
						if(this.mapa.get(letra+"") != null) {
							int index = this.gramaticaDistr.get(i).get(j).indexOf(this.gramaticaDistr.get(i).get(j).charAt(k));
							String p1, p2, gramatica;
							p1 =this.gramaticaDistr.get(i).get(j).substring(0, index);
							p2 = this.gramaticaDistr.get(i).get(j).substring(index + 1, this.gramaticaDistr.get(i).get(j).length());
							gramatica = (p1+this.mapa.get(letra+"")+p2);
							this.gramaticaDistr.get(i).set(j, gramatica);
						}else {
							//se le asigna
							String key = this.gramaticaDistr.get(i).get(j);
							String value = this.disponibles.remove(0);
							this.mapa.put(key,value);
							this.gramaticaDistr.get(i).set(j, value);
							ArrayList<String> nvo = new ArrayList();
							nvo.add(value); // gramatica
							nvo.add(key); // produccion

							this.gramaticaDistr.add(nvo);;
						}
					}
				}
			}	
		}
		/*Convertir a FNCh aaa*/
		/*Una gramatica*/
		for (int j = 0; j < cant; j++) {
			for (int i = 1; i < this.gramaticaDistr.get(j).size(); i++) {
				if(this.gramaticaDistr.get(j).get(i).length()==1) {
					for(ArrayList<String> ele : this.gramaticaDistr) {
						if(this.gramaticaDistr.get(j).get(i).equals(ele.get(0))) {
							this.gramaticaDistr.get(j).set(i, ele.get(1));
						}
					}
				}
			}
		}
		/*for (int i = 0; i< this.gramaticaDistr.size(); i++) {
			for(int j =0; j < this.gramaticaDistr.get(j).size();j++) {
				if(Character.isLowerCase(this.gramaticaDistr.get(j).get(i).charAt(0))) {
					if(this.mapa.get(this.gramaticaDistr.get(j).get(i).charAt(0)+"") != null) {
						String elim =this.mapa.get(this.gramaticaDistr.get(j).get(i).charAt(0)+"");
						for(int ii = 0; ii< this.gramaticaDistr.size(); ii++) {
							if(this.gramaticaDistr.get(ii).get(0).equals(elim)) {
								this.gramaticaDistr.remove(ii);
							}
							
						}
					}
				}
			}
		}*/
		//Proceso de recorrido
		for(int i = 0; i< this.gramaticaDistr.size(); i ++) {
			for (int j = 1; j < this.gramaticaDistr.get(i).size(); j++) {
				if(this.gramaticaDistr.get(i).get(j).length()>=3) {
						String p1, p2, gramatica;
						p1 =this.gramaticaDistr.get(i).get(j).substring(0, 1);
						p2 = this.gramaticaDistr.get(i).get(j).substring(1, this.gramaticaDistr.get(i).get(j).length());
						String value = this.disponibles.remove(0);
						this.gramaticaDistr.get(i).set(j, p1+value);
						ArrayList<String> nvo = new ArrayList();
						nvo.add(value); // gramatica
						nvo.add(p2); // produccion
						this.gramaticaDistr.add(nvo);
				}
			}
		}
		
		
		
		return "a";

	}
	public boolean isFNCh() {
		for(int i =0;i< this.gramaticaDistr.size();i++) {
			for(int j =1; j < this.gramaticaDistr.get(i).size();j++) {
				if(!((this.gramaticaDistr.get(i).get(j).length()==1 && Character.isLowerCase(this.gramaticaDistr.get(i).get(j).charAt(0)))
						|| (this.gramaticaDistr.get(i).get(j).length()==2 && Character.isUpperCase(this.gramaticaDistr.get(i).get(j).charAt(0)) && Character.isUpperCase(this.gramaticaDistr.get(i).get(j).charAt(1))))) {
					return false;
				}	
			}
		}
		

		return true;
	}

	public boolean isThere(ArrayList<String> ar, String cadena) {
		for (String ele : ar) {
			if (cadena.equals(ele)) {
				return true;
			}
		}
		return false;
	}
	public ArrayList<String> backtracking() {
		ArrayList<String> aux = new ArrayList<String>();
		for(String ele : this.cola2) {
			aux.add(ele);
		}
		ArrayList<String> validados= new ArrayList<>();
		validados.add(this.memoizacion.length-1+"");
		for(int i = aux.size()-1; i>0;i--) {
			String[] nvo = aux.get(i).split("->");
			int c= validados.size();
			for(int j = 0;j <c;j++) {
				if(validados.get(j).equals(nvo[0])) {
					validados.add(nvo[1]);
				}
			}
			
		}
		return validados;
	}

	public static void main(String[] args) {

		CYK cyk = new CYK("aabbab","S=AB|SS|AC|BD|BA,A=a,B=b,C=SB,D=SA");
		//CYK cyk = new CYK("abb", "S=aSb|ab");
		//CYK cyk = new CYK("aabbab", "S=aA|B,A=aA|bA|B,B=b|[");
		try {

			System.out.println("La cadena pertenece: " + cyk.algoritmoCYK());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

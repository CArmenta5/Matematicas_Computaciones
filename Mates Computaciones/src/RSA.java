import java.io.*;
import java.lang.Math;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.JOptionPane;


public class RSA {
	
	private BigInteger p,q,n, miN, keyPriv,e;
	private Map<String, Integer> referencias;
	private Map<Integer,String > referenciasRev;
	private String respuesta;
    private PrintWriter writer, writer2;

	public RSA() {
		this.referencias = new HashMap<String, Integer>();
		this.referenciasRev = new HashMap<Integer, String>();
		this.respuesta = JOptionPane.showInputDialog("Manda tu mensaje: ");
		System.out.println(respuesta);
		try {
			this.writer = new PrintWriter("encriptado.txt", "UTF-8");
			this.writer2 = new PrintWriter("desencriptado.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		asignValueMap();
		generacionLlaves();
        JOptionPane.showMessageDialog(null, "¡Se han generado las llaves exitosamente!", "Generación de llaves", JOptionPane.DEFAULT_OPTION );
		encriptaciónRSA();
        JOptionPane.showMessageDialog(null, "Mensaje encriptado (generado): encriptado.txt", "Generación de archivo", JOptionPane.DEFAULT_OPTION );
		desencriptaciónRSA();
        JOptionPane.showMessageDialog(null, "Mensaje desencriptado (generado): desencriptado.txt", "Generación de archivo", JOptionPane.DEFAULT_OPTION );
	}
	
	public void asignValueMap() {
		int cont = 2;
		for(String c: this.respuesta.split("")) {
			if(this.referencias.get(c)==null) {
				this.referencias.put(c, cont);
				this.referenciasRev.put(cont, c);
				cont++;
			}
		}
	}
	
	public void generacionLlaves() {
		boolean flag = false;
		while(!flag) {
			this.p = generadorPrimo();
			this.q = generadorPrimo();
			this.n = this.p.multiply(this.q);
			this.miN = (this.p.subtract(BigInteger.ONE)).multiply(this.q.subtract(BigInteger.ONE));
			this.e =  RandomBigInteger();
			if(this.e.gcd(this.miN).equals(BigInteger.ONE)) {
				this.keyPriv = llavePrivada(this.e,this.miN);
				if(!this.keyPriv.equals(BigInteger.ZERO)) {
					flag = true;
				}
			}
		}
		//System.out.println("p: "+this.p+" q: "+" "+this.q+" n: "+this.n+" miN: "+ this.miN+" e: "+this.e+" keyPriv: "+ this.keyPriv);
	}
	
	public BigInteger RandomBigInteger() {
        Random rand = new Random();
        BigInteger result;
        do {
            result = new BigInteger(this.miN.bitLength(), rand); 
        }while(result.compareTo(BigInteger.TWO) <= 0 && result.compareTo(this.miN) >= 0);   
        return result;
    }
	
	public BigInteger generadorPrimo() {
		int num=0;
		Random rm = new Random();
		boolean flag = false;
		while(!flag) {
			num = rm.nextInt(10000-4000)+4000;
			int cont = 3;
			double fin = Math.sqrt(num);
			while((cont <= fin) && (num % cont != 0)) {
				cont+=2;
			}
			if(num%cont != 0 && num % 2 != 0) {
				flag = true;
			}
		}
		return new BigInteger(num+"");
	}
	
	public BigInteger llavePrivada(BigInteger e, BigInteger miN) {
		BigInteger x = miN.multiply(miN);
		for(BigInteger i = BigInteger.valueOf(0); i.compareTo(x) < 0; i = i.add(BigInteger.ONE)) {
			if((i.multiply(e)).mod(miN).equals(BigInteger.ONE)) {
				return i;
			}
		}
		return BigInteger.ZERO;
	}
	
	public void encriptaciónRSA() {
		String cadena ="";
		for(String c : this.respuesta.split("")) {
			String str = Integer.toString(this.referencias.get(c));
			BigInteger textoPlano = new BigInteger(str);
			BigInteger y =  textoPlano.modPow(this.e,this.n);
			cadena = cadena + y.toString()+",";
		}
		this.writer.println(cadena);
        this.writer.close();
	}
	
	public void desencriptaciónRSA() {
		BufferedReader br = null;
		String line = null;
		try {
			br = new BufferedReader(new FileReader("encriptado.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			 line = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String cadena="";
		for(String c : line.split(",")) {
			BigInteger textoCifrado = new BigInteger(c);
			BigInteger y =  textoCifrado.modPow(this.keyPriv,this.n);
			cadena = cadena + this.referenciasRev.get(Integer.parseInt(y.toString()));
		}
        this.writer2.println(cadena);
        this.writer2.close();
	}
	
	public static void main(String args[]) {
		RSA rsa = new RSA();	
	}
}


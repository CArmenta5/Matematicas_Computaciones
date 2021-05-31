import java.lang.Math;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class RSA {

	private BigInteger p,q,n, miN, keyPriv,e, y;
	private BigInteger x;
	public RSA() {
		// Alice
		String respuesta = JOptionPane.showInputDialog("Manda tu mensaje en formato int: ");
		this.x = new BigInteger(respuesta);
		System.out.println(respuesta);

		//Bob
		generacionLlaves();
        JOptionPane.showMessageDialog(null, "¡Se han generado las llaves exitosamente!", "Generación de llaves", JOptionPane.DEFAULT_OPTION );
		// Alice
		BigInteger enc = encriptaciónRSA(this.x);
        JOptionPane.showMessageDialog(null, "Mensaje encriptado: "+enc, "Generación de llaves", JOptionPane.DEFAULT_OPTION );
		BigInteger des = desencriptaciónRSA(enc);
        JOptionPane.showMessageDialog(null, "Mensaje desencriptado: "+des, "Generación de llaves", JOptionPane.DEFAULT_OPTION );
		System.out.println(des);
	}
	
	
	public void generacionLlaves() {
		boolean flag = false;
		while(!flag) {
			this.p = /*new BigInteger("3");**/generadorPrimo();
			this.q = /*new BigInteger("11");*/generadorPrimo();
			this.n = this.p.multiply(this.q);
			this.miN = (this.p.subtract(BigInteger.ONE)).multiply(this.q.subtract(BigInteger.ONE));
			Random rm = new Random();
			this.e =  RandomBigInteger();
			if(MCDCoprimos(this.e, this.miN)) {
				this.keyPriv = llavePrivada(this.e,this.miN);
				if(!this.keyPriv.equals(BigInteger.ZERO)) {
					flag = true;
				}
			}
		}
		System.out.println(this.p+" "+" "+this.q+" "+this.n+" "+ this.miN+" "+this.e+" "+ this.keyPriv);

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
			num = rm.nextInt(5000-2)+2;
			// System.out.println("Random says:" +num);
			if(num == 2) {
				return new BigInteger("2");
			}else if(num == 3) {
				return new BigInteger("3");
			}
			// solo mayores a 3
			int cont = 3;
			double fin = Math.sqrt(num);
			while((cont <= fin) && (num % cont != 0)) {
				// System.out.println("num "+cont +" "+ fin+" "+cont+" "+num%cont);
				cont+=2;
			}
			if(num%cont != 0 && num % 2 != 0) {
				flag = true;
			}
		}
		return new BigInteger(num+"");
	}
	
	public boolean MCDCoprimos(BigInteger e, BigInteger miN) {
        BigInteger mcd = e.gcd(miN);
        return mcd.equals(miN.ONE);	
	}
	public BigInteger llavePrivada(BigInteger e, BigInteger miN) {
		for(BigInteger i = BigInteger.valueOf(0); i.compareTo(miN.multiply(miN))<0;i=i.add(BigInteger.ONE)) {
			if((i.multiply(e)).mod(miN).equals(BigInteger.ONE)) {
				return i;
			}
		}
		return BigInteger.ZERO;

	}
	public BigInteger encriptaciónRSA(BigInteger textoPlano) {
		BigInteger y1 =  pow(textoPlano,this.e);
		BigInteger y = y1.mod(this.n);
		return y;
	}
	public BigInteger desencriptaciónRSA(BigInteger textoCifrado) {
		BigInteger x1 = pow(textoCifrado,this.keyPriv);
		BigInteger x = x1.mod(this.n);
		return x;
	}
	// https://stackoverflow.com/questions/4582277/biginteger-powbiginteger
	public BigInteger pow(BigInteger base, BigInteger exponent) {
		  BigInteger result = BigInteger.ONE;
		  while (exponent.signum() > 0) {
		    if (exponent.testBit(0)) result = result.multiply(base);
		    base = base.multiply(base);
		    exponent = exponent.shiftRight(1);
		  }
		  return result;
		}
	public static void main(String args[]) {
		RSA rsa = new RSA();	
		/*System.out.println(rsa.llavePrivada(new BigInteger("7"),new BigInteger("20")));
		System.out.println(rsa.pow(new BigInteger("7"),new BigInteger("2")));
		System.out.println(rsa.encriptaciónRSA(new BigInteger("4")));
		System.out.println(rsa.desencriptaciónRSA(rsa.encriptaciónRSA(new BigInteger("4"))));
*/
	}
}


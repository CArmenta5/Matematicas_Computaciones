import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelPrincipal extends JPanel implements ActionListener{
	private JLabel jlTitulo,
	jlCadenaOriginal, 
	jlPatronER, 
	jlCadenaReemplazadora,
	jlSalida;
	private JTextField tfCadenaOriginal, 
					tfPatronER, 
					tfCadenaReemplazadora,
					tfSalida;
	private JButton btnGenerar;
	public PanelPrincipal() {
		super();
		this.setLayout(null);
		
		this.jlCadenaOriginal  = new JLabel();
		this.jlPatronER  = new JLabel();
		this.jlCadenaReemplazadora  = new JLabel();
		this.jlSalida  = new JLabel();
		this.jlTitulo  = new JLabel();
		
		this.tfCadenaOriginal = new JTextField();
		this.tfPatronER = new JTextField();
		this.tfCadenaReemplazadora = new JTextField();
		this.tfSalida = new JTextField();
		this.btnGenerar = new JButton("Generar");
		
		this.btnGenerar.addActionListener(this);

		
		this.jlTitulo.setText("Reemplazador con expresiones regulares");
		this.jlCadenaOriginal.setText("Cadena original:");

		this.jlPatronER.setText("Patron de entrada:");
		this.jlCadenaReemplazadora.setText("Cadena reemplazadora:");
		this.jlSalida.setText("Salida:");
		this.setPreferredSize(new Dimension(600,400));
		
		//Set Font
		this.jlTitulo.setFont(new Font("Serif", Font.PLAIN, 32));

		this.jlCadenaOriginal.setFont(new Font("Serif", Font.ITALIC, 18));
		this.jlPatronER.setFont(new Font("Serif", Font.ITALIC, 18));
		this.jlCadenaReemplazadora.setFont(new Font("Serif", Font.ITALIC, 18));		
		this.jlSalida.setFont(new Font("Serif", Font.BOLD, 18));
		
		
		//SetBounds

		this.jlTitulo.setBounds(50, 10, 4000, 60);
		this.jlCadenaOriginal.setBounds(225, 70, 150, 25);
		this.jlPatronER.setBounds(225, 130, 150, 25);
		this.jlCadenaReemplazadora.setBounds(210, 190, 180, 25);
		this.jlSalida.setBounds(260, 250, 75, 25);
		
		this.tfCadenaOriginal.setBounds(100, 100, 400, 25);
		this.tfPatronER.setBounds(100, 160, 400, 25);		
		this.tfCadenaReemplazadora.setBounds(100, 220, 400, 25);
		this.tfSalida.setBounds(100, 280, 400, 25);
		
		this.btnGenerar.setBounds(225, 325, 150, 50);
		
		
		this.add(this.tfCadenaReemplazadora);
		this.add(this.tfCadenaOriginal);
		this.add(this.tfPatronER);
		this.add(this.tfSalida);
		
		this.add(this.jlTitulo);
		this.add(this.jlCadenaReemplazadora);
		this.add(this.jlCadenaOriginal);
		this.add(this.jlPatronER);
		this.add(this.jlSalida);
		
		this.add(this.btnGenerar);
	}
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
	}
	@Override
	public void actionPerformed(ActionEvent evt) {
		String cadenaOriginal, 
		patronER, 
		cadenaReemplazadora;
		cadenaOriginal=	patronER=cadenaReemplazadora="";
		
		boolean flag=true; 
		if((this.tfCadenaOriginal.getText().equals(""))) {
			flag = false;
			JOptionPane.showMessageDialog(null,"La cadena original esta vacia");

		}else {
			cadenaOriginal = this.tfCadenaOriginal.getText();

		}
		if((this.tfPatronER.getText().equals("")) || this.tfPatronER.getText() == null) {
			JOptionPane.showMessageDialog(null,"La cadena reemplazadora no puede ser de longitud 0 (cadena vacia)");
			
			flag = false;
		}else {
			patronER = this.tfPatronER.getText();
		}
		if(this.tfCadenaReemplazadora.getText() == null || this.tfCadenaReemplazadora.getText().length()==0) {
			if(this.tfCadenaReemplazadora.getText().equals("")) {
				cadenaReemplazadora = this.tfCadenaReemplazadora.getText();
			}else {
				JOptionPane.showMessageDialog(null,"La cadena reemplazadora no puede ser de longitud 0");
				flag = false;
			}

		}else {
			cadenaReemplazadora = this.tfCadenaReemplazadora.getText();
		}
		if(flag  == true) {
			JOptionPane.showMessageDialog(null,"Se logro con exito");

			Reemplazador reemplazador = new Reemplazador(cadenaOriginal,patronER,cadenaReemplazadora);
			reemplazador.replacer();
			this.tfSalida.setText(reemplazador.useSwap());
		}else {
			
		}	
					
	}
	
}

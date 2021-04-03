import javax.swing.JFrame;

public class Gui extends JFrame  {
	 public Gui() {
		 super("Reemplazador con expresiones regulares");
		 this.setVisible(true);
		 this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		 this.add(new PanelPrincipal());
		 this.setResizable(false);
		 this.pack();
	 }
	public static void main(String[] args) {
		Gui gui= new Gui();
		
	}
}

public class Meno extends Thread {
	
	int[][] utvonal;
	int lepes;
	FallingGemsPanel panel;

	Meno(FallingGemsPanel panel,int[][] utvonal,int lepes) {
		this.panel=panel;
		this.utvonal=utvonal;
		this.lepes=lepes;
		if (FallingGemsPanel.DEBUG) for (int i=0;i<=lepes;i++) System.out.print(Integer.toString(utvonal[0][i])+","+Integer.toString(utvonal[1][i])+";");
		if (FallingGemsPanel.DEBUG) System.out.println();
	}

        @Override
	public void run() {
		panel.valasztott=false;
		int col=panel.tabla[utvonal[0][0]][utvonal[1][0]];
		if (FallingGemsPanel.DEBUG) System.out.println(col);
		for (int i=1;i<=lepes;i++) {
			try {
				sleep(50);
			} catch (InterruptedException e) {  }
			panel.tabla[utvonal[0][i-1]][utvonal[1][i-1]]=0;
			if (FallingGemsPanel.DEBUG) System.out.println("Meneteles: "+Integer.toString(i)+". lepes: "+Integer.toString(utvonal[0][i])+","+Integer.toString(utvonal[1][i]));
			panel.tabla[utvonal[0][i]][utvonal[1][i]]=col;
			// if (panel.debug) panel.tablakiir(panel.tabla);
			panel.validate();
			panel.repaint();
		
		}
		int pluszpont=panel.sullyed();
		panel.pont+=pluszpont;
		if (pluszpont==0) panel.ejtes();
		panel.pont+=panel.sullyed();
		panel.validate();
		panel.repaint();
		panel.menes=false;
		if (FallingGemsPanel.DEBUG) System.out.println("Szal vege.");
	}
}
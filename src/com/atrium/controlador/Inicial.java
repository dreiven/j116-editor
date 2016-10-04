package com.atrium.controlador;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Inicial {

	public static void main(String[] args) {
		// CARGAR EL NUEVO LOOK & FEEL SI SE PUEDE
				// RECOGEMOS LA LISTA DE OBJETOS LOOK AND FELL DISPONIBLES EN EL JAVA
				// JDK
				LookAndFeelInfo[] tabla_laf = UIManager.getInstalledLookAndFeels();

				for (LookAndFeelInfo objeto_apariencia : tabla_laf) {
					// COMPROBAMOS SI EXISTE NIMBUS
					if (objeto_apariencia.getName().equals("Nimbus")) {

						try {

							UIManager.setLookAndFeel(objeto_apariencia.getClassName());
							System.out.println("cargando Nimbus");

						} catch (Exception ex) {
							System.out.println("error en la carga de nimbus");
							ex.printStackTrace();
						}

					}

				}
         
		Ventana view = new Ventana();
	}

}

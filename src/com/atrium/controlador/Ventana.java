package com.atrium.controlador;

import javax.swing.JFrame;

public class Ventana extends JFrame{
	
	public Ventana(){
		
		setBounds(100, 100, 500, 450);
		setLayout(null);
	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		crear_interface();
		setVisible(true);
	
		
	}
	public void crear_interface(){
		Editor editor = new Editor(480,400);
		editor.setActivar_menu(true);
		add(editor);
	}

}

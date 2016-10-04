package com.atrium.util;

/**
 * Excepcion que nos informara del fallo en la copia de seguridad de los
 * ficheros.
 * 
 * @author Albert Arranz.
 * @version 1.0.
 * @since 31-3-2016.
 * 
 */
public class Fallo_EnCopiaException extends Exception

implements ITipos_FalloCopias {

	private int causa_fallo;

	public Fallo_EnCopiaException(int causa_fallo) {

		this.causa_fallo = causa_fallo;

	}

	// ACCESORES
	public void setCausa_fallo(int causa_fallo) {
		this.causa_fallo = causa_fallo;
	}

	public int getCausa_fallo() {
		return causa_fallo;
	}

}

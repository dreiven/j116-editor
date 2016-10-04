package com.atrium.imagenes;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Tratamiento general de las imagenes en nuestra aplicacion.
 * 
 * @author Albert Arranz
 * @version 1.5
 * @since 31-3-2016.
 * 
 */
public class Obtener_Imagenes implements IDefinicion_Imagenes{


	/**
	 * Proceso de escalado de imagenes.
	 * 
	 * @param ruta_nombre
	 *            Ruta interna del icono/imagen.
	 * @param tamaño
	 *            Valor entero que representa el tamaño a aplicar, definido en
	 *            constantes de clase.
	 * @return Imagen escalada.
	 */
	
	public static ImageIcon escalar_Imagen(String ruta_nombre, int tamaño) {
		// OBTENEMOS LA IMAGEN A ESCALAR
		ImageIcon imagen_tratada = new ImageIcon(
				Obtener_Imagenes.class.getResource(ruta_nombre));
		// ESTABLECEMOS LA ESCALA SEGUN NECESIDADES 
		// SE INSTANCIA DE NUEVO IMAGEN_TRATADA YA QUE HAY QUE HACERLO EN 2 PASOS PRIMERO SE CARGA ,LUEGO SE TRATA
		if (tamaño == _16X16) {
			imagen_tratada = new ImageIcon(imagen_tratada.getImage()
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH));
		}
		if (tamaño == _24X24) {
			imagen_tratada = new ImageIcon(imagen_tratada.getImage()
					.getScaledInstance(24, 24, Image.SCALE_SMOOTH));
		}
		if (tamaño == _32X32) {
			imagen_tratada = new ImageIcon(imagen_tratada.getImage()
					.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
		}
		if (tamaño == _48X48) {
			imagen_tratada = new ImageIcon(imagen_tratada.getImage()
					.getScaledInstance(48, 48, Image.SCALE_SMOOTH));
		}
		if (tamaño == _60X60) {
			imagen_tratada = new ImageIcon(imagen_tratada.getImage()
					.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
		}
		if (tamaño == _256X256) {
			imagen_tratada = new ImageIcon(imagen_tratada.getImage()
					.getScaledInstance(256, 256, Image.SCALE_SMOOTH));
		}
		return imagen_tratada;
	}
	/**
	 * Proceso de escalado de imagenes a 24x24.<br/>
	 * Metodo de conveniencia para pasar menos parametros en la llamada al
	 * metodo.
	 * 
	 * @param ruta_nombre
	 *            Ruta interna del icono/imagen.
	 * @return Imagen escalada.
	 */
	public static ImageIcon escalar_Imagen(String ruta_nombre) {
		return escalar_Imagen(ruta_nombre, IDefinicion_Imagenes._24X24);
	}
	/**
	 * Proceso de escalado de imagenes a como se quiera.<br/>
	 * Metodo de conveniencia para pasar menos parametros en la llamada al
	 * metodo.
	 * 
	 * @param ruta_nombre
	 *            Ruta interna del icono/imagen.
	 * @return Imagen escalada.
	 */
	public static ImageIcon escalar_Imagen(String ruta_nombre,int ancho,int largo) {
		return escalar_Imagen(ruta_nombre,ancho,largo);
	}
	
}

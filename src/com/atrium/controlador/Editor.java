package com.atrium.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.undo.UndoManager;





import com.atrium.imagenes.Obtener_Imagenes;
import com.atrium.util.Copia_Seguridad;
import com.atrium.util.Fallo_EnCopiaException;
import java.util.ResourceBundle;

/**
 * Editor de texto plano reutilizable siguientes opciones:
 * <ul>
 * <li>Posicion</li>
 * <li>Tamaño</li>
 * <li>Botones (operativa)</li>
 * <li>Uso de menu contextual.</li>
 * </ul>
 * 
 * @author Albert Arranz.
 * @version 2.1.
 * @since 31-3-2016.
 * 
 */
public class Editor extends JPanel implements ActionListener ,MouseListener{
	private  ResourceBundle br = ResourceBundle.getBundle("com.atrium.controlador.Textos_en"); //$NON-NLS-1$
	
    
	// PROPIEDADES DE CLASE PARA EL CONTROL DE TAMAÑO Y POSICION
	private int x;
	private int y;
	private int ancho;
	private int alto;
	// PROPIEDAD DE CONTROL QUE INDICA Y SE USAN LOS BOTONES ADICIONALES DE LA
	// BARRA DE HERRAMIENTAS
	private boolean mostrar_ag;
	// ESCUCHADOR DE EVENTOS QUE GESTIONA LOS PROCESOS DE HACER/DESHACER
	private UndoManager gestor_undo;
	// ***** PANELES DE PANTALLA ******
	// **** COMPONENTES DEL EDITOR ****
	private boolean elegido = false;
	// COMPONENTE DE SWING QUE MUESTRA LA VENTANA DE ABRI/CERAR FICHEROS
    private JFileChooser selector_ficheros;
	// VARIABLE QUE CONTENDRA EL TEXTO DEL FICHERO
	private StringBuilder texto_total;
	// GUARDA EL FILE PREVIAMENTE ABIERTO POR EL USUARIO PARA SOBREESCRIBIR
	// DIRECTAMENTE
	private File fichero_seleccionado;
	// BARRA DE HERRAMIENTAS
	private JToolBar barra_botones;
	// CONTENDOR DE SCROLL PARA VISUALIZAR EL CONTENDO DEL TEXTO
	private JScrollPane ventana_editor;
	// EDITOR PARA TEXTO SIN FORMATO
	private JTextArea area_texto;
	// EDITOR PARA TEXTO CON FORMATO (RTF) SIN IMPLEMENTAR.
	private JEditorPane area_texto_formato;
	// BOTONES DE LA BARRRA DE HERRAMIENTAS
	private JButton cortar;
	private JButton copiar;
	private JButton pegar;
	private JButton rehacer;
	private JButton deshacer;
	private JButton abrir;
	private JButton cerrar;
	private JButton cerrar_directo;
	private JButton imprimir;
	private JButton limpiar_texto;

	// MENU DE DESPLIEGLE RAPIDO
	private JPopupMenu menu_rapido;
	private JMenuItem cortar_m;
	private JMenuItem copiar_m;
	private JMenuItem pegar_m;
	private JMenuItem borrar_m;
	private boolean activar_menu = false;
	
	// ************* ZONA DE CONSTRUCTORES *******************************
	/**
	 * Constructor por defecto. <br/>
	 * Nos sirve para seguir la norma javabean. OBLIGATORIO
	 */
	public Editor() {
	}

	/**
	 * Constructor que establece de forma dinamica la posicion (x,y) y el ancho
	 * y alto necesario en cada momento.<br/>
	 * La unidad de medida es pixels.<br/>
	 * El valor por defecto para el indicador de uso de ficheros externos es
	 * TRUE (se usan).
	 * 
	 * @param x
	 *            Posicion en el contenedor.
	 * @param y
	 *            Posicion en el contenedor.
	 * @param ancho
	 *            Tamaño del componente.
	 * @param alto
	 *            Tamaño del componente.
	 */
	public Editor(int x, int y, int ancho, int alto) {
		// PARAMETROS DE POSICION Y TAMAÑO
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		// POLITICA POR DEFECTO es true
		mostrar_ag = true;
		// CONFIGURACION DE PROPIEDADES DEL CONTENEDOR
		this.setBounds(this.x, this.y, this.ancho, this.alto);
		this.setLayout(null);

		this.crear_Interface();
	}

	/**
	 * Constructor que establece de forma dinamica la posicion (x,y) y el ancho
	 * y alto necesario en cada momento.<br/>
	 * La unidad de medida es pixels.<br/>
	 * El valor por defecto para el indicador de uso de ficheros externos es
	 * falso (no se usan). Por lo tanto puede ser un valor nulo.
	 * 
	 * @param x
	 *            Posicion en el contenedor.
	 * @param y
	 *            Posicion en el contenedor.
	 * @param ancho
	 *            Tamaño del componente.
	 * @param alto
	 *            Tamaño del componente.
	 * @param mostar_ag
	 *            Indicador del uso de las herramientas de abrir/guardar/guardar
	 *            como.
	 */
	public Editor(int x, int y, int ancho, int alto, Boolean mostar_ag) {
		// PARAMETROS DE POSICION Y TAMAÑO
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		// INDICADOR DE OPCIONES EN LA BARA DE HERRAMIENTAS
		if (mostar_ag != null) {
			this.mostrar_ag = mostar_ag.booleanValue();
		}
		// CONFIGURACION DE PROPIEDADES DEL CONTENEDOR
		this.setBounds(this.x, this.y, this.ancho, this.alto);
		this.setLayout(null);

		this.crear_Interface();
	}

	/**
	 * Constructor que establece de forma dinamica el ancho y alto necesario en
	 * cada momento.<br/>
	 * La unidad de medida es pixels.<br/>
	 * El valor por defecto para el indicador de uso de ficheros externos es
	 * TRUE (se usan) y la posicion dentro del contenedor sera la 0,0.
	 * 
	 * @param ancho
	 *            Tamaño del componente.
	 * @param alto
	 *            Tamaño del componente.
	 */
	public Editor(int ancho, int alto) {
		// PARAMETROS DE TAMAÑO
		this.ancho = ancho;
		this.alto = alto;
		// POLITICA POR DEFECTO (ya que no vienen en parametros)
		mostrar_ag = true;
		x = 0;
		y = 0;
		// CONFIGURACION DE PROPIEDADES DEL CONTENEDOR
		this.setBounds(this.x, this.y, this.ancho, this.alto);
		this.setLayout(null);

		this.crear_Interface();
	}

	// ************* FIN ZONA DE CONSTRUCTORES *******************************

	/**
	 * Proceso de creacion de la parte grafica del componente.
	 */
	private void crear_Interface() {

		// INICIO LA BARRA DE BOTONES
		barra_botones = new JToolBar();
		// BARRA DE BOTONES INAMOVIBLE
		barra_botones.setFloatable(false);
		// MODIFICO EL TIPO DE BORDE DEL CONTENEDOR
		barra_botones.setBorder(new EtchedBorder(EtchedBorder.RAISED, null,
				null));
		// CONFIGURO EL TAMAÑO DE LA BARRA DE BOTONES
		barra_botones.setBounds(1, 3, ancho - 2, 40);
		// AÑADO LA BARRA DE BOTONES AL PANEL
		this.add(barra_botones);

		// CREO LOS BOTONES CON ICONOS
		cortar = new JButton(Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/cortar.png",
				Obtener_Imagenes._32X32));
		copiar = new JButton(Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/copiar.png",
				Obtener_Imagenes._32X32));
		pegar = new JButton(Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/pegar.png",
				Obtener_Imagenes._32X32));
		rehacer = new JButton(Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/rehacer.png",
				Obtener_Imagenes._32X32));
		deshacer = new JButton(Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/deshacer.png",
				Obtener_Imagenes._32X32));
		imprimir = new JButton(Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/imprimir.png",
				Obtener_Imagenes._32X32));
		// BOTONES OPCIONALES PARA LA GESTION DE FICHEROS
		if (mostrar_ag) {
			abrir = new JButton(Obtener_Imagenes.escalar_Imagen(
					"/com/atrium/imagenes/iconos/abrir.png",
					Obtener_Imagenes._32X32));
			cerrar = new JButton(Obtener_Imagenes.escalar_Imagen(
					"/com/atrium/imagenes/iconos/guardar_como.png",
					Obtener_Imagenes._32X32));
			cerrar_directo = new JButton(Obtener_Imagenes.escalar_Imagen(
					"/com/atrium/imagenes/iconos/guardar.png",
					Obtener_Imagenes._32X32));
			limpiar_texto = new JButton(Obtener_Imagenes.escalar_Imagen(
					"/com/atrium/imagenes/iconos/trash.png",
					Obtener_Imagenes._32X32));
		}

		// CREAMOS LA BARA DE BOTONES Y SUS BOTONES
		// CARGO LA BARRA DE BOTONES CON LOS MISMOS
		barra_botones.add(cortar);
		barra_botones.add(copiar);
		barra_botones.add(pegar);
		barra_botones.add(rehacer);
		barra_botones.add(deshacer);
		barra_botones.addSeparator();
		barra_botones.add(imprimir);
		if (mostrar_ag) {
			barra_botones.addSeparator();
			barra_botones.add(abrir);
			barra_botones.add(cerrar);
			barra_botones.add(cerrar_directo);
			barra_botones.add(limpiar_texto);
		}

		// CREAMOS LA ZONA DE EDICION DE TEXTO Y SU CONTENEDOR
		// PREPARAMOS EL AREA DE TEXTO
		area_texto = new JTextArea();
		// CONTROL DEL PASO DE LAS PALABRAS COMPLETAS A LA SIGUIENTE LINEA
		area_texto.setWrapStyleWord(true);
		area_texto.setLineWrap(true);
		// GESTION DE LAS OPCIONES DE REHACER/DESHACER MEDIANTE EL UN
		// ESCUCHADOR DE EVENTOS
		gestor_undo = new UndoManager();
		area_texto.getDocument().addUndoableEditListener(gestor_undo);
		// CREAMOS Y PREPARAMOS EL CONTENEDOR DEL AREA DE TEXTO
		ventana_editor = new JScrollPane();
		ventana_editor.setBounds(0, 46, ancho, alto - 45);
		// PASAMOS EL CONTENIDO AL CONTENEDOR SERIA EL METODO ADD
		ventana_editor.setViewportView(area_texto);
		// ESTABLECEMOS LAS PROPIEDADES DE LAS BARRAS DE SCROLL SEGUN NECESIDAD
		// DE DISEÑO
		ventana_editor
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		ventana_editor
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// ACTIVAMOS EL RECONOCIMIENTO DE LA RUEDA DEL RATON PARA QUE MUEVA EL
		// SCROLL
		ventana_editor.setWheelScrollingEnabled(true);
		// MONTAMOS EL SCROLLPANE EN EL JPANEL
		this.add(ventana_editor);
		
		if (mostrar_ag){
			
			selector_ficheros = new JFileChooser();
			
		} 
		// ESTABLEZCO ESCUCHADORES PARA LOS BOTONES
		cortar.addActionListener(this);
		copiar.addActionListener(this);
		pegar.addActionListener(this);
		rehacer.addActionListener(this);
		deshacer.addActionListener(this);
		imprimir.addActionListener(this);
		if (mostrar_ag) {
			abrir.addActionListener(this);
			cerrar.addActionListener(this);
			cerrar_directo.addActionListener(this);
			limpiar_texto.addActionListener(this);
		}

		// DAMOS VALOR A LA PROPIEDAD NAME
		cortar.setName("cortar");
		copiar.setName("copiar");
		pegar.setName("pegar");
		rehacer.setName("rehacer");
		deshacer.setName("deshacer");
		imprimir.setName("imprimir");
		if (mostrar_ag) {
			abrir.setName("abrir");
			cerrar.setName("cerrar");
			cerrar_directo.setName("cerrar_directo");
			limpiar_texto.setName("limpiar_texto");
		}

		// CREAMOS LAS AYUDAS CONTEXTUALES DE LOS BOTONES
		cortar.setToolTipText("Boton de cortar texto");
		copiar.setToolTipText("Boton de copiar texto");
		pegar.setToolTipText("Boton de pegar texto");
		rehacer.setToolTipText("Boton de repetir la ultima accion");
		deshacer.setToolTipText("Boton de anular la ultima accion");
		imprimir.setToolTipText("Boton de imprimir");
		if (mostrar_ag) {
			abrir.setToolTipText("Boton de abrir fichero");
			cerrar.setToolTipText("Boton de cerrar fichero");
			cerrar_directo
					.setToolTipText("Guardar sobre escribiendo el fichero");
			limpiar_texto.setToolTipText("Borrado del texto en el editor");
		}
    
	}
	//********************AREA DE LOGICA
	/**
	 * Proceso de lectura del fichero seleccionado por el usuario en la ventana
	 * de abrir.<br/>
	 * En caso de que el fichero elegido no exista se crea vacio para su
	 * posterior uso.<br/>
	 * Una vez leido se vuelca al jtextarea.
	 */
	private void leer_Fichero() {
		// RECOGEMOS EL FICHERO SELECCIONADO DEL JFILECHOOSER POR ESO NO INSTANCIAMOS
		fichero_seleccionado = selector_ficheros.getSelectedFile();
		// CREAMOS EL FLUJO DE LECTURA
		FileReader flujo = null;
		try {
			flujo = new FileReader(fichero_seleccionado);
		} catch (FileNotFoundException e) {}
			// EL FICHERO NO EXISTE
//			try {
//				// SE CREA EL FICHERO EN BLANCO
//				FileWriter escritor = new FileWriter(fichero_seleccionado);
//				escritor.write("");
//				escritor.flush();
//				escritor.close();
//				flujo = new FileReader(fichero_seleccionado);
//			} catch (Exception exception) {
//				// ERROR EN LA CREACION
//			}
//		}
		// ENVOLTORIO DE LECTURA
		BufferedReader lector = new BufferedReader(flujo);
		String texto_leido = "";
		texto_total = new StringBuilder();
		// REALIZAMOS EL PROCESO DE LECTURA
		while (texto_leido != null) {
			try {
				// LECTURA HASTA EL INTRO (O CUALQUIER OTRO CARACTER EN FUNCION
				// DEL OPERATIVO)
				texto_leido = lector.readLine();
				if (texto_leido != null) {
					// ACUMULAMOS EL TEXTO TOTAL YA LEIDO DEL FICHERO Y
					// AÑADIMOS EL SALTO DE LINEA DEL OPERATIVO
					// FUNCIONA EN CUALQUIER SISTEMA OPERATIVO 
					texto_total.append(texto_leido
							+ System.getProperty("line.separator"));
				}
			} catch (IOException eVENT) {
				// SIN TRATAMIENTO
			}
		}
		// CARGAMOS EL TEXTO EN EL AREA DE TEXTO
		area_texto.setText(texto_total.toString());
		
		try {
			// CERRAMOS EL FLUJO PARA DESBLOQUEAR EL FICHERO
			//CERRAR LOS FLUJOS SINO PERMANECE BLOQUEADO POR EL SISTEMA OPERATIVO
			flujo.close();
		} catch (IOException eVE) {
			// SIN TRATAMIENTO
		}
		}
	
	/**
	 * Proceso de escritura el fichero.<br/>
	 * Se gestiona una copia de seguridad de la version anterior, si es que
	 * existe previamente el fichero. Se añade en el nombre de la copia de
	 * seguridad la datacion del momento en que se realiza (fecha-hora) y se
	 * graba en la misma ruta del original.
	 * 
	 * @param fichero
	 *            Nombre y ruta del fichero a guardar.
	 */
	private void escribir_Fichero(File fichero) {
		// FLUJO DE ESCRITURA
		FileWriter escritor = null;
		// OBJETO FILE QUE APUNTA AL FICHERO
		File fichero_ruta = null;
		if (fichero == null) {
			// CAPTURAMOS EL FILE PARA HACER EL PROCESO DE ESCRITURA
			fichero_ruta = selector_ficheros.getSelectedFile();
		} else {
			fichero_ruta = fichero;
		}
		// GESTION DE COPIA DE SEGURIDAD EN CASO DE EXISTIR PREVIAMENTE
		if (fichero_ruta.exists()) {
			try {
				String rutaTemp = "C:\\workspace";
				
				Copia_Seguridad.crear_copia(fichero_ruta); 
			} catch (Fallo_EnCopiaException e) {
				// TRATAMIENTO DE EXCEPCION PROPIA
				int tipo_fallo = e.getCausa_fallo();
			}
		}
		try {
			escritor = new FileWriter(fichero_ruta);
			// ESCRITURA DEL TEXTO
			escritor.write(area_texto.getText());
			//OBLIGATORIO EL FLUSH EN ESCRITURA YA QUE NOS ARRIESGAMOS A PERDER CONTENIDO
			escritor.flush();
			escritor.close();
		} catch (Exception e) {
			// FALTA TRATAMIENTO
		}
	}
	/**
	 * Proceso de guardado del fichero con el mismo nombre y ruta que tenia
	 * previamente.<br/>
	 * Segun se inicia fichero_seleccionado a null este proceso solo se podra
	 * hacer una vez, si se quiere dejar hacer mas veces seguidas el proceso de
	 * guardar se eliminara la linea.
	 */
	private void guardar_Fichero() {
		if (fichero_seleccionado != null) {
			this.escribir_Fichero(fichero_seleccionado);
		}
		// CONTROL DE REPETICION DE PROCESO
		fichero_seleccionado = null;
	}
	/**
	 * Proceso de creacion de los elementos necesarios para habilitar el menu
	 * contextual del editor.
	 */
	private void crear_MenuContextual() {
		// ESCUCHAMOS EVENTOS DE RATON SOBRE EL AREA DE TEXTO
		area_texto.addMouseListener(this);
		// CREAMOS EL MENU CONTEXTUAL
		menu_rapido = new JPopupMenu();
		copiar_m = new JMenuItem("Copiar", Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/copiar.png",
				Obtener_Imagenes._24X24));
		cortar_m = new JMenuItem("Cortar", Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/cortar.png",
				Obtener_Imagenes._24X24));
		pegar_m = new JMenuItem("Pegar", Obtener_Imagenes.escalar_Imagen(
				"/com/atrium/imagenes/iconos/pegar.png",
				Obtener_Imagenes._24X24));
		borrar_m = new JMenuItem("Borrar");
		// COLOCAMOS EL CONTENIDO EN EL MENU EN EL ORDEN QUE QUERAMOS
		menu_rapido.add(copiar_m);
		menu_rapido.add(cortar_m);
		menu_rapido.addSeparator();
		menu_rapido.add(pegar_m);
		menu_rapido.addSeparator();
		menu_rapido.add(borrar_m);
		// AÑADIMOS VALOR A LA PROPIEDAD NAME PARA EL TRATAMIENTO DEL EVENTO
		copiar_m.setName("copiar");
		cortar_m.setName("cortar");
		pegar_m.setName("pegar");
		borrar_m.setName("limpiar_texto");
		// ASOCIAMOS EL ESCUCHADOR AL COMPONENTE
		copiar_m.addActionListener(this);
		cortar_m.addActionListener(this);
		pegar_m.addActionListener(this);
		borrar_m.addActionListener(this);
	}
	/**
	 * Proceso basico de impresion de fichero controlado por codigo.
	 */
	private void imprimir_Fichero() {
		// FLUJO LECTURA FICHERO
		FileInputStream lector = null;
		try {
			lector = new FileInputStream(fichero_seleccionado);
		} catch (FileNotFoundException ffne) {
			// NO IMPLEMENTADO
		}
		if (lector == null) {
			return;
		}
		// ESTABLECEMOS EL FORMATO DEL FICHERO
		DocFlavor formato_texto = DocFlavor.INPUT_STREAM.AUTOSENSE;
		// CREAMOS EL DOCUMENTO
		Doc documento = new SimpleDoc(lector, formato_texto, null);
		// CARACTERISTICAS DE LA IMPRESION, DADAS POR LA IMPRESORA DONDE SE
		// IMPRIMA
		PrintRequestAttributeSet caracteristicas = new HashPrintRequestAttributeSet();
		// caracteristicas.add(new Copies(5));
		// caracteristicas.add(MediaSizeName.ISO_A4);
		// caracteristicas.add(Sides.DUPLEX);
		// SE BUSCA LA IMPRESORA CAPAZ DE GESTIONAR EL DOCUMENTO EN FUNCION DE
		// LAS CARACTERISTICAS DEFINIDAS
		PrintService impresoras[] = PrintServiceLookup.lookupPrintServices(
				formato_texto, caracteristicas);
		// CREAMOS UN SERVICIO DE IMPRESORA A PARTIR DE LAS IMPRESORAS DEL
		// SISTEMA OPERATIVO
		if (impresoras.length > 0) {
			DocPrintJob tarea_impresion = impresoras[0].createPrintJob();
			try {
				tarea_impresion.print(documento, caracteristicas);
			} catch (PrintException pe) {
				System.out.println("ERROR EN LA IMPRESION");
			}
		}
	}

	/**
	 * Proceso de impresion con control interactivo por parte del usuario.
	 */
	public void imprimir_Ficherointeractivo() {
		try {
			// METODO QUE HABRE LA VENTANA DE SELECCION DE OPCIONES DE
			// IMPRESORA.
			area_texto.print(null, null, true, null, null, true);
		} catch (PrinterException e) {
			System.out.println("ERROR EN LA IMPRESION");
		}
	}
	// ************** FIN AREA DE LOGICA DE PROCESO ******************

	
	// **************** AREA DE EVENTOS ******************************
	// ************** EVENTOS DE ACCION
	/**
	 * Gestion de las acciones de eventos de boton.<br/>
	 * <br/>
	 * <table border=1>
	 * <thead>
	 * <tr aling="center">
	 * <th colspan="2" >
	 * Procesos definidos</td></th>
	 * <tr>
	 * <th>
	 * Nombre Evento</th>
	 * <th>
	 * Accion</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>
	 * Cortar</td>
	 * <td>
	 * Cortar el texto previamente seleccionado</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * Copiar</td>
	 * <td>
	 * Copia el texto previamente seleccionado</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * Pegar</td>
	 * <td>
	 * Pega el texto previamente seleccionado</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * Rehacer</td>
	 * <td>
	 * Repite la ultima accion de texto echa</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * Deshacer</td>
	 * <td>
	 * Elimina el ultimo texto o accion echa</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * Abrir</td>
	 * <td>
	 * El usuario seleccion en la ventana de abrir un nuevo fichero</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * Imprimir</td>
	 * <td>
	 * Inicio del proceso de impresion del texto</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * Guardar</td>
	 * <td>
	 * Se guarda el contenido de la modificacion del texto echa por el usuario</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * Guardar como</td>
	 * <td>
	 * Se guarda el contenido de la modificacion del texto con un nuevo nombre
	 * y/o ruta</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	@Override
	public void actionPerformed(ActionEvent evento) {
   
    		// OBTENEMOS EL VALOR NECESARIO PARA SABER EL BOTON PULSADO.
    			String nombre_boton = ((JComponent) evento.getSource()).getName();
    			// ******** BOTONES SIEMPRE VISIBLES OPERACIONES SIEMPRE DISPONIBLES
    			if (nombre_boton.equals("cortar")) {
    				if (area_texto.getSelectedText() != null) {
    					// METODO QUE CORTA
    					area_texto.cut();
    					elegido = true;
    				}
    			}
    			if (nombre_boton.equals("copiar")) {
    				if (area_texto.getSelectedText() != null) {
    					// METODO QUE COPIA
    					area_texto.copy();
    					elegido = true;
    				}
    			}
    			if (nombre_boton.equals("pegar")) {
    				if (elegido) {
    					// METODO QUE PEGA
    					area_texto.paste();
    					//SOLO REALIZA UNA COPIA CON FALSE
    					//elegido = false;
    				}
    			}
    			if (nombre_boton.equals("rehacer")) {
    				if (gestor_undo.canRedo()) {
    					// METODO QUE REPITE LA ULTIMA OPERACION
    					gestor_undo.redo();
    				}
    			}
    			if (nombre_boton.equals("deshacer")) {
    				if (gestor_undo.canUndo()) {
    					// METODO QUE DESHACE LA ULTIMA OPERACION
    					gestor_undo.undo();
    				}
    			}

    			if (nombre_boton.equals("imprimir")) {
    				// LLAMADA AL PROCESO DE IMPRESION
    				this.imprimir_Ficherointeractivo();
    			}

    			// *********** BOTONES OPCIONALES OPERACIONES NO SIEMPRE DISPONIBLES
    			if (nombre_boton.equals("abrir")) {
    				// VENTANA MODAL DE APERTURA DE FICHEROS
    				int boton_pulsado = selector_ficheros.showOpenDialog(this);
    				if (boton_pulsado == JFileChooser.APPROVE_OPTION) {
    				this.leer_Fichero();
    				}
    			}
    			if (nombre_boton.equals("cerrar")) {
    				// VENTANA MODAL DE GUARDAR FICHEROS
    				int boton_pulsado = selector_ficheros.showSaveDialog(this);
    				if (boton_pulsado == JFileChooser.APPROVE_OPTION) {
    					this.escribir_Fichero(null);
    				}
    			//}
    			if (nombre_boton.equals("cerrar_directo")) {
    				this.guardar_Fichero();
    			}
    			if (nombre_boton.equals("limpiar_texto")) {
    				area_texto.setText(" ");
    				
    			
    			}
		
		
	}

	}

	@Override
	public void mouseClicked(java.awt.event.MouseEvent evento) {
		// TRATAMOS SI ES EL BOTON DERECHO
				if (evento.getButton() == java.awt.event.MouseEvent.BUTTON3) {
					// MOSTRAMOS EL MENU EL LA POSICION DEL CURSOR DEL RATON
					menu_rapido.show(area_texto, evento.getX(), evento.getY());
				}
		
	}

	@Override
	public void mouseEntered(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(java.awt.event.MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	// **************** FIN AREA DE EVENTOS **************************

	// ********** ACCESORES PARA LAS PROPIEDADES DE CLASE ************

	public void setActivar_menu(boolean activar_menu) {
		this.activar_menu = activar_menu;
		if (activar_menu) {
			crear_MenuContextual();
		}
	}

	public boolean isActivar_menu() {
		return activar_menu;
	}

}
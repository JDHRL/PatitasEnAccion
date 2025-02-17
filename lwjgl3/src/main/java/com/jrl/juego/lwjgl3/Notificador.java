package com.jrl.juego.lwjgl3;

import com.jrl.juego.entidades.Notificacion;

import java.awt.*;
import java.awt.event.*;

public class Notificador implements Notificacion {
    
    private SystemTray tray;
    private TrayIcon trayIcon;
    
    public Notificador() {
        // Verifica si el sistema soporta SystemTray
        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();
            // Crear una imagen para el icono en la bandeja (usa una imagen válida)
            Image image = Toolkit.getDefaultToolkit().createImage("icono.png");  // Reemplaza con la ruta de tu icono
            trayIcon = new TrayIcon(image, "Notificador");

            // Configurar el tamaño automático de la imagen
            trayIcon.setImageAutoSize(true);

            // Crear un menú emergente (popup menu)
            PopupMenu popupMenu = new PopupMenu();
            
            // Opción para salir del programa
            MenuItem exitItem = new MenuItem("Salir");
            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tray.remove(trayIcon);  // Eliminar el icono de la bandeja
                    System.exit(0);  // Cerrar la aplicación
                }
            });
            popupMenu.add(exitItem);
            
            // Asignar el menú al trayIcon
            trayIcon.setPopupMenu(popupMenu);

            // Agregar el trayIcon a la bandeja del sistema
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("SystemTray no es compatible con este sistema por favor instale las dependencias nesesarias para java.");
        }
    }

    @Override
    public void notificar(String mensaje) {
        // Mostrar una notificación con el mensaje
        if (SystemTray.isSupported()) {
        trayIcon.displayMessage("Notificación", mensaje, TrayIcon.MessageType.INFO);
        }else{
            System.out.println("SystemTray no es compatible con este sistema por favor instale las dependencias nesesarias para java.");
        }
    }
    
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.puntoVenta;

import com.formularios.AgregarCliente;
import com.formularios.AgregarProducto;
import com.formularios.AgregarVendedor;
import com.oyentes.OyenteAgregarCliente;
import com.oyentes.OyenteAgregarProducto;
import com.oyentes.OyenteAgregarVendedor;
import com.oyentes.OyenteReporteVentas;
import com.tablas.TablaModeloProducto;
import com.tablas.TablaRenderizadorProducto;
import com.modificar.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author JR
 */
public class OyenteReportes implements KeyListener, ActionListener, WindowListener  {
    private Conexion usuario;
    static Reportes ventanaReporte;
    private JTable productos;
    private PanelVentas p;
    private PanelVendedores p1;
    private PanelProductosMasVendidos ppmv;
    private PanelProductos p2;
    private PanelCatalogo catalogo;
    private PanelNuevaVenta panelNuevaVenta;
    private VentanaEmergente ventana;
    private TableRowSorter trsfiltro;
    private com.tablas.TablaModeloProducto modelo;
    private String nombreVendedor;
    private boolean ctrl=false;
    private boolean alt=false;

    
    OyenteReportes() {
        panelNuevaVenta = null;
    }

    OyenteReportes(Reportes ventaReportes, Conexion usuario) {
        this.ventanaReporte = ventaReportes;
        this.usuario = usuario;
    }

    public void setVentanaReporte(Reportes ventanaReporte) {
        this.ventanaReporte = ventanaReporte;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Ventas":
                p = new PanelVentas();
                p.addEventos(new OyenteReporteVentas(p,usuario));

                ventana = new VentanaEmergente("Ventas");
                ventana.add(p);
                ventana.addWindowListener(this);
                ventana.setSize(570, 210);
                ventana.setLocationRelativeTo(null);
                ventana.setResizable(false);
                ventana.setVisible(true);
                SwingUtilities.updateComponentTreeUI(ventanaReporte);
                ventanaReporte.validate();

                break;
                
            case "Ventas por Vendedor":
                BuscarVendedor busqueda = new BuscarVendedor();
                
                break;
                
            case "Productos mas vendidos":
                
                System.out.println("Aqui se muestran los productos mas vendidos");
                
                ppmv = new PanelProductosMasVendidos();

                ventana = new VentanaEmergente("Productos más vendidos");
                OyenteProductosMasVendidos oppmv = new OyenteProductosMasVendidos(ppmv, usuario);
                ppmv.addEventos(oppmv);
                
                ventana.setSize(850, 450);
                ventana.setLocationRelativeTo(null);
                ventana.setResizable(false);
                ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                ventana.add(ppmv);
                
                ventana.addWindowListener(oppmv);
                ventana.setVisible(true);
                
                SwingUtilities.updateComponentTreeUI(ventanaReporte);
                ventanaReporte.validate();
                break;
                
            case "Cerrar sesión":
                int opcion = JOptionPane.showConfirmDialog(ventanaReporte, "Se cerrará la sesión actual", 
                        "Cerrar sesión", JOptionPane.OK_CANCEL_OPTION);
                
                if(opcion == JOptionPane.OK_OPTION){
                    ventanaReporte.dispose();
                    main.mostrarLogin();
                }
                break;
                
            case "Acerca de":
                System.out.println("Mostrara acerca del programa");
                JOptionPane.showMessageDialog(null, "PiñaSports®"
                        + "\nVersión 1.0.0"
                        + "\nCopyright© 2014 - 2015"
                        + "\nTodos los derechos reservados"
                        + "\nEste software fue desarrollado por:"
                        + "\n     Bobadilla Contreras Miguel Fernando"
                        + "\n     Márquez Solano José Ramón"
                        + "\n     Pérez Muñoz Luis Ángel"
                        + "\n     Pérez Rodríguez José Rubén",
                        
                        
                        "Acerca de", JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon("src/img/sistema/acercaDe.png"));

                break;
                
            case "Actualizar":
                System.out.println("Aqui actualizara");
                break;
                
            
            case "Nueva Venta":
                ventana = new VentanaEmergente("Ventas");
                
                 JTable prods = generarCatalogo();
                panelNuevaVenta = new PanelNuevaVenta(prods, usuario);
                panelNuevaVenta.getTextAtiende().setText(nombreVendedor);
                
                OyenteNuevaVenta oyenteNV = new OyenteNuevaVenta(ventana, panelNuevaVenta);
                
                panelNuevaVenta.addEventos(oyenteNV);
                
                ventana.add(panelNuevaVenta);
                ventana.setSize(900, 555);
                ventana.setLocationRelativeTo(null);
                ventana.addWindowListener(this);
                ventana.setVisible(false);
                oyenteNV.validarNuevaVenta();
                break;
                
                
            case "Catalogo de Productos":
                productos = generarCatalogo();
                catalogo = new PanelCatalogo(productos);
                catalogo.addEventos(this);
                trsfiltro = new TableRowSorter(modelo);
                productos.setRowSorter(trsfiltro);

                ventana = new VentanaEmergente("Ventas");
                ventana.add(catalogo);
                ventana.addWindowListener(this);
                ventana.setSize(900, 520);
                ventana.setLocationRelativeTo(null);
                ventana.setResizable(false);
                ventana.setVisible(true);
                SwingUtilities.updateComponentTreeUI(ventanaReporte);
                break;

            case "Agrega un Producto":
                AgregarProducto aP =new AgregarProducto();
                usuario.iniciarConexion();

                aP.addEventos(new OyenteAgregarProducto(aP, usuario));

                break;
                
            case "Agregar un Cliente":
                AgregarCliente aC = new AgregarCliente();
                usuario.iniciarConexion();
                aC.addEventos(new OyenteAgregarCliente(aC, usuario));
                break;
                
            case "Agregar un Vendedor":
                AgregarVendedor aV = new AgregarVendedor();
                usuario.iniciarConexion();
                aV.addEventos(new OyenteAgregarVendedor(usuario,aV));
                break;
                
            case  "Modificar un Cliente" : 
//                System.out.println("Mod cliente");
                ModificarCliente mc = new ModificarCliente(usuario);
                break;
                
            case  "Modificar un Vendedor" : 
                System.out.println("Mod vendedor");
                usuario.iniciarConexion();
                ModificarVendedor mv = new ModificarVendedor(usuario);
                break;
                
            case  "Modificar un Producto" : 
                System.out.println("Mod producto");
                ModificarProducto mp = new ModificarProducto(usuario);
                break;

            case  "Eliminar un Cliente" : 
                EliminarCliente ec = new EliminarCliente(usuario);
                System.out.println("DEl cliente");
                break;
                
            case  "Eliminar un Vendedor" : 
                System.out.println("del vendedor");
                EliminarVendedor ev = new EliminarVendedor(usuario);
                break;
            case  "Eliminar un Producto" : 
                System.out.println("del producto");
                EliminarProducto ep = new EliminarProducto(usuario);
                break;
                
            case "Eliminar Cabecera Factura":
                System.out.println("Eliminar cabecera");
                EliminarCabecera EC = new EliminarCabecera(usuario);
                break;
                
            case "Eliminar Detalle Factura":
                System.out.println("Eliminar detalle ");
                EliminarDetalle ed = new EliminarDetalle(usuario);
                break;
                
                
                    
        }
        
        
    }
     
   
    public void filtro(){
        try{
            String filtro = catalogo.getTbusqueda().getText();
            switch(catalogo.getBusqueda().getSelectedIndex()){
                case 0:
                     trsfiltro.setRowFilter(RowFilter.regexFilter("(?i)"+filtro, 0));
                    break;
                case 1:
                     trsfiltro.setRowFilter(RowFilter.regexFilter("(?i)"+filtro, 1));
                    break;
                case 2:
                     trsfiltro.setRowFilter(RowFilter.regexFilter("(?i)"+filtro, 2));
                     break;
                case 3: 
                     trsfiltro.setRowFilter(RowFilter.regexFilter("(?i)"+filtro, 4));
                     break;

            }
        
        }catch(NullPointerException e){}
       
    }
    
    private JTable generarCatalogo() {
        String query = "SELECT * FROM Producto";
        modelo = new TablaModeloProducto();
        TablaRenderizadorProducto render = new TablaRenderizadorProducto();
        
        JTable tablaProductos = new JTable(modelo);
        tablaProductos.setRowHeight(100);
        tablaProductos.setDefaultRenderer(ImageIcon.class, render);
       

        try {
            usuario.iniciarConexion();
            usuario.setResult(usuario.getStament().executeQuery(query));

//            // Bucle para cada resultado en la consulta
            while (usuario.getResult().next()) {
                // Se crea un array que será una de las filas de la tabla.
                Object[] fila = new Object[modelo.getColumnCount()]; // Hay tres columnas en la tabla

                // Se rellena cada posición del array con una de las columnas de la tabla en base de datos.
                for (int i = 0; i < modelo.getColumnCount(); i++) {
                    fila[i] = usuario.getResult().getObject(i + 1); // El primer indice en rs es el 1, no el cero, por eso se suma 1.
                }
                Producto p = new Producto(fila);
                modelo.agregarProducto(p);

            }
            
            tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(55);
            tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(130);
            tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(85);
            tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(120);
            tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(510);
            
            // Para alinear el texto de la primera celda al centro...
            DefaultTableCellRenderer alinear = new DefaultTableCellRenderer();
            alinear.setHorizontalAlignment(SwingConstants.CENTER);
            tablaProductos.getColumnModel().getColumn(0).setCellRenderer(alinear);
            
            usuario.getStament().close();
        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        } finally {
            usuario.cerrarConexion();
        }

        return tablaProductos;

    }
     
    ////////////////////////////GETTER Y SETTER ////////////////////////////////////    
    public Conexion getUsuario() {
        return usuario;
    }

    public void setUsuario(Conexion usuario) {
        this.usuario = usuario;
    }

    public String getNombreVendedor() {
        return nombreVendedor;
    }

    public void setNombreVendedor(String nombreVendedor) {
        this.nombreVendedor = nombreVendedor;
    }

    /* **********   */
    
    @Override
    public void keyTyped(KeyEvent e){
        try{
            filtro();
            
        }catch(NullPointerException ex){}
    }

    @Override
    public void keyPressed(KeyEvent e){
        //filtro();
        try{
            if(e.getKeyCode() == KeyEvent.VK_CONTROL ){
                ctrl = true;

            }
            if(e.getKeyCode() == KeyEvent.VK_ALT ){
                alt = true;

            }

            if(ctrl && alt &&e.getKeyCode() == KeyEvent.VK_C){
                System.out.println("Area de consulta oculta");
                AreaConsulta a = new AreaConsulta();
                ctrl = false;
                alt = false;
            }
        }catch(NullPointerException ex){}
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getSource().getClass().isInstance(ventana)) {
            ventana.dispose();

            
        } else if (e.getSource().getClass().isInstance(ventanaReporte)) {
            int opcion = JOptionPane.showConfirmDialog(ventanaReporte, 
                    "Se cerrará la sesión actual", "Advertencia", JOptionPane.OK_CANCEL_OPTION);

            if (opcion == JOptionPane.OK_OPTION) {
                ventanaReporte.dispose();
                main.mostrarLogin();
            }
        }
    }

}

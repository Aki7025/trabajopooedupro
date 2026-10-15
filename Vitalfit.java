package vitalfit;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;


public class Vitalfit extends JFrame {
    private ArrayList<Producto> productos = new ArrayList<>(); // almacenamiento temporal
    private JTextArea areaLista = new JTextArea();
    private JTextField txtNombre = new JTextField(12);

    public Vitalfit() {
        setTitle("Tienda Fitness - Productos");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelTop = new JPanel();
        panelTop.add(new JLabel("Buscar nombre:"));
        panelTop.add(txtNombre);
        JButton btnCargar = new JButton("Cargar todos");
        JButton btnBuscar = new JButton("Buscar");
        panelTop.add(btnCargar);
        panelTop.add(btnBuscar);

        areaLista.setEditable(false);
        add(panelTop, BorderLayout.NORTH);
        add(new JScrollPane(areaLista), BorderLayout.CENTER);

        btnCargar.addActionListener(e -> { cargarProductos(); mostrar(""); });
        btnBuscar.addActionListener(e -> {
            if (productos.isEmpty()) cargarProductos();
            mostrar(txtNombre.getText().trim());
        });
    }

    private void cargarProductos() {
        productos.clear();
        String sql = "SELECT id_producto, nombre, precio, stock FROM producto";
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                productos.add(new Producto(rs.getInt(1), rs.getString(2),
                                           rs.getInt(3), rs.getInt(4)));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de BD: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrar(String filtro) {
        areaLista.setText("");
        for (Producto p : productos) {
            if (filtro.isEmpty() || p.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                areaLista.append(p.getId() + " | " + p.getNombre()
                        + " | $" + p.getPrecio() + " | stock: " + p.getStock() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Vitalfit().setVisible(true));
    }
}

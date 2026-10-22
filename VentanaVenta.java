package vitalfit;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class VentanaVenta extends JFrame {
    private ArrayList<Producto> productos = new ArrayList<>();
    private JComboBox<String> cmbCliente = new JComboBox<>();
    private JComboBox<String> cmbProducto = new JComboBox<>();
    private JTextField txtCantidad = new JTextField(5);
    private JTextArea areaResumen = new JTextArea(8, 30);

    public VentanaVenta() {
        setTitle("Caja - Tienda Fitness");
        setSize(450, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Cliente:"));
        panel.add(cmbCliente);
        panel.add(new JLabel("Producto:"));
        panel.add(cmbProducto);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);
        JButton btnVender = new JButton("Registrar venta");
        panel.add(btnVender);

        areaResumen.setEditable(false);
        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(areaResumen), BorderLayout.CENTER);

        cargarDatos();
        btnVender.addActionListener(e -> registrarVenta());
    }

    private void cargarDatos() {
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery("SELECT id_cliente, nombre FROM cliente");
            while (rs.next()) {
                cmbCliente.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }

            productos.clear();
            cmbProducto.removeAllItems();
            rs = st.executeQuery("SELECT id_producto, nombre, precio, stock, id_categoria FROM producto");
            while (rs.next()) {
                productos.add(new Producto(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4)));
                cmbProducto.addItem(rs.getInt(1) + " - " + rs.getString(2));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de BD: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarVenta() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim()); // falla si escribes una letra
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }

            int idCliente = Integer.parseInt(cmbCliente.getSelectedItem().toString().split(" - ")[0]);
            Producto p = productos.get(cmbProducto.getSelectedIndex());

            if (cantidad > p.getStock()) {
                throw new IllegalArgumentException("Stock insuficiente. Disponible: " + p.getStock());
            }

            try (Connection con = Conexion.conectar()) {
                con.setAutoCommit(false);

                PreparedStatement psVenta = con.prepareStatement(
                        "INSERT INTO venta (id_cliente, fecha) VALUES (?, CURDATE())",
                        Statement.RETURN_GENERATED_KEYS);
                psVenta.setInt(1, idCliente);
                psVenta.executeUpdate();
                ResultSet keys = psVenta.getGeneratedKeys();
                keys.next();
                int idVenta = keys.getInt(1);

                PreparedStatement psDet = con.prepareStatement(
                        "INSERT INTO detalle_venta (id_venta, id_producto, cantidad) VALUES (?, ?, ?)");
                psDet.setInt(1, idVenta);
                psDet.setInt(2, p.getId());
                psDet.setInt(3, cantidad);
                psDet.executeUpdate();

                PreparedStatement psStock = con.prepareStatement(
                        "UPDATE producto SET stock = stock - ? WHERE id_producto = ?");
                psStock.setInt(1, cantidad);
                psStock.setInt(2, p.getId());
                psStock.executeUpdate();

                con.commit();
            }

            int total = p.getPrecio() * cantidad;
            areaResumen.append("Venta registrada: " + p.getNombre() + " x" + cantidad
                    + " = $" + total + "\n");
            JOptionPane.showMessageDialog(this, "Venta registrada. Total: $" + total);

            txtCantidad.setText("");
            cargarDatos(); // refleja el nuevo stock

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un número válido en cantidad",
                    "Error de formato", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de BD: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaVenta().setVisible(true));
    }
          }

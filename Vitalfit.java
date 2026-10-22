import javax.swing.*;
import java.awt.*;

public class vitalfit extends JFrame {

    public VentanaInicio() {
        setTitle("Tienda Fitness - Inicio");
        setSize(350, 430);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel titulo = new JLabel("Panel de administración", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titulo);

        // {texto del botón, nombre de la tabla, llave primaria}
        String[][] tablas = {
            {"Categorías", "categoria", "id_categoria"},
            {"Productos", "producto", "id_producto"},
            {"Clientes", "cliente", "id_cliente"},
            {"Ventas", "venta", "id_venta"},
            {"Detalle de ventas", "detalle_venta", "id_detalle"}
        };

        for (String[] t : tablas) {
            JButton btn = new JButton(t[0]);
            btn.addActionListener(e -> new VentanaTabla(t[0], t[1], t[2]).setVisible(true));
            panel.add(btn);
        }

        JButton btnCaja = new JButton("Caja (registrar venta)");
        btnCaja.addActionListener(e -> new VentanaVenta().setVisible(true));
        panel.add(btnCaja);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaInicio().setVisible(true));
    }
}

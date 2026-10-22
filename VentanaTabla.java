package vitalfit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class VentanaTabla extends JFrame {
    private String tabla, pk;
    private ArrayList<String> columnas = new ArrayList<>();
    private DefaultTableModel modelo = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private JTable jtable = new JTable(modelo);

    public VentanaTabla(String titulo, String tabla, String pk) {
        this.tabla = tabla;
        this.pk = pk;
        setTitle("Tabla: " + titulo);
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel botones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnRefrescar);

        add(new JScrollPane(jtable), BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregar());
        btnEditar.addActionListener(e -> editar());
        btnEliminar.addActionListener(e -> eliminar());
        btnRefrescar.addActionListener(e -> cargar());

        cargar();
    }

    private void cargar() {
        columnas.clear();
        modelo.setRowCount(0);
        try (Connection con = Conexion.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + tabla)) {

            ResultSetMetaData md = rs.getMetaData();
            int n = md.getColumnCount();
            for (int i = 1; i <= n; i++) {
                columnas.add(md.getColumnName(i));
            }
            modelo.setColumnIdentifiers(columnas.toArray());

            while (rs.next()) {
                Object[] fila = new Object[n];
                for (int i = 0; i < n; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregar() {
        ArrayList<String> cols = new ArrayList<>();
        ArrayList<Object> valores = new ArrayList<>();

        for (String c : columnas) {
            if (c.equals(pk)) continue; // el id es AUTO_INCREMENT
            String v = JOptionPane.showInputDialog(this, "Ingresa " + c + ":");
            if (v == null) return; // canceló
            cols.add(c);
            valores.add(v.trim().isEmpty() ? null : v.trim());
        }

        String sql = "INSERT INTO " + tabla + " (" + String.join(", ", cols) + ") VALUES ("
                + String.join(", ", Collections.nCopies(cols.size(), "?")) + ")";
        ejecutar(sql, valores);
    }

    private void editar() {
        int fila = jtable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<String> sets = new ArrayList<>();
        ArrayList<Object> valores = new ArrayList<>();

        for (int i = 0; i < columnas.size(); i++) {
            String c = columnas.get(i);
            if (c.equals(pk)) continue;
            Object actual = modelo.getValueAt(fila, i);
            String v = JOptionPane.showInputDialog(this, "Nuevo valor de " + c + ":",
                    actual == null ? "" : actual.toString());
            if (v == null) return;
            sets.add(c + " = ?");
            valores.add(v.trim().isEmpty() ? null : v.trim());
        }

        valores.add(modelo.getValueAt(fila, columnas.indexOf(pk)));
        String sql = "UPDATE " + tabla + " SET " + String.join(", ", sets) + " WHERE " + pk + " = ?";
        ejecutar(sql, valores);
    }

    private void eliminar() {
        int fila = jtable.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una fila primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int op = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este registro?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;

        ArrayList<Object> valores = new ArrayList<>();
        valores.add(modelo.getValueAt(fila, columnas.indexOf(pk)));
        ejecutar("DELETE FROM " + tabla + " WHERE " + pk + " = ?", valores);
    }

    private void ejecutar(String sql, ArrayList<Object> params) {
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.executeUpdate();
            cargar(); // refleja el cambio en la tabla
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo completar la operación:\n" + ex.getMessage(),
                    "Error de BD", JOptionPane.ERROR_MESSAGE);
        }
    }
}

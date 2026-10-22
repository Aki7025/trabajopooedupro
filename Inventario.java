package vitalfit;

import java.util.ArrayList;

public class Inventario {

    public static String revisarStock(ArrayList<Producto> lista) {
        StringBuilder sb = new StringBuilder();
        for (Producto p : lista) {
            if (p.getStock() == 0) {
                sb.append(p.getNombre()).append(": SIN STOCK\n");
            } else if (p.getStock() < 5) {
                sb.append(p.getNombre()).append(": stock crítico (").append(p.getStock()).append(")\n");
            } else {
                sb.append(p.getNombre()).append(": OK (").append(p.getStock()).append(")\n");
            }
        }
        return sb.toString();
    }

    public static double descuento(int idCategoria) {
    switch (idCategoria) {
        case 1:  return 0.10;
        case 3:  return 0.05;
        case 4:  return 0.15;
        default: return 0.0;
    }
    }

    public static int totalBoleta(ArrayList<Integer> subtotales) {
        int total = 0;
        int i = 0;
        while (i < subtotales.size()) {
            total += subtotales.get(i);
            i++;
        }
        return total;
    }
}

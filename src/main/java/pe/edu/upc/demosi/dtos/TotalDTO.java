package pe.edu.upc.demosi.dtos;

public class TotalDTO {
    private String nombreCategoria;
    private double quantityTotal;

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public double getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(double quantityTotal) {
        this.quantityTotal = quantityTotal;
    }
}
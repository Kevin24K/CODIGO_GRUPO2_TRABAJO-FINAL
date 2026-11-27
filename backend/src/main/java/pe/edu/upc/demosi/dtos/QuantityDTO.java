package pe.edu.upc.demosi.dtos;

public class QuantityDTO {
    private Long cantidad_Total;
    public QuantityDTO() {}

        public QuantityDTO(Long cantidad_Total) {
            this.cantidad_Total = cantidad_Total;
        }

        public Long getCantidad_Total() {
            return cantidad_Total;
        }

        public void setCantidad_Total(Long cantidad_Total) {
            this.cantidad_Total = cantidad_Total;
        }
}

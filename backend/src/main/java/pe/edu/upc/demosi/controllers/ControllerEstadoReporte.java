package pe.edu.upc.demosi.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.demosi.dtos.EstadoReporteDTOList;
import pe.edu.upc.demosi.dtos.EstadoReporteDTOinsert;
import pe.edu.upc.demosi.entities.EstadoReporte;
import pe.edu.upc.demosi.servicesinterfaces.IEstadoReporteService;

import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/api/EstadoReporte")
public class ControllerEstadoReporte {
    private final IEstadoReporteService eR;
    private final ModelMapper modelMapper;

    public ControllerEstadoReporte(IEstadoReporteService eR, ModelMapper modelMapper) {
        this.eR = eR;
        this.modelMapper = modelMapper;
    }


    @GetMapping
    public ResponseEntity<List<EstadoReporteDTOList>>listar() {

        List<EstadoReporteDTOList> lista = eR.list()
                .stream()
                .map(categoria -> modelMapper.map(categoria, EstadoReporteDTOList.class))
                .toList();

        return ResponseEntity.ok(lista);
    }


    @PostMapping
    public ResponseEntity<EstadoReporteDTOinsert> registrar(
            @Valid @RequestBody EstadoReporteDTOinsert dto) {
        EstadoReporte eRport = modelMapper.map(dto, EstadoReporte.class);

        eR.insert(eRport);

        EstadoReporteDTOinsert responseDTO =
                modelMapper.map(eRport, EstadoReporteDTOinsert.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(eRport.getIdEstadoReporte())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }
}

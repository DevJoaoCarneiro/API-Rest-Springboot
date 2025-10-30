package com.example.demo.controller;

import com.example.demo.dto.CarroDTO;
import com.example.demo.dto.CarroResponseDTO;
import com.example.demo.service.CarroService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Carro", description = "Endpoints para gerenciamento de carros")
@RestController
@RequestMapping("/api/carro")
@AllArgsConstructor
public class CarroController {

    private CarroService carroService;

    @PostMapping
    public ResponseEntity<CarroDTO> cadastraCarro(@RequestBody CarroDTO carroDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(carroService.cadastraNovoCarro(carroDto));
    }


    @GetMapping
    public ResponseEntity<List<CarroResponseDTO>> consultaTodosCarros(){
            return ResponseEntity.ok().body(carroService.consultarTodosOsCarros());

    }

    @GetMapping("/{id}")
    public ResponseEntity<CarroResponseDTO> consultaCarroPorId(@PathVariable Long id){
            return ResponseEntity.ok().body(carroService.consultaCarroPorId(id));

    }

    @GetMapping("/status")
    public ResponseEntity<List<CarroDTO>> listaTodosOsCarroDisponiveis(){
        return ResponseEntity.ok().body(carroService.filtraCarroPorReservas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletaCarroPorId(@PathVariable Long id){
        carroService.deletaCarroPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarroDTO> editaCarroPorId(@PathVariable Long id, @RequestBody CarroDTO carroDto){
            return ResponseEntity.ok().body(carroService.editaCarroPorId(id,carroDto));
    }
}

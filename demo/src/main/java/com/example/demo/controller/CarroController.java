package com.example.demo.controller;

import com.example.demo.dto.CarroDTO;
import com.example.demo.service.CarroService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CarroDTO>> consultaTodosCarros(){
        try {
            return ResponseEntity.ok().body(carroService.consultarTodosOsCarros());
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarroDTO> consultaCarroPorId(@PathVariable Long id){
        try {
            return ResponseEntity.ok().body(carroService.consultaCarroPorId(id));
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletaCarroPorId(@PathVariable Long id){
        carroService.deletaCarroPorId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarroDTO> editaCarroPorId(@PathVariable Long id, @RequestBody CarroDTO carroDto){
        try{
            return ResponseEntity.ok().body(carroService.editaCarroPorId(id,carroDto));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

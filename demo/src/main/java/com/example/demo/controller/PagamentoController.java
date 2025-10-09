package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entities.Pagamento;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("pagamento")
public class PagamentoController {

    @PostMapping
    public void cadastraPagamento(@RequestBody Pagamento pagamento){
        System.out.println(pagamento);
    }
}

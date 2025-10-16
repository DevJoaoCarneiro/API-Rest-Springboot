package com.example.demo.service.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.demo.Entities.Reserva;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservaService {

    private final Map<Long, Reserva> reservas = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public Reserva criarReserva(Reserva reserva) {
        long id = idCounter.incrementAndGet();
        reserva.setId(id);
        reservas.put(id, reserva);
        return reserva;
    }

    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas.values());
    }
 
    
    public Optional<Reserva> buscarPorId(Long id) {
        return Optional.ofNullable(reservas.get(id));
    }

    public boolean deletarReserva(Long id) {
        return reservas.remove(id) != null;
    }
}
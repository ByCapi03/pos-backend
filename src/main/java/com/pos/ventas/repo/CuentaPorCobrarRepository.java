package com.pos.ventas.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pos.ventas.domain.CuentaPorCobrar;

public interface CuentaPorCobrarRepository extends JpaRepository<CuentaPorCobrar, Integer> {

    List<CuentaPorCobrar> findByVenta_IdVenta(Integer idVenta);

    List<CuentaPorCobrar> findByVenta_IdCliente(Integer idCliente);
}

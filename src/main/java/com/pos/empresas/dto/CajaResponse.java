package com.pos.empresas.dto;

import java.time.LocalDate;

public record CajaResponse(
		Integer idCaja,
		Integer idSucursal,
		String nombre,
		String codigo,
		LocalDate fechaCreacion,
		Boolean activo) {
}

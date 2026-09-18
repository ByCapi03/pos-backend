package com.pos.empresas.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pos.common.access.EmpresaAccess;
import com.pos.common.security.SecurityUtils;
import com.pos.empresas.domain.ConfiguracionSistema;
import com.pos.empresas.domain.Empresa;
import com.pos.empresas.dto.ConfiguracionSistemaResponse;
import com.pos.empresas.dto.ConfiguracionSistemaUpdate;
import com.pos.empresas.repo.ConfiguracionSistemaRepository;

@Service
public class ConfiguracionSistemaService {

	private final EmpresaAccess empresaAccess;
	private final ConfiguracionSistemaRepository configuracionSistemaRepository;

	public ConfiguracionSistemaService(
			EmpresaAccess empresaAccess,
			ConfiguracionSistemaRepository configuracionSistemaRepository) {
		this.empresaAccess = empresaAccess;
		this.configuracionSistemaRepository = configuracionSistemaRepository;
	}

	@Transactional
	public ConfiguracionSistemaResponse obtener(Integer idEmpresa) {
		Empresa empresa = empresaAccess.requireEmpresa(SecurityUtils.currentUser(), idEmpresa);
		ConfiguracionSistema config = configuracionSistemaRepository.findByEmpresa_IdEmpresa(idEmpresa)
				.orElseGet(() -> crearValoresDefecto(empresa));
		return toResponse(config);
	}

	@Transactional
	public ConfiguracionSistemaResponse actualizar(Integer idEmpresa, ConfiguracionSistemaUpdate datos) {
		Empresa empresa = empresaAccess.requireEmpresa(SecurityUtils.currentUser(), idEmpresa);
		ConfiguracionSistema config = configuracionSistemaRepository.findByEmpresa_IdEmpresa(idEmpresa)
				.orElseGet(() -> crearValoresDefecto(empresa));

		config.setTema(datos.tema());
		config.setIdioma(datos.idioma());
		config.setZonaHoraria(datos.zonaHoraria());
		config.setMoneda(datos.moneda());
		config.setActivarNotificacionesPush(datos.activarNotificacionesPush());
		config.setActivarSonido(datos.activarSonido());
		config.setActivarVibracion(datos.activarVibracion());
		config.setConfirmarAntesDeEliminar(datos.confirmarAntesDeEliminar());
		config.setCerrarSesionPorInactividad(datos.cerrarSesionPorInactividad());
		config.setMinutosInactividad(datos.minutosInactividad());
		config.setImprimirAutomaticamente(datos.imprimirAutomaticamente());
		config.setNumeroCopias(datos.numeroCopias());
		config.setTamanoTicket(datos.tamanoTicket());
		return toResponse(configuracionSistemaRepository.save(config));
	}

	private ConfiguracionSistema crearValoresDefecto(Empresa empresa) {
		ConfiguracionSistema config = new ConfiguracionSistema();
		config.setEmpresa(empresa);
		config.setTema("claro");
		config.setIdioma("es");
		config.setZonaHoraria("America/La_Paz");
		config.setMoneda("BOB");
		config.setActivarNotificacionesPush(Boolean.TRUE);
		config.setActivarSonido(Boolean.TRUE);
		config.setActivarVibracion(Boolean.TRUE);
		config.setConfirmarAntesDeEliminar(Boolean.TRUE);
		config.setCerrarSesionPorInactividad(Boolean.FALSE);
		config.setMinutosInactividad(15);
		config.setImprimirAutomaticamente(Boolean.FALSE);
		config.setNumeroCopias(1);
		config.setTamanoTicket("80mm");
		return configuracionSistemaRepository.saveAndFlush(config);
	}

	private ConfiguracionSistemaResponse toResponse(ConfiguracionSistema config) {
		Integer idEmpresa = config.getEmpresa() != null ? config.getEmpresa().getIdEmpresa() : null;
		return new ConfiguracionSistemaResponse(
				config.getIdConfiguracion(),
				idEmpresa,
				config.getTema(),
				config.getIdioma(),
				config.getZonaHoraria(),
				config.getMoneda(),
				config.getActivarNotificacionesPush(),
				config.getActivarSonido(),
				config.getActivarVibracion(),
				config.getConfirmarAntesDeEliminar(),
				config.getCerrarSesionPorInactividad(),
				config.getMinutosInactividad(),
				config.getImprimirAutomaticamente(),
				config.getNumeroCopias(),
				config.getTamanoTicket());
	}
}

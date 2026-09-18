package com.pos.common.seed;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pos.empresas.domain.Plan;
import com.pos.empresas.domain.PlanModulo;
import com.pos.empresas.domain.TipoMovimientoCaja;
import com.pos.empresas.repo.PlanModuloRepository;
import com.pos.empresas.repo.PlanRepository;
import com.pos.empresas.repo.TipoMovimientoCajaRepository;
import com.pos.inventario.domain.TipoMovimiento;
import com.pos.inventario.repo.TipoMovimientoRepository;
import com.pos.usuarios.domain.Modulo;
import com.pos.usuarios.domain.Rol;
import com.pos.usuarios.repo.ModuloRepository;
import com.pos.usuarios.repo.RolRepository;
import com.pos.ventas.domain.MetodoPago;
import com.pos.ventas.domain.TipoVenta;
import com.pos.ventas.repo.MetodoPagoRepository;
import com.pos.ventas.repo.TipoVentaRepository;

@Component
public class CatalogSeedRunner implements ApplicationRunner {

    private final RolRepository rolRepository;
    private final ModuloRepository moduloRepository;
    private final TipoMovimientoCajaRepository tipoMovimientoCajaRepository;
    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final TipoVentaRepository tipoVentaRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final PlanRepository planRepository;
    private final PlanModuloRepository planModuloRepository;

    public CatalogSeedRunner(
            RolRepository rolRepository,
            ModuloRepository moduloRepository,
            TipoMovimientoCajaRepository tipoMovimientoCajaRepository,
            TipoMovimientoRepository tipoMovimientoRepository,
            TipoVentaRepository tipoVentaRepository,
            MetodoPagoRepository metodoPagoRepository,
            PlanRepository planRepository,
            PlanModuloRepository planModuloRepository) {
        this.rolRepository = rolRepository;
        this.moduloRepository = moduloRepository;
        this.tipoMovimientoCajaRepository = tipoMovimientoCajaRepository;
        this.tipoMovimientoRepository = tipoMovimientoRepository;
        this.tipoVentaRepository = tipoVentaRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.planRepository = planRepository;
        this.planModuloRepository = planModuloRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRoles();
        seedModulos();
        seedTiposMovimientoCaja();
        seedTiposMovimiento();
        seedTiposVenta();
        seedMetodosPago();
        seedPlanes();
    }

    private void seedRoles() {
        for (String nombre : List.of("ADMINISTRADOR", "CLIENTE", "EMPLEADO")) {
            if (rolRepository.findByNombreIgnoreCase(nombre).isEmpty()) {
                Rol rol = new Rol();
                rol.setNombre(nombre);
                rol.setTipo("SISTEMA");
                rol.setDescripcion(nombre);
                rol.setActivo(true);
                rolRepository.save(rol);
            }
        }
    }

    private void seedModulos() {
        Map<String, String> modulos = Map.of(
                "USUARIOS", "Gestion de Usuarios",
                "EMPRESAS", "Gestion de Empresas",
                "INVENTARIO", "Gestion de Productos e Inventario",
                "VENTAS", "Gestion de Ventas",
                "CLIENTES", "Gestion de Clientes",
                "CAJAS", "Gestion de Cajas",
                "REPORTES", "Gestion de Reportes",
                "PEDIDOS", "Gestion de Pedidos Mayoristas");
        modulos.forEach((codigo, nombre) -> {
            if (moduloRepository.findByCodigoIgnoreCase(codigo).isEmpty()) {
                Modulo modulo = new Modulo();
                modulo.setCodigo(codigo);
                modulo.setNombre(nombre);
                moduloRepository.save(modulo);
            }
        });
    }

    private void seedTiposMovimientoCaja() {
        seedTipoCaja("APERTURA");
        seedTipoCaja("INGRESO");
        seedTipoCaja("EGRESO");
        seedTipoCaja("CIERRE");
        seedTipoCaja("AJUSTE_POSITIVO");
        seedTipoCaja("AJUSTE_NEGATIVO");
    }

    private void seedTipoCaja(String nombre) {
        if (tipoMovimientoCajaRepository.findByNombreIgnoreCase(nombre).isEmpty()) {
            TipoMovimientoCaja tipo = new TipoMovimientoCaja();
            tipo.setNombre(nombre);
            tipo.setDescripcion(nombre);
            tipoMovimientoCajaRepository.save(tipo);
        }
    }

    private void seedTiposMovimiento() {
        seedTipoMovimiento("Entrada manual", "Incrementa stock por ingreso manual", "ENTRADA");
        seedTipoMovimiento("Salida manual", "Reduce stock por salida manual", "SALIDA");
        seedTipoMovimiento("Venta", "Reduce stock por venta", "SALIDA");
        seedTipoMovimiento("Ajuste positivo", "Corrige stock aumentando cantidad", "ENTRADA");
        seedTipoMovimiento("Ajuste negativo", "Corrige stock reduciendo cantidad", "SALIDA");
        seedTipoMovimiento("Merma", "Reduce stock por perdida o dano", "SALIDA");
    }

    private void seedTipoMovimiento(String nombre, String descripcion, String direccion) {
        if (tipoMovimientoRepository.findByNombreIgnoreCase(nombre).isEmpty()) {
            TipoMovimiento tipo = new TipoMovimiento();
            tipo.setNombre(nombre);
            tipo.setDescripcion(descripcion);
            tipo.setDireccion(direccion);
            tipoMovimientoRepository.save(tipo);
        }
    }

    private void seedTiposVenta() {
        seedTipoVenta("Contado", "Venta al contado");
        seedTipoVenta("Credito", "Venta al Credito");
    }

    private void seedTipoVenta(String nombre, String descripcion) {
        if (tipoVentaRepository.findByNombreIgnoreCase(nombre).isEmpty()) {
            TipoVenta tipo = new TipoVenta();
            tipo.setNombre(nombre);
            tipo.setDescripcion(descripcion);
            tipoVentaRepository.save(tipo);
        }
    }

    private void seedMetodosPago() {
        seedMetodoPago("EFECTIVO", "Pago en efectivo");
        seedMetodoPago("QR", "Pago por QR");
        seedMetodoPago("TARJETA", "Pago con tarjeta");
    }

    private void seedMetodoPago(String nombre, String descripcion) {
        if (metodoPagoRepository.findByNombreIgnoreCase(nombre).isEmpty()) {
            MetodoPago metodo = new MetodoPago();
            metodo.setNombre(nombre);
            metodo.setDescripcion(descripcion);
            metodoPagoRepository.save(metodo);
        }
    }

    private void seedPlanes() {
        seedPlan("Básico", "Plan ideal para emprendedores y pequeños negocios.", new BigDecimal("29.99"),
                List.of("USUARIOS", "INVENTARIO", "VENTAS", "CAJAS"));
        seedPlan("Pro", "Plan diseñado para medianas empresas con mayores necesidades.", new BigDecimal("59.99"),
                List.of("USUARIOS", "EMPRESAS", "INVENTARIO", "VENTAS", "CLIENTES", "CAJAS"));
        seedPlan("Premium", "Plan completo con todas las características del sistema.", new BigDecimal("99.99"),
                List.of("USUARIOS", "EMPRESAS", "INVENTARIO", "VENTAS", "CLIENTES", "CAJAS", "REPORTES", "PEDIDOS"));
    }

    private void seedPlan(String nombre, String descripcion, BigDecimal precio, List<String> modulos) {
        Plan plan = planRepository.findByNombreIgnoreCase(nombre).orElseGet(() -> {
            Plan nuevo = new Plan();
            nuevo.setNombre(nombre);
            nuevo.setDescripcion(descripcion);
            nuevo.setPrecio(precio);
            return planRepository.save(nuevo);
        });
        for (String codigo : modulos) {
            moduloRepository.findByCodigoIgnoreCase(codigo).ifPresent(modulo -> {
                boolean exists = planModuloRepository.findByPlan_IdPlanAndIdModulo(plan.getIdPlan(), modulo.getIdModulo()).isPresent();
                if (!exists) {
                    PlanModulo relacion = new PlanModulo();
                    relacion.setPlan(plan);
                    relacion.setIdModulo(modulo.getIdModulo());
                    planModuloRepository.save(relacion);
                }
            });
        }
    }
}

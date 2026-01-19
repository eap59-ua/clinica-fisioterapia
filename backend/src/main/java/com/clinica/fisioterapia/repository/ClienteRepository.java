package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM Cliente c WHERE " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(c.dni) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "c.telefono LIKE CONCAT('%', :termino, '%')")
    List<Cliente> buscarPorTermino(@Param("termino") String termino);
}
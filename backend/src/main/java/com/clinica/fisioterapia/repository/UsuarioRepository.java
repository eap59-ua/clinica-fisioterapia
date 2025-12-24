package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.RolUsuario;
import com.clinica.fisioterapia.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    List<Usuario> findByRol(RolUsuario rol);
    List<Usuario> findByActivoTrue();
}
package com.prizo.api.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findById(Long id);

    @Query(
            value = "select p.* from produtos p where p.nome LIKE %:nome%",
            countQuery = "SELECT count(p.id) from produtos p where p.nome LIKE %:nome%",
            nativeQuery = true
    )
    List<Produto> findByNomeLike(String nome, Pageable pageable);
}
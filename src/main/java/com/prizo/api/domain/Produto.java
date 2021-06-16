package com.prizo.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Schema(name = "Produtos", description = "Data object para um produto", oneOf = Produto.class)
@EntityListeners(AuditingEntityListener.class)
public class Produto {

    @Schema(description = "UUID para um Item", example = "1", hidden=true)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(updatable = false)
    protected Long id;

    @Schema(description = "Nome do Item", example = "Agulha Tulip", required = true)
    @Size(max = 255)
    protected String nome;

    @Schema(description = "Quantidade do Item.", example = "1", required = true)
    protected Long quantidade;

    @Schema(description = "Valor do Item", example = "123.00", required = true)
    protected Double valor;

    @CreatedDate
    @Column(updatable = false)
    @Schema(hidden=true)
    protected Instant createdAt = Instant.now();

    @LastModifiedDate
    @Column(updatable = false)
    @Schema(hidden=true)
    protected Instant updatedAt = Instant.now();

    @Schema(description = "Marca como deletado o item", example = "false", required = false)
    protected Boolean deleted = Boolean.FALSE;
}

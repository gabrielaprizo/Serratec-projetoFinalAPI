package com.prizo.api.web;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import com.prizo.api.domain.Produto;
import com.prizo.api.domain.ProdutoRepository;
import com.prizo.api.exception.ProdutoDeletadoException;
import com.prizo.api.exception.ProdutoNaoEncontradoException;

@RestController
@RequestMapping(value = "/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Produto", description = "API de Produtos")
public class ProdutoRestController {
    @Autowired
    private ProdutoRepository repository;

    @Operation(
        summary = "Adiciona Produto",
        description = "endpoint para criar um produto",
        tags = {"Produto"}
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "Produto criado"),
            @ApiResponse(responseCode = "400", description = "Payload inválido"),
            @ApiResponse(responseCode = "409", description = "Produto já existe")
        }
    )
    @PostMapping(name="salvarProduto", path="")
    @ResponseStatus(HttpStatus.CREATED)
    public Produto salvarProduto(
        @Parameter(description = "Produto", required = true) @NotNull @RequestBody Produto produto
    ) {
        Produto produtoSalvo = repository.save(produto);
        return produtoSalvo;
    }

    @Operation(
        summary = "Listar Produtos",
        description = "endpoint para listar todos os produtos",
        tags = {"Produto"}
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "206",
                description = "Lista todos os produtos",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Produto.class)))
            )
        }
    )
    @GetMapping(name="verProdutos", path="")
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    public Collection<Produto> verProdutos() {
        Collection<Produto> collection = repository.findAll();
        return collection;
    }

    @Operation(
        summary = "Encontra um produto pelo nome",
        description = "endpoint para retornar dados de produtos baseados em um nome",
        tags = {"Produto"}
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "206",
                description = "Dados dos produtos",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Produto.class)))
            )
        }
    )
    @GetMapping(name="obterPorNome", path = "/obterPorNome")
    @Parameter(name="q", description = "Nome do produto para pesquisar")
    @Parameter(name="pagina", description = "Número da página", required = false)
    @Parameter(name="por_pagina", description = "Quantidade de registros por página", required = false)
    @ResponseStatus(HttpStatus.PARTIAL_CONTENT)
    public Collection<Produto> obterPorNome(
        @RequestParam(value = "q", required = true) String q,
        @RequestParam(value = "pagina", defaultValue = "0", required = false) String pagina,
        @RequestParam(value = "por_pagina", defaultValue = "10", required = false) String por_pagina
    ) {
        Collection<Produto> produtos = repository.findByNomeLike(
            q,
            PageRequest.of(Integer.parseInt(pagina), Integer.parseInt(por_pagina))
        );

        if (produtos.size() == 0) {
            throw  new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return produtos;
    }

    @Operation(
        summary = "Encontra um produto pelo ID",
        description = "endpoint para retornar dados de um produto baseado em um ID",
        tags = {"Produto"}
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200", description = "Dados do produto",
                content = @Content(schema = @Schema(implementation = Produto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
        }
    )
    @GetMapping(name="obterPorId",path="/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Produto obterPorId(
        @Parameter(description = "Id do Produto", required = true)
        @PathVariable Long id
    ) {
        Produto produto = repository.findById(id)
            .orElseThrow(() -> new ProdutoNaoEncontradoException());

        if (produto.getDeleted()) {
            throw new ProdutoDeletadoException();
        }

        return produto;
    }

    @Operation(
        summary = "Atualiza um produto",
        description = "endpoint para atualizar um produto",
        tags = {"Produto"}
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "204", description = "Atualização bem sucedida"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
        }
    )
    @PutMapping(name="atualizarProduto", path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Produto atualizarProduto(
        @Parameter(description = "ID do produto.", required = true)
        @PathVariable Long id,
        @Parameter(description = "Produto para atualizar", required = true)
        @RequestBody Produto produto
    ) {
        Produto produtoAtualizado = repository.findById(id)
            .map((entity) -> {
                if (entity.getDeleted()) {
                    throw new ProdutoDeletadoException();
                }

                if (produto.getNome() != null) {
                    entity.setNome(produto.getNome());
                }

                if (produto.getQuantidade() != null) {
                    entity.setQuantidade(produto.getQuantidade());
                }

                if (produto.getValor() != null) {
                    entity.setValor(produto.getValor());
                }

                entity.setUpdatedAt(Instant.now());
                return repository.save(entity);
            })
            .orElseThrow(ProdutoNaoEncontradoException::new);

        return produtoAtualizado;
    }

    @Operation(
        summary = "Delete um produto",
        description = "endpoint para deletar um produto",
        tags = {"Produto"}
    )
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "Operação bem sucedida"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
        }
    )
    @DeleteMapping(name="deletarProdutoPorId", path="/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deletarProdutoPorId(
        @Parameter(description = "ID do produto para deletar.", required = true)
        @PathVariable Long id
    ) {
        Produto produtoDeletado = repository.findById(id)
            .orElseThrow(() -> new ProdutoNaoEncontradoException());

        produtoDeletado.setDeleted(Boolean.TRUE);
        repository.save(produtoDeletado);
        return "produto.deletado";
    }
}

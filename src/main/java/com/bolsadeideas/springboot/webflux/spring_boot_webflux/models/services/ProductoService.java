package com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.services;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.documents.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Flux<Producto> findAll();

    Flux<Producto> findAllConNombreUpperCase();

    Flux<Producto> findAllConNombreUpperCaseRepeat();

    Mono<Producto> findById(String id);

    Mono<Producto> save(Producto producto);

    Mono<Void> delete(Producto producto);

    Flux<Categoria> findAllCategoria();

    Mono<Categoria> findCategoriaById(String id);

    Mono<Categoria> saveCategoria(Categoria categoria);
}
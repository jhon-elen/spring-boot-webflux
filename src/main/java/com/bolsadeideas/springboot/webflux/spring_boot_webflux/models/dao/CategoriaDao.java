package com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.dao;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.documents.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String> {

}

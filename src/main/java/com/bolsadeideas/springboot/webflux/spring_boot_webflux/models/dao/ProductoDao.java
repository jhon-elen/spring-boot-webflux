package com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.dao;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

}

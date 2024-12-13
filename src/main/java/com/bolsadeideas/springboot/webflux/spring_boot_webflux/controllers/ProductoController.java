package com.bolsadeideas.springboot.webflux.spring_boot_webflux.controllers;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.spring_boot_webflux.models.services.ProductoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@SessionAttributes("producto")
@Controller
public class ProductoController {
    
    @Autowired
    private ProductoService service;
    
    @Value("${config.uploads.path}")
    private String path;
    
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);
    
    @ModelAttribute("categorias")
    public Flux<Categoria> categorias() {
        return service.findAllCategoria();
    }
    
    @GetMapping({"/listar", "/"})
    public Mono<String> listar(Model model) {
        Flux<Producto> productos = service.findAllConNombreUpperCase();
        productos.subscribe(producto -> log.info(producto.getNombre()));
        
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");
        return Mono.just("listar");
    }
    
    @GetMapping("/form")
    public Mono<String> crear(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Formulario de producto");
        model.addAttribute("boton", "Crear");
        return Mono.just("form");
    }
    
    @PostMapping("/form")
    public Mono<String> guardar(@Valid Producto producto, BindingResult result, Model model, @RequestPart FilePart file, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Errores en formulario producto");
            model.addAttribute("boton", "Guardar");
            return Mono.just("form");
        } else {
            status.setComplete();
            
            Mono<Categoria> categoria = service.findCategoriaById(producto.getCategoria().getId());
            
            return categoria.flatMap(c -> {
                    if (producto.getCreatedAt() == null) {
                        producto.setCreatedAt(new Date());
                    }
                    
                    if (!file.filename().isEmpty()) {
                        producto.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                            .replace(" ", "")
                            .replace(":", "")
                            .replace("\\", "")
                        );
                    }
                    producto.setCategoria(c);
                    return service.save(producto);
                })
                .doOnNext(p -> {
                    log.info("Categoría asignada: " + p.getCategoria() + " Id Cat: " + p.getCategoria().getId());
                    log.info("Producto guardado: " + p.getNombre() + " Id: " + p.getId());
                })
                .flatMap(p -> {
                    if (!file.filename().isEmpty()) {
                        return file.transferTo(
                            new File(path + p.getFoto()));
                    }
                    return Mono.empty();
                })
                .thenReturn("redirect:/listar?success=producto+guardado+con+éxito");
        }
    }
    
    @GetMapping("/form/{id}")
    public Mono<String> editar(@PathVariable String id, Model model) {
        Mono<Producto> productoMono = service.findById(id).doOnNext(p -> {
            log.info("Producto: " + p.getNombre());
        }).defaultIfEmpty(new Producto());
        model.addAttribute("titulo", "Editar producto");
        model.addAttribute("boton", "Editar");
        model.addAttribute("producto", productoMono);
        return Mono.just("form");
    }
    
    @GetMapping("/form-v2/{id}")
    public Mono<String> editarV2(@PathVariable String id, Model model) {
        return service.findById(id).doOnNext(p -> {
                log.info("Producto: " + p.getNombre());
                model.addAttribute("titulo", "Editar producto");
                model.addAttribute("boton", "Editar");
                model.addAttribute("producto", p);
            })
            .defaultIfEmpty(new Producto())
            .flatMap(p -> {
                if (p.getId() == null) {
                    return Mono.error(new InterruptedException("Producto no encontrado"));
                }
                return Mono.just(p);
            })
            .then(Mono.just("form"))
            .onErrorResume(ex -> Mono.just("redirect:/listar?error=no+existe+el+producto"));
    }
    
    @GetMapping("/eliminar/{id}")
    public Mono<String> eliminar(@PathVariable String id) {
        return service.findById(id)
            .defaultIfEmpty(new Producto())
            .flatMap(p -> {
                if (p.getId() == null) {
                    return Mono.error(new InterruptedException("No existe el producto a eliminar!"));
                }
                return Mono.just(p);
            })
            .flatMap(p -> {
                log.info("Eliminando producto: " + p.getNombre());
                log.info("Eliminando producto Id: " + p.getId());
                return service.delete(p);
            })
            .then(Mono.just("redirect:/listar?success=Producto+eliminado+con+éxito"))
            .onErrorResume(ex -> Mono.just("redirect:/listar?error=no+existe+el+producto+a+eliminar"));
    }
    
    @GetMapping("/listar-datadriver")
    public String listarDataDriver(Model model) {
        Flux<Producto> productos = service.findAllConNombreUpperCase().delayElements(Duration.ofSeconds(1));
        productos.subscribe(producto -> log.info(producto.getNombre()));
        
        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 1));
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
    
    @GetMapping("/listar-full")
    public String listarFull(Model model) {
        Flux<Producto> productos = service.findAllConNombreUpperCaseRepeat();
        
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");
        return "listar";
    }
    
    @GetMapping("/listar-chunked")
    public String listarChunked(Model model) {
        Flux<Producto> productos = service.findAllConNombreUpperCaseRepeat();
        
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Listado de productos");
        return "listar-chunked";
    }
}

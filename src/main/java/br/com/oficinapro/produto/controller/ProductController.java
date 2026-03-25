package br.com.oficinapro.produto.controller;

import br.com.oficinapro.produto.dto.request.ProductRequest;
import br.com.oficinapro.produto.dto.response.ProductResponse;
import br.com.oficinapro.produto.service.ActivateProductService;
import br.com.oficinapro.produto.service.CreateProductService;
import br.com.oficinapro.produto.service.DeleteProductService;
import br.com.oficinapro.produto.service.FindAllProductService;
import br.com.oficinapro.produto.service.FindByProductCodeService;
import br.com.oficinapro.produto.service.UpdateProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final CreateProductService createProductService;
    private final UpdateProductService updateProductService;
    private final FindAllProductService findAllProductService;
    private final FindByProductCodeService findByProductCodeService;
    private final DeleteProductService deleteProductService;
    private final ActivateProductService activateProductService;

    public ProductController(CreateProductService createProductService,
                             UpdateProductService updateProductService,
                             FindAllProductService findAllProductService,
                             FindByProductCodeService findByProductCodeService,
                             DeleteProductService deleteProductService,
                             ActivateProductService activateProductService) {
        this.createProductService = createProductService;
        this.updateProductService = updateProductService;
        this.findAllProductService = findAllProductService;
        this.findByProductCodeService = findByProductCodeService;
        this.deleteProductService = deleteProductService;
        this.activateProductService = activateProductService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse response = createProductService.create(productRequest);
        URI location = URI.create("/api/v1/products/" + response.code());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}")
    public ResponseEntity<ProductResponse> update(@PathVariable String code, @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse response = updateProductService.update(code, productRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> findAll(
            @RequestParam(name = "enabled", defaultValue = "true") boolean enabled,
            Pageable pageable) {
        Page<ProductResponse> response = findAllProductService.findAll(enabled, pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<ProductResponse> findByCode(@PathVariable String code) {
        ProductResponse response = findByProductCodeService.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        deleteProductService.delete(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}/active")
    public ResponseEntity<Void> activate(@PathVariable String code) {
        activateProductService.activate(code);
        return ResponseEntity.noContent().build();
    }
}

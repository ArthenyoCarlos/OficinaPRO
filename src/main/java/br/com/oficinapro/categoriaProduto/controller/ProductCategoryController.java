package br.com.oficinapro.categoriaProduto.controller;

import br.com.oficinapro.categoriaProduto.dto.request.ProductCategoryRequest;
import br.com.oficinapro.categoriaProduto.dto.response.ProductCategoryResponse;
import br.com.oficinapro.categoriaProduto.service.ActivateProductCategoryService;
import br.com.oficinapro.categoriaProduto.service.CreateProductCategoryService;
import br.com.oficinapro.categoriaProduto.service.DeleteProductCategoryService;
import br.com.oficinapro.categoriaProduto.service.FindAllProductCategoryService;
import br.com.oficinapro.categoriaProduto.service.FindByProductCategoryCodeService;
import br.com.oficinapro.categoriaProduto.service.UpdateProductCategoryService;
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
@RequestMapping("/api/v1/product-categories")
public class ProductCategoryController {

    private final CreateProductCategoryService createProductCategoryService;
    private final UpdateProductCategoryService updateProductCategoryService;
    private final FindAllProductCategoryService findAllProductCategoryService;
    private final FindByProductCategoryCodeService findByProductCategoryCodeService;
    private final DeleteProductCategoryService deleteProductCategoryService;
    private final ActivateProductCategoryService activateProductCategoryService;

    public ProductCategoryController(CreateProductCategoryService createProductCategoryService,
                                     UpdateProductCategoryService updateProductCategoryService,
                                     FindAllProductCategoryService findAllProductCategoryService,
                                     FindByProductCategoryCodeService findByProductCategoryCodeService,
                                     DeleteProductCategoryService deleteProductCategoryService,
                                     ActivateProductCategoryService activateProductCategoryService) {
        this.createProductCategoryService = createProductCategoryService;
        this.updateProductCategoryService = updateProductCategoryService;
        this.findAllProductCategoryService = findAllProductCategoryService;
        this.findByProductCategoryCodeService = findByProductCategoryCodeService;
        this.deleteProductCategoryService = deleteProductCategoryService;
        this.activateProductCategoryService = activateProductCategoryService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ProductCategoryResponse> create(@Valid @RequestBody ProductCategoryRequest productCategoryRequest) {
        ProductCategoryResponse response = createProductCategoryService.create(productCategoryRequest);
        URI location = URI.create("/api/v1/product-categories/" + response.code());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{code}")
    public ResponseEntity<ProductCategoryResponse> update(@PathVariable String code,
                                                          @Valid @RequestBody ProductCategoryRequest productCategoryRequest) {
        ProductCategoryResponse response = updateProductCategoryService.update(code, productCategoryRequest);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ProductCategoryResponse>> findAll(
            @RequestParam(name = "enabled", defaultValue = "true") boolean enabled,
            Pageable pageable) {
        Page<ProductCategoryResponse> response = findAllProductCategoryService.findAll(enabled, pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{code}")
    public ResponseEntity<ProductCategoryResponse> findByCode(@PathVariable String code) {
        ProductCategoryResponse response = findByProductCategoryCodeService.findByCode(code);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        deleteProductCategoryService.delete(code);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{code}/active")
    public ResponseEntity<Void> activate(@PathVariable String code) {
        activateProductCategoryService.activate(code);
        return ResponseEntity.noContent().build();
    }
}

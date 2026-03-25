package br.com.oficinapro.produto.service;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import br.com.oficinapro.categoriaProduto.repository.ProductCategoryRepository;
import br.com.oficinapro.common.exception.ResourceConflictException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.fornecedor.domain.Supplier;
import br.com.oficinapro.fornecedor.repository.SupplierRepository;
import br.com.oficinapro.produto.domain.Product;
import br.com.oficinapro.produto.dto.request.ProductRequest;
import br.com.oficinapro.produto.dto.response.ProductResponse;
import br.com.oficinapro.produto.mapper.ProductMapper;
import br.com.oficinapro.produto.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCategoryRepository productCategoryRepository;
    private final SupplierRepository supplierRepository;

    public CreateProductService(ProductRepository productRepository,
                                ProductMapper productMapper,
                                ProductCategoryRepository productCategoryRepository,
                                SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productCategoryRepository = productCategoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        validateSkuUniqueness(productRequest.sku());

        Product product = productMapper.toEntity(productRequest);
        assignCategory(product, productRequest.categoryCode());
        assignSupplier(product, productRequest.supplierCode());
        normalizeFields(product);
        product.setEnabled(true);

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    private void validateSkuUniqueness(String sku) {
        if (productRepository.findBySku(sku.trim().toUpperCase()) != null) {
            throw new ResourceConflictException("SKU already exists");
        }
    }

    private void assignCategory(Product product, String categoryCode) {
        ProductCategory category = productCategoryRepository.findByCode(categoryCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with code: " + categoryCode));
        product.setCategory(category);
    }

    private void assignSupplier(Product product, String supplierCode) {
        if (supplierCode == null || supplierCode.trim().isEmpty()) {
            product.setSupplier(null);
            return;
        }

        Supplier supplier = supplierRepository.findByCode(supplierCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with code: " + supplierCode));
        product.setSupplier(supplier);
    }

    private void normalizeFields(Product product) {
        product.setName(product.getName().trim());
        product.setSku(product.getSku().trim().toUpperCase());
        product.setUnit(product.getUnit().trim().toUpperCase());
    }
}

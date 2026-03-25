package br.com.oficinapro.categoriaProduto.service;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import br.com.oficinapro.categoriaProduto.dto.request.ProductCategoryRequest;
import br.com.oficinapro.categoriaProduto.dto.response.ProductCategoryResponse;
import br.com.oficinapro.categoriaProduto.mapper.ProductCategoryMapper;
import br.com.oficinapro.categoriaProduto.repository.ProductCategoryRepository;
import br.com.oficinapro.common.exception.ResourceConflictException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    public UpdateProductCategoryService(ProductCategoryRepository productCategoryRepository,
                                        ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Transactional
    public ProductCategoryResponse update(String code, ProductCategoryRequest productCategoryRequest) {
        ProductCategory productCategory = productCategoryRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with code: " + code));

        updateName(productCategory, productCategoryRequest.name());
        productCategoryMapper.updateEntity(productCategoryRequest, productCategory);

        ProductCategory updatedProductCategory = productCategoryRepository.save(productCategory);
        return productCategoryMapper.toResponse(updatedProductCategory);
    }

    private void updateName(ProductCategory productCategory, String name) {
        if (name == null || name.equals(productCategory.getName())) {
            return;
        }

        ProductCategory existingProductCategory = productCategoryRepository.findByName(name);
        if (existingProductCategory != null && !existingProductCategory.getId().equals(productCategory.getId())) {
            throw new ResourceConflictException("Product category name already exists");
        }

        productCategory.setName(name);
    }
}

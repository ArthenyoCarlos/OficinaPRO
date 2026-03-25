package br.com.oficinapro.categoriaProduto.service;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import br.com.oficinapro.categoriaProduto.dto.request.ProductCategoryRequest;
import br.com.oficinapro.categoriaProduto.dto.response.ProductCategoryResponse;
import br.com.oficinapro.categoriaProduto.mapper.ProductCategoryMapper;
import br.com.oficinapro.categoriaProduto.repository.ProductCategoryRepository;
import br.com.oficinapro.common.exception.ResourceConflictException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    public CreateProductCategoryService(ProductCategoryRepository productCategoryRepository,
                                        ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Transactional
    public ProductCategoryResponse create(ProductCategoryRequest productCategoryRequest) {
        validateNameUniqueness(productCategoryRequest.name());

        ProductCategory productCategory = productCategoryMapper.toEntity(productCategoryRequest);
        productCategory.setEnabled(true);

        ProductCategory savedProductCategory = productCategoryRepository.save(productCategory);
        return productCategoryMapper.toResponse(savedProductCategory);
    }

    private void validateNameUniqueness(String name) {
        if (productCategoryRepository.findByName(name) != null) {
            throw new ResourceConflictException("Product category name already exists");
        }
    }
}

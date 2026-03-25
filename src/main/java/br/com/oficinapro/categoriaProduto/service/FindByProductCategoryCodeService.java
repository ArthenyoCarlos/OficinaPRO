package br.com.oficinapro.categoriaProduto.service;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import br.com.oficinapro.categoriaProduto.dto.response.ProductCategoryResponse;
import br.com.oficinapro.categoriaProduto.repository.ProductCategoryRepository;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindByProductCategoryCodeService {

    private final ProductCategoryRepository productCategoryRepository;

    public FindByProductCategoryCodeService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Transactional(readOnly = true)
    public ProductCategoryResponse findByCode(String code) {
        ProductCategory productCategory = productCategoryRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with code: " + code));

        return new ProductCategoryResponse(productCategory);
    }
}

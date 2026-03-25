package br.com.oficinapro.categoriaProduto.service;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import br.com.oficinapro.categoriaProduto.dto.response.ProductCategoryResponse;
import br.com.oficinapro.categoriaProduto.repository.ProductCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public FindAllProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductCategoryResponse> findAll(boolean enabled, Pageable pageable) {
        Page<ProductCategory> categories = productCategoryRepository.findAll(enabled, pageable);
        return categories.map(ProductCategoryResponse::new);
    }
}

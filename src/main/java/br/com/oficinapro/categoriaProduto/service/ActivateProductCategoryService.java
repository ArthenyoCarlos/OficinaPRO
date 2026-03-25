package br.com.oficinapro.categoriaProduto.service;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import br.com.oficinapro.categoriaProduto.repository.ProductCategoryRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ActivateProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public ActivateProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public void activate(String code) {
        ProductCategory productCategory = productCategoryRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with code: " + code));

        if (productCategory.isEnabled()) {
            throw new BusinessException("Product category already enabled");
        }

        productCategory.setEnabled(true);
        productCategoryRepository.save(productCategory);
    }
}

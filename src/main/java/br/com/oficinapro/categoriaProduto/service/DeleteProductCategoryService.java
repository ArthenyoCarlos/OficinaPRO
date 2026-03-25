package br.com.oficinapro.categoriaProduto.service;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import br.com.oficinapro.categoriaProduto.repository.ProductCategoryRepository;
import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public DeleteProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(String code) {
        ProductCategory productCategory = productCategoryRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found"));

        if (!productCategory.isEnabled()) {
            throw new BusinessException("Product category already disabled");
        }

        productCategory.setEnabled(false);
        productCategoryRepository.save(productCategory);
    }
}

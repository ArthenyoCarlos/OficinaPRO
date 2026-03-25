package br.com.oficinapro.produto.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.produto.domain.Product;
import br.com.oficinapro.produto.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivateProductService {

    private final ProductRepository productRepository;

    public ActivateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void activate(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with code: " + code));

        if (product.isEnabled()) {
            throw new BusinessException("Product already enabled");
        }

        product.setEnabled(true);
        productRepository.save(product);
    }
}

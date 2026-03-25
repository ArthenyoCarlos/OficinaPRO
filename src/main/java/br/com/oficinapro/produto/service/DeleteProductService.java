package br.com.oficinapro.produto.service;

import br.com.oficinapro.common.exception.BusinessException;
import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.produto.domain.Product;
import br.com.oficinapro.produto.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProductService {

    private final ProductRepository productRepository;

    public DeleteProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (!product.isEnabled()) {
            throw new BusinessException("Product already disabled");
        }

        product.setEnabled(false);
        productRepository.save(product);
    }
}

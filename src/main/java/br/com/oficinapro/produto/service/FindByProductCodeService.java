package br.com.oficinapro.produto.service;

import br.com.oficinapro.common.exception.ResourceNotFoundException;
import br.com.oficinapro.produto.domain.Product;
import br.com.oficinapro.produto.dto.response.ProductResponse;
import br.com.oficinapro.produto.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindByProductCodeService {

    private final ProductRepository productRepository;

    public FindByProductCodeService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductResponse findByCode(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with code: " + code));

        return new ProductResponse(product);
    }
}

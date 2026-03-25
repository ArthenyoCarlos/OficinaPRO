package br.com.oficinapro.produto.service;

import br.com.oficinapro.produto.domain.Product;
import br.com.oficinapro.produto.dto.response.ProductResponse;
import br.com.oficinapro.produto.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindAllProductService {

    private final ProductRepository productRepository;

    public FindAllProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(boolean enabled, Pageable pageable) {
        Page<Product> products = productRepository.findAll(enabled, pageable);
        return products.map(ProductResponse::new);
    }
}

package ktichenpos.menu.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ktichenpos.menu.menu.domain.Product;
import ktichenpos.menu.menu.domain.ProductRepository;
import ktichenpos.menu.menu.ui.request.ProductRequest;
import ktichenpos.menu.menu.ui.response.ProductResponse;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product entity = request.toEntity();
        Product product = productRepository.save(entity);
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.listFrom(productRepository.findAll());
    }
}

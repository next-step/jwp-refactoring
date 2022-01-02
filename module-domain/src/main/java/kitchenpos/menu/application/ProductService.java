package kitchenpos.menu.application;

import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.*;

import kitchenpos.common.*;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.*;
import kitchenpos.menu.repository.*;

@Service
public class ProductService {
    private static final String PRODUCT = "상품";

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException(PRODUCT)
            );
    }

    public List<Product> findByIdIn(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

    public ProductResponse save(ProductRequest request) {
        Product product = productRepository.save(
            Product.of(request.getName(), request.getPrice())
        );
        return ProductResponse.from(product);
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
            .stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
    }
}

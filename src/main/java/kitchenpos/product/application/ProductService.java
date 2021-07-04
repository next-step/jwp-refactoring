package kitchenpos.product.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest product) {
        Product createdProduct = Product.create(product);
        productRepository.save(createdProduct);
        return ProductResponse.of(createdProduct);
    }

    public List<ProductResponse> list() {
        List<Product> list = productRepository.findAll();
        return list.stream()
            .map(ProductResponse::of)
            .collect(Collectors.toList());
    }
}

package kitchenpos.menu.aplication;

import kitchenpos.menu.domain.product.Product;
import kitchenpos.menu.domain.product.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다"));
    }

    public ProductResponse saveProduct(final ProductRequest product) {
        return ProductResponse.of(productRepository.save(product.toProduct()));
    }

    public List<ProductResponse> findAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }
}

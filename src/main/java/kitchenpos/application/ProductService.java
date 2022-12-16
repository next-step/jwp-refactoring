package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(isolation = READ_COMMITTED)
    public Product create(final Product request) {
        return productRepository.save(Product.of(request.getName(), request.getPrice()));
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }

    public void existProducts(List<Product> productList) {
        if (!productRepository.countAllByIds(productList.stream()
                        .map(Product::getId).collect(Collectors.toList()))
                .equals(productList.size())) {
            throw new IllegalArgumentException("존재하지 않는 상품이 포함되어 있습니다");
        }
    }
}

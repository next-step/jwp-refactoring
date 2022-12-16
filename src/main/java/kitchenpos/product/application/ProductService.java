package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product request) {
        return productRepository.save(Product.of(request.getName(), request.getPrice()));
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productRepository.findAll();
    }

    public void existProducts(List<Product> productList) {
        if (!equalsRealProductCount(productList)) {
            throw new IllegalArgumentException("존재하지 않는 상품이 포함되어 있습니다");
        }
    }

    private boolean equalsRealProductCount(List<Product> productList) {
        return productRepository.countAllByIds(productList.stream()
                        .map(Product::getId).collect(Collectors.toList()))
                .equals(productList.size());
    }
}

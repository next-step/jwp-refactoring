package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.application.exception.NotExistProductsException;
import kitchenpos.product.presentation.dto.ProductRequest;
import kitchenpos.product.presentation.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = Product.of(productRequest.getName(), productRequest.getPrice());
        return ProductResponse.of(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return ProductResponse.ofList(productRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<Product> findByIdIn(List<Long> productIds) {
        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.isEmpty()) {
            throw new NotExistProductsException();
        }

        return products;
    }
}

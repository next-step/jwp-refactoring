package kitchenpos.application;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductRequest productRequest) {
        return new ProductResponse(create(productRequest.toProduct()));
    }

    @Transactional
    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<ProductResponse> list() {
        return ProductResponse.of(productRepository.findAll());
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAllById(List<Long> productsId) {
        List<Product> products = productRepository.findAllById(productsId);
        validateIfExists(products, productsId);
        return products;
    }

    private void validateIfExists(List<Product> products, List<Long> productsId) {
        if (products.size() != new HashSet<>(productsId).size()) {
            throw new EntityNotFoundException();
        }
    }
}

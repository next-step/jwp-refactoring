package kitchenpos.application;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.exception.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final Product product) {
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(EntityNotFoundException::new);
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

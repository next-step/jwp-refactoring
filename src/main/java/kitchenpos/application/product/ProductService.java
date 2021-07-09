package kitchenpos.application.product;

import kitchenpos.domain.menu.Quantity;
import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.exception.InvalidEntityException;
import kitchenpos.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        Product product = productRequest.toEntity();
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new InvalidEntityException("Not found Product Id" + id));
    }

    public Price getProductPrice(Long productId, Quantity quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new InvalidEntityException(productId));
        return product.multiply(quantity.getBigDecimalValue());
    }

}

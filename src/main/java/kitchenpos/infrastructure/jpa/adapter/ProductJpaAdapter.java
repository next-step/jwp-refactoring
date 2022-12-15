package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.Product;
import kitchenpos.infrastructure.jpa.repository.ProductJpaRepository;
import kitchenpos.port.ProductPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductJpaAdapter implements ProductPort {

    private final ProductJpaRepository productJpaRepository;

    public ProductJpaAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product save(Product entity) {
        return null;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return null;
    }

    @Override
    public List<Product> findAllByIdIn(List<Long> productId) {
        return productJpaRepository.findAllByIdIn(productId);
    }
}

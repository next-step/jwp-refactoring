package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.Product;
import kitchenpos.infrastructure.jpa.repository.ProductJpaRepository;
import kitchenpos.port.ProductPort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static kitchenpos.constants.ErrorCodeType.NOT_FOUND_PRODUCT;

@Service
@Transactional
public class ProductJpaAdapter implements ProductPort {

    private final ProductJpaRepository productJpaRepository;

    public ProductJpaAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Product save(Product entity) {
        return productJpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productJpaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllByIdIn(List<Long> productId) {
        return productJpaRepository.findAllByIdIn(productId);
    }
}

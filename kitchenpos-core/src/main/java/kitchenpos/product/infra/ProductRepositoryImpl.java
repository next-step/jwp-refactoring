package kitchenpos.product.infra;

import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository jpaProductRepository;

    public ProductRepositoryImpl(JpaProductRepository jpaProductRepository) {
        this.jpaProductRepository = jpaProductRepository;
    }

    @Override
    public List<Product> findAllByIds(List<Long> productIds) {
        return jpaProductRepository.findAllByIds(productIds);
    }

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return jpaProductRepository.findAll();
    }
}

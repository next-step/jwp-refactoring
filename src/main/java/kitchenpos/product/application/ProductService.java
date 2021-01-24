package kitchenpos.product.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
    public Product create(final Product product) {
        return productRepository.save(product);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public void checkExistsProducts(List<Product> products) {
        List<Product> persistProducts = list();
        products.forEach(product -> {
            if (!persistProducts.contains(product)) {
                throw new NotFoundEntityException("해당 Product를 찾을 수가 없습니다.");
            }
        });
    }
}

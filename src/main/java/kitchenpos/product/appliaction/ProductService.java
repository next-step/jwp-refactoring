package kitchenpos.product.appliaction;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        final Name name = Name.of(productRequest.getName());
        final Price price = Price.of(productRequest.getPrice());

        return productRepository.save(Product.of(name, price));
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public List<Product> findByIdIn(List<Long> productsIds) {
        return productRepository.findByIdIn(productsIds);
    }
}

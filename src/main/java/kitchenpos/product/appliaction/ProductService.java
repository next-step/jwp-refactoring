package kitchenpos.product.appliaction;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        final Name name = Name.of(productRequest.getName());
        final Price price = Price.of(productRequest.getPrice());

        Product product = productRepository.save(Product.of(name, price));

        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(ProductResponse::of).collect(Collectors.toList());
    }

    public List<Product> findByIdIn(List<Long> productsIds) {
        return productRepository.findByIdIn(productsIds);
    }
}

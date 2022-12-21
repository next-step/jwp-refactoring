package kitchenpos.product.application;

import kitchenpos.product.domain.Price;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.port.ProductPort;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductPort productPort;

    public ProductService(final ProductPort productPort) {
        this.productPort = productPort;
    }

    public ProductResponse create(final ProductRequest request) {
        Product product = new Product(new Price(request.getPrice()), request.getName());
        productPort.save(product);

        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productPort.findAll();
    }
}

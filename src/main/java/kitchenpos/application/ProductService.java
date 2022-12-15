package kitchenpos.application;

import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.port.ProductPort;
import kitchenpos.domain.Product;
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
        Product price = Product.of(request.getPrice(), request.getName());
        return ProductResponse.of(price);
    }

    @Transactional(readOnly = true)
    public List<Product> list() {
        return productPort.findAll();
    }
}

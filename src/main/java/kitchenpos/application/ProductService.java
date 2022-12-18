package kitchenpos.application;

import kitchenpos.domain.Price;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.port.ProductPort;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Product> all = productPort.findAll();
        return productPort.findAll();
    }
}

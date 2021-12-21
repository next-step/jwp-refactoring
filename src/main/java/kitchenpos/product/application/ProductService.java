package kitchenpos.product.application;

import java.util.List;
import kitchenpos.exception.CannotCreateException;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product;
        try {
            product = productRequest.toEntity();
        } catch (InvalidArgumentException e) {
            throw new CannotCreateException("상품을 생성할 수 없습니다. 다시 입력해 주세요.");
        }
        Product persist = productRepository.save(product);
        return ProductResponse.of(persist);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.ofList(products);
    }
}

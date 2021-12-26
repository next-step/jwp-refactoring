package kitchenpos.application;

import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse create(final ProductCreateRequest request) {
        final Product product = new Product(request.getName(), request.getPrice());

        final Product savedProduct = productRepository.save(product);

        return ProductMapper.toProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<Product> findProducts(List<Long> id) {
        return productRepository.findByIdIn(id);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        final List<Product> products = productRepository.findAll();

        return ProductMapper.toProductResponses(products);
    }
}

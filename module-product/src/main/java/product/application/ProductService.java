package product.application;

import product.repository.ProductRepository;
import product.domain.Product;
import product.dto.ProductRequest;
import product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest request) {
        Product product = productRepository.save(request.toProduct());
        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        return ProductResponse.of(productRepository.findAll());
    }
}

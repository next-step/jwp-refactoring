package api.product.application;

import api.product.domain.Product;
import api.product.domain.ProductRepository;
import api.product.dto.ProductRequest;
import api.product.dto.ProductResponse;
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
    public ProductResponse create(final ProductRequest request) {
        Product product = productRepository.save(request.toEntity());
        return ProductResponse.of(product);
    }

    public List<ProductResponse> list() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.ofList(products);
    }
}

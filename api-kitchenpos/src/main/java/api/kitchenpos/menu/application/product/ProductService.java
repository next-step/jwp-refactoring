package api.kitchenpos.menu.application.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.kitchenpos.menu.dto.product.ProductRequest;
import api.kitchenpos.menu.dto.product.ProductResponse;
import domain.kitchenpos.menu.product.Product;
import domain.kitchenpos.menu.product.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productRequest.toProduct();
        return ProductResponse.of(productRepository.save(product));
    }

    public List<ProductResponse> list() {
        return ProductResponse.ofList(productRepository.findAll());
    }

}

package kitchenpos.product.application;

import java.util.List;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.stream.Collectors.toList;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(String name, Long price) {
        return ProductDto.of(productRepository.save(new Product(name, price)));
    }

    public List<ProductDto> list() {
        return productRepository.findAll()
                                .stream()
                                .map(ProductDto::of)
                                .collect(toList());
    }
}

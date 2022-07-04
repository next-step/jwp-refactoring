package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.dto.ProductRequestDto;
import kitchenpos.product.dto.ProductResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDto create(final ProductRequestDto request) {
        Product product = productRepository.save(new Product(request.getName(), request.getPrice()));
        return new ProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> list() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
}

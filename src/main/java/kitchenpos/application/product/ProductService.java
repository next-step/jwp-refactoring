package kitchenpos.application.product;

import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.product.ProductDto;
import kitchenpos.exception.product.NotFoundProductException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductDto create(final ProductDto productDto) {
        Product product = Product.of(productDto.getName(), Price.of(productDto.getPrice()));

        return ProductDto.of(productRepository.save(product));
    }

    public List<ProductDto> list() {
        return productRepository.findAll().stream()
                                .map(ProductDto::of)
                                .collect(Collectors.toList());
    }

    public Product findById(Long productId) {
        return productRepository.findById(productId).orElseThrow(NotFoundProductException::new);
    }

    public List<Product> findAllByIds(List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
}

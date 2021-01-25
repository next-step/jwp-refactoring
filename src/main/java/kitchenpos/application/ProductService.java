package kitchenpos.application;

import kitchenpos.advice.exception.ProductException;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product create(final ProductRequest productRequest) {
        final BigDecimal price = productRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ProductException("상품 가격이 없거나 0보다 작습니다", price);
        }

        return productRepository.save(productRequest.toProduct());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductException("존재하지 않는 상품 id입니다", id));
    }
}

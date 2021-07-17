package kitchenpos.product.application;

import kitchenpos.advice.exception.ProductException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.domain.ProductRepository;
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
    public Product create(final ProductRequest productRequest) {
        return productRepository.save(productRequest.toProduct());
    }

    public List<Product> list() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductException("존재하지 않는 상품 id입니다", id));
    }

    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids);
    }


}

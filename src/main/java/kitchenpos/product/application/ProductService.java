package kitchenpos.product.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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
    public ProductResponse create(final ProductRequest productRequest) {
        Product persistProduct = productRepository.save(productRequest.toProduct());
        return ProductResponse.of(persistProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Product> findProductsByIds(List<Long> productIds) {
        List<Product> persistProducts = productRepository.findAllById(productIds);

        checkExistsProducts(productIds, persistProducts);

        return persistProducts;
    }

    private void checkExistsProducts(List<Long> productIds, List<Product> persistProducts) {
        if (productIds.size() != persistProducts.size()) {
            throw new NotFoundEntityException("등록되지 않은 상품이 있습니다.");
        }
    }
}

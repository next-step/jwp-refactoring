package kitchenpos.product;

import kitchenpos.exception.NotFoundEntityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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

    public List<Product> findProductsByIds(final List<Long> productIds) {
        List<Product> persistProducts = productRepository.findAllById(productIds);

        checkExistsProducts(productIds, persistProducts);

        return persistProducts;
    }

    private void checkExistsProducts(final List<Long> productIds, final List<Product> persistProducts) {
        if (productIds.size() != persistProducts.size()) {
            throw new NotFoundEntityException("등록되지 않은 상품이 있습니다.");
        }
    }
}

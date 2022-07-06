package kitchenpos.product.application;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dao.ProductRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product product = productRepository.save(productRequest.toProduct());
        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> list() {
        return productRepository.findAll().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public void validPriceCheck(final MenuRequest menuRequest) {
        BigDecimal sum = BigDecimal.ZERO;

        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            sum = sum.add(product.multiplyPrice(menuProductRequest.getQuantity()));
        }

        if (sum.compareTo(menuRequest.getPrice()) < 0) {
            throw new IllegalArgumentException("메뉴의 금액은 상품의 합 보다 작아야합니다.");
        }
    }
}

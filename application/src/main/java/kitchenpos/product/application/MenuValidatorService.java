package kitchenpos.product.application;

import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuValidatorService implements MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidatorService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void validateHasProducts(List<Long> productIds) {
        for (Long productId : productIds) {
            productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));
        }
    }

    @Override
    public void validateMenuPrice(long menuPrice, List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        long totalPrice = getTotalPrice(products);
        if (menuPrice > totalPrice) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 총 합보다 클 수 없습니다");
        }
    }

    private long getTotalPrice(List<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce(Long::sum)
                .orElse(0L);
    }
}

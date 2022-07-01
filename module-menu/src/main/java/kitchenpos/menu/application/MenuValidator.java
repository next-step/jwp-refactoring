package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateProducts(List<MenuProductRequest> menuProducts) {
        List<Long> productIds = createProductIds(menuProducts);
        int productsSize = findProductsByIdIn(productIds);
        if (isSameSize(productIds.size(), productsSize)) {
            throw new IllegalArgumentException("잘못된 상품 정보 입니다.");
        }
    }

    public void validateMenuProductsAmount(Menu menu) {
        BigDecimal sum = calculateProductsSum(menu.getMenuProducts());

        if (menu.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 가격의 합보다 작거나 같아야 합니다.");
        }
    }

    private int findProductsByIdIn(List<Long> productIds) {
        return productRepository.countAllByIdIn(productIds);
    }

    private List<Long> createProductIds(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private boolean isSameSize(int size, int comparison) {
        return size != comparison;
    }

    private BigDecimal calculateProductsSum(MenuProducts menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts.getMenuProducts()) {
            sum = sum.add(calculateAmount(menuProduct));
        }

        return sum;
    }

    private BigDecimal calculateAmount(MenuProduct menuProduct) {
        Product product = productRepository.findById(menuProduct.getProductId())
                .orElseThrow(NoSuchElementException::new);
        return product.calculateAmount(menuProduct.getQuantity());
    }
}

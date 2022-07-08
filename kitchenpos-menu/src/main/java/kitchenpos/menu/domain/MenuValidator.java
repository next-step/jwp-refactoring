package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class MenuValidator {
    private static final String ERROR_MESSAGE_EMPTY = "상품이 존재하지 않습니다.";
    private static final String ERROR_MESSAGE_INVALID_SUM = "상품 금액의 합이 메뉴 가격보다 작아야 합니다.";
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest);
        validateEmpty(menuProducts);
        validateMenuPrice(menuProducts, new Price(menuRequest.getPrice()));
    }

    private List<MenuProduct> getMenuProducts(MenuRequest request) {
        return request.getMenuProductRequests().stream()
                .map(MenuProductRequest::toMenuProduct)
                .collect(Collectors.toList());
    }

    private void validateMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
                .orElseThrow(NoSuchElementException::new);
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts, Price price) {
        Price sum = calculateTotalPrice(menuProducts, price);
        if (sum.isMoreExpensive(price)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_SUM);
        }
    }

    private void validateEmpty(List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_EMPTY);
        }
    }

    private Price calculateTotalPrice(List<MenuProduct> menuProducts, Price price) {
        Price sum = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            Product product = getProductById(menuProduct.getProductId());
            Price totalPrice = getTotalPrice(product.getPrice(), menuProduct.getQuantity());
            sum = sum.add(totalPrice.getPrice());
        }
        return sum;
    }

    private Price getTotalPrice(Price price, Quantity quantity) {
        return new Price(price.getPrice().multiply(BigDecimal.valueOf(quantity.getQuantity())));
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
    }
}

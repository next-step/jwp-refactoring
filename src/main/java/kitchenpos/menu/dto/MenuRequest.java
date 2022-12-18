package kitchenpos.menu.dto;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private static final String EXCEPTION_MESSAGE_INVALID_MENU_PRODUCTS = "유효한 MenuProduct 가 없습니다! 값을 확인해주세요.";
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    private MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        validateMenuRequest(menuProducts);

        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Long getQuantityByProductId(Long productId) {
        return menuProducts.stream()
                .filter(menuProductRequest -> menuProductRequest.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getQuantity();
    }

    private void validateMenuRequest(List<MenuProductRequest> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_INVALID_MENU_PRODUCTS);
        }
    }
}

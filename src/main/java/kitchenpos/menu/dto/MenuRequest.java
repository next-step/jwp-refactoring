package kitchenpos.menu.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.CommonErrorCode;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuRequest {

    private String name;
    private Integer price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, Integer price, Long menuGroupId,
        List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    public Menu toMenu(MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return Menu.of(name, price, menuGroup, menuProducts);
    }

    public Long getProductQuantity(Long productId) {
        MenuProductRequest menuProductRequest = menuProducts.stream()
            .filter(menuProductRequestTarget -> menuProductRequestTarget.isSameProductId(productId))
            .findFirst()
            .orElseThrow(() -> new InvalidParameterException(CommonErrorCode.PRODUCT_NOT_FOUND));

        return menuProductRequest.getQuantity();
    }
}

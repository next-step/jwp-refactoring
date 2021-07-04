package kitchenpos.dto.request;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.MenuCreate;
import kitchenpos.domain.menuproduct.MenuProductCreate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCreateRequest> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductCreateRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }


    public MenuCreate toCreate() {
        List<MenuProductCreate> menuProductCreates = menuProducts.stream()
                .map(MenuProductCreateRequest::toCreate)
                .collect(Collectors.toList());

        return new MenuCreate(
                new Name(name),
                new Price(price),
                menuGroupId,
                menuProductCreates
        );
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public void setMenuProducts(List<MenuProductCreateRequest> menuProducts) {
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

    public List<MenuProductCreateRequest> getMenuProducts() {
        return menuProducts;
    }

}

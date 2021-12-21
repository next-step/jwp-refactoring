package kitchenpos.menu.dto;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;

public class MenuRequest {

    private String name;

    private Integer price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public Menu toEntity() {
        return Menu.of(name, BigDecimal.valueOf(price), menuGroupId, menuProducts.stream()
            .map(MenuProductRequest::toEntity)
            .collect(toList()));
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}

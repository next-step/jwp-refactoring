package kitchenpos.menu.dto;

import java.util.List;

public class CreateMenuDto {

    private String name;
    private Long price;
    private Long menuGroupId;
    private List<CreateMenuProductDto> menuProducts;

    public CreateMenuDto() { }

    public CreateMenuDto(String name, Long price, Long menuGroupId, List<CreateMenuProductDto> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<CreateMenuProductDto> getMenuProducts() {
        return menuProducts;
    }
}

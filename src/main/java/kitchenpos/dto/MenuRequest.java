package kitchenpos.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

public class MenuRequest {
    private String name;
    @Positive
    private long price;
    private long menuGroupId;
    @NotEmpty
    private List<MenuProductRequest> menuProducts;

    protected MenuRequest(){}

    public MenuRequest(String name, long price, long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}

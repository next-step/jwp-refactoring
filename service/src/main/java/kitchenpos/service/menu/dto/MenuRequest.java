package kitchenpos.service.menu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.menu.InvalidNameException;
import kitchenpos.domain.menu.InvalidPriceException;

import java.util.List;
import java.util.Objects;

public class MenuRequest {
    private String name;
    private long price;
    private long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    @JsonCreator
    public MenuRequest(
            @JsonProperty("name") String name, @JsonProperty("price") long price,
            @JsonProperty("menuGroupId") long menuGroupId,
            @JsonProperty("menuProducts") List<MenuProductRequest> menuProducts) {
        check(name, price);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public long getPrice() {
        return price;
    }

    private void check(String name, long price) {
        if (price < 0) {
            throw new InvalidPriceException();
        }

        if (Objects.isNull(name)) {
            throw new InvalidNameException();
        }
    }
}

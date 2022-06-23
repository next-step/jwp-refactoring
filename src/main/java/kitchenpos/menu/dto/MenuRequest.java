package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    @JsonCreator
    public MenuRequest(@JsonProperty("name") String name, @JsonProperty("price") BigDecimal price,
            @JsonProperty("menuGroupId") Long menuGroupId,
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void check(BigDecimal price) {
        if (this.price.compareTo(price) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private void check(String name, BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }
    }
}

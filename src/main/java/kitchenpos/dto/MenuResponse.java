package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuProduct;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    // TODO : 추후 엔티티 작업 후 개선
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
                        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

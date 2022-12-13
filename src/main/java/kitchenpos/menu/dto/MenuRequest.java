package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<Long> productIds;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<Long> productIds) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productIds = productIds;
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

    public List<Long> getProductIds() {
        return productIds;
    }
}

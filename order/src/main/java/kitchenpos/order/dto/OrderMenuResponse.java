package kitchenpos.order.dto;

import kitchenpos.order.domain.MenuAdapter;

import java.math.BigDecimal;

public class OrderMenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean matchId(Long menuId) {
        return id.equals(menuId);
    }

    public MenuAdapter toMenuAdapter() {
        return new MenuAdapter(id, name, price);
    }
}

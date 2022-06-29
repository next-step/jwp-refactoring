package kitchenpos.menu.dto;

import java.math.BigDecimal;

public class ChangeMenuRequest {
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
}

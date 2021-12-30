package kitchenpos.order.dto;

import kitchenpos.order.domain.MenuAdapter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class OrderLineItemRequest {

    @Positive
    @NotNull
    private Long menuId;

    @NotBlank
    private String menuName;

    @Positive
    @NotNull
    private BigDecimal menuPrice;

    @Positive
    @NotNull
    private Long quantity;

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public boolean match(MenuAdapter menu) {
        return menuId.equals(menu.getId());
    }
}

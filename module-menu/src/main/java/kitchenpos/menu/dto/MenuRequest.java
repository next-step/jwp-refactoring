package kitchenpos.menu.dto;

import static kitchenpos.common.message.ValidationMessage.MIN_SIZE_IS_ONE;
import static kitchenpos.common.message.ValidationMessage.NOT_EMPTY;
import static kitchenpos.common.message.ValidationMessage.POSITIVE;
import static kitchenpos.common.message.ValidationMessage.POSITIVE_OR_ZERO;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

public class MenuRequest {
    @NotEmpty(message = NOT_EMPTY)
    private String name;

    @PositiveOrZero(message = POSITIVE_OR_ZERO)
    private Long price;

    @Positive(message = POSITIVE)
    private Long menuGroupId;

    @Size(min = 1, message = MIN_SIZE_IS_ONE)
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, Long price, Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }
}

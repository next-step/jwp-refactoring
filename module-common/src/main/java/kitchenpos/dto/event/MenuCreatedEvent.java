package kitchenpos.dto.event;

import java.math.BigDecimal;
import java.util.Map;

public class MenuCreatedEvent {

    private final Map<Long, Long> quantityPerProduct;
    private final BigDecimal menuPrice;

    public MenuCreatedEvent(Map<Long, Long> quantityPerProduct, BigDecimal menuPrice) {
        this.quantityPerProduct = quantityPerProduct;
        this.menuPrice = menuPrice;
    }

    public Map<Long, Long> getQuantityPerProduct() {
        return quantityPerProduct;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
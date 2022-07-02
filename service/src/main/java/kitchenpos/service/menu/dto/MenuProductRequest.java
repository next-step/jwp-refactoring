package kitchenpos.service.menu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuProductRequest {
    private Long productId;
    private long quantity;

    @JsonCreator
    public MenuProductRequest(@JsonProperty("productId") long productId, @JsonProperty("quantity") long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return this.productId;
    }

    public long getQuantity() {
        return this.quantity;
    }
}

package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

public final class MenuRequest {

    private final String name;
    private final BigDecimal price;
    private final long menuGroupId;
    private final long productId;
    private final int quantity;

    @JsonCreator
    public MenuRequest(
        @JsonProperty("name") String name,
        @JsonProperty("price") BigDecimal price,
        @JsonProperty("menuGroupId") long menuGroupId,
        @JsonProperty("productId") long productId,
        @JsonProperty("quantity") int quantity) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Name name() {
        return Name.from(name);
    }

    public Price price() {
        return Price.from(price);
    }

    public long menuGroupId() {
        return menuGroupId;
    }

    public long productId() {
        return productId;
    }

    public int quantity() {
        return quantity;
    }
}

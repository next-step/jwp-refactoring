package kichenpos.order.product.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public final class MenuDto {

    private final long id;
    private final BigDecimal price;
    private final String name;

    @JsonCreator
    public MenuDto(@JsonProperty("id") long id,
        @JsonProperty("price") BigDecimal price,
        @JsonProperty("name") String name) {
        this.id = id;
        this.price = price;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}

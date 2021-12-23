package kichenpos.order.product.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public final class MenuDto {

    private final long id;
    private final String name;
    private final BigDecimal price;

    @JsonCreator
    public MenuDto(@JsonProperty("id") long id,
        @JsonProperty("name") String name,
        @JsonProperty("price") BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

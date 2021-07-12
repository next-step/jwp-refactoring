package kitchenpos.dto.menu;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.math.BigDecimal;

public class PriceResponse {

    BigDecimal value;

    public PriceResponse() {
    }

    @JsonCreator
    private PriceResponse(BigDecimal value) {
        this.value = value;
    }

    public static PriceResponse of(BigDecimal value) {
        return new PriceResponse(value);
    }

    public BigDecimal getValue() {
        return value;
    }

}

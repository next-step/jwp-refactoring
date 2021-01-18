package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductRequest {
    private String name;
    @Positive
    private long price;

    @Builder
    public ProductRequest(String name, long price) {
        this.name = name;
        this.price = price;
    }
}

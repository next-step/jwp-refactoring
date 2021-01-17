package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductResponse {
    private long id;
    private String name;
    private long price;

    @Builder
    public ProductResponse(long id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

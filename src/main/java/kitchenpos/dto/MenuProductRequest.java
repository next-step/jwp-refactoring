package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuProductRequest {
    private long productId;
    private long quantity;

    @Builder
    public MenuProductRequest(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}

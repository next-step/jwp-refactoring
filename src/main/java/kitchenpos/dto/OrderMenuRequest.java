package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenuRequest {
    private long menuId;
    private long quantity;

    @Builder
    public OrderMenuRequest(long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
}

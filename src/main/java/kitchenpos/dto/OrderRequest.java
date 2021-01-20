package kitchenpos.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderRequest {
    private long orderTableId;
    @NotEmpty
    private List<OrderMenuRequest> orderMenuRequests;

    @Builder
    public OrderRequest(long orderTableId, List<OrderMenuRequest> orderMenuRequests) {
        this.orderTableId = orderTableId;
        this.orderMenuRequests = orderMenuRequests;
    }
}

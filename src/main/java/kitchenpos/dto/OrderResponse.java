package kitchenpos.dto;

import kitchenpos.domain.OrderStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderResponse {
    private long id;
    private long orderTableId;
    private String orderStatus;
    private LocalDateTime createdTime;
    private List<OrderMenuResponse> orderMenuResponses;

    @Builder
    public OrderResponse(long id, long orderTableId, OrderStatus orderStatus, LocalDateTime createdTime, List<OrderMenuResponse> orderMenuResponses) {
        this.id = id;
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus.name();
        this.createdTime = createdTime;
        this.orderMenuResponses = orderMenuResponses;
    }
}

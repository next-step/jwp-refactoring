package kitchenpos.tablegroup.dto;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTableIdRequests {

    private List<OrderTableIdRequest> orderTableIdRequests = new ArrayList<>();

    protected OrderTableIdRequests() {
    }

    private OrderTableIdRequests(List<OrderTableIdRequest> orderTableIdRequests) {
        if (CollectionUtils.isEmpty(orderTableIdRequests) || orderTableIdRequests.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.orderTableIdRequests = orderTableIdRequests;
    }

    public static OrderTableIdRequests of(List<OrderTableIdRequest> orderTableIdRequests) {
        return new OrderTableIdRequests(orderTableIdRequests);
    }

    public List<OrderTableIdRequest> getOrderTableIdRequests() {
        return orderTableIdRequests;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}

package kitchenpos.table.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableRequests {
    List<OrderTableRequest> list;

    public OrderTableRequests() {
    }

    public OrderTableRequests(List<OrderTableRequest> list) {
        if (CollectionUtils.isEmpty(list) || list.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.list = list;
    }

    public List<Long> getOrderTableIds() {
        return list.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public int getSize() {
        return list.size();
    }
}

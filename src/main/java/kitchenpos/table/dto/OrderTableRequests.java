package kitchenpos.table.dto;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableRequests {
    List<OrderTableRequest> elements;

    public OrderTableRequests() {
    }

    public OrderTableRequests(List<OrderTableRequest> list) {
        if (CollectionUtils.isEmpty(list) || list.size() < 2) {
            throw new IllegalArgumentException();
        }
        this.elements = list;
    }

    public List<Long> getOrderTableIds() {
        return elements.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public int getSize() {
        return elements.size();
    }
}

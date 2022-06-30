package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTableRequests {
    List<OrderTableRequest> list;

    public OrderTableRequests() {
    }

    public OrderTableRequests(List<OrderTableRequest> list) {
        this.list = list;
    }

    public void validateForTableGroupCreate() {
        if (CollectionUtils.isEmpty(list) || list.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> getOrderTableIds() {
        return list.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }

    public boolean isSameSize(List<OrderTable> target) {
        return list.size() == target.size();
    }
}

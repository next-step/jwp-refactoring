package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.dto.OrderTableResponse;

@Embeddable
public class OrderTables {
    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableResponse> getResponses() {
        return orderTables
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }
}

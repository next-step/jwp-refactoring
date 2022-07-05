package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableResponse;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderTable> value = new ArrayList<>();

    protected OrderTables() {}

    public OrderTables(Collection<OrderTable> orderTables) {
        this.value.addAll(orderTables);
    }

    public List<OrderTableResponse> toResponse() {
        return this.value.stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    public void ungroup() {
        this.value.forEach(OrderTable::leaveGroup);
    }

    public List<Long> orderTableIds() {
        return this.value.stream()
                .mapToLong(OrderTable::getId)
                .boxed()
                .collect(Collectors.toList());
    }

    public List<OrderTable> getValue() {
        return value;
    }
}

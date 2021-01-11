package kitchenpos.ui.dto.tableGroup;

import java.util.Objects;

public class OrderTableInTableGroupResponse {
    private Long id;

    public OrderTableInTableGroupResponse() {
    }

    public OrderTableInTableGroupResponse(final Long id) {
        this.id = id;
    }

    public static OrderTableInTableGroupResponse of(Long orderTableId) {
        return new OrderTableInTableGroupResponse(orderTableId);
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTableInTableGroupResponse that = (OrderTableInTableGroupResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTableInTableGroupResponse{" +
                "id=" + id +
                '}';
    }
}

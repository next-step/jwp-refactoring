package kitchenpos.ui.dto.tablegroup;

import java.util.Objects;

public class OrderTableInTableGroupRequest {
    private Long id;

    OrderTableInTableGroupRequest() {
    }

    public OrderTableInTableGroupRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTableInTableGroupRequest that = (OrderTableInTableGroupRequest) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTableInTableGroupRequest{" +
                "id=" + id +
                '}';
    }
}

package kitchenpos.dto.order;

import java.util.Objects;

public class OrderTableId {

    private final Long id;

    public OrderTableId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableId that = (OrderTableId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderTableRequest{" +
                "id=" + id +
                '}';
    }
}

package kitchenpos.dto.order;

import java.util.Objects;

public class OrderTableRequest {

    private final Long id;

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableRequest that = (OrderTableRequest) o;
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

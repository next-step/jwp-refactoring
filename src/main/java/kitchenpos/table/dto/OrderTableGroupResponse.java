package kitchenpos.table.dto;

import kitchenpos.table.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderTableGroupResponse {
    private Long id;
    private LocalDateTime createdDate;

    protected OrderTableGroupResponse() {
    }

    public OrderTableGroupResponse(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public static OrderTableGroupResponse ofEmpty() {
        return new OrderTableGroupResponse();
    }

    public static OrderTableGroupResponse of(TableGroup tableGroup) {
        if(Objects.isNull(tableGroup)){
            return ofEmpty();
        }
        return new OrderTableGroupResponse(tableGroup.getId(), tableGroup.getCreatedDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTableGroupResponse that = (OrderTableGroupResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate);
    }
}

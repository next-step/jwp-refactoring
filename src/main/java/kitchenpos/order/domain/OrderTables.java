package kitchenpos.order.domain;

import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public void add(Long tableGroupId, List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("그룹지정은 주문 테이블 최소 2개 이상이어야 합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("이미 그룹 지정이 되었거나 빈 테이블이 아닙니다.");
            }

            orderTable.changeEmpty(false);
            orderTable.changeTableGroupId(tableGroupId);
            this.orderTables.add(orderTable);
        }
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }
}

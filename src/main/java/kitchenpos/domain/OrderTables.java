package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    private static final int MIN_ORDER_TABLE_CNT = 2;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {

    }

    private OrderTables(List<OrderTable> orderTables) {
        validGroup(orderTables);
        this.orderTables = orderTables;
    }


    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public void group(Long tableGroupId) {
        if (isNotAbleGroup(orderTables)) {
            throw new IllegalArgumentException("빈테이블이 존재하거나. 이미 단체석인경우에는 단체석으로 지정할수 없습니디");
        }
        this.orderTables.forEach((it) -> it.changeTableGroupId(tableGroupId));
    }

    public void unGroup() {
        orderTables.forEach((it)-> it.changeTableGroupId(null));
    }

    private void validGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_CNT) {
            throw new IllegalArgumentException("단체지정은 2개이상 테이블만 가능합니다.");
        }
    }


    private boolean isNotAbleGroup(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch((it) -> it.isEmpty() || Objects.nonNull(it.getTableGroupId()));
    }

    public List<OrderTable> getTables() {
        return Collections.unmodifiableList(orderTables);
    }

}

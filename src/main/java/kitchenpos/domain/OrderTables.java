package kitchenpos.domain;

import kitchenpos.exception.BadRequestException;
import kitchenpos.exception.InvalidTableCountException;
import kitchenpos.exception.TableInUseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Stream;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderTables {
    private static final int MINIMUM_GROUP_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new InvalidTableCountException("테이블 그룹은 최소 두 개의 테이블이 포함되어야 합니다");
        }

        for (OrderTable table : orderTables) {
            validateTable(table);
        }

        this.orderTables = orderTables;
    }

    public Stream<OrderTable> stream() {
        return orderTables.stream();
    }

    public void group(TableGroup tableGroup) {
        orderTables.forEach(it -> it.group(tableGroup));
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    private void validateTable(OrderTable table) {
        if (!table.isEmpty() || table.isGroupped()) {
            throw new TableInUseException("사용중인 테이블은 그룹에 포함시킬 수 없습니다.");
        }
    }
}

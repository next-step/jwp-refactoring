package kitchenpos.factory;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;

public class TableGroupFixture {
    public static OrderTable 테이블_생성(Long id) {
        return new OrderTable(id, null, 2, false);
    }

    public static OrderTable 테이블_생성(Long id, TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, tableGroup, numberOfGuests, isEmpty);
    }
}

package kitchenpos.tablegroup.fixture;

import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class TableGroupTestFixture {

    public static TableGroupRequest 테이블그룹요청(List<OrderTableRequest> orderTables) {
        return TableGroupRequest.of(null, null, orderTables);
    }

    public static TableGroup 테이블그룹() {
        return TableGroup.of();
    }

    public static TableGroup setId(final long id, final TableGroup tableGroup) {
        Field idField = Objects.requireNonNull(ReflectionUtils.findField(TableGroup.class, "id"));
        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, tableGroup, id);
        return tableGroup;
    }
}

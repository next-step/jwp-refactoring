package kitchenpos.fixture;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.dto.OrderTableRequest;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderTableTestFixture {

    public static OrderTableRequest 주문테이블(Long id, Long getTableGroupId, int numberOfGuests, boolean empty) {
        return OrderTableRequest.of(id, numberOfGuests, empty);
    }

    public static OrderTableRequest 주문테이블(Long getTableGroupId, int numberOfGuests, boolean empty) {
        return OrderTableRequest.of(null, numberOfGuests, empty);
    }

    public static OrderTableRequest 주문테이블2_요청() {
        return 주문테이블(null, null, 20, false);
    }

    public static OrderTableRequest 주문테이블1_요청() {
        return 주문테이블(null, null, 10, false);
    }

    public static OrderTable 그룹_있는_주문테이블_생성(OrderTableRequest request) {
        return OrderTable.of(request.getNumberOfGuests(), request.isEmpty());
    }

    public static OrderTable 그룹_없는_주문테이블_생성(OrderTableRequest request) {
        return OrderTable.of(request.getNumberOfGuests(), request.isEmpty());
    }

    public static List<OrderTableRequest> 주문정보요청목록(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> OrderTableRequest.of(orderTable.getId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    public static List<OrderTable> 주문정보목록(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(orderTable -> OrderTable.of(orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    public static List<OrderTable> mapToEntityForNoGroup(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(orderTableRequest -> OrderTable.of(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty()))
                .collect(Collectors.toList());
    }

    public static OrderTable setId(final long id, final OrderTable orderTable) {
        Field idField = Objects.requireNonNull(ReflectionUtils.findField(OrderTable.class, "id"));
        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, orderTable, id);
        return orderTable;
    }

    public static OrderTable setMenuGroup(final TableGroup tableGroup, final OrderTable orderTable) {
        Field menuGroupField = Objects.requireNonNull(ReflectionUtils.findField(OrderTable.class, "tableGroup"));
        ReflectionUtils.makeAccessible(menuGroupField);
        ReflectionUtils.setField(menuGroupField, orderTable, tableGroup);
        return orderTable;
    }
}

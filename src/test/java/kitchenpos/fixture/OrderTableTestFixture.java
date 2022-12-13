package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.TableGroupTestFixture.createTableGroup;

public class OrderTableTestFixture {

    public static OrderTableRequest createOrderTable(Long id, Long getTableGroupId, int numberOfGuests, boolean empty) {
        return OrderTableRequest.of(id, getTableGroupId, numberOfGuests, empty);
    }

    public static OrderTableRequest createOrderTable(Long getTableGroupId, int numberOfGuests, boolean empty) {
        return OrderTableRequest.of(null, getTableGroupId, numberOfGuests, empty);
    }

    public static OrderTableRequest 주문테이블2_요청() {
        return createOrderTable(null, null, 20, false);
    }

    public static OrderTableRequest 주문테이블1_요청() {
        return createOrderTable(null, null, 10, false);
    }

    public static OrderTable 그룹_있는_주문테이블_생성(OrderTableRequest request) {
        return OrderTable.of(request.getId(), createTableGroup(), request.getNumberOfGuests(), request.isEmpty());
    }

    public static OrderTable 그룹_없는_주문테이블_생성(OrderTableRequest request) {
        return OrderTable.of(request.getId(), null, request.getNumberOfGuests(), request.isEmpty());
    }

    public static List<OrderTableRequest> mapToRequest(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> OrderTableRequest.of(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    public static List<OrderTable> mapToEntity(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(orderTable -> OrderTable.of(orderTable.getId(), createTableGroup(), orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }

    public static List<OrderTable> mapToEntityForNoGroup(List<OrderTableRequest> orderTables) {
        return orderTables.stream()
                .map(orderTable -> OrderTable.of(orderTable.getId(), null, orderTable.getNumberOfGuests(), orderTable.isEmpty()))
                .collect(Collectors.toList());
    }
}

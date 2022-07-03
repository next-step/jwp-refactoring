package kitchenpos.table;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.TableGroupRequest;

public class TableAcceptanceAPI {

    public static ExtractableResponse<Response> 손님_입장(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        return AcceptanceTest.doPost("/api/tables", orderTable);
    }

    public static ExtractableResponse<Response> 손님_리스트_조회() {
        return AcceptanceTest.doGet("/api/tables");
    }

    public static ExtractableResponse<Response> 테이블_상태_변경_요청(OrderTable orderTable, boolean empty) {
        orderTable.setEmpty(empty);

        return AcceptanceTest.doPut("/api/tables/" + orderTable.getId() + "/empty", orderTable);
    }

    public static ExtractableResponse<Response> 테이블_손님수_변경_요청(OrderTable orderTable, int numberOfGuests) {
        orderTable.changeNumberOfGuests(numberOfGuests);

        return AcceptanceTest.doPut("/api/tables/" + orderTable.getId() + "/number-of-guests", orderTable);
    }

    public static ExtractableResponse<Response> 단체_손님을_생성(List<OrderTable> orderTableList) {
        List<Long> orderTableIds = orderTableList.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        TableGroupRequest tableGroup = new TableGroupRequest(orderTableIds);

        return AcceptanceTest.doPost("/api/table-groups", tableGroup);
    }

    public static ExtractableResponse<Response> 단체_손님을_해제(Long orderTableId) {
        return AcceptanceTest.doDelete("/api/table-groups/" + orderTableId);
    }
}

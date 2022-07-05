package kitchenpos.table;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TableGenerator {
    private static final String TABLE_PATH = "/api/tables";
    private static final String TABLE_GROUP_PATH = "/api/table-groups";

    public static OrderTable 주문_테이블_생성(NumberOfGuests numberOfGuests) {
        return new OrderTable(numberOfGuests);
    }

    public static OrderTables 주문_테이블_목록_생성(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    public static TableGroup 테이블_그룹_생성(OrderTables orderTables) {
        return new TableGroup(orderTables);
    }

    public static OrderTableCreateRequest 주문_테이블_생성_요청(int numberOfGuests) {
        return new OrderTableCreateRequest(numberOfGuests);
    }

    public static TableGroupCreateRequest 테이블_그룹_생성_요청(List<Long> orderTableIds) {
        return new TableGroupCreateRequest(orderTableIds);
    }

    public static ExtractableResponse<Response> 테이블_생성_API_호출(OrderTableCreateRequest request) {
        return RestAssuredRequest.postRequest(TABLE_PATH, Collections.emptyMap(), request);
    }

    public static ExtractableResponse<Response> 테이블_생성_목록_조회_API_호출() {
        return RestAssuredRequest.getRequest(TABLE_PATH, Collections.emptyMap());
    }

    public static ExtractableResponse<Response> 빈_테이블_변경_API_호출(Long orderTableId, boolean changeEmpty) {
        Map<String, Object> params = Collections.singletonMap("empty", changeEmpty);
        return RestAssuredRequest.putRequest(TABLE_PATH + "/{orderTableId}/empty", params, Collections.emptyMap(), orderTableId);
    }

    public static ExtractableResponse<Response> 테이블_좌석수_변경_API_호출(Long orderTableId, int numberOfGuests) {
        Map<String, Object> params = Collections.singletonMap("numberOfGuests", numberOfGuests);
        return RestAssuredRequest.putRequest(TABLE_PATH + "/{orderTableId}/number-of-guests", params, Collections.emptyMap(), orderTableId);
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_API_호출(TableGroupCreateRequest request) {
        return RestAssuredRequest.postRequest(TABLE_GROUP_PATH, Collections.emptyMap(), request);
    }

    public static ExtractableResponse<Response> 테이블_그룹_해제_API_호출(Long tableGroupId) {
        return RestAssuredRequest.deleteRequest(TABLE_GROUP_PATH + "/{tableGroupId}", Collections.emptyMap(), tableGroupId);
    }
}

package kitchenpos.tableGroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;

import java.util.Collections;
import java.util.List;

public class TableGroupGenerator {
    private static final String PATH = "/api/table-groups";

    public static TableGroup 테이블_그룹_생성(OrderTables orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroupCreateRequest 테이블_그룹_생성_요청(List<Long> orderTableIds) {
        return new TableGroupCreateRequest(orderTableIds);
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_API_호출(TableGroupCreateRequest request) {
        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), request);
    }

    public static ExtractableResponse<Response> 테이블_그룹_해제_API_호출(Long tableGroupId) {
        return RestAssuredRequest.deleteRequest(PATH + "/{tableGroupId}", Collections.emptyMap(), tableGroupId);
    }
}

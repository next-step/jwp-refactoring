package kitchenpos.tablegroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

import static kitchenpos.AcceptanceTest.*;

public class TableGroupSteps {

    private static final String TABLE_GROUP_URI = "/api/table-groups";

    public static TableGroupResponse 테이블_그룹_등록되어_있음(TableGroupRequest tableGroupRequest) {
        return post(TABLE_GROUP_URI, tableGroupRequest).as(TableGroupResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_그룹_등록_요청(TableGroupRequest tableGroupRequest) {
        return post(TABLE_GROUP_URI, tableGroupRequest);
    }

    public static ExtractableResponse<Response> 테이블_그룹에서_테이블_제거_요청(Long id) {
        return delete(TABLE_GROUP_URI + "/{tableGroupId}", id);
    }
}

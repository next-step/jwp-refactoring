package kitchenpos.Acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.TestFixture;
import kitchenpos.domain.TableGroup;

public class TableGroupTestFixture extends TestFixture {
    public static final String TABLE_GROUP_BASE_URI = "/api/table-groups";

    public static ExtractableResponse<Response> 단체_지정_요청(TableGroup tableGroup) {
        return post(TABLE_GROUP_BASE_URI, tableGroup);
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(Long tableGroupId) {
        return delete(TABLE_GROUP_BASE_URI + "/" + tableGroupId);
    }

    public static void 단체_지정_생성됨(ExtractableResponse<Response> response) {
        created(response);
    }

    public static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        noContent(response);
    }
}

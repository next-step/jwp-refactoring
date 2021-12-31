package kitchenpos.fixture;

import static kitchenpos.common.AcceptanceFixture.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.table.dto.*;

public class TableGroupAcceptanceFixture extends AcceptanceTest {
    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroupRequest tableGroupRequest) {
        return post("/api/table-groups", tableGroupRequest);
    }

    public static ExtractableResponse<Response> 테이블_그룹_해제_요청(Long id) {
        return delete("/api/table-groups/{tableGroupId}", id);
    }
}

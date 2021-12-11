package kitchenpos.menu.domain.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.domain.tablegroup.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.utils.AcceptanceTest;

import static kitchenpos.utils.AcceptanceFixture.delete;
import static kitchenpos.utils.AcceptanceFixture.post;

public class TableGroupDomainFixture extends AcceptanceTest {

    public static TableGroup 한식_양식_합석 = new TableGroup();


    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroupRequest tableGroupRequest) {
        return post("/api/table-groups", tableGroupRequest);
    }

    public static ExtractableResponse<Response> 테이블_그룹_해제_요청(Long id) {
        return delete("/api/table-groups/{tableGroupId}", id);
    }
}

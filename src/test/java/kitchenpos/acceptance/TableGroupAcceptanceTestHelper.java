package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupAcceptanceTestHelper {

    static ExtractableResponse<Response> createTableGroup(List<OrderTable> orderTables) {
        final TableGroup requestBody = new TableGroup(orderTables);
        return AcceptanceTestHelper.post("/api/table-groups", requestBody);
    }

    static ExtractableResponse<Response> deleteTableGroup(Long id) {
        return AcceptanceTestHelper.delete(String.format("/api/table-groups/%d", id));
    }

}

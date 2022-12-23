package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;

class TableAcceptanceTestHelper {

    static ExtractableResponse<Response> createTable(OrderTable requestBody) {
        return AcceptanceTestHelper.post("/api/tables", requestBody);
    }

    static ExtractableResponse<Response> getTables() {
        return AcceptanceTestHelper.get("/api/tables");
    }

    static ExtractableResponse<Response> changeTableEmptyStatus(Long id, OrderTable orderTable) {
        return AcceptanceTestHelper.put(String.format("/api/tables/%d/empty", id), orderTable);
    }

    static ExtractableResponse<Response> changeTableNumberOfGuests(Long id, OrderTable orderTable) {
        return AcceptanceTestHelper.put(String.format("/api/tables/%d/number-of-guests", id), orderTable);
    }

}

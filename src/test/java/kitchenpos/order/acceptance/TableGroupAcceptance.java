package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.dto.TableGroupRequest;
import org.springframework.http.MediaType;

import java.util.List;

public class TableGroupAcceptance {
    public static ExtractableResponse<Response> create_table_group(List<Long> orderTables) {
        TableGroupRequest request = new TableGroupRequest(orderTables);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> ungroup(Long tableGroupId) {
        return RestAssured.given().log().all()
                .when().delete("/api/table-groups/" + tableGroupId)
                .then().log().all()
                .extract();
    }
}

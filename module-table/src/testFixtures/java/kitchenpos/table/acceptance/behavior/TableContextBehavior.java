package kitchenpos.table.acceptance.behavior;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

public class TableContextBehavior {
    private TableContextBehavior() {
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(orderTableRequest).post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static OrderTableResponse 테이블_생성됨(OrderTableRequest orderTableRequest) {
        return 테이블_생성_요청(orderTableRequest).as(OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_목록조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static List<OrderTableResponse> 테이블_목록조회() {
        ExtractableResponse<Response> response = 테이블_목록조회_요청();
        return response.jsonPath().getList("$", OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_공석여부_변경_요청(Long orderTableId, OrderTableRequest param) {
        String uri = String.format("/api/tables/%d/empty", orderTableId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_인원수_변경_요청(Long orderTableId, OrderTableRequest param) {
        String uri = String.format("/api/tables/%d/number-of-guests", orderTableId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(param).put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블그룹_생성_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().body(tableGroupRequest).post("/api/table-groups/")
                .then().log().all()
                .extract();
    }

    public static TableGroupResponse 테이블그룹_생성(TableGroupRequest tableGroupRequest) {
        return 테이블그룹_생성_요청(tableGroupRequest).as(TableGroupResponse.class);
    }

    public static ExtractableResponse<Response> 테이블그룹_해제_요청(Long tableGroupId) {
        String uri = String.format("/api/table-groups/%d", tableGroupId);
        return RestAssured
                .given().contentType(ContentType.JSON).log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }
}

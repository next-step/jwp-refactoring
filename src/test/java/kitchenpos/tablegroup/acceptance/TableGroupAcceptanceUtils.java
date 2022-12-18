package kitchenpos.tablegroup.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.acceptance.OrderTableId;

public class TableGroupAcceptanceUtils {
    private TableGroupAcceptanceUtils() {}

    public static ExtractableResponse<Response> 단체_지정_등록_요청(List<OrderTableId> orderTableIds) {
        Map<String, Object> body = new HashMap<>();
        body.put("orderTables", orderTableIds.stream()
            .map(OrderTableIdParam::new).collect(
                Collectors.toList())
        );

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    public static void 단체_지정_등록_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체_지정_등록_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(TableGroupId tableGroupId) {
        return RestAssured
            .given().log().all()
            .when().delete("/api/table-groups/" + tableGroupId.value())
            .then().log().all()
            .extract();
    }

    public static void 단체_지정_해제_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 단체_지정_해제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static TableGroupId 단체_지정_ID_추출(ExtractableResponse<Response> response) {
        return new TableGroupId(response.jsonPath().getLong("id"));
    }
}

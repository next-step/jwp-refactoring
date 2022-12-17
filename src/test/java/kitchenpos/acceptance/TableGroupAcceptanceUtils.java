package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.TableGroupRequest;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceUtils {
    private TableGroupAcceptanceUtils() {
    }

    public static ExtractableResponse<Response> 단체_지정_등록되어_있음(TableGroupRequest request) {
        return 단체_지정_요청함(request);
    }

    public static ExtractableResponse<Response> 단체_지정_요청함(TableGroupRequest request) {
        return RestAssured
                .given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_취소_요청함(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }
}

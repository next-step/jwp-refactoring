package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.product.TableGroup;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceStep {
    private TableGroupAcceptanceStep() {
    }

    public static ExtractableResponse<Response> 단체_지정_등록되어_있음(TableGroup tableGroup) {
        return 단체_지정_요청함(tableGroup);
    }

    public static ExtractableResponse<Response> 단체_지정_요청함(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .body(tableGroup)
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

package kitchenpos.tablegroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupRestAssured {
    public static ExtractableResponse<Response> 테이블군_생성_요청(List<Long> orderTables) {
        TableGroupRequest request = new TableGroupRequest(orderTables);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블군_삭제_요청(Long tableGroupId) {
        return RestAssured.given().log().all()
                .when().delete("/api/table-groups/" + tableGroupId)
                .then().log().all()
                .extract();
    }

}

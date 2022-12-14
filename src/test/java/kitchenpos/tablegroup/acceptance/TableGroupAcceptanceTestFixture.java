package kitchenpos.tablegroup.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceTestFixture {
    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroupRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성되어_있음(TableGroupRequest request) {
        return 테이블_그룹_생성_요청(request);
    }

    public static ExtractableResponse<Response> 테이블_그룹_해제_요청(Long tableGroupId) {
        return RestAssured.given().log().all()
                .when().delete("/api/table-groups/{tableGroupsId}", tableGroupId)
                .then().log().all()
                .extract();
    }

    public static void 테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 테이블_그룹_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

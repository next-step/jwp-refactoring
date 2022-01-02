package kitchenpos.accpetance.tablegroup;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TableGroupAcceptanceTestHelper {
    private TableGroupAcceptanceTestHelper() {
    }

    public static ExtractableResponse<Response> 테이블_그룹을_생성(Map<String, Object> params) {
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/table-groups")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성되어_있음(List<Map<String, Integer>> tableIds) {
        Map<String, Object> params = Collections.singletonMap("orderTables", tableIds);
        return 테이블_그룹을_생성(params);
    }

    public static void 테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 테이블_그룹_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 테이블_그룹_삭제(long id) {
        return RestAssured
            .given().log().all()
            .when().delete("/api/table-groups/" + id)
            .then().log().all().extract();
    }

    public static void 테이블_그룹_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 테이블_그룹_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}

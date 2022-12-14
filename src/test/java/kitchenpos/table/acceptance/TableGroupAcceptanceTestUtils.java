package kitchenpos.table.acceptance;

import static kitchenpos.table.domain.TableGroupTestFixture.tableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceTestUtils {
    private static final String TABLE_GROUP_PATH = "/api/table-groups";

    private TableGroupAcceptanceTestUtils() {}

    public static ExtractableResponse<Response> 단체_테이블_생성_요청(List<Long> ids) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest(ids))
                .when().post(TABLE_GROUP_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_테이블_해제_요청(Long tableGroupId) {
        return RestAssured.given().log().all()
                .when().delete(TABLE_GROUP_PATH + "/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }

    public static TableGroupResponse 단체_테이블_등록되어_있음(Long... ids) {
        ExtractableResponse<Response> response = 단체_테이블_생성_요청(Arrays.asList(ids));
        단체_테이블_생성됨(response);
        return response.as(TableGroupResponse.class);
    }

    public static void 단체_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체_테이블_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 단체_테이블_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 단체_테이블_해제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}

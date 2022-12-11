package kitchenpos.acceptance;

import static kitchenpos.domain.TableGroupTestFixture.tableGroup;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class TableGroupAcceptanceTestUtils {
    private static final String TABLE_GROUP_PATH = "/api/table-groups";

    private TableGroupAcceptanceTestUtils() {}

    public static ExtractableResponse<Response> 단체_테이블_생성_요청(OrderTable... orderTables) {
        TableGroup tableGroup = tableGroup(null, Arrays.asList(orderTables));

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
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

    public static TableGroup 단체_테이블_등록되어_있음(OrderTable... orderTables) {
        ExtractableResponse<Response> response = 단체_테이블_생성_요청(orderTables);
        단체_테이블_생성됨(response);
        return response.as(TableGroup.class);
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

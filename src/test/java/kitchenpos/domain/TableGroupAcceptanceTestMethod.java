package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.http.HttpStatus;

class TableGroupAcceptanceTestMethod extends AcceptanceTest {

    private static final String TABLE_GROUP_PATH = "/api/table-groups";
    private static final String SLASH = "/";

    public static ExtractableResponse<Response> 단체_등록_요청(TableGroupRequest params) {
        return post(TABLE_GROUP_PATH, params);
    }

    public static ExtractableResponse<Response> 단체_등록되어_있음(TableGroupRequest params) {
        return 단체_등록_요청(params);
    }

    public static ExtractableResponse<Response> 단체_해제_요청(Long id) {
        return delete(TABLE_GROUP_PATH + SLASH + id);
    }

    public static void 단체_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(parseURIFromLocationHeader(response)).isNotBlank();
    }

    public static void 단체_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
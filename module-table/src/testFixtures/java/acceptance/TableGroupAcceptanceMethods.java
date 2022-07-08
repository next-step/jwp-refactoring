package acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.http.HttpStatus;

import static acceptance.RestAssuredMethods.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceMethods {
    public static ExtractableResponse<Response> 테이블그룹_등록_요청(TableGroupRequest params) {
        return post("/api/table-groups", params);
    }

    public static ExtractableResponse<Response> 테이블그룹_삭제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return delete(uri);
    }

    public static void 테이블그룹_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 테이블그룹_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 테이블그룹_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}

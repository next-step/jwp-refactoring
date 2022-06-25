package kitchenpos.helper.AcceptanceAssertionHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

public class TableGroupAssertionHelper {

    public static void 단체_테이블_등록되어있음(ExtractableResponse<Response> 등록결과) {
        assertAll(
            () -> assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(등록결과.jsonPath().get("id").toString()).isNotNull()
        );
    }

    public static void 단체_테이블_삭제됨(ExtractableResponse<Response> 등록결과) {
        assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 단체_테이블_에러발생(ExtractableResponse<Response> 등록결과) {
        assertThat(등록결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}

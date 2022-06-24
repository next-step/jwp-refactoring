package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.OrderTableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 관련 인수테스트")
public class TableAcceptanceTest extends AcceptanceTest{

    private static final String TABLE_URL = "/v2/api/tables";

    @BeforeEach
    void before() {
        super.setUp();
    }

    @Test
    @DisplayName("주문 테이블을 생성 한다.")
    void createTest() {
        ExtractableResponse<Response> 테이블_생성_요청_응답 = 테이블_생성_요청(2, true);
        주문_테이블_등록됨(테이블_생성_요청_응답);
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(int numberOfGuests, boolean empty) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTableRequest(numberOfGuests, empty))
                .when().post(TABLE_URL)
                .then().log().all().
                extract();
    }

    public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}

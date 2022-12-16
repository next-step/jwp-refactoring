package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void createTable() {
        ExtractableResponse<Response> response = 테이블_생성을_요청(0, true);

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @DisplayName("주문 테이블 목록을 조회한다")
    @Test
    void list() {
        테이블_생성을_요청(0, true);

        ExtractableResponse<Response> response = 테이블_목록을_요청();

        assertThat(response.jsonPath().getList(".", OrderTableResponse.class)).hasSize(1);
    }

    public static ExtractableResponse<Response> 테이블_생성을_요청(int numberOfGuests, boolean empty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, empty);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_목록을_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }
}

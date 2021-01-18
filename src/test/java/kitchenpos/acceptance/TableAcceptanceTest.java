package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 관리")
public class TableAcceptanceTest extends AcceptanceTest {
    @DisplayName("테이블을 관리한다")
    @Test
    void manage() {
        OrderTable orderTable = 테이블_생성();
        테이블_조회();
        테이블_상태_변경(orderTable);
        테이블_고객_수_변경(orderTable);
    }

    private OrderTable 테이블_생성() {
        OrderTable request = createRequest();
        ExtractableResponse<Response> createdResponse = 생성_요청(request);

        생성됨(createdResponse, request);
        return createdResponse.as(OrderTable.class);
    }

    private void 테이블_조회() {
        ExtractableResponse<Response> selectedResponse = 조회_요청();

        조회됨(selectedResponse);
    }

    private void 테이블_상태_변경(OrderTable orderTable) {
        orderTable.setEmpty(false);
        ExtractableResponse<Response> updatedResponse = 테이블_상태_변경_요청(orderTable);

        테이블_상태_변경됨(updatedResponse, orderTable);
    }

    private void 테이블_고객_수_변경(OrderTable orderTable) {
        orderTable.setNumberOfGuests(4);
        ExtractableResponse<Response> updatedResponse = 고객_수_변경_요청(orderTable);

        고객_수_변경됨(updatedResponse, orderTable);
    }

    public static OrderTable createRequest() {
        OrderTable request = new OrderTable();
        request.setNumberOfGuests(0);
        request.setEmpty(true);

        return request;
    }

    public static ExtractableResponse<Response> 생성_요청(OrderTable request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response, OrderTable request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        OrderTable product = response.as(OrderTable.class);
        assertThat(product.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(product.isEmpty()).isEqualTo(request.isEmpty());
    }

    public static ExtractableResponse<Response> 조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    public static void 조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<OrderTable> menuGroups = Arrays.asList(response.as(OrderTable[].class));
        assertThat(menuGroups.size()).isEqualTo(1);
    }

    public static ExtractableResponse<Response> 테이블_상태_변경_요청(OrderTable request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/tables/{orderTableId}/empty", request.getId())
                .then().log().all()
                .extract();
    }

    public static void 테이블_상태_변경됨(ExtractableResponse<Response> response, OrderTable request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        OrderTable product = response.as(OrderTable.class);
        assertThat(product.isEmpty()).isEqualTo(request.isEmpty());
    }

    public static ExtractableResponse<Response> 고객_수_변경_요청(OrderTable request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().put("/api/tables/{orderTableId}/number-of-guests", request.getId())
                .then().log().all()
                .extract();
    }

    public static void 고객_수_변경됨(ExtractableResponse<Response> response, OrderTable request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        OrderTable product = response.as(OrderTable.class);
        assertThat(product.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }
}

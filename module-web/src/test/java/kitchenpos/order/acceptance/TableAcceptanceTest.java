package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 관리한다.")
    @Test
    void manageOrderTable() {
        // when
        OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(orderTableRequest);

        // then
        주문_테이블_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_응답됨(findResponse);
        주문_테이블_목록_포함됨(findResponse, Arrays.asList(createResponse));

        // when
        orderTableRequest.setEmpty(false);
        ExtractableResponse<Response> emptyResponse = 주문_테이블_주문_상태_변경_요청(createResponse, orderTableRequest);

        // then
        주문_테이블_응답됨(emptyResponse);

        orderTableRequest.setNumberOfGuests(4);
        ExtractableResponse<Response> guestResponse = 주문_테이블_손님_수_변경_요청(createResponse, orderTableRequest);

        // then
        주문_테이블_응답됨(guestResponse);
    }

    private void 주문_테이블_목록_포함됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedProductIds = createResponses.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> actualIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(actualIds).containsAll(expectedProductIds);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().post("/api/tables")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_등록_되어있음(OrderTableRequest orderTableRequest) {
        return 주문_테이블_생성_요청(orderTableRequest);
    }

    private void 주문_테이블_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/tables")
                .then().log().all()
                .extract();
    }

    private void 주문_테이블_응답됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 주문_테이블_주문_상태_변경_요청(ExtractableResponse<Response> createResponse, OrderTableRequest orderTableRequest) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put(location+ "/empty")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청(ExtractableResponse<Response> createResponse, OrderTableRequest orderTableRequest) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderTableRequest)
                .when().put(location + "/number-of-guests")
                .then().log().all()
                .extract();
    }
}

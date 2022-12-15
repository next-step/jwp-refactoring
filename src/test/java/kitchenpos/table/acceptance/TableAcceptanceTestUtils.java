package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.acceptance.OrderAcceptanceTestUtils.주문_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceTestUtils.주문_상태_수정_요청;
import static kitchenpos.order.domain.OrderLineItemTestFixture.짜장면_1그릇_주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TableAcceptanceTestUtils {
    private static final String TABLE_PATH = "/api/tables";

    private TableAcceptanceTestUtils() {}

    public static OrderTableResponse 주문이_들어간_테이블() {
        OrderTableResponse 주문이_들어간_테이블 = 주문_테이블_등록되어_있음(2, false);
        주문_등록되어_있음(주문이_들어간_테이블.getId(), 짜장면_1그릇_주문_항목());
        return 주문이_들어간_테이블;
    }

    public static OrderTableResponse 주문이_완료된_테이블() {
        OrderTableResponse 주문이_들어간_테이블 = 주문_테이블_등록되어_있음(2, false);
        OrderResponse 주문 = 주문_등록되어_있음(주문이_들어간_테이블.getId(), 짜장면_1그릇_주문_항목());
        주문_상태_수정_요청(주문.getId(), OrderStatus.COMPLETION);
        return 주문이_들어간_테이블;
    }

    public static ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(TABLE_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(int numberOfGuests, boolean empty) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTableRequest(numberOfGuests, empty))
                .when().post(TABLE_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_빈_테이블_수정_요청(Long orderTableId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(TABLE_PATH + "/{orderTableId}/empty", orderTableId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_테이블_손님_수_수정_요청(Long orderTableId, int numberOfGuests) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderTableRequest(numberOfGuests, false))
                .when().put(TABLE_PATH + "/{orderTableId}/number-of-guests", orderTableId)
                .then().log().all()
                .extract();
    }

    public static OrderTableResponse 주문_테이블_등록되어_있음(int numberOfGuests, boolean empty) {
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(numberOfGuests, empty);
        주문_테이블_생성됨(response);
        return response.as(OrderTableResponse.class);
    }

    public static void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        Long tableGroupId = response.jsonPath()
                .getObject("tableGroupId", Long.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(tableGroupId).isNull();
    }

    public static void 테이블_손님_수_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 테이블_손님_수_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 테이블_손님_수_검증(ExtractableResponse<Response> response, int expect) {
        OrderTableResponse changeTable = response.as(OrderTableResponse.class);
        assertThat(changeTable.getNumberOfGuests()).isEqualTo(expect);
    }

    public static void 비어있는_테이블로_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 비어있는_테이블로_수정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 비어있는_주문_테이블_검증(ExtractableResponse<Response> response) {
        OrderTableResponse changeTable = response.as(OrderTableResponse.class);
        assertTrue(changeTable.isEmpty());
    }

    public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response, OrderTableResponse... orderTableResponses) {
        List<Long> actualIds = response.jsonPath()
                .getList("id", Long.class);
        List<Long> expectIds = Arrays.stream(orderTableResponses)
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsExactlyElementsOf(expectIds);
    }
}

package kitchenpos.order.acceptance;

import static kitchenpos.order.acceptance.TableGroupAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupRequest;

@DisplayName("주문 테이블 관련 기능")
public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void createTable() {
        // given
        OrderTableRequest request = new OrderTableRequest(0, true);

        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(request);

        // then
        주문_테이블_생성됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAllTable() {
        // given
        List<OrderTableResponse> orderTableResponses = Arrays.asList(
            주문_테이블_생성_요청(new OrderTableRequest(0, true))
                .as(OrderTableResponse.class),
            주문_테이블_생성_요청(new OrderTableRequest(2, true))
                .as(OrderTableResponse.class)
        );

        테이블_그룹_생성_요청(new TableGroupRequest(orderTableResponses.stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList())));

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        주문_테이블_목록_조회됨(response, orderTableResponses);
    }

    @DisplayName("주문 테이블의 주문 등록 가능 여부를 갱신한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성_요청(new OrderTableRequest(0, true))
            .as(OrderTableResponse.class);
        OrderTableRequest orderTableRequest = new OrderTableRequest(orderTableResponse.getNumberOfGuests(),
            false);

        // when
        ExtractableResponse<Response> response = 주문_테이블_empty_갱신_요청(orderTableResponse.getId(),
            orderTableRequest);

        // then
        주문_테이블_empty_갱신됨(response, orderTableRequest.isEmpty());
    }

    @DisplayName("주문 테이블의 방문 손님 수를 갱신한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성_요청(new OrderTableRequest(0,
            false)).as(OrderTableResponse.class);
        int numberOfGuests = 2;
        OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests, orderTableResponse.isEmpty());

        // when
        ExtractableResponse<Response> response = 주문_테이블_방문_손님수_갱신(orderTableResponse.getId(),
            orderTableRequest);

        // then
        주문_테이블_방문_손님수_갱신됨(response, numberOfGuests);
    }

    public static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest request) {
        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/tables")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("/api/tables")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_테이블_empty_갱신_요청(Long orderTableId,
        OrderTableRequest orderTableRequest) {
        return RestAssured
            .given().log().all()
            .body(orderTableRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/api/tables/{orderTableId}/empty", orderTableId)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_테이블_방문_손님수_갱신(Long orderTableId,
        OrderTableRequest orderTableRequest) {
        return RestAssured
            .given().log().all()
            .body(orderTableRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
            .then().log().all().extract();
    }

    private void 주문_테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response,
        List<OrderTableResponse> responses) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(new TypeRef<List<OrderTableResponse>>() {
        }))
            .extracting("id")
            .containsExactlyElementsOf(responses.stream().map(OrderTableResponse::getId)
                .collect(Collectors.toList()));
    }

    private void 주문_테이블_empty_갱신됨(ExtractableResponse<Response> response, boolean empty) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.as(OrderTableResponse.class).isEmpty(), empty);
    }

    private void 주문_테이블_방문_손님수_갱신됨(ExtractableResponse<Response> response, int numberOfGuests) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertEquals(response.as(OrderTableResponse.class).getNumberOfGuests(), numberOfGuests);
    }
}

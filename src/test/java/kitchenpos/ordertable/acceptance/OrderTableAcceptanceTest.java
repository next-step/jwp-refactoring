package kitchenpos.ordertable.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

@DisplayName("테이블 인수 테스트")
public class OrderTableAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest orderTableRequest;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTableRequest = new OrderTableRequest(2, false);
    }

    @Test
    @DisplayName("테이블를 등록한다.")
    void create() {
        // when
        ExtractableResponse<Response> response = 테이블_등록_요청(orderTableRequest);

        // then
        테이블_등록됨(response);
    }

    @Test
    @DisplayName("테이블의 목록을 조회한다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 테이블_목록_조회_요청();

        // then
        테이블_목록_조회됨(response);
    }

    @Test
    @DisplayName("테이블의 주문 등록 가능 여부를 변경한다.")
    void changeEmpty() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_등록되어_있음(orderTableRequest);
        OrderTableRequest modifyOrderTableRequest = new OrderTableRequest(false);

        // when
        ExtractableResponse<Response> response = 테이블_주문_등록_가능_여부_변경_요청(savedOrderTableResponse.getId(), modifyOrderTableRequest);

        // then
        테이블_주문_등록_가능_여부_변경됨(response);
    }

    @Test
    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTableResponse savedOrderTableResponse = 테이블_등록되어_있음(orderTableRequest);
        OrderTableRequest orderTableRequest = new OrderTableRequest(4);

        // when
        ExtractableResponse<Response> response = 테이블_방문한_손님_수_변경_요청(savedOrderTableResponse.getId(), orderTableRequest);

        // then
        테이블_방문한_손님_수_변경됨(response);
    }

    public static OrderTableResponse 테이블_등록되어_있음(OrderTableRequest orderTableRequest) {
        return 테이블_등록_요청(orderTableRequest).as(OrderTableResponse.class);
    }

    public static ExtractableResponse<Response> 테이블_등록_요청(OrderTableRequest orderTableRequest) {
        return post("/api/tables", orderTableRequest);
    }

    public static ExtractableResponse<Response> 테이블_목록_조회_요청() {
        return get("/api/tables");
    }

    public static ExtractableResponse<Response> 테이블_주문_등록_가능_여부_변경_요청(Long orderTableId, OrderTableRequest orderTableRequest) {
        return put("/api/tables/{orderTableId}/empty", orderTableRequest, orderTableId);
    }

    private ExtractableResponse<Response> 테이블_방문한_손님_수_변경_요청(long orderTableId, OrderTableRequest orderTableRequest) {
        return put("/api/tables/{orderTableId}/number-of-guests", orderTableRequest, orderTableId);
    }

    private void 테이블_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 테이블_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", OrderTable.class).size()).isPositive();
    }

    private void 테이블_주문_등록_가능_여부_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 테이블_방문한_손님_수_변경됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

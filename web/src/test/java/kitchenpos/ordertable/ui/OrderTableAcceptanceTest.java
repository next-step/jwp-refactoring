package kitchenpos.ordertable.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관련 기능")
class OrderTableAcceptanceTest extends OrderTableAcceptanceTestSupport {
    @DisplayName("주문 테이블의 생성 / 목록 조회 / 주문 상태 변경 / 방문한 손님 수 변경")
    @Test
    void manageOrderTable() {
        // Given
        OrderTableRequest request = new OrderTableRequest(0, true);

        // When
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(request);

        // Then
        주문_테이블_생성_완료(createResponse);

        // When
        ExtractableResponse<Response> findResponse = 주문_테이블_목록_조회_요청();

        // Then
        주문_테이블_응답(findResponse);

        // When
        OrderTableRequest emptyRequest = new OrderTableRequest(5, false);
        ExtractableResponse<Response> emptyResponse = 주문_테이블_주문_상태_변경_요청(createResponse, emptyRequest);

        // Then
        주문_테이블_응답(emptyResponse);

        // When
        OrderTableRequest guestRequest = new OrderTableRequest(4, false);
        ExtractableResponse<Response> guestResponse = 주문_테이블_방문한_손님_수_변경_요청(createResponse, guestRequest);

        // Then
        주문_테이블_응답(guestResponse);
    }
}

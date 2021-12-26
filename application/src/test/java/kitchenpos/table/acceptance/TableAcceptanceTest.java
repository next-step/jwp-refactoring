package kitchenpos.table.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.utils.Http;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 테이블 관리 기능")
public class TableAcceptanceTest extends AcceptanceTest {
    @DisplayName("주문 테이블을 관리한다")
    @Test
    void testManagement() {
        // when
        ExtractableResponse<Response> createResponse = 주문_테이블_생성_요청(4, false);
        // then
        OrderTableResponse 주문_테이블 = 주문_테이블_생성됨(createResponse);

        // when
        ExtractableResponse<Response> changeResponse = 주문_테이블_인원_변경_요청(주문_테이블.getId(), 4);
        // then
        OrderTableResponse 인원_변경된_주문_테이블 = 주문_테이블_인원_변경됨(changeResponse);

        // when
        ExtractableResponse<Response> listResponse = 모든_주문_테이블_조회_요청();
        // then
        모든_주문_테이블_조회_응답됨(listResponse);
        모든_주문_테이블이_포함됨(listResponse, 인원_변경된_주문_테이블);

        // when
        ExtractableResponse<Response> emptyResponse = 주문_테이블_비우기_요청(주문_테이블.getId());
        // then
        주문_테이블_비워짐(emptyResponse);
    }

    /**
     * 요청 관련
     */
    private static ExtractableResponse<Response> 주문_테이블_생성_요청(int numberOfGuest, boolean isEmpty) {
        return Http.post("/api/tables/", new OrderTableRequest(numberOfGuest, isEmpty));
    }

    private static ExtractableResponse<Response> 주문_테이블_인원_변경_요청(long tableId, int numberOfGuests) {
        return Http.put("/api/tables/" + tableId + "/number-of-guests", new OrderTableRequest(numberOfGuests, false));
    }

    private static ExtractableResponse<Response> 모든_주문_테이블_조회_요청() {
        return Http.get("/api/tables/");
    }

    private static ExtractableResponse<Response> 주문_테이블_비우기_요청(long tableId) {
        return Http.put("/api/tables/" + tableId + "/empty", new OrderTableRequest(0, true));
    }

    /**
     * 응답 관련
     */
    private static void 주문_테이블_비워짐(ExtractableResponse<Response> emptyResponse) {
        assertThat(emptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 모든_주문_테이블_조회_응답됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static OrderTableResponse 주문_테이블_인원_변경됨(ExtractableResponse<Response> changeResponse) {
        모든_주문_테이블_조회_응답됨(changeResponse);
        return changeResponse.as(OrderTableResponse.class);
    }

    private OrderTableResponse 주문_테이블_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return createResponse.as(OrderTableResponse.class);
    }

    private static void 모든_주문_테이블이_포함됨(ExtractableResponse<Response> listResponse, OrderTableResponse changedOrderTable) {
        List<OrderTableResponse> orderTables = listResponse.jsonPath().getList(".", OrderTableResponse.class);
        assertThat(orderTables).first()
                .isEqualTo(changedOrderTable);
    }

    /**
     * 테스트 픽스처 관련
     */
    public static OrderTableResponse 주문_테이블_등록되어_있음(int numberOfGuests, boolean isEmpty) {
        return 주문_테이블_생성_요청(numberOfGuests, isEmpty).as(OrderTableResponse.class);
    }
}

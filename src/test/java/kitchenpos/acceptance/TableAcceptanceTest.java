package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
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
        ExtractableResponse<Response> createResponse = Http.post("/api/tables/", new OrderTable(2, false));
        // then
        주문_테이블_생성됨(createResponse);

        // when
        OrderTable savedOrderTable = createResponse.as(OrderTable.class);
        ExtractableResponse<Response> changeResponse = 주문_테이블_인원_변경_요청(savedOrderTable.getId(), 4);
        // then
        주문_테이블_인원_변경됨(changeResponse);

        // when
        ExtractableResponse<Response> listResponse = 모든_주문_테이블_조회_요청();
        // then
        모든_주문_테이블_조회_응답됨(listResponse);
        모든_주문_테이블이_포함됨(listResponse, changeResponse.as(OrderTable.class));

        // when
        ExtractableResponse<Response> emptyResponse = 주문_테이블_비우기_요청(savedOrderTable.getId());
        // then
        주문_테이블_비워짐(emptyResponse);
    }

    /**
     * 요청 관련
     */
    private ExtractableResponse<Response> 주문_테이블_인원_변경_요청(long tableId, int numberOfGuests) {
        return Http.put("/api/tables/" + tableId + "/number-of-guests", new OrderTable(numberOfGuests));
    }

    private ExtractableResponse<Response> 모든_주문_테이블_조회_요청() {
        return Http.get("/api/tables/");
    }

    private ExtractableResponse<Response> 주문_테이블_비우기_요청(long tableId) {
        return Http.put("/api/tables/" + tableId + "/empty", new OrderTable(true));
    }

    /**
     * 응답 관련
     */
    private void 주문_테이블_비워짐(ExtractableResponse<Response> emptyResponse) {
        assertThat(emptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 모든_주문_테이블_조회_응답됨(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 주문_테이블_인원_변경됨(ExtractableResponse<Response> changeResponse) {
        모든_주문_테이블_조회_응답됨(changeResponse);
    }

    private void 주문_테이블_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 모든_주문_테이블이_포함됨(ExtractableResponse<Response> listResponse, OrderTable changedOrderTable) {
        List<OrderTable> orderTables = listResponse.jsonPath().getList(".", OrderTable.class);
        assertThat(orderTables).first()
                .isEqualTo(changedOrderTable);
    }
}

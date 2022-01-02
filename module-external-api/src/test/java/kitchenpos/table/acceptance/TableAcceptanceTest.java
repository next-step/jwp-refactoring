package kitchenpos.table.acceptance;

import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        OrderTableRequest 주문_테이블_일번 = new OrderTableRequest(5, true);

        // when
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(주문_테이블_일번);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void findAllTables() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTableRequest orderTableRequest) {
        return ofRequest(Method.POST, "/api/tables", orderTableRequest);
    }

    private ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return ofRequest(Method.GET, "/api/tables");
    }

    public static OrderTableResponse 주문_테이블_생성됨(OrderTableRequest orderTable) {
        return 주문_테이블_생성_요청(orderTable)
                .body()
                .as(OrderTableResponse.class);
    }
}

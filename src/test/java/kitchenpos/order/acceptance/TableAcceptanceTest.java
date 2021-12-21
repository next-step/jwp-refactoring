package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.order.OrderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        OrderTable 주문_테이블_일번 = OrderFactory.ofOrderTable(true, 5);

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

    private static ExtractableResponse<Response> 주문_테이블_생성_요청(OrderTable orderTable) {
        return ofRequest(Method.POST, "/api/tables", orderTable);
    }

    private ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
        return ofRequest(Method.GET, "/api/tables");
    }

    public static OrderTable 주문_테이블_생성됨(OrderTable orderTable) {
        return 주문_테이블_생성_요청(orderTable)
                .body()
                .as(OrderTable.class);
    }
}

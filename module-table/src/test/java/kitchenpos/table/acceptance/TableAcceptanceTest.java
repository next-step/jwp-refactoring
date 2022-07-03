package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceSupport.등록한_주문_테이블_검증됨;
import static kitchenpos.table.acceptance.TableAcceptanceSupport.주문_테이블_등록요청;
import static kitchenpos.table.acceptance.TableAcceptanceSupport.주문_테이블_목록_조회됨;
import static kitchenpos.table.acceptance.TableAcceptanceSupport.주문_테이블_목록_조회요청;
import static kitchenpos.table.acceptance.TableAcceptanceSupport.주문_테이블_빈테이블로_변경됨;
import static kitchenpos.table.acceptance.TableAcceptanceSupport.주문_테이블_빈테이블로_변경요청;
import static kitchenpos.table.acceptance.TableAcceptanceSupport.주문_테이블_손님수_변경됨;
import static kitchenpos.table.acceptance.TableAcceptanceSupport.주문_테이블_손님수_변경요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.acceptance.utils.AcceptanceTest;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블에 대한 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest 주문_테이블_request;
    private OrderTableRequest 주문_테이블2_request;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_request = new OrderTableRequest(null, null, 3, true);
        주문_테이블2_request = new OrderTableRequest(null, null, 1, true);
    }

    @DisplayName("주문 테이블을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 주문_테이블_등록요청(주문_테이블_request);

        // then
        등록한_주문_테이블_검증됨(response);
    }

    @DisplayName("주문 테이블 목록을 조회한다")
    @Test
    void find_test() {
        // given
        주문_테이블_등록요청(주문_테이블_request);
        주문_테이블_등록요청(주문_테이블2_request);

        // when
        ExtractableResponse<Response> response = 주문_테이블_목록_조회요청();

        // then
        주문_테이블_목록_조회됨(response, 2);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경한다")
    @Test
    void change_empty_test() {
        // given
        OrderTableResponse orderTable = 주문_테이블_등록요청(주문_테이블_request).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_테이블_빈테이블로_변경요청(orderTable.getId());

        // then
        주문_테이블_빈테이블로_변경됨(response);
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다")
    @Test
    void change_number_of_guests_test() {
        // given
        주문_테이블_request = new OrderTableRequest(null, null, 3, false);
        OrderTableResponse orderTable = 주문_테이블_등록요청(주문_테이블_request).as(OrderTableResponse.class);

        int 변경할_손님수 = 5;
        OrderTableRequest 변경할_주문_request = new OrderTableRequest(null, null, 변경할_손님수, true);

        // when
        ExtractableResponse<Response> response = 주문_테이블_손님수_변경요청(orderTable.getId(), 변경할_주문_request);

        // then
        주문_테이블_손님수_변경됨(response, 변경할_손님수);
    }
}

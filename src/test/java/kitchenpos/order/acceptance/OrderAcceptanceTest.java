package kitchenpos.order.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.testfixtures.MenuAcceptanceFixtures;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.testfixtures.acceptance.OrderAcceptanceFixtures;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.testfixtures.TableAcceptanceFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class OrderAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 첫번째_테이블;
    private MenuResponse 후라이드_앤드_양념;
    private MenuResponse 허니콤보;

    @BeforeEach
    public void setUp() {
        super.setUp();
        //background
        첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, false)).getBody();
        후라이드_앤드_양념 = MenuAcceptanceFixtures.후라이드_앤드_양념_메뉴_생성();
        허니콤보 = MenuAcceptanceFixtures.허니콤보_메뉴_생성();
    }

    @DisplayName("주문 등록")
    @Test
    void create() {
        //given
        OrderLineItemRequest 후라이드_앤드_양념_주문항목 = new OrderLineItemRequest(후라이드_앤드_양념.getId(), 2);
        OrderRequest 첫번째_주문_요청_정의 = new OrderRequest(첫번째_테이블.getId(),
            Arrays.asList(후라이드_앤드_양념_주문항목));

        //when
        ResponseEntity<OrderResponse> 주문_등록_결과 = OrderAcceptanceFixtures.주문_등록_요청(첫번째_주문_요청_정의);

        //then
        주문_등록_정상_확인(주문_등록_결과);
    }

    @DisplayName("주문종료된 테이블에서 주문 등록시 예외")
    @Test
    void create_exception() {
        //given
        OrderTableResponse 주문종료된_테이블 = TableAcceptanceFixtures.주문테이블_상태_변경_요청(
            첫번째_테이블.getId(), new OrderTableRequest(true)).getBody();

        OrderLineItemRequest 후라이드_앤드_양념_주문항목 = new OrderLineItemRequest(후라이드_앤드_양념.getId(), 2);
        OrderRequest 주문_요청_정의 = new OrderRequest(주문종료된_테이블.getId(),
            Arrays.asList(후라이드_앤드_양념_주문항목));

        //when
        ResponseEntity<OrderResponse> 주문_등록_결과 = OrderAcceptanceFixtures.주문_등록_요청(주문_요청_정의);

        //then
        예외발생_확인(주문_등록_결과);
    }

    @DisplayName("주문 전체 조회")
    @Test
    void list() {
        //given
        OrderLineItemRequest 후라이드_앤드_양념_주문항목 = new OrderLineItemRequest(후라이드_앤드_양념.getId(), 2);
        OrderResponse 첫번째_주문됨 = OrderAcceptanceFixtures.주문_등록_요청(
            new OrderRequest(첫번째_테이블.getId(), Arrays.asList(후라이드_앤드_양념_주문항목))).getBody();

        OrderLineItemRequest 허니콤보_주문_항목 = new OrderLineItemRequest(허니콤보.getId(), 1);
        OrderResponse 두번째_주문됨 = OrderAcceptanceFixtures.주문_등록_요청(
            new OrderRequest(첫번째_테이블.getId(), Arrays.asList(허니콤보_주문_항목))).getBody();

        //when
        ResponseEntity<List<OrderResponse>> 주문_조회_결과 = OrderAcceptanceFixtures.주문_전체_요청();

        //then
        조회_정상(주문_조회_결과);
        조회목록_정상(주문_조회_결과, Arrays.asList(첫번째_주문됨, 두번째_주문됨));
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        //given
        OrderLineItemRequest 후라이드_앤드_양념_주문항목 = new OrderLineItemRequest(후라이드_앤드_양념.getId(), 2);
        OrderResponse 첫번째_주문됨 = OrderAcceptanceFixtures.주문_등록_요청(
            new OrderRequest(첫번째_테이블.getId(), Arrays.asList(후라이드_앤드_양념_주문항목))).getBody();

        //when
        OrderRequest 주문상태_식사중_변경 = new OrderRequest(OrderStatus.MEAL);
        ResponseEntity<OrderResponse> 변경_결과 = OrderAcceptanceFixtures.주문_상태_변경_요청(
            첫번째_주문됨.getId(), 주문상태_식사중_변경);

        //then
        정상_처리_확인(변경_결과);
        상태변경_확인(변경_결과, 주문상태_식사중_변경);
    }

    private void 주문_등록_정상_확인(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 조회_정상(ResponseEntity<List<OrderResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 조회목록_정상(ResponseEntity<List<OrderResponse>> response,
        List<OrderResponse> expectedOrderResponse) {
        List<OrderResponse> orderResponses = response.getBody();
        assertThat(orderResponses).containsAll(expectedOrderResponse);
    }

    private void 정상_처리_확인(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 상태변경_확인(ResponseEntity<OrderResponse> response,
        OrderRequest orderRequest) {
        OrderResponse orderResponse = response.getBody();
        assertThat(orderResponse.getOrderStatus()).isEqualTo(orderRequest.getOrderStatus());
    }

    private void 예외발생_확인(ResponseEntity<OrderResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}

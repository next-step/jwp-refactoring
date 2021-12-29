package kitchenpos.ordertable.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import java.util.Arrays;
import java.util.List;

import kitchenpos.menu.MenuAcceptanceFixtures;
import kitchenpos.order.OrderAcceptanceFixtures;
import kitchenpos.ordertable.TableAcceptanceFixtures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("주문테이블 등록")
    @Test
    void create() {
        //given
        OrderTableRequest 첫번째_테이블 = TableAcceptanceFixtures.테이블_정의(6, false);

        //when
        ResponseEntity<OrderTableResponse> 테이블_생성_결과 = TableAcceptanceFixtures.테이블_등록_요청(첫번째_테이블);

        //then
        테이블_생성_정상_확인(테이블_생성_결과);
    }

    @DisplayName("주문테이블 전체 조회")
    @Test
    void list() {
        //given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, false)).getBody();
        OrderTableResponse 두번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(3, false)).getBody();

        //when
        ResponseEntity<List<OrderTableResponse>> 조회_결과 = TableAcceptanceFixtures.테이블_전체_조회_요청();

        //then
        조회_정상(조회_결과);
        조회목록_정상(조회_결과, Arrays.asList(첫번째_테이블, 두번째_테이블));
    }

    @DisplayName("주문테이블 상태 변경")
    @Test
    void changeOrderClose() {
        //given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, false)).getBody();

        //when
        OrderTableRequest 주문종료_상태로_변경 = new OrderTableRequest(true);
        ResponseEntity<OrderTableResponse> 변경_결과 = TableAcceptanceFixtures.주문테이블_상태_변경_요청(
            첫번째_테이블.getId(), 주문종료_상태로_변경);

        //then
        정상_처리_확인(변경_결과);
        상태변경_확인(변경_결과, 주문종료_상태로_변경);
    }

    @DisplayName("계산완료가 아닌 주문이 있는 경우 주문테이블 상태변경 예외")
    @Test
    void changeOrderClose_exception() {
        //given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, false)).getBody();
        OrderResponse 후라이드_주문 = 후라이드_주문_생성(첫번째_테이블.getId());
        OrderRequest 주문상태_계산완료_변경 = new OrderRequest(OrderStatus.COMPLETION);
        OrderAcceptanceFixtures.주문_상태_변경_요청(후라이드_주문.getId(), 주문상태_계산완료_변경);
        허니콤보_주문_생성(첫번째_테이블.getId()); // 허니콤보는 주문상태 COOKING으로 남아있음

        //when
        OrderTableRequest 주문종료_상태로_변경 = new OrderTableRequest(true);
        ResponseEntity<OrderTableResponse> 주문테이블_상태_변경_결과 = TableAcceptanceFixtures.주문테이블_상태_변경_요청(
            첫번째_테이블.getId(), 주문종료_상태로_변경);

        //then
        예외발생_확인(주문테이블_상태_변경_결과);
    }

    @DisplayName("방문자수 변경 변경")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTableResponse 첫번째_테이블 = TableAcceptanceFixtures.테이블_등록_요청(
            TableAcceptanceFixtures.테이블_정의(6, false)).getBody();

        //when
        OrderTableRequest 두명으로_변경 = new OrderTableRequest(2);
        ResponseEntity<OrderTableResponse> 변경_결과 = TableAcceptanceFixtures.주문테이블_방문자수_변경_요청(
            첫번째_테이블.getId(), 두명으로_변경);

        //then
        정상_처리_확인(변경_결과);
        방문자수변경_확인(변경_결과, 두명으로_변경);
    }

    private void 테이블_생성_정상_확인(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 조회목록_정상(ResponseEntity<List<OrderTableResponse>> response,
        List<OrderTableResponse> expectedOrderTables) {
        List<OrderTableResponse> orderTableResponses = response.getBody();
        assertThat(orderTableResponses).containsAll(expectedOrderTables);
    }

    private void 조회_정상(ResponseEntity<List<OrderTableResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 정상_처리_확인(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 상태변경_확인(ResponseEntity<OrderTableResponse> response,
        OrderTableRequest orderTableRequest) {
        OrderTableResponse orderTableResponse = response.getBody();
        assertThat(orderTableResponse.isOrderClose()).isEqualTo(orderTableRequest.isOrderClose());
    }

    private void 방문자수변경_확인(ResponseEntity<OrderTableResponse> response,
        OrderTableRequest orderTableRequest) {
        OrderTableResponse orderTableResponse = response.getBody();
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(
            orderTableRequest.getNumberOfGuests());
    }

    private void 예외발생_확인(ResponseEntity<OrderTableResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private OrderResponse 후라이드_주문_생성(Long tableId) {
        MenuResponse 후라이드_앤드_양념 = MenuAcceptanceFixtures.후라이드_앤드_양념_메뉴_생성();
        OrderLineItemRequest 후라이드_앤드_양념_주문항목 = new OrderLineItemRequest(후라이드_앤드_양념.getId(), 2);
        return OrderAcceptanceFixtures.주문_등록_요청(
            OrderAcceptanceFixtures.주문_정의(tableId,
                Arrays.asList(후라이드_앤드_양념_주문항목))).getBody();
    }

    private OrderResponse 허니콤보_주문_생성(Long tableId) {
        MenuResponse 허니콤보 = MenuAcceptanceFixtures.허니콤보_메뉴_생성();
        OrderLineItemRequest 허니콤보_주문항목 = new OrderLineItemRequest(허니콤보.getId(), 2);
        return OrderAcceptanceFixtures.주문_등록_요청(
            OrderAcceptanceFixtures.주문_정의(tableId,
                Arrays.asList(허니콤보_주문항목))).getBody();
    }
}

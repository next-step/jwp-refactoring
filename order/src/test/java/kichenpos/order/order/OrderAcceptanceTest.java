package kichenpos.order.order;

import static kichenpos.order.order.step.OrderAcceptanceStep.주문_등록_되어_있음;
import static kichenpos.order.order.step.OrderAcceptanceStep.주문_등록_됨;
import static kichenpos.order.order.step.OrderAcceptanceStep.주문_등록_요청;
import static kichenpos.order.order.step.OrderAcceptanceStep.주문_목록_조회_됨;
import static kichenpos.order.order.step.OrderAcceptanceStep.주문_목록_조회_요청;
import static kichenpos.order.order.step.OrderAcceptanceStep.주문_상태_수정_됨;
import static kichenpos.order.order.step.OrderAcceptanceStep.주문_상태_수정_요청;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import kichenpos.order.AcceptanceTest;
import kichenpos.order.order.domain.OrderStatus;
import kichenpos.order.order.ui.response.OrderResponse;
import kichenpos.order.product.infrastructure.MenuClient;
import kichenpos.order.product.infrastructure.dto.MenuDto;
import kichenpos.order.table.infrastructure.OrderTableClient;
import kichenpos.order.table.infrastructure.dto.OrderTableDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

@DisplayName("주문 관련 기능")
@SuppressWarnings("NonAsciiCharacters")
class OrderAcceptanceTest extends AcceptanceTest {

    private static final long MENU_ID = 1L;
    private static final long TABLE_ID = 1L;

    @MockBean
    private MenuClient menuClient;
    @MockBean
    private OrderTableClient tableClient;

    @BeforeEach
    void setUp() {
        메뉴_조회_됨(new MenuDto(MENU_ID, "후라이드치킨세트", BigDecimal.TEN));
        테이블_조회_됨(new OrderTableDto(false));
    }

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create() {
        //given
        int quantity = 2;

        //when
        ExtractableResponse<Response> response = 주문_등록_요청(TABLE_ID, MENU_ID, quantity);

        //then

        assertAll(
            () -> 주문_등록_됨(response, quantity, MENU_ID),
            this::테이블_주문됨_상태_변경_요청됨
        );
    }

    @Test
    @DisplayName("주문들을 조회할 수 있다.")
    void list() {
        //given
        OrderResponse order = 주문_등록_되어_있음(TABLE_ID, MENU_ID, 2);

        //when
        ExtractableResponse<Response> response = 주문_목록_조회_요청();

        //then
        주문_목록_조회_됨(response, order);
    }

    @Test
    @DisplayName("주문을 식사중 상태로 변경할 수 있다.")
    void changeOrderStatus_meal() {
        //given
        OrderResponse order = 주문_등록_되어_있음(TABLE_ID, MENU_ID, 2);
        OrderStatus status = OrderStatus.MEAL;

        //when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(order.getId(), status);

        //then
        주문_상태_수정_됨(response, status);
    }

    @Test
    @DisplayName("주문을 완료된 상태로 변경할 수 있다.")
    void changeOrderStatus_completion() {
        //given
        OrderResponse order = 주문_등록_되어_있음(TABLE_ID, MENU_ID, 2);
        OrderStatus status = OrderStatus.COMPLETION;

        //when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(order.getId(), status);

        //then
        assertAll(
            () -> 주문_상태_수정_됨(response, status),
            this::테이블_종료됨_상태_변경_요청됨
        );
    }

    private void 테이블_조회_됨(OrderTableDto orderTableDto) {
        when(tableClient.getTable(anyLong())).thenReturn(orderTableDto);
    }

    private void 메뉴_조회_됨(MenuDto menuDto) {
        when(menuClient.list(anyList())).thenReturn(
            Collections.singletonList(menuDto));
    }

    private void 테이블_주문됨_상태_변경_요청됨() {
        verify(tableClient, times(1)).changeOrdered(TABLE_ID);
    }

    private void 테이블_종료됨_상태_변경_요청됨() {
        verify(tableClient, times(1)).changeFinish(TABLE_ID);
    }
}

package kitchenpos.order.application;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;
import static kitchenpos.helper.OrderFixtures.주문_요청_만들기;
import static kitchenpos.helper.OrderLineItemFixtures.주문_항목_요청_만들기;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관련 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuService menuService;
    @Mock
    private TableService tableService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, tableService, menuService);
    }


    @DisplayName("주문 항목이 없으면 주문을 등록 할 수 없다.")
    @Test
    void create_empty() {
        //given
        OrderTable orderTable = 테이블_만들기(1L, 3, false);
        OrderRequest emptyRequest = 주문_요청_만들기(orderTable.getId(), Collections.emptyList());

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(emptyRequest));
    }

    @DisplayName("등록 되어있지 않은 메뉴가 있는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_registered_menu() {
        //given
        Menu menu = 메뉴_만들기(1L, "테스트 메뉴", 15000);
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(menu.getId(), 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(menu.getId(), 2);
        OrderTable orderTable = 테이블_만들기(1L, 3, false);
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        given(tableService.findOrderTable(request.getOrderTableId())).willReturn(orderTable);
        given(menuService.findMenu(anyLong())).willThrow(IllegalArgumentException.class);

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }


    @DisplayName("빈 테이블인 경우 주문을 등록 할 수 없다.")
    @Test
    void create_empty_table() {
        //given
        Menu menu = 메뉴_만들기(1L, "테스트 메뉴", 15000);
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(menu.getId(), 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(menu.getId(), 2);
        OrderTable orderTable = 테이블_만들기(1L, 3, true);
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        given(tableService.findOrderTable(request.getOrderTableId())).willReturn(orderTable);
        given(menuService.findMenu(anyLong())).willThrow(IllegalArgumentException.class);

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }

    @DisplayName("주문 테이블 없는 경우 주문을 등록 할 수 없다.")
    @Test
    void create_not_exist_order_table() {
        //given
        Menu menu = 메뉴_만들기(1L, "테스트 메뉴", 15000);
        OrderLineItemRequest orderLineItem1 = 주문_항목_요청_만들기(menu.getId(), 1);
        OrderLineItemRequest orderLineItem2 = 주문_항목_요청_만들기(menu.getId(), 2);
        OrderTable orderTable = 테이블_만들기(1L, 3, true);
        OrderRequest request = 주문_요청_만들기(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        given(tableService.findOrderTable(request.getOrderTableId())).willReturn(orderTable);

        //when then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(request));
    }


}

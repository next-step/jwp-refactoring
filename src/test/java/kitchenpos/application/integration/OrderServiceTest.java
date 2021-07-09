package kitchenpos.application.integration;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문 등록 예외 - 주문항목이 없을 경우")
    @Test
    public void 주문항목이없는경우_주문_등록_예외() throws Exception {
        //given
        OrderTable orderTable = 테이블_등록됨(5, false);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .hasMessage("주문항목이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 예외 - 요청한 주문의 메뉴와 디비의 메뉴가 불일치할 경우")
    @Test
    public void 요청주문메뉴와디비메뉴가불일치한경우_주문_등록_예외() throws Exception {
        //given
        Menu menu = 메뉴_등록됨("메뉴", BigDecimal.valueOf(1000));
        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu.getId(), 2L);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu.getId(), 3L);
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemRequest1,
                orderLineItemRequest2));

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .hasMessage("요청한 주문의 메뉴와 디비의 메뉴가 불일치합니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 예외 - 주문테이블이 없는 경우")
    @Test
    public void 주문테이블이없는경우_주문_등록_예외() throws Exception {
        //given
        Menu menu = 메뉴_등록됨("메뉴", BigDecimal.valueOf(1000));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(-1L, Arrays.asList(orderLineItemRequest));

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .hasMessage("주문테이블이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 예외 - 주문테이블이 빈테이블인 경우")
    @Test
    public void 주문테이블이빈테이블인경우_주문_등록_예외() throws Exception {
        //given
        Menu menu = 메뉴_등록됨("메뉴", BigDecimal.valueOf(1000));
        OrderTable orderTable = 테이블_등록됨(5, true);
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .hasMessage("주문테이블이 빈테이블입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록")
    @Test
    public void 주문_등록_확인() throws Exception {
        //given
        Menu menu = 메뉴_등록됨("메뉴", BigDecimal.valueOf(1000));
        OrderTable orderTable = 테이블_등록됨(5, false);
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));

        //when
        OrderResponse orderResponse = orderService.create(orderRequest);

        //then
        assertThat(orderResponse.getId()).isNotNull();
    }

    @DisplayName("주문 목록 조회")
    @Test
    public void 주문목록_조회_확인() throws Exception {
        //given
        Menu menu = 메뉴_등록됨("메뉴", BigDecimal.valueOf(1000));
        OrderTable orderTable = 테이블_등록됨(5, false);
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));
        orderService.create(orderRequest);
        orderService.create(orderRequest);
        orderService.create(orderRequest);

        //when
        List<OrderResponse> orders = orderService.list();

        //then
        assertThat(orders.size()).isEqualTo(3);
    }

    @DisplayName("주문상태 변경 예외 - 주문이 없는 경우")
    @Test
    public void 주문이없는경우_주문상태_변경_예외() throws Exception {
        //given
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL);

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, orderStatusRequest))
                .hasMessage("주문이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태 변경")
    @Test
    public void 주문상태_변경_확인() throws Exception {
        //given
        Menu menu = 메뉴_등록됨("치킨", BigDecimal.valueOf(1_000));
        OrderTable orderTable = 테이블_등록됨(5, false);
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));
        OrderResponse orderResponse = orderService.create(orderRequest);
        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL);

        //when
        OrderResponse changeOrderResponse = orderService.changeOrderStatus(orderResponse.getId(), orderStatusRequest);

        //then
        assertThat(changeOrderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    private OrderTable 테이블_등록됨(int numberOfGuests, boolean empty) {
        return orderTableRepository.save(new OrderTable(numberOfGuests, empty));
    }

    public Menu 메뉴_등록됨(String name, BigDecimal price) {
        return menuRepository.save(new Menu(name, price, null));
    }
}

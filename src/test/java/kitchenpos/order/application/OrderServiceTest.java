package kitchenpos.order.application;

import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.OrderLineItemNotFoundException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.order.exception.OrderStatusUpdateException;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.InvalidTableException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menugroup.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.menu.fixtures.MenuProductFixtures.메뉴상품;
import static kitchenpos.order.fixtures.OrderFixtures.*;
import static kitchenpos.table.fixtures.OrderTableFixtures.주문가능_다섯명테이블;
import static kitchenpos.table.fixtures.OrderTableFixtures.주문불가_다섯명테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : MenuServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@DisplayName("주문 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    private Order 후라이드반양념반두개주문;
    private Menu 후라이드반양념반메뉴;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    void setUp() {
        BigDecimal 메뉴가격 = new BigDecimal(32000);

        MenuProduct 양념치킨메뉴상품 = 메뉴상품(1L, 1L);
        MenuProduct 후라이드메뉴상품 = 메뉴상품(2L, 1L);

        후라이드반양념반메뉴 = new Menu("후라이드반양념반메뉴", 메뉴가격, 메뉴그룹("반반메뉴").getId(), Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품));

        OrderLineItem 주문정보_후라이드양념반두개 = new OrderLineItem(후라이드반양념반메뉴.getId(), 2L);

        후라이드반양념반두개주문 = new Order(주문가능_다섯명테이블().getId(), Lists.newArrayList(주문정보_후라이드양념반두개));
    }

    @Test
    @DisplayName("주문을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(orderRepository.findAllJoinFetch()).willReturn(Lists.newArrayList(후라이드반양념반두개주문));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).hasSize(1);
    }


    @Test
    @DisplayName("주문을 등록할 수 있다.")
    public void createOrder() {
        // when
        OrderResponse actual = orderService.create(주문등록요청());

        // then
        assertAll(
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }


    @Test
    @DisplayName("주문 상태를 변경할 수 있다")
    public void changeOrderStatus() {
        // given
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(후라이드반양념반두개주문));

        // when
        OrderResponse actual = orderService.changeOrderStatus(1L, 식사중으로_변경요청());

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("주문이 존재하지 않은 경우 주문 상태를 변경할 수 없다.")
    public void changeOrderStatusFailByUnknownOrder() {
        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 식사중으로_변경요청())).isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("주문상태가 완료인 경우 변경할 수 없다.")
    public void changeOrderStatusFailByOrderStatus() {
        // given
        given(orderRepository.findById(anyLong())).willReturn(Optional.of(후라이드반양념반두개주문));
        orderService.changeOrderStatus(1L, 식사완료로_변경요청());

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 식사중으로_변경요청())).isInstanceOf(OrderStatusUpdateException.class);
    }
}

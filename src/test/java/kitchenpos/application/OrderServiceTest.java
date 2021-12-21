package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderFixture.신규_주문;
import static kitchenpos.fixture.OrderFixture.완료_주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.fixture.TableFixture.비어있는_테이블;
import static kitchenpos.fixture.TableFixture.일반_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @DisplayName("주문 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        Order order = new Order();
        order.setOrderTableId(일반_테이블.getId());
        order.setOrderLineItems(Arrays.asList(주문_항목));

        given(menuDao.countByIdIn(anyList())).willReturn(Long.valueOf(order.getOrderLineItems().size()));
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(일반_테이블));
        given(orderDao.save(any(Order.class))).willReturn(신규_주문);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(주문_항목);

        // when
        Order 생성된_주문 = orderService.create(order);

        // then
        assertThat(생성된_주문).isEqualTo(신규_주문);
    }

    @DisplayName("주문 생성 실패 테스트 - 주문 항목 없음")
    @Test
    void create_failure_notExistOrderLineItems() {
        // given
        Order order = new Order();
        order.setOrderTableId(일반_테이블.getId());
        order.setOrderLineItems(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 생성 실패 테스트 - 주문 항목 수 일치하지 않음")
    @Test
    void create_failure_invalidSize() {
        // given
        Order order = new Order();
        order.setOrderTableId(일반_테이블.getId());
        order.setOrderLineItems(Arrays.asList(주문_항목));

        given(menuDao.countByIdIn(anyList())).willReturn(0L);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 생성 실패 테스트 - 주문 테이블 존재하지 않음")
    @Test
    void create_failure_notFoundOrderTable() {
        // given
        Order order = new Order();
        order.setOrderTableId(일반_테이블.getId());
        order.setOrderLineItems(Arrays.asList(주문_항목));

        given(menuDao.countByIdIn(anyList())).willReturn(Long.valueOf(order.getOrderLineItems().size()));
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 생성 실패 테스트 - 주문 테이블 비어있음")
    @Test
    void create_failure_emptyOrderTable() {
        // given
        Order order = new Order();
        order.setOrderTableId(비어있는_테이블.getId());
        order.setOrderLineItems(Arrays.asList(주문_항목));

        given(menuDao.countByIdIn(anyList())).willReturn(Long.valueOf(order.getOrderLineItems().size()));
        given(orderTableDao.findById(any(Long.class))).willReturn(Optional.of(비어있는_테이블));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void list() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(신규_주문));

        // when
        List<Order> 조회된_주문_목록 = orderService.list();

        // then
        assertThat(조회된_주문_목록).containsExactly(신규_주문);
    }

    @DisplayName("주문 상태 수정 성공 테스트")
    @Test
    void changeOrderStatus_success() {
        // given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(신규_주문));
        given(orderDao.save(any(Order.class))).willReturn(신규_주문);
        given(orderLineItemDao.findAllByOrderId(any(Long.class))).willReturn(신규_주문.getOrderLineItems());

        // when
        Order 수정된_주문 = orderService.changeOrderStatus(신규_주문.getId(), order);

        // then
        assertThat(수정된_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 수정 실패 테스트 - 수정 전 주문 상태가 COMPLETION")
    @Test
    void changeOrderStatus_failure_orderStatus() {
        // given
        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(완료_주문));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(완료_주문.getId(), order));
    }
}

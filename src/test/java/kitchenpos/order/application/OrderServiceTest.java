package kitchenpos.order.application;

import static kitchenpos.util.TestDataSet.원플원_후라이드;
import static kitchenpos.util.TestDataSet.주문_1번;
import static kitchenpos.util.TestDataSet.테이블_1번;
import static kitchenpos.util.TestDataSet.테이블_3번_존재;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderLineItemDao;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.product.constant.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("주문에 메뉴가 없으면 실패한다.")
    void noMenus() {
        //whren
        OrderRequest createOrder = new OrderRequest(주문_1번.getId(), OrderStatus.COOKING, Collections.emptyList());

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.create(createOrder);
        });
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 선택할 경우 에러가 발생한다.")
    void notMenus() {
        //given
        given(menuRepository.findAllById(any())).willReturn(Collections.emptyList());

        //whren
        OrderRequest requet = new OrderRequest(주문_1번.getId(), OrderStatus.COOKING,
            Arrays.asList(new OrderLineItemRequest(1L, 3L)));

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.create(requet);
        });

        verify(menuRepository, times(1)).findAllById(any());
    }

    @Test
    @DisplayName("테이블이 존재하지 않을경우 실패한다.")
    void noTable() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        //when
        OrderRequest requet = new OrderRequest(주문_1번.getId(), OrderStatus.COOKING,
            Arrays.asList(new OrderLineItemRequest(1L, 3L)));

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.create(requet);
        });

        verify(orderTableRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("빈 테이블은 실패한다.")
    void emptyTable() {
        //given
        given(menuRepository.findAllById(any())).willReturn(Arrays.asList(원플원_후라이드));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(테이블_1번));

        //when
        OrderRequest requet = new OrderRequest(주문_1번.getId(), OrderStatus.COOKING,
            Arrays.asList(new OrderLineItemRequest(1L, 3L)));

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.create(requet);
        });

        verify(menuRepository, times(1)).findAllById(any());
        verify(orderTableRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("주문 업데이트 성공 케이스 ")
    void changeOrderStatus() {
        //given
        Order createOrder = new Order(주문_1번.getId(), 테이블_3번_존재, 주문_1번.getOrderLineItems());
        createOrder.setOrderStatus(OrderStatus.COMPLETION);

        given(orderDao.findById(any())).willReturn(Optional.of(주문_1번));
        given(orderDao.save(any())).willReturn(createOrder);
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(주문_1번.getOrderLineItems());

        //when
        Order result = orderService.changeOrderStatus(주문_1번.getId(), createOrder);

        // then
        assertThat(result.getId()).isEqualTo(주문_1번.getId());
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);

        verify(orderDao, times(1)).findById(any());
        verify(orderDao, times(1)).save(any());
        verify(orderLineItemDao, times(1)).findAllByOrderId(any());
    }

    @Test
    @DisplayName("유효하지 않은 주문은 실패한다.")
    void noOder() {
        //given
        Order createOrder = new Order(주문_1번.getId(), 테이블_3번_존재, 주문_1번.getOrderLineItems());
        createOrder.setOrderStatus(OrderStatus.COMPLETION);

        given(orderDao.findById(any())).willReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.changeOrderStatus(주문_1번.getId(), createOrder);
        });

        verify(orderDao, times(1)).findById(any());
        verify(orderDao, times(0)).save(any());
        verify(orderLineItemDao, times(0)).findAllByOrderId(any());
    }

    @Test
    @DisplayName("정산된 주문일 경우 실패한다.")
    void alreadyEnd() {
        //given
        Order createOrder = new Order(주문_1번.getId(), 테이블_3번_존재, 주문_1번.getOrderLineItems());
        createOrder.setOrderStatus(OrderStatus.COMPLETION);

        given(orderDao.findById(any())).willReturn(Optional.of(createOrder));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.changeOrderStatus(주문_1번.getId(), createOrder);
        });

        verify(orderDao, times(1)).findById(any());
        verify(orderDao, times(0)).save(any());
        verify(orderLineItemDao, times(0)).findAllByOrderId(any());
    }

}

package kitchenpos.application;

import kitchenpos.order.dto.*;
import kitchenpos.order.exception.EmptyOrderTableException;
import kitchenpos.order.exception.OrderStatusCompleteException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    ApplicationEventPublisher publisher;


    OrderService orderService;

    OrderCreateRequest orderCreateRequest;
    OrderStatusChangeRequest orderStatusChangeRequest;

    @BeforeEach
    void setUp() {
        orderCreateRequest = new OrderCreateRequest(1L,
                Arrays.asList(
                        new OrderLineItemRequest(1L, 1L),
                        new OrderLineItemRequest(2L, 1L)
                )
        );
        orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        orderService = new OrderService(orderRepository, orderTableRepository, publisher);
    }

    @DisplayName("주문이 발생함")
    @Test
    void 주문이_발생함() {
        //given
        OrderTable orderTable = new OrderTable(1L, new TableGroup(), 4, false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
        Order savedOrder = new Order(1L, orderTable, OrderStatus.COOKING.name());
        given(orderRepository.save(any())).willReturn(savedOrder);
        //when
        OrderResponse orderResponse = orderService.create(orderCreateRequest);

        //then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("주문에, 존재하지 않는 주문테이블을 입력한 경우")
    @Test
    void 주문에_존재하지_않는_주문테이블을_입력한_경우() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest)).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주문 테이블이 비어 있어서 주문이 나갈 수 없는 경우")
    @Test
    void 주문테이블이_비어있는_경우() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(new OrderTable(1L, new TableGroup(), 0, true)));

        //when, then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(EmptyOrderTableException.class);
    }

    @DisplayName("주문테이블 리스트 가져오기")
    @Test
    void 주문테이블_리스트_가져오기() {
        //given
        given(orderRepository.findAll()).willReturn(Arrays.asList(
                new Order(1L, new OrderTable(), OrderStatus.COOKING.name()),
                new Order(2L, new OrderTable(), OrderStatus.COOKING.name())
        ));

        //when, then
        assertThat(orderService.list()).hasSize(2);
    }

    @DisplayName("주문상태 변경")
    @Test
    void 주문상태_변경() {
        //given
        given(orderRepository.findById(1L)).willReturn(Optional.of(new Order(1L, new OrderTable(), OrderStatus.COOKING.name())));

        //when
        OrderResponse orderResponse = orderService.changeOrderStatus(1L, orderStatusChangeRequest);

        //then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문상태 변경시 이미 계산 완료된 주문")
    @Test
    void 주문상태_변경시_이미_계산_완료된_주문() {
        //given
        given(orderRepository.findById(1L)).willReturn(Optional.of(new Order(1L, new OrderTable(), OrderStatus.COMPLETION.name())));
        //when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderStatusChangeRequest))
                .isInstanceOf(OrderStatusCompleteException.class);
    }

    @DisplayName("주문상태 변경시 주문번호가 없는 경우")
    @Test
    void 주문상태_변경시_주문번호가_없는_경우() {
        //when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderStatusChangeRequest))
                .isInstanceOf(NoSuchElementException.class);
    }
}

package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.common.NotFoundEntityException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusDto;
import kitchenpos.order.dto.CreateOrderDto;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderLineItemDto item;

    private Menu menu;

    @BeforeEach
    void setUp() {
        item = new OrderLineItemDto(null, null, 1L, 1);
        menu = new Menu("name", 0L, null);
    }

    @DisplayName("create order 실패 - orderLineItems 가 비어 있음")
    @Test
    void createFail01() {
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.create(new CreateOrderDto(null, new ArrayList<>())));
    }

    @DisplayName("create order 실패 - orderLineItems에 menu id가 중복인 데이터가 있음")
    @Test
    void createFail02() {
        // given
        CreateOrderDto orderDto = new CreateOrderDto(null, Stream.generate(() -> item)
                                                                 .limit(5)
                                                                 .collect(toList()));

        given(menuRepository.findById(any())).willReturn(Optional.of(menu));
        given(menuRepository.countByIdIn(any())).willReturn(0);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderDto));
    }

    @DisplayName("create order 실패 - order table id를 찾을 수 없음")
    @Test
    void createFail03() {
        // given
        CreateOrderDto orderDto = new CreateOrderDto(1L, Collections.singletonList(item));

        given(menuRepository.findById(any())).willReturn(Optional.of(menu));
        given(menuRepository.countByIdIn(any())).willReturn(1);
        given(orderTableRepository.findById(orderDto.getOrderTableId())).willReturn(Optional.empty());

        // when
        assertThatExceptionOfType(NotFoundEntityException.class).isThrownBy(() -> orderService.create(orderDto));
    }

    @DisplayName("create order 실패 - order table의 상태가 empty")
    @Test
    void createFail04() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.empty();

        CreateOrderDto orderDto = new CreateOrderDto(1L, Collections.singletonList(item));

        given(menuRepository.findById(any())).willReturn(Optional.of(menu));
        given(menuRepository.countByIdIn(any())).willReturn(1);
        given(orderTableRepository.findById(orderDto.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderDto));
    }

    @DisplayName("create order 성공")
    @Test
    void createSuccess() {
        // given
        CreateOrderDto orderDto = new CreateOrderDto(1L, Lists.newArrayList(item));

        OrderTable orderTable = new OrderTable(1L, null, 0, false);
        Order order = new Order();

        given(menuRepository.findById(any())).willReturn(Optional.of(menu));
        given(menuRepository.countByIdIn(any())).willReturn(1);
        given(orderTableRepository.findById(orderDto.getOrderTableId())).willReturn(Optional.of(orderTable));
        given(orderRepository.save(any())).willReturn(order);

        // when
        orderService.create(orderDto);

        // then
        verify(orderLineItemRepository).save(any());
    }

    @DisplayName("주문 상태 변경 실패 - order id를 찾을 수 없음")
    @Test
    void changeOrderStatusFail01() {
        // given
        given(orderRepository.findById(any())).willReturn(Optional.empty());

        // when
        assertThatExceptionOfType(NotFoundEntityException.class).isThrownBy(
            () -> orderService.changeOrderStatus(1L, new ChangeOrderStatusDto(OrderStatus.COMPLETION.name())));
    }

    @DisplayName("주문 상태 변경 실패 - 완료된 주문은 주문 상태 변경 불가능")
    @Test
    void changeOrderStatusFail02() {
        // given
        Order order = new Order();
        order.changeOrderStatus(OrderStatus.COMPLETION);

        OrderTable orderTable = new OrderTable(false);
        orderTable.addOrder(order);

        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // when
        assertThatIllegalArgumentException().isThrownBy(
            () -> orderService.changeOrderStatus(1L, new ChangeOrderStatusDto(OrderStatus.COMPLETION.name())));
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void changeOrderStatusSuccess() {
        // given
        Order savedOrder = new Order();
        OrderTable orderTable = new OrderTable(false);
        orderTable.addOrder(savedOrder);

        given(orderRepository.findById(any())).willReturn(Optional.of(savedOrder));

        // when
        orderService.changeOrderStatus(1L, new ChangeOrderStatusDto(OrderStatus.MEAL.name()));
    }
}

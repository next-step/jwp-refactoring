package kitchenpos.order.application;

import kitchenpos.application.MenuRepository;
import kitchenpos.application.OrderRepository;
import kitchenpos.application.OrderService;
import kitchenpos.application.OrderTableRepository;
import kitchenpos.common.ErrorCode;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블;
    private Order 주문;
    private MenuGroup 양식;
    private Menu 양식_세트1;
    private Menu 양식_세트2;
    private OrderLineItem 주문_메뉴1;
    private OrderLineItem 주문_메뉴2;
    private List<OrderLineItem> 주문_메뉴_목록;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable(2, false);
        주문 = new Order(주문테이블, OrderStatus.COOKING);

        양식 = new MenuGroup("양식");
        양식_세트1 = new Menu("양식 세트1", new BigDecimal(43000), 양식);
        양식_세트2 = new Menu("양식 세트2", new BigDecimal(50000), 양식);

        ReflectionTestUtils.setField(주문테이블, "id", 1L);
        ReflectionTestUtils.setField(주문, "id", 1L);
        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트1, "id", 1L);
        ReflectionTestUtils.setField(양식_세트2, "id", 2L);

        주문_메뉴1 = new OrderLineItem(주문, 양식_세트1, 1L);
        주문_메뉴2 = new OrderLineItem(주문, 양식_세트2, 1L);

        주문_메뉴_목록 = Arrays.asList(주문_메뉴1, 주문_메뉴2);
        주문.order(주문_메뉴_목록);
    }

    @Test
    void 주문_테이블이_등록되어_있지_않으면_주문할_수_없음() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.create(new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                    OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2))));
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage());
    }

    @Test
    void 주문_메뉴_중_등록되지_않은_메뉴가_있으면_주문할_수_없음() {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(menuRepository.findAllById(request.findAllMenuIds()))
                .willReturn(Arrays.asList(양식_세트1));

        assertThatThrownBy(() -> {
            orderService.create(request);
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage());
    }

    @Test
    void 주문_테이블이_빈_테이블이면_주문할_수_없음() {
        주문테이블.changeEmpty(true, Collections.emptyList());
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(menuRepository.findAllById(request.findAllMenuIds()))
                .willReturn(Arrays.asList(양식_세트1, 양식_세트2));

        assertThatThrownBy(() -> {
            orderService.create(request);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.ORDER_TABLES_CANNOT_BE_EMPTY.getErrorMessage());
    }

    @Test
    void 주문_생성() {
        OrderRequest request = new OrderRequest(주문테이블.getId(), OrderStatus.COOKING,
                OrderLineItemRequest.list(Arrays.asList(주문_메뉴1, 주문_메뉴2)));

        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(menuRepository.findAllById(request.findAllMenuIds()))
                .willReturn(Arrays.asList(양식_세트1, 양식_세트2));
        given(orderRepository.save(any(Order.class))).willReturn(주문);

        OrderResponse orderResponse = orderService.create(request);

        assertThat(orderResponse).satisfies(res -> {
            assertEquals(주문.getId(), res.getId());
            assertEquals(주문테이블.getId(), res.getOrderTableId());
            assertEquals(OrderStatus.COOKING, res.getOrderStatus());
            assertEquals(request.getOrderLineItems().size(), res.getOrderLineItems().size());
        });
    }

    @Test
    void 주문_목록_조회() {
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문));

        List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.stream().map(OrderResponse::getId).collect(toList()))
                        .containsExactly(주문.getId())
        );
    }

    @Test
    void 등록된_주문이_아니면_주문_상태를_변경할_수_없음() {
        given(orderRepository.findById(주문.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), OrderStatus.COOKING);
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ErrorCode.NOT_FOUND_BY_ID.getErrorMessage());
    }

    @Test
    void 이미_완료된_주문이면_주문_상태를_변경할_수_없음() {
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
    }

    @Test
    void 주문_상태_변경() {
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));
        given(orderRepository.save(주문)).willReturn(주문);

        orderService.changeOrderStatus(주문.getId(), OrderStatus.COMPLETION);

        assertEquals(OrderStatus.COMPLETION, 주문.getOrderStatus());
    }
}

package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static kitchenpos.testfixture.OrderTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    @InjectMocks
    OrderService orderService;

    private MenuProduct 싱글세트_싸이버거, 싱글세트_콜라;
    private OrderTable 주문_테이블;
    private OrderTable 빈_테이블;
    private OrderLineItem 주문항목;
    private OrderLineItemRequest 주문항목_요청;
    private OrderMenu 주문_싱글세트;

    @BeforeEach
    void setUp() {
        싱글세트_싸이버거 = createMenuProduct(1L, 1);
        싱글세트_콜라 = createMenuProduct(2L, 1);
        주문_싱글세트 = createOrderMenu(1L, "싱글세트", BigDecimal.valueOf(5000));

        주문_테이블 = createOrderTable(1L, null, 4, false);
        빈_테이블 = createOrderTable(2L, null, 0, true);
        주문항목 = createOrderLineItem(주문_싱글세트, 1);
        주문항목_요청 = createOrderLineItemRequest(1L, "싱글세트", BigDecimal.valueOf(5000), 1);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create_success() {
        // given
        Order 주문 = createOrder(주문_테이블.getId(), Lists.newArrayList(주문항목));
        OrderRequest 주문_요청 = createOrderRequest(주문_테이블.getId(), OrderStatus.valueOf(OrderStatus.COOKING.name()), Lists.newArrayList(주문항목_요청));
        given(orderRepository.save(any())).willReturn(주문);

        // when
        OrderResponse saved = orderService.create(주문_요청);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved.getId()).isEqualTo(주문.getId()),
                () -> assertThat(saved.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @DisplayName("주문 등록에 실패한다. (주문항목이 비어있는 경우)")
    @Test
    void create_fail_empty_orderLineItem() {
        // given
        OrderRequest 주문_요청 = createOrderRequest(주문_테이블.getId(), OrderStatus.valueOf(OrderStatus.COOKING.name()), Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록에 실패한다. (주문 항목의 메뉴 중 존재하지 않는 메뉴가 있는 경우)")
    @Test
    void create_fail_duplicated_menu() {
        // given
        OrderLineItemRequest 미존재메뉴_주문항목_요청 = createOrderLineItemRequest(null, "싱글세트", BigDecimal.valueOf(5000), 1);
        OrderRequest 주문_요청 = createOrderRequest(주문_테이블.getId(), OrderStatus.valueOf(OrderStatus.COOKING.name()), Lists.newArrayList(주문항목_요청, 미존재메뉴_주문항목_요청));
        willThrow(new IllegalArgumentException()).given(orderValidator).validateCreate(주문_요청);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록에 실패한다. (주문 테이블이 존재하지 않는 경우)")
    @Test
    void create_fail_empty_orderTable() {
        // given
        OrderRequest 주문_요청 = createOrderRequest(주문_테이블.getId(), OrderStatus.valueOf(OrderStatus.COOKING.name()), Lists.newArrayList(주문항목_요청));
        willThrow(new NoSuchElementException()).given(orderValidator).validateCreate(주문_요청);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_요청);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주문 등록에 실패한다. (주문 테이블이 빈 테이블인 경우)")
    @Test
    void create_fail_emptyTable() {
        // given
        OrderRequest 주문_요청 = createOrderRequest(빈_테이블.getId(), OrderStatus.valueOf(OrderStatus.COOKING.name()), Lists.newArrayList(주문항목_요청));
        willThrow(new IllegalArgumentException()).given(orderValidator).validateCreate(주문_요청);

        // then
        assertThatThrownBy(() -> {
            orderService.create(주문_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회한다.")
    @Test
    void list() {
        // given
        Order 주문1 = createOrder(주문_테이블.getId(), Lists.newArrayList(주문항목, 주문항목));
        Order 주문2 = createOrder(주문_테이블.getId(), Lists.newArrayList(주문항목, 주문항목));
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문1, 주문2));

        // when
        List<OrderResponse> list = orderService.list();

        // then
        assertThat(list).hasSize(2);
    }

    @DisplayName("주문의 상태를 변경할 수 있다")
    @Test
    void changeOrderStatus_success() {
        // given
        Order 주문 = createOrder(빈_테이블.getId(), Lists.newArrayList(주문항목));
        OrderRequest 주문_계산완료 = createOrderRequest(빈_테이블.getId(), OrderStatus.COMPLETION, Lists.newArrayList(주문항목_요청));
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        // when
        OrderResponse 변경된_주문 = orderService.changeOrderStatus(주문.getId(), 주문_계산완료);

        // then
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }


    @DisplayName("주문의 상태 변경에 실패한다. (존재하지 않는 주문인 경우)")
    @Test
    void changeOrderStatus_fail_emptyOrder() {
        // given
        Order 주문 = createOrder(빈_테이블.getId(), Lists.newArrayList(주문항목));
        OrderRequest 주문_계산완료 = createOrderRequest(빈_테이블.getId(), OrderStatus.COMPLETION, Lists.newArrayList(주문항목_요청));

        // then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 주문_계산완료);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주문의 상태 변경에 실패한다. (이미 계산완료된 주문인 경우)")
    @Test
    void changeOrderStatus_fail_completion() {
        // given
        Order 주문 = createOrder(빈_테이블.getId(), OrderStatus.COMPLETION, Lists.newArrayList(주문항목));
        OrderRequest 주문_계산완료 = createOrderRequest(빈_테이블.getId(), OrderStatus.COMPLETION, Lists.newArrayList(주문항목_요청));
        given(orderRepository.findById(주문.getId())).willReturn(Optional.of(주문));

        // then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 주문_계산완료);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

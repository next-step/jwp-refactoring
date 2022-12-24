package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.domin.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.NoSuchDataException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static kitchenpos.domain.MenuFixture.메뉴;
import static kitchenpos.domain.OrderFixture.주문;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 테스트")
public class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @InjectMocks
    private OrderService orderService;

    private Menu 풀코스;
    private Menu 오일2인세트;

    private Order 주문1;
    private Order 주문2;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 빈테이블;

    private OrderLineItem 풀코스_주문1;
    private OrderLineItem 오일2인세트_주문1;
    private OrderLineItem 오일2인세트_주문2;


    @BeforeEach
    void setup() {

        테이블1 = OrderTableFixture.주문테이블(1L, null, 4, false);
        테이블2 = OrderTableFixture.주문테이블(2L, null, 2, false);
        빈테이블 = OrderTableFixture.빈주문테이블(3L);

        주문1 = 주문(1L, OrderStatus.COOKING.name(), 테이블1.getId());
        주문2 = 주문(2L, OrderStatus.COMPLETION.name(), 테이블2.getId());

        오일2인세트 = 메뉴(1L, "오일2인세트", 34000, null);
        풀코스 = 메뉴(2L, "풀코스", 62000, null);

        풀코스_주문1 = OrderLineItemFixture.주문라인아이템(1L, 주문1, 풀코스.getId(), 1);
        오일2인세트_주문1 = OrderLineItemFixture.주문라인아이템(2L, 주문1, 오일2인세트.getId(), 1);
        오일2인세트_주문2 = OrderLineItemFixture.주문라인아이템(3L, 주문2, 오일2인세트.getId(), 2);
    }

    @DisplayName("주문을 생성한다")
    @Test
    void 주문_생성() {
        // given
        given(orderTableRepository.findById(테이블1.getId())).willReturn(Optional.ofNullable(테이블1));
        given(orderRepository.save(any())).willReturn(주문1);
        given(menuRepository.findById(풀코스_주문1.getMenuId())).willReturn(Optional.ofNullable(풀코스));
        given(menuRepository.findById(오일2인세트_주문1.getMenuId())).willReturn(Optional.ofNullable(풀코스));
        given(orderLineItemRepository.saveAll(any())).willReturn(Arrays.asList(풀코스_주문1, 오일2인세트_주문1));
        given(orderRepository.findById(주문1.getId())).willReturn(Optional.ofNullable(주문1));
        given(orderLineItemRepository.findOrderLineItemsByOrderId(주문1.getId())).willReturn(Arrays.asList(풀코스_주문1, 오일2인세트_주문1));

        // when
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(OrderLineItemRequest.of(풀코스_주문1), OrderLineItemRequest.of(오일2인세트_주문1));
        OrderRequest orderRequest = new OrderRequest(테이블1.getId(), OrderStatus.COOKING.name(), orderLineItemRequests);
        Long orderId = orderService.create(orderRequest);
        OrderResponse orderResponse = orderService.convertOrderResponse(orderId);

        // then
        verify(orderRepository).save(any());
        verify(orderLineItemRepository).saveAll(any());
        assertAll(
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(orderResponse.getOrderLineItems()).hasSize(2),
                () -> assertThat(checkOrderInOrderLineItems(orderResponse, 주문1)).isTrue()
        );
    }

    @DisplayName("전체 주문 목록을 조회한다")
    @Test
    void 전체_주문_목록_조회() {
        // given
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문1, 주문2));
        given(orderLineItemRepository.findOrderLineItemsByOrderId(주문1.getId())).willReturn(Arrays.asList(풀코스_주문1, 오일2인세트_주문1));
        given(orderLineItemRepository.findOrderLineItemsByOrderId(주문2.getId())).willReturn(Arrays.asList(오일2인세트_주문2));
        // when
        List<OrderResponse> orders = orderService.list();

        //then
        assertAll(
                () -> assertThat(orders).hasSize(2),
                () -> assertThat(checkOrderInOrderLineItems(orders.get(0), 주문1)).isTrue(),
                () -> assertThat(checkOrderInOrderLineItems(orders.get(1), 주문2)).isTrue()
        );
    }

    @DisplayName("주문 상태를 갱신한다")
    @Test
    void 주문_상태_갱신() {
        // given
        given(orderRepository.findById(주문1.getId())).willReturn(Optional.ofNullable(주문1));
        given(orderLineItemRepository.findOrderLineItemsByOrderId(주문1.getId())).willReturn(Arrays.asList(오일2인세트_주문1, 풀코스_주문1));

        // when
        OrderRequest orderRequest = new OrderRequest(OrderStatus.MEAL.name());
        OrderResponse orderResponse = orderService.changeOrderStatus(주문1.getId(), orderRequest);

        //then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문라인아이템 정보가 없는 주문을 생성한다")
    @Test
    void 주문라인아이템_정보가_없는_주문_생성() {
        // given
        OrderRequest orderRequest = new OrderRequest(테이블1.getId(), OrderStatus.COOKING.name(), null);

        // when & then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("존재하지 않는 메뉴가 있는 주문을 생성한다")
    @Test
    void 존재하지_않는_메뉴가_포함된_주문_생성() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(OrderLineItemRequest.of(풀코스_주문1), null);
        OrderRequest orderRequest = new OrderRequest(테이블1.getId(), OrderStatus.COOKING.name(), orderLineItemRequests);

        // when & then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("주문테이블 정보가 없는 주문을 생성한다")
    @Test
    void 주문테이블_정보가_없는_주문_생성() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(OrderLineItemRequest.of(풀코스_주문1), null);
        OrderRequest orderRequest = new OrderRequest(테이블1.getId(), OrderStatus.COOKING.name(), orderLineItemRequests);
        given(orderTableRepository.findById(테이블1.getId())).willReturn(Optional.ofNullable(null));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("비어있는 주문테이블에 주문을 생성한다")
    @Test
    void 빈_주문테이블에_주문_생성() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(OrderLineItemRequest.of(풀코스_주문1), null);
        OrderRequest orderRequest = new OrderRequest(빈테이블.getId(), OrderStatus.COOKING.name(), orderLineItemRequests);
        given(orderTableRepository.findById(빈테이블.getId())).willReturn(Optional.ofNullable(빈테이블));

        // when & then
        assertThatThrownBy(
                () -> orderService.create(orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문의 주문상태를 갱신한다")
    @Test
    void 존재하지_않는_주문의_주문상태_갱신() {
        // given
        given(orderRepository.findById(주문1.getId())).willReturn(Optional.ofNullable(null));
        OrderRequest orderRequest = new OrderRequest(OrderStatus.COMPLETION.name());

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문1.getId(), orderRequest)
        ).isInstanceOf(NoSuchDataException.class);
    }

    @DisplayName("완료된 주문의 주문상태를 갱신한다")
    @Test
    void 완료된_주문의_주문상태_갱신() {
        // given
        given(orderRepository.findById(주문2.getId())).willReturn(Optional.ofNullable(주문2));
        OrderRequest orderRequest = new OrderRequest(OrderStatus.COMPLETION.name());

        // when & then
        assertThatThrownBy(
                () -> orderService.changeOrderStatus(주문2.getId(), orderRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private boolean checkOrderInOrderLineItems(OrderResponse orderResponse, Order order) {
        Set<Order> orderSet = orderResponse.getOrderLineItems().stream()
                .map(OrderLineItemResponse::getOrder)
                .collect(Collectors.toSet());

        return new ArrayList<>(orderSet).contains(order);
    }
}

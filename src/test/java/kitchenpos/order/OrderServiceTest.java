package kitchenpos.order;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.TableServiceTest;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.menu.MenuGroupServiceTest.메뉴_그룹_등록;
import static kitchenpos.menu.MenuServiceTest.메뉴_등록;
import static kitchenpos.menu.MenuServiceTest.메뉴_상품_등록;
import static kitchenpos.product.ProductServiceTest.상품_등록;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 관련 기능")
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    private Product 짜장면;
    private Product 탕수육;
    private MenuGroup 중국음식;
    private Menu 짜장면메뉴1;
    private Menu 짜장면메뉴2;
    private Menu 짜장면메뉴3;

    private OrderTable 테이블;

    @BeforeEach
    void setUp() {
        짜장면 = 상품_등록("짜장면", new BigDecimal(5000)).toProduct();
        탕수육 = 상품_등록("탕수육", new BigDecimal(15000)).toProduct();
        중국음식 = 메뉴_그룹_등록("중국음식");
        짜장면메뉴1 = 메뉴_등록(1L, "짜장면탕수육세트", 짜장면.getPrice().add(탕수육.getPrice()).getPrice(), 중국음식, Arrays.asList(메뉴_상품_등록(짜장면, 1L), 메뉴_상품_등록(탕수육, 1L)));
        짜장면메뉴2 = 메뉴_등록(2L, "짜장면", 짜장면.getPrice().getPrice(), 중국음식, Arrays.asList(메뉴_상품_등록(짜장면, 1L)));
        짜장면메뉴3 = 메뉴_등록(3L, "탕수육", 탕수육.getPrice().getPrice(), 중국음식, Arrays.asList(메뉴_상품_등록(탕수육, 1L)));

        테이블 = TableServiceTest.테이블_등록(1L, 6, true);
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 항목이 비어있으면 실패한다.")
    void createWithNoOrder() {
        OrderRequest 주문 = 주문_등록(테이블.getId(), Lists.emptyList());
        주문_등록_실패(주문, "주문 메뉴가 없습니다");
    }

    @Test
    @DisplayName("주문을 등록할 때 메뉴 정보가 DB에 없으면 실패한다.")
    void createWithInvalidMenuId() {
        OrderRequest 주문 = 주문_등록(테이블.getId(), Arrays.asList(주문_항목(짜장면메뉴1.getId(), 1L), 주문_항목(짜장면메뉴2.getId(), 1L)));
        테이블.changeEmpty(false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(테이블));
        given(menuRepository.findById(any())).willReturn(Optional.empty());

        주문_등록_실패(주문, "메뉴 정보가 없습니다");
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블 정보가 DB에 없으면 실패한다.")
    void createWithInvalidOrderTable() {
        OrderRequest 주문 = 주문_등록(테이블.getId(), Arrays.asList(주문_항목(짜장면메뉴1.getId(), 1L), 주문_항목(짜장면메뉴2.getId(), 1L)));
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());
        주문_등록_실패(주문, "주문 테이블 정보가 없습니다");
    }

    @Test
    @DisplayName("주문을 등록할 때 주문 테이블이 비어있으면 실패한다.")
    void createWithEmptyOrderTable() {
        OrderRequest 주문 = 주문_등록(테이블.getId(), Arrays.asList(주문_항목(짜장면메뉴1.getId(), 1L), 주문_항목(짜장면메뉴2.getId(), 1L)));
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블));
        주문_등록_실패(주문, "주문 테이블이 비어있습니다");
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void createOrder() {
        OrderRequest 주문 = 주문_등록(테이블.getId(), Arrays.asList(주문_항목(짜장면메뉴1.getId(), 1L), 주문_항목(짜장면메뉴2.getId(), 1L)));
        테이블.changeEmpty(false);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블));
        Order order = new Order(테이블.getId(), OrderStatus.COOKING);
        given(orderRepository.save(any())).willReturn(order);
        given(menuRepository.findById(짜장면메뉴1.getId())).willReturn(Optional.ofNullable(짜장면메뉴1));
        given(menuRepository.findById(짜장면메뉴2.getId())).willReturn(Optional.ofNullable(짜장면메뉴2));
        OrderResponse 새주문 = orderService.create(주문);
        assertThat(새주문.getOrderLineItems()).hasSize(2);
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, null, 짜장면메뉴1.getId(), 1L), new OrderLineItem(2L, null, 짜장면메뉴2.getId(), 1L));
        Order 주문1 = new Order(1L, 테이블.getId(), OrderStatus.COOKING);
        주문1.order(짜장면메뉴1.getId(), 1L);
        주문1.order(짜장면메뉴2.getId(), 1L);

        Order 주문2 = new Order(2L, 테이블.getId(), OrderStatus.COOKING);
        주문1.order(짜장면메뉴1.getId(), 1L);
        given(orderRepository.findAll()).willReturn(Arrays.asList(주문1, 주문2));

        List<OrderResponse> orders = orderService.list();
        assertThat(orders).hasSize(2);
    }

    @Test
    @DisplayName("주문 상태가 완료이면 상태 변경에 실패한다.")
    void changeOrderStatusOfCompleted() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, null, 짜장면메뉴1.getId(), 1L), new OrderLineItem(2L, null, 짜장면메뉴2.getId(), 1L));
        Order 주문 = new Order(1L, 테이블.getId(), OrderStatus.COMPLETION, orderLineItems);
        //주문.order(orderLineItems);
        주문.setOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(any())).willReturn(Optional.of(주문));
        OrderRequest 주문상태변경 = new OrderRequest(OrderStatus.COMPLETION.name());

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderService.changeOrderStatus(주문.getId(), 주문상태변경);
        }).withMessageContaining("완료된 주문은 상태를 변경할 수 없습니다");
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() {
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, null, 짜장면메뉴1.getId(), 1L), new OrderLineItem(2L, null, 짜장면메뉴2.getId(), 1L));
        Order 주문_조리중 = new Order(1L, 테이블.getId(), OrderStatus.COOKING);
        //주문_조리중.order(orderLineItems);
        OrderRequest 주문_식사중 = new OrderRequest(OrderStatus.MEAL.name());

        given(orderRepository.findById(any())).willReturn(Optional.of(주문_조리중));
        given(orderRepository.save(any())).willReturn(주문_조리중);

        OrderResponse 주문_상태_변경 = orderService.changeOrderStatus(주문_조리중.getId(), 주문_식사중);
        assertThat(주문_상태_변경.getOrderStatus()).isEqualTo(주문_조리중.getOrderStatus().name());
    }

    private void 주문_등록_실패(OrderRequest order, String message) {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            orderService.create(order);
        }).withMessageContaining(message);
    }

    private OrderLineItemRequest 주문_항목(Long menuId, long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    private OrderRequest 주문_등록(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        return new OrderRequest(orderTableId, orderLineItems);
    }
}

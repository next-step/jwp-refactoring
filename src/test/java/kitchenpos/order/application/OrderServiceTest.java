package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuFactory;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupFactory;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductFactory;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderFactory;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductFactory;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    private Menu 후라이드세트;
    private OrderTable 주문테이블;
    private Order 주문;
    private OrderLineItem 후라이드세트주문항목;
    private OrderLineItemRequest 주문항목요청;

    @BeforeEach
    public void setUp() {
        MenuGroup 메뉴분류세트 = MenuGroupFactory.create(1L, "메뉴분류세트");

        Product 후라이드 = ProductFactory.create(1L, "후라이드", BigDecimal.valueOf(15000));
        Product 콜라 = ProductFactory.create(2L, "콜라", BigDecimal.valueOf(1000));

        MenuProduct 후라이드메뉴상품 = MenuProductFactory.create(1L, 후라이드세트, 후라이드, 1L);
        MenuProduct 콜라메뉴상품 = MenuProductFactory.create(2L, 후라이드세트, 콜라, 1L);

        MenuProductRequest 후라이드메뉴상품요청 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 콜라메뉴상품요청 = new MenuProductRequest(콜라.getId(), 1L);

        후라이드세트 = MenuFactory.create(1L, "후라이드세트", BigDecimal.valueOf(16000), 메뉴분류세트, Arrays.asList(후라이드메뉴상품, 콜라메뉴상품));
        주문테이블 = new OrderTable(1L, null, 4, false);
        후라이드세트주문항목 = new OrderLineItem(1L, 주문, 후라이드세트, 1L);
        주문 = OrderFactory.create(1L, 주문테이블, Collections.singletonList(후라이드세트주문항목));
        주문항목요청 = new OrderLineItemRequest(후라이드세트.getId(), 1L);
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        //given
        OrderRequest 주문요청 = new OrderRequest(주문테이블.getId(), null, Collections.singletonList(주문항목요청));
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(menuRepository.findById(후라이드세트.getId())).willReturn(Optional.ofNullable(후라이드세트));
        given(orderRepository.save(any())).willReturn(주문);
        //when
        OrderResponse orderResponse = orderService.create(주문요청);
        //then
        assertAll(
                () -> assertThat(orderResponse.getId()).isNotNull(),
                () -> assertThat(orderResponse.getOrderedTime()).isNotNull(),
                () -> assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(orderResponse.getOrderTableId()).isEqualTo(주문테이블.getId()),
                () -> assertThat(orderResponse.getOrderLineItems().size()).isEqualTo(1)
        );

    }

    @DisplayName("주문 테이블이 존재하지 않으면 에러가 발생한다.")
    @Test
    void createOrderTable() {
        //given
        OrderRequest 주문요청 = new OrderRequest(주문테이블.getId(), null, Collections.singletonList(주문항목요청));
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderService.create(주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성 요청시 주문 항목이 없으면 에러가 발생한다.")
    @Test
    void createOrderLineItems() {
        //given
        OrderRequest 주문요청 = new OrderRequest(주문테이블.getId(), null, Collections.emptyList());
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        //when & then
        assertThatThrownBy(() -> orderService.create(주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목에 등록되지 않은 메뉴가 있으면 에러가 발생한다.")
    @Test
    void createOrderItemCount() {
        //given
        OrderRequest 주문요청 = new OrderRequest(주문테이블.getId(), null, Collections.singletonList(주문항목요청));
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.ofNullable(주문테이블));
        given(menuRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderService.create(주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 테이블이 비어 있으면 에러가 발생한다.")
    @Test
    void createOrderTableEmpty() {
        //given
        OrderTable 빈주문테이블 = new OrderTable(1L, null, 0, true);
        OrderRequest 주문요청 = new OrderRequest(빈주문테이블.getId(), null, Collections.singletonList(주문항목요청));
        given(orderTableRepository.findById(빈주문테이블.getId())).willReturn(Optional.ofNullable(빈주문테이블));
        given(menuRepository.findById(후라이드세트.getId())).willReturn(Optional.ofNullable(후라이드세트));

        //when & then
        assertThatThrownBy(() -> orderService.create(주문요청))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(orderRepository.findAll()).willReturn(Collections.singletonList(주문));
        //when
        List<OrderResponse> list = orderService.list();
        //then
        assertThat(list.size()).isEqualTo(1);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        //given
        OrderStatus 식사중 = OrderStatus.MEAL;
        OrderRequest 주문상태변경요청 = new OrderRequest(null, 식사중, null);
        given(orderRepository.findById(1L)).willReturn(Optional.ofNullable(주문));
        //when
        OrderResponse orderResponse = orderService.changeOrderStatus(1L, 주문상태변경요청);
        //then
        assertThat(orderResponse.getOrderStatus()).isEqualTo(식사중);

    }


    @DisplayName("존재하는 주문만 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatusExist() {
        //given
        OrderStatus 식사중 = OrderStatus.MEAL;
        OrderRequest 주문상태변경요청 = new OrderRequest(null, 식사중, null);
        given(orderRepository.findById(any())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 주문상태변경요청))
                .isInstanceOf(IllegalArgumentException.class);

    }
    @DisplayName("주문 상태가 완료이면 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatusCompletion() {
        //given
        주문.changeStatus(OrderStatus.COMPLETION);
        OrderStatus 식사중 = OrderStatus.MEAL;
        OrderRequest 주문상태변경요청 = new OrderRequest(null, 식사중, null);
        given(orderRepository.findById(1L)).willReturn(Optional.of(주문));
        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, 주문상태변경요청))
                .isInstanceOf(IllegalArgumentException.class);

    }
}

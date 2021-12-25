package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private MenuService menuService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TableService tableService;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void 주문_등록() {
        // given
        Menu 메뉴 = Menu.of("메뉴", 5000, MenuGroup.from("메뉴그룹"));
        
        OrderTable 첫번째_테이블 = OrderTable.of(3, false);
        OrderTable 두번째_테이블 = OrderTable.of(5, false);
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(첫번째_테이블, 두번째_테이블));
        
        Order 주문 = Order.of(첫번째_테이블, OrderStatus.COOKING);
        
        OrderLineItem 주문_메뉴 = OrderLineItem.of(주문, 메뉴, 1L);
        주문.addOrderLineItems(Arrays.asList(주문_메뉴));
        
        given(tableService.findById(nullable(Long.class))).willReturn(첫번째_테이블);
        given(menuService.countByIdIn(anyList())).willReturn((long) 주문.getOrderLineItems().size());
        given(orderRepository.save(any())).willReturn(주문);

        // when
        OrderResponse 등록된_주문 = orderService.create(OrderRequest.from(주문));

        // then
        assertThat(등록된_주문).isEqualTo(OrderResponse.from(주문));
    }
    
    @DisplayName("메뉴없이 주문할 수 없다 - 예외처리")
    @Test
    void 주문_등록_메뉴_필수() {
        // given
        Order 메뉴없는_주문 = Order.of(null, OrderStatus.COOKING);
        OrderTable 테이블 = OrderTable.of(3, false);
        given(tableService.findById(nullable(Long.class))).willReturn(테이블);
        
        // when, then
        assertThatThrownBy(() -> {
            orderService.create(OrderRequest.from(메뉴없는_주문));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주문에 메뉴가 없습니다");
    
    }
    
    @DisplayName("주문 등록시 주문 상태는 조리 상태이다")
    @Test
    void 주문_등록_상태_확인() {
        // given
        OrderTable 첫번째_테이블 = OrderTable.of(3, false);
        OrderTable 두번째_테이블 = OrderTable.of(5, false);
        
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(첫번째_테이블, 두번째_테이블));
        
        Order 주문 = Order.of(첫번째_테이블, OrderStatus.COOKING);
        Menu 메뉴 = Menu.of("햄버거", 5500, MenuGroup.from("메뉴그룹"));
        
        OrderLineItem 주문_메뉴 = OrderLineItem.of(주문, 메뉴, 1L);
        주문.addOrderLineItems(Arrays.asList(주문_메뉴));
        
        given(tableService.findById(nullable(Long.class))).willReturn(첫번째_테이블);
        given(menuService.countByIdIn(anyList())).willReturn((long) 주문.getOrderLineItems().size());
        given(orderRepository.save(any())).willReturn(주문);
    
        // when
        OrderResponse 등록된_주문 = orderService.create(OrderRequest.from(주문));
    
        // then
        assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }
    
    @DisplayName("주문 목록을 조회할 수 있다")
    @Test
    void 주문_목록_조회() {
        // given
        Order 첫번째_주문 = Order.of(null, OrderStatus.COOKING);
        Order 두번째_주문 = Order.of(null, OrderStatus.COOKING);
        
        given(orderRepository.findAll()).willReturn(Arrays.asList(첫번째_주문, 두번째_주문));
    
        // when
        List<OrderResponse> 주문_목록 = orderService.list();
    
        // then
        assertThat(주문_목록).containsExactly(OrderResponse.from(첫번째_주문), OrderResponse.from(두번째_주문));
    }
    
    @DisplayName("주문상태를 변경 할 수 있다")
    @Test
    void 주문_상태_변경() {
        // given
        Order 저장된_주문 = Order.of(null, OrderStatus.MEAL);
        저장된_주문.addOrderLineItems(Arrays.asList(OrderLineItem.of(저장된_주문, Menu.of("메뉴", 3000, null), 2L)));
        
        Order 변경할_주문 = Order.of(null, OrderStatus.COMPLETION);
        변경할_주문.addOrderLineItems(Arrays.asList(OrderLineItem.of(변경할_주문, Menu.of("메뉴", 3000, null), 2L)));
        
        given(orderRepository.findById(nullable(Long.class))).willReturn(Optional.of(저장된_주문));
        given(orderRepository.save(any())).willReturn(변경할_주문);
        
        // when
        OrderResponse 변경후_주문 = orderService.changeOrderStatus(저장된_주문.getId(), OrderRequest.from(변경할_주문));
    
        // then
        assertThat(변경후_주문.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus());
    }
    
    @DisplayName("계산이 완료된 주문은 상태를 변경 할 수 없다 - 예외처리")
    @Test
    void 계산_완료_주문_변경_불가() {
        // given
        Order 저장된_주문 = Order.of(null, OrderStatus.COMPLETION);
        저장된_주문.addOrderLineItems(Arrays.asList(OrderLineItem.of(저장된_주문, Menu.of("메뉴", 3000, null), 2L)));
        given(orderRepository.findById(nullable(Long.class))).willReturn(Optional.of(저장된_주문));
    
        // when, then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(저장된_주문.getId(), OrderRequest.from(저장된_주문));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("계산이 완료된 주문은 상태를 변경 할 수 없습니다");
    }

}

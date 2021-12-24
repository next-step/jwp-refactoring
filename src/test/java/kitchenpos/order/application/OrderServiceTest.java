package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.dao.OrderLineItemRepository;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

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

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void 주문_등록() {
        // given
        Menu 메뉴 = Menu.of("메뉴", new BigDecimal("5000"), MenuGroup.from("메뉴그룹"), new ArrayList<MenuProduct>());
        
        OrderTable 주문_테이블 = OrderTable.of(TableGroup.from(new ArrayList<OrderTable>()), 3, false);
        
        Order 주문 = Order.of(주문_테이블, OrderStatus.COOKING, new ArrayList<OrderLineItem>());
        
        OrderLineItem 주문_메뉴 = OrderLineItem.of(주문, 메뉴, 1L);
        주문.setOrderLineItems(Arrays.asList(주문_메뉴));
        
        given(menuRepository.countByIdIn(anyList())).willReturn((long) 주문.getOrderLineItems().size());
        given(orderTableRepository.findById(주문.getOrderTable().getId())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.save(주문)).willReturn(주문);

        // when
        Order 등록된_주문 = orderService.create(주문);

        // then
        assertThat(등록된_주문).isEqualTo(주문);
    }
    
    @DisplayName("메뉴없이 주문할 수 없다 - 예외처리")
    @Test
    void 주문_등록_메뉴_필수() {
        // given
        Order 메뉴없는_주문 = Order.of(null, OrderStatus.COOKING, new ArrayList<OrderLineItem>());
        
        // when, then
        assertThatThrownBy(() -> {
            orderService.create(메뉴없는_주문);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("주문에 메뉴가 없습니다");
    
    }
    
    @DisplayName("메뉴 주문시 메뉴는 등록된 메뉴여야한다 - 예외처리")
    @Test
    void 주문_등록_등록된_메뉴만() {
        // given
        Menu 미등록_메뉴 = Menu.of("메뉴", new BigDecimal("5000"), MenuGroup.from("메뉴그룹"), new ArrayList<MenuProduct>());
        OrderLineItem 주문_항목 = OrderLineItem.of(null, 미등록_메뉴, 1L);
        Order 미등록_메뉴_주문 = Order.of(null, OrderStatus.COOKING, new ArrayList<OrderLineItem>());
        미등록_메뉴_주문.setOrderLineItems(Arrays.asList(주문_항목));
    
        // when
        when(menuRepository.countByIdIn(anyList())).thenReturn(0L);
        
        // then
        assertThatThrownBy(() -> {
            orderService.create(미등록_메뉴_주문);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록된 메뉴만 주문할 수 있습니다");
    
    }
    
    @DisplayName("등록된 주문 테이블만 주문할 수 있다- 예외처리")
    @Test
    void 주문_등록_주문_테이블_필수() {
        // given
        OrderTable 주문_테이블 = OrderTable.of(null, 3, false);
        Order 등록된_테이블_없이_주문 = Order.of(주문_테이블, OrderStatus.COOKING, new ArrayList<OrderLineItem>());
        Menu 메뉴 = Menu.of("햄버거", new BigDecimal("5500"), MenuGroup.from("메뉴그룹"), new ArrayList<MenuProduct>());
        
        OrderLineItem 주문_메뉴 = OrderLineItem.of(등록된_테이블_없이_주문, 메뉴, 1L);
        등록된_테이블_없이_주문.setOrderLineItems(Arrays.asList(주문_메뉴));
        
        given(menuRepository.countByIdIn(anyList())).willReturn((long) 등록된_테이블_없이_주문.getOrderLineItems().size());
    
        // when
        when(orderTableRepository.findById(nullable(Long.class))).thenReturn(Optional.empty());
        
        // then
        assertThatThrownBy(() -> {
            orderService.create(등록된_테이블_없이_주문);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("등록된 주문 테이블만 주문할 수 없습니다");
    }
    
    @DisplayName("주문 등록시 주문 상태는 조리 상태이다")
    @Test
    void 주문_등록_상태_확인() {
        // given
        TableGroup 단체지정 = TableGroup.from(new ArrayList<OrderTable>());
        OrderTable 주문_테이블 = OrderTable.of(단체지정, 3, false);
        Order 주문 = Order.of(주문_테이블, OrderStatus.COOKING, new ArrayList<OrderLineItem>());
        Menu 메뉴 = Menu.of("햄버거", new BigDecimal("5500"), MenuGroup.from("메뉴그룹"), new ArrayList<MenuProduct>());
        
        OrderLineItem 주문_메뉴 = OrderLineItem.of(주문, 메뉴, 1L);
        주문.setOrderLineItems(Arrays.asList(주문_메뉴));
        
        given(menuRepository.countByIdIn(anyList())).willReturn((long) 주문.getOrderLineItems().size());
        given(orderTableRepository.findById(nullable(Long.class))).willReturn(Optional.of(주문_테이블));
        given(orderRepository.save(주문)).willReturn(주문);
    
        // when
        Order 등록된_주문 = orderService.create(주문);
    
        // then
        assertThat(등록된_주문.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }
    
    @DisplayName("주문 목록을 조회할 수 있다")
    @Test
    void 주문_목록_조회() {
        // given
        Order 첫번째_주문 = Order.of(null, OrderStatus.COOKING, new ArrayList<OrderLineItem>());
        Order 두번째_주문 = Order.of(null, OrderStatus.COOKING, new ArrayList<OrderLineItem>());
        
        given(orderRepository.findAll()).willReturn(Arrays.asList(첫번째_주문, 두번째_주문));
        given(orderLineItemRepository.findAllByOrderId(첫번째_주문.getId())).willReturn(Arrays.asList(OrderLineItem.of(null, null, 1L)));
    
        // when
        List<Order> 주문_목록 = orderService.list();
    
        // then
        assertThat(주문_목록).containsExactly(첫번째_주문, 두번째_주문);
    }
    
    @DisplayName("주문상태를 변경 할 수 있다")
    @Test
    void 주문_상태_변경() {
        // given
        Order 저장된_주문 = Order.of(null, OrderStatus.MEAL, new ArrayList<OrderLineItem>());
        given(orderRepository.findById(nullable(Long.class))).willReturn(Optional.of(저장된_주문));
        
        Order 변경할_주문 = Order.of(null, OrderStatus.COMPLETION, new ArrayList<OrderLineItem>());
        given(orderLineItemRepository.findAllByOrderId(nullable(Long.class))).willReturn(Arrays.asList(OrderLineItem.of(null, null, 1L)));
    
        // when
        Order 변경후_주문 = orderService.changeOrderStatus(저장된_주문.getId(), 변경할_주문);
    
        // then
        assertThat(변경후_주문.getOrderStatus()).isEqualTo(변경할_주문.getOrderStatus());
    }
    
    @DisplayName("계산이 완료된 주문은 상태를 변경 할 수 없다 - 예외처리")
    @Test
    void 계산_완료_주문_변경_불가() {
        // given
        Order 저장된_주문 = Order.of(null, OrderStatus.COMPLETION, new ArrayList<OrderLineItem>());
        given(orderRepository.findById(nullable(Long.class))).willReturn(Optional.of(저장된_주문));
    
        // when, then
        assertThatThrownBy(() -> {
            orderService.changeOrderStatus(저장된_주문.getId(), 저장된_주문);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("계산이 완료된 주문은 상태를 변경 할 수 없습니다");
    }

}

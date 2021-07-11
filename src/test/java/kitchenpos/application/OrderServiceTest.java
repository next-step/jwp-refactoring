package kitchenpos.application;

import kitchenpos.application.order.OrderService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.*;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 관리")
class OrderServiceTest extends DataBaseCleanSupport {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup 착한세트;
    private Menu 후라이드반마리;
    private OrderTable 주문테이블;
    private OrderTable 주문하지않은_주문테이블;
    private OrderLineItemRequest 주문할_후라이드반마리;

    @BeforeEach
    void setUp() {
        착한세트 = menuGroupRepository.save(MenuGroup.of("착한세트"));
        후라이드반마리 = menuRepository.save(Menu.of("후라이드반마리", BigDecimal.valueOf(7000), 착한세트));

        주문테이블 = orderTableRepository.save(OrderTable.of(1, false));
        주문하지않은_주문테이블 = orderTableRepository.save(OrderTable.of(0, true));

        주문할_후라이드반마리 = OrderLineItemRequest.of(후라이드반마리.getId(), 1);
    }

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        //given
        OrderRequest request = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(주문할_후라이드반마리));

        //when
        OrderResponse actual = orderService.create(request);

        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(actual.getOrderLineItems().size()).isEqualTo(1);
        assertThat(actual.getOrderTableId()).isEqualTo(주문테이블.getId());
    }

    @DisplayName("주문은 메뉴를 지정해야한다.")
    @Test
    void createOrderExceptionIfMenuIsNull() {
        //given
        OrderRequest request = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list());

        //when
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문은 메뉴 수량을 지정해야한다.")
    @Test
    void createOrderExceptionIfMenuQuantityIsNull() {
        //TODO: 추가 기능 개발
    }

    @DisplayName("주문은 주문 테이블을 지정해야한다.")
    @Test
    void createOrderExceptionIfOrderTableIsNull() {
        //given
        OrderRequest request = OrderRequest.of(0L, OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(주문할_후라이드반마리));

        //when
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문 불가능 상태일 경우 주문할 수 없다.")
    @Test
    void createOrderExceptionIfTableEmptyIsTrue() {
        //given
        OrderRequest request = OrderRequest.of(주문하지않은_주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(주문할_후라이드반마리));

        //when
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class); //then
    }

    @DisplayName("주문을 모두 조회한다.")
    @Test
    void list() {
        //given
        OrderRequest 주문요청 = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(주문할_후라이드반마리));
        orderService.create(주문요청);

        //when
        List<OrderResponse> actual = orderService.list();

        //then
        assertThat(actual.size()).isNotZero();
    }

    @DisplayName("특정 주문의 상태는 조리, 식사, 계산완료 순서로 진행된다.")
    @Test
    void changeOrderStatus() {
        //given
        OrderRequest 주문전 = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(주문할_후라이드반마리));
        OrderResponse 주문후 = orderService.create(주문전);

        //when
        OrderResponse actual1 = orderService.changeOrderStatus(주문후.getId(), OrderStatus.MEAL.name());
        //then
        assertThat(actual1.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());

        //when
        OrderResponse actual2 = orderService.changeOrderStatus(주문후.getId(), OrderStatus.COMPLETION.name());
        //then
        assertThat(actual2.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문 완료 상태인 경우 변경할 수 없다.")
    @Test
    void changeOrderStatusExceptionIfSameStatusBefore() {
        //given
        OrderRequest 주문전 = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Lists.list(주문할_후라이드반마리));
        OrderResponse 주문후 = orderService.create(주문전);
        OrderResponse 주문완료 = orderService.changeOrderStatus(주문후.getId(), OrderStatus.COMPLETION.name());
        Long id = 주문완료.getId();
        String meal = OrderStatus.MEAL.name();

        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(id, meal))
                .isInstanceOf(IllegalArgumentException.class); //then
    }
}

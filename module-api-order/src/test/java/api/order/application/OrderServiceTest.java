package kitchenpos.application;

import api.order.application.exception.BadMenuIdException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import api.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import kitchenpos.order.presentation.dto.OrderRequest;
import kitchenpos.order.presentation.dto.OrderResponse;
import api.order.application.exception.NotExistOrderTableException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
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

    private OrderTable 주문테이블;
    private OrderLineItemRequest 주문할_후라이드반마리;
    private OrderLineItemRequest 중복_주문할_후라이드반마리;

    @BeforeEach
    void setUp() {
        MenuGroup 착한세트 = menuGroupRepository.save(MenuGroup.of("착한세트"));
        Menu 후라이드반마리 = menuRepository.save(Menu.of("후라이드반마리", BigDecimal.valueOf(7000), 착한세트));
        주문테이블 = orderTableRepository.save(OrderTable.of(1, false));
        주문할_후라이드반마리 = OrderLineItemRequest.of(후라이드반마리.getId(), 1);
        중복_주문할_후라이드반마리 = OrderLineItemRequest.of(후라이드반마리.getId(), 3);
    }

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        //given
        OrderRequest request = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Lists.list(주문할_후라이드반마리));

        //when
        OrderResponse actual = orderService.create(request);

        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(actual.getOrderLineItems().size()).isEqualTo(1);
        assertThat(actual.getOrderTableId()).isEqualTo(주문테이블.getId());
    }

    @Test
    @DisplayName("주문은 중복된 메뉴를 설정할 수 없다.")
    void createOrderException() {
        //given
        OrderRequest request = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Lists.list(주문할_후라이드반마리, 중복_주문할_후라이드반마리));

        //when
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(BadMenuIdException.class); //then
    }

    @DisplayName("주문은 주문 테이블을 지정해야한다.")
    @Test
    void createOrderExceptionIfOrderTableIsNull() {
        //given
        OrderRequest request = OrderRequest.of(0L, OrderStatus.COOKING, Lists.list(주문할_후라이드반마리));

        //when
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(NotExistOrderTableException.class); //then
    }

    @DisplayName("주문을 모두 조회한다.")
    @Test
    void list() {
        //given
        OrderRequest 주문요청 = OrderRequest.of(주문테이블.getId(), OrderStatus.COOKING, Lists.list(주문할_후라이드반마리));
        orderService.create(주문요청);

        //when
        List<OrderResponse> actual = orderService.findOrderResponses();

        //then
        assertThat(actual.size()).isNotZero();
    }
}

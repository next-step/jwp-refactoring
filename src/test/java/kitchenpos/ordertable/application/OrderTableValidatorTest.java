package kitchenpos.ordertable.application;

import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import kitchenpos.common.exception.OrderStatusNotProcessingException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@DisplayName("주문 테이블 벨리데이터 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableValidatorTest {
    @Mock
    private TableGroupService tableGroupService;

    private OrderTableValidator orderTableValidator;

    private TableGroup 테이블_그룹_1번;
    private OrderTable 주문_테이블_1번;
    private OrderTable 주문_테이블_2번;
    private OrderTable 주문_테이블_3번;

    private OrderTable 빈_주문_테이블;
    private Product 짜장면_상품;
    private MenuProduct 짜장면_곱배기;
    private MenuProduct 짜장면_보통;
    private Menu 짜장면;
    private OrderLineItem 짜장면_주문1;
    private OrderLineItem 짜장면_주문2;
    private Order 주문;

    private OrderTableRequest 주문_테이블_1번_요청;
    private OrderTableRequest 빈_주문_테이블_요청;


    @BeforeEach
    void setUp() {
        orderTableValidator = new OrderTableValidator(tableGroupService);

        주문_테이블_1번 = new OrderTable(3);
        주문_테이블_2번 = new OrderTable(0);
        주문_테이블_3번 = new OrderTable(0);
        테이블_그룹_1번 = new TableGroup(Lists.newArrayList(주문_테이블_2번, 주문_테이블_3번));


        짜장면_상품 = new Product("짜장면", new BigDecimal(1000));
        짜장면_곱배기 = new MenuProduct(1L, 짜장면, 짜장면_상품.getId(), 2);
        짜장면_보통 = new MenuProduct(1L, 짜장면, 짜장면_상품.getId(), 1);
        짜장면 = new Menu("짜장면", 10000, new MenuGroup(), Lists.newArrayList(짜장면_곱배기, 짜장면_보통));
        짜장면_주문1 = new OrderLineItem(주문, 짜장면.getId(), 10);
        짜장면_주문2 = new OrderLineItem(주문, 짜장면.getId(), 3);

        주문 = new Order(주문_테이블_1번, Lists.newArrayList(짜장면_주문1, 짜장면_주문2));
        주문테이블에_주문_추가(주문_테이블_1번, 주문);
        주문_상태를_변경한다(주문, OrderStatus.COOKING);
    }

    @DisplayName("주문 상태는 cooking이나 meal이 아니어야 한다. ")
    @Test
    void changeEmptyTableOrderStatusExceptionTest() {
        assertThatThrownBy(() -> {
            빈_주문테이블로_바꿀수있는지_확인한다(주문_테이블_1번);

            // then
        }).isInstanceOf(OrderStatusNotProcessingException.class);
    }


    @DisplayName("테이블 그룹에 속해있다면 빈 테이블로 변경할 수 없다.")
    @Test
    void changeEmptyTableNotHavingTableGroupExceptionTest() {
        assertThatThrownBy(() -> {
            when(tableGroupService.findByTableGroupId(any())).thenReturn(테이블_그룹_1번);
            // when
            빈_주문테이블로_바꿀수있는지_확인한다(주문_테이블_2번);

            // then
        }).isInstanceOf(NotEmptyOrderTableStatusException.class);
    }

    private void 빈_주문테이블로_바꿀수있는지_확인한다(OrderTable orderTable) {
        orderTableValidator.validateChangeableEmpty(orderTable);
    }

    public static void 주문테이블에_주문_추가(OrderTable orderTable, Order order) {
        orderTable.addOrder(order);
    }

    private void 주문_상태를_변경한다(Order order, OrderStatus orderStatus) {
        order.changeOrderStatus(orderStatus);
    }
}
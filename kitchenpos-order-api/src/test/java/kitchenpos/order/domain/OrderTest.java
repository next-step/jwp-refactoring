package kitchenpos.order.domain;

/*import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;*/
import kitchenpos.order.exception.OrderExceptionCode;
import kitchenpos.tablegroup.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Disabled
@DisplayName("주문 클래스 테스트")
class OrderTest {

    /*private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 에이드;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(22000), 양식);
        스파게티 = new Product("스파게티", new BigDecimal(18000));
        에이드 = new Product("에이드", new BigDecimal(4000));
        주문테이블 = new OrderTable(1, false);

        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트, "id", 1L);
        ReflectionTestUtils.setField(스파게티, "id", 1L);
        ReflectionTestUtils.setField(에이드, "id", 2L);
        ReflectionTestUtils.setField(주문테이블, "id", 1L);

        양식_세트.create(Arrays.asList(new MenuProduct(양식_세트, 스파게티, 1L),
                new MenuProduct(양식_세트, 에이드, 2L)));
    }

    @Test
    void 주문_상품_추가() {
        Order 주문 = new Order(주문테이블.getId(), OrderStatus.COOKING);
        OrderLineItem 양식_세트_주문 = new OrderLineItem(주문, 양식_세트.getId(), 1L);

        주문.order(Arrays.asList(양식_세트_주문));

        assertThat(주문.getOrderLineItems()).hasSize(1);
    }

    @Test
    void 기존에_포함되어_있는_주문_상품은_추가되지_않음() {
        Order 주문 = new Order(주문테이블.getId(), OrderStatus.COOKING);
        OrderLineItem 양식_세트_주문 = new OrderLineItem(주문, 양식_세트.getId(), 1L);

        주문.order(Arrays.asList(양식_세트_주문));
        주문.order(Arrays.asList(양식_세트_주문));

        assertThat(주문.getOrderLineItems()).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중이거나_식사중인_주문_테이블은_변경할_수_없음(OrderStatus orderStatus) {
        Order 주문 = new Order(주문테이블.getId(), orderStatus);

        assertThatThrownBy(() -> {
            주문.checkCookingOrEatingMealOrder();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.CANNOT_BE_CHANGED.getMessage());
    }

    @Test
    void 이미_완료된_주문은_변경할_수_없음() {
        Order 주문 = new Order(주문테이블.getId(), OrderStatus.COMPLETION);

        assertThatThrownBy(() -> {
            주문.changeOrderStatus(OrderStatus.MEAL);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.CANNOT_CHANGE_COMPLETION_ORDER.getMessage());
    }*/
}
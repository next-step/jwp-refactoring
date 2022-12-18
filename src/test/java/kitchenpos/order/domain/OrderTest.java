package kitchenpos.order.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {
    private MenuGroup 양식;
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

        양식_세트.create(Arrays.asList(new MenuProduct(양식_세트, 스파게티, 1L),
                new MenuProduct(양식_세트, 에이드, 2L)));
    }

    @Test
    void 주문_테이블이_존재하지_않으면_주문을_생성할_수_없음() {
        assertThatThrownBy(() -> {
            new Order(null, OrderStatus.COOKING);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_ORDER.getErrorMessage());
    }

    @Test
    void 주문_테이블이_비어있으면_주문을_생성할_수_없음() {
        OrderTable 빈_주문_테이블 = new OrderTable(0, true);

        assertThatThrownBy(() -> {
            new Order(빈_주문_테이블, OrderStatus.COOKING);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.ORDER_TABLES_CANNOT_BE_EMPTY.getErrorMessage());
    }

    @Test
    void 주문_상품_추가() {
        Order 주문 = new Order(주문테이블, OrderStatus.COOKING);
        OrderLineItem 양식_세트_주문 = new OrderLineItem(주문, 양식_세트, 1L);

        주문.order(Arrays.asList(양식_세트_주문));

        assertThat(주문.getOrderLineItems()).hasSize(1);
    }

    @Test
    void 기존에_포함되어_있는_주문_상품은_추가되지_않음() {
        Order 주문 = new Order(주문테이블, OrderStatus.COOKING);
        OrderLineItem 양식_세트_주문 = new OrderLineItem(주문, 양식_세트, 1L);

        주문.order(Arrays.asList(양식_세트_주문));
        주문.order(Arrays.asList(양식_세트_주문));

        assertThat(주문.getOrderLineItems()).hasSize(1);
    }

    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중이거나_식사중인_주문_테이블은_변경할_수_없음(OrderStatus orderStatus) {
        Order 주문 = new Order(주문테이블, orderStatus);

        assertThatThrownBy(() -> {
            주문.checkForChangingOrderTable();
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
    }

    @Test
    void 이미_완료된_주문은_변경할_수_없음() {
        Order 주문 = new Order(주문테이블, OrderStatus.COMPLETION);

        assertThatThrownBy(() -> {
            주문.changeOrderStatus(OrderStatus.MEAL);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
    }
}

package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {
    private MenuGroup 뼈치킨;
    private Menu 뿌링클_세트;
    private Product 뿌링클;
    private Product 치즈볼;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        뼈치킨 = new MenuGroup("뼈치킨");
        뿌링클_세트 = new Menu("뼈치킨 세트", BigDecimal.valueOf(22000), 뼈치킨);
        뿌링클 = new Product("뿌링클", BigDecimal.valueOf(18000));
        치즈볼 = new Product("치즈볼", BigDecimal.valueOf(4000));
        주문테이블 = new OrderTable(1, false);

        뿌링클_세트.create(Arrays.asList(new MenuProduct(뿌링클_세트, 뿌링클, 1L),
                new MenuProduct(뿌링클_세트, 치즈볼, 2L)));
    }

    @DisplayName("등록되지 않은 주문 테이블로 주문을 생성할 수 없다.")
    @Test
    void 등록되지않은_주문_테이블_주문_생성() {
        assertThatThrownBy(() -> new Order(null, OrderStatus.COOKING)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 주문 테이블로 주문을 생성할 수 없다.")
    @Test
    void 빈_주문_테이블_주문_생성() {
        OrderTable 빈_주문_테이블 = new OrderTable(0, true);

        assertThatThrownBy(() -> new Order(빈_주문_테이블, OrderStatus.COOKING)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상품을 추가한다.")
    @Test
    void 주문_상품_추가() {
        Order 주문 = new Order(주문테이블, OrderStatus.COOKING);
        OrderLineItem 뿌링클_세트_주문 = new OrderLineItem(주문, 뿌링클_세트, 1L);

        주문.order(Arrays.asList(뿌링클_세트_주문));

        assertThat(주문.getOrderLineItems().getOrderLineItems()).hasSize(1);
    }

    @DisplayName("이미 포함된 주문 상품은 추가되지 않는다.")
    @Test
    void 기주문한_주문_상품_추가() {
        Order 주문 = new Order(주문테이블, OrderStatus.COOKING);
        OrderLineItem 뿌링클_세트_주문 = new OrderLineItem(주문, 뿌링클_세트, 1L);

        주문.order(Arrays.asList(뿌링클_세트_주문));
        주문.order(Arrays.asList(뿌링클_세트_주문));

        assertThat(주문.getOrderLineItems().getOrderLineItems()).hasSize(1);
    }

    @DisplayName("조리중, 식사중인 주문인지 확인한다.")
    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중_식사중_주문_확인(OrderStatus orderStatus) {
        Order 주문 = new Order(주문테이블, orderStatus);

        assertThatThrownBy(() -> 주문.checkOngoingOrderTable()).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("완료된 주문인지 확인한다.")
    @Test
    void 완료_주문_확인() {
        Order 주문 = new Order(주문테이블, OrderStatus.COMPLETION);

        assertThatThrownBy(() -> 주문.changeOrderStatus(OrderStatus.MEAL)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 추가시 주문 아이템이 빈값인지 확인한다.")
    @Test
    void 주문_추가_확인() {
        Order 주문 = new Order(주문테이블, OrderStatus.COMPLETION);

        assertThatThrownBy(() -> 주문.order(new ArrayList<>())).isInstanceOf(IllegalArgumentException.class);
    }
}

package kitchenpos.order.domain;

import static kitchenpos.menu.fixture.MenuFixture.*;
import static kitchenpos.order.fixture.OrderFixture.*;
import static kitchenpos.table.fixture.TableFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Product product;
    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        //given
        product = ProductFixture.상품_생성(1L, "봉골레파스타", 13000);
        menuGroup = 메뉴그룹_생성(1L, "파스타메뉴");
        menu = 메뉴_생성("봉골레파스타세트", product.getPrice(), 1L, Arrays.asList(메뉴상품_생성(1L, 1L, 1L)));
        orderTable = 테이블_생성(1L, 2, false);
        orderLineItems = Arrays.asList(주문항목_생성(0L, menu.getId(), 1L));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        //when
        Order order = 주문_생성(1L, orderTable.getId(), OrderStatus.COOKING, orderLineItems);

        //then
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderTableId()).isEqualTo(orderTable.getId());
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = 주문_생성(1L, orderTable.getId(), OrderStatus.COOKING, orderLineItems);

        //when
        order.changeOrderStatus(OrderStatus.MEAL);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("완료된 주문의 주문상태 수정에 실패한다.")
    @Test
    void changeOrderStatus_invalidOrderStatus() {
        //given
        Order order = 주문_생성(1L, orderTable.getId(), OrderStatus.COMPLETION, orderLineItems);

        //when & then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료된 주문의 상태는 수정할 수 없습니다.");
    }

}

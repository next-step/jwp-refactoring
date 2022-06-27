package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.EmptyOrderLineItemsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class OrderLineItemsTest {

    private MenuGroup 메뉴그룹;
    private Menu 메뉴;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroupFixtureFactory.create("메뉴그룹");
        메뉴 = MenuFixtureFactory.create("메뉴", BigDecimal.valueOf(30_000), 메뉴그룹.getId());
        주문테이블 = OrderTableFixtureFactory.createWithGuest(false, 2);
    }

    @DisplayName("OrderLineItems 는 OrderLineItem 리스트로 생성한다.")
    @Test
    void creat01() {
        // given
        List<OrderLineItem> orderLineItems = Lists.newArrayList();
        orderLineItems.add(OrderLineItem.of(메뉴.getId(), 1L));
        // when & then
        assertThatNoException().isThrownBy(() -> OrderLineItems.from(orderLineItems));
    }

    @DisplayName("OrderLineItems 생성 시, OrderLineItem 리스트가 존재하지 않으면 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    void creat2(List<OrderLineItem> orderLineItems) {
        // when & then
        assertThatExceptionOfType(EmptyOrderLineItemsException.class)
                .isThrownBy(() -> OrderLineItems.from(orderLineItems));
    }
}
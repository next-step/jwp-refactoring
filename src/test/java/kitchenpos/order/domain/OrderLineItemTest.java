package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.exception.OrderLineItemExceptionCode;
import kitchenpos.tablegroup.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 상품 클래스 테스트")
class OrderLineItemTest {

    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 에이드;
    private OrderTable 주문테이블;
    private Order 주문;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(26000), 양식);

        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트, "id", 1L);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -5, -10, -15 })
    void 수량이_음수이면_주문_상품을_생성할_수_없음(long quantity) {
        assertThatThrownBy(() -> {
            new OrderLineItem(주문, 양식_세트.getId(), quantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderLineItemExceptionCode.INVALID_QUANTITY.getMessage());
    }
}
package kitchenpos.order.domain;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("주문 단위 테스트")
public class OrderTest {
    private MenuGroup 중식;
    private Menu 중식_세트;
    private Product 짜장면;
    private Product 탕수육;
    private OrderTable 주문테이블_1;

    @BeforeEach
    void setUp() {
        중식 = new MenuGroup("중식");
        중식_세트 = new Menu("중식_세트", 22000, 중식);
        짜장면 = new Product("짜장면", 7000);
        탕수육 = new Product("탕수육", 15000);
        주문테이블_1 = new OrderTable(1, false);

        중식_세트.create(Arrays.asList(
                new MenuProduct(중식_세트, 짜장면, 2L),
                new MenuProduct(중식_세트, 탕수육, 1L))
        );
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문을 생성할 수 없습니다.")
    @Test
    void 주문_테이블이_존재하지_않으면_주문을_생성할_수_없습니다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(null, OrderStatus.COOKING))
                .withMessage(ErrorMessage.ORDER_REQUIRED_ORDER_TABLE.getMessage());
    }

    @DisplayName("주문 테이블이 비어있으면 주문을 생성할 수 없습니다.")
    @Test
    void 주문_테이블이_비어있으면_주문을_생성할_수_없습니다() {
        OrderTable 빈_주문_테이블 = new OrderTable(0, true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Order(빈_주문_테이블, OrderStatus.COOKING))
                .withMessage(ErrorMessage.ORDER_TABLE_CANNOT_BE_EMPTY.getMessage());
    }

    @DisplayName("주문 상품 생성에 성공한다.")
    @Test
    void 주문_상품_생성에_성공한다() {
        Order 주문 = new Order(주문테이블_1, OrderStatus.COOKING);

        주문.order(Arrays.asList(new OrderLineItem(주문, 중식_세트, 1L)));

        assertThat(주문.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("기존에 포함되어 있는 주문 상품은 추가되지 않습니다.")
    @Test
    void 기존에_포함되어_있는_주문_상품은_추가되지_않음() {
        Order 주문 = new Order(주문테이블_1, OrderStatus.COOKING);
        OrderLineItem 중식_세트_주문_수량 = new OrderLineItem(주문, 중식_세트, 1L);

        주문.order(Arrays.asList(중식_세트_주문_수량));
        주문.order(Arrays.asList(중식_세트_주문_수량));

        assertThat(주문.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("조리중이거나 식사중인 주문 테이블은 변경할 수 없습니다.")
    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중이거나_식사중인_주문_테이블은_변경할_수_없습니다(OrderStatus orderStatus) {
        Order 주문 = new Order(주문테이블_1, orderStatus);
        assertThatIllegalArgumentException()
                .isThrownBy(주문::checkCookingOrMeal)
                .withMessage(ErrorMessage.ORDER_CANNOT_BE_CHANGED.getMessage());
    }

    @DisplayName("이미 완료된 주문은 변경할 수 없다.")
    @Test
    void 이미_완료된_주문은_변경할_수_없다() {
        Order 주문 = new Order(주문테이블_1, OrderStatus.COMPLETION);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문.updateOrderStatus(OrderStatus.MEAL))
                .withMessage(ErrorMessage.ORDER_CANNOT_CHANGE_COMPLETION_ORDER.getMessage());
    }
}

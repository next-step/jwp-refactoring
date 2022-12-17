package kitchenpos.order;

import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    private final Product 참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
    private final Product 라볶이 = new Product(2L, "라볶이", new Price(new BigDecimal(4500)));
    private final Product 돈까스 = new Product(3L, "돈까스", new Price(new BigDecimal(7000)));

    private final MenuGroup 분식 = new MenuGroup(1L, "분식");

    private final MenuProduct 라볶이세트참치김밥 = new MenuProduct(참치김밥, new Quantity(1));
    private final MenuProduct 라볶이세트라볶이 = new MenuProduct(라볶이, new Quantity(1));
    private final MenuProduct 라볶이세트돈까스 = new MenuProduct(돈까스, new Quantity(1));

    private final MenuProducts 라볶이세트구성 = new MenuProducts(Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));
    private final Menu 라볶이세트 = new Menu(1L, "라볶이세트", new Price(new BigDecimal(14000)), 분식, 라볶이세트구성);

    private final OrderLineItem 주문항목1 = new OrderLineItem(1L, null, 라볶이세트, new Quantity(1));
    private final OrderLineItem 주문항목2 = new OrderLineItem(2L, null, 라볶이세트, new Quantity(1));

    private final OrderTable orderTable = new OrderTable(new NumberOfGuests(10), false);

    @DisplayName("비어있는 주문테이블로 주문 생성 불가 테스트")
    @Test
    void createOrderEmptyOrderTableExceptionTest() {
        //given
        final OrderTable orderTable = new OrderTable(new NumberOfGuests(10), true);
        //when
        //then
        assertThatThrownBy(() -> new Order(orderTable, OrderStatus.COMPLETION,
                new OrderLineItems(Arrays.asList(주문항목1, 주문항목2))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중, 식사중 오류발생 체크 로직 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING","MEAL"})
    void checkCookingOrMealExceptionTest(OrderStatus orderStatus) {
        //given
        final Order order = new Order(orderTable, orderStatus, new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));
        //when
        //then
        assertThatThrownBy(() -> order.checkCookingOrMeal())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리중, 식사중 변경 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING","MEAL"})
    void changeOrderStatusTest(OrderStatus changeOrderStatus) {
        //given
        OrderStatus orderStatus = OrderStatus.COOKING;
        if(changeOrderStatus == OrderStatus.COOKING) {
            orderStatus = OrderStatus.MEAL;
        }
        final Order order = new Order(orderTable, orderStatus, new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));
        //when
        order.change(changeOrderStatus);
        //then
        assertThat(order.getOrderStatus())
                .isEqualTo(changeOrderStatus);
    }

    @DisplayName("식사완료 변경불가 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING","MEAL"})
    void changeOrderStatusCompleteExceptionTest(OrderStatus changeOrderStatus) {
        //given
        final Order order = new Order(orderTable, OrderStatus.COMPLETION, new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));

        //when
        //then
        assertThatThrownBy(() -> order.change(changeOrderStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

package kitchenpos.order.domain;

import static kitchenpos.order.TableFixture.일번테이블;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    Order 식사중;
    Order 조리중;

    @BeforeEach
    void setup() {
        Product 강정치킨 = new Product(1L, "강정치킨", new BigDecimal(17_000));
        MenuProduct 더블강정치킨상품 = new MenuProduct(1L, null, 강정치킨.getId(), 2L);
        MenuGroup 추천메뉴 = new MenuGroup(1L, "추천메뉴");
        Menu 더블강정치킨 = new Menu(1L, "더블강정치킨", new BigDecimal(19_000), 추천메뉴,
            Collections.singletonList(더블강정치킨상품));
        OrderLineItem 주문항목 = new OrderLineItem(1L, null, OrderMenu.from(더블강정치킨), 2L);
        식사중 = new Order(1L, 일번테이블, MEAL, null, Collections.singletonList(주문항목));
        조리중 = new Order(1L, 일번테이블, COOKING, null, Collections.singletonList(주문항목));
    }

    @Test
    @DisplayName("주문 테이블 등록시 단체 지정은 해제 된다")
    void orderTableGroupIdMustBeNullWhenItCreate() {
        //when
        OrderTable orderTable = new OrderTable(1L, 0, false);

        //then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문 테이블 등록시 손님수는 음수면 안된다")
    void numberOfGuestMustNotLessThanZero() {
        //when & then
        assertThatThrownBy(() -> new OrderTable(1L, -1, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님의 수는 0명 이하일 수 없습니다.");
    }

    @Test
    @DisplayName("빈 테이블로 변경")
    void changeToEmpty() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, false);

        //when
        orderTable.changeEmpty(true);

        //then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블의 손님수는 음수가 될수 없다.")
    void noNumberOfGuestLessThanZero() {
        //given
        OrderTable 식사중테이블 = new OrderTable(1L, 0, false);

        //when & then
        assertThatThrownBy(() -> 식사중테이블.changeNumberOfGuest(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님의 수는 0명 이하일 수 없습니다.");
    }

    @Test
    @DisplayName("빈 테이블은 손님 수를 변경할 수 없다.")
    void emptyTableChangeNumberOfGuestException() {
        //given
        OrderTable 빈테이블 = new OrderTable(1L, 0, true);

        //when & then
        assertThatThrownBy(() -> 빈테이블.changeNumberOfGuest(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("빈 테이블의 손님 수를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("손님 수 변경")
    void changeNumberOfGuest() {
        //given
        OrderTable 식사중테이블 = new OrderTable(1L, 0, false);

        //when
        식사중테이블.changeNumberOfGuest(5);

        //then
        assertThat(식사중테이블.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("단체 지정 가능 여부 확인")
    void ableToGroup() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, true);

        //when
        boolean ableToGroup = orderTable.ableToGroup();

        //then
        assertThat(ableToGroup).isTrue();
    }

    @Test
    @DisplayName("단체 지정 해제 성공")
    void ungroup() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, true);

        //when
        orderTable.ungroup();

        //then
        assertThat(orderTable.getTableGroupId()).isNull();
    }
}

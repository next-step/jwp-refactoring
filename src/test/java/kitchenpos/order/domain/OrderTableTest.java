package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
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
        OrderLineItem 주문항목 = new OrderLineItem(1L, null, 더블강정치킨.convertToOrderMenu(), 2L);
        식사중 = new Order(1L, 일번테이블, MEAL, null, Collections.singletonList(주문항목));
        조리중 = new Order(1L, 일번테이블, COOKING, null, Collections.singletonList(주문항목));
    }

    @Test
    @DisplayName("주문 테이블 등록시 단체 지정은 해제 된다")
    void orderTableGroupIdMustBeNullWhenItCreate() {
        //when
        OrderTable orderTable = new OrderTable(1L, 0, false, Collections.singletonList(식사중));

        //then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문 테이블 등록시 손님수는 음수면 안된다")
    void numberOfGuestMustNotLessThanZero() {
        //when & then
        assertThatThrownBy(() -> new OrderTable(1L, -1, false, Collections.singletonList(식사중)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님의 수는 0명 이하일 수 없습니다.");
    }

    @Test
    @DisplayName("빈 테이블로 변경")
    void changeToEmpty() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, false, Collections.emptyList());

        //when
        orderTable.changeEmpty(true);

        //then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("조리중이거나 식사중일떄는 테이블을 빈 상태로 변경 불가능")
    void cantEmptyWhenCookingOrMeal() {
        //given
        OrderTable 식사중테이블 = new OrderTable(1L, 0, false, Collections.singletonList(식사중));

        //when & then
        assertThatThrownBy(() -> 식사중테이블.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");

        //given
        OrderTable 조리중테이블 = new OrderTable(1L, 0, false, Collections.singletonList(조리중));

        //when & then
        assertThatThrownBy(() -> 조리중테이블.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중 이거나 식사중에는 테이블을 비울 수 없습니다.");
    }

    @Test
    @DisplayName("단체 지정된 테이블은 빈 상태로 변경 불가능")
    void cantEmptyWhenHasGroup() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, false, Collections.emptyList());
        orderTable.setTableGroupId(1L);

        //when & then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정된 테이블은 비울 수 없습니다.");
    }

    @Test
    @DisplayName("조리중 식사중 여부 확인")
    void checkIsOnCookingOrMeal() {
        //given
        OrderTable 조리중테이블 = new OrderTable(1L, 0, false, Collections.singletonList(조리중));
        OrderTable 식사중테이블 = new OrderTable(1L, 0, false, Collections.singletonList(식사중));
        OrderTable 빈테이블 = new OrderTable(1L, 0, true, Collections.emptyList());

        //when
        boolean 조리중여부 = 조리중테이블.onCookingOrMeal();
        boolean 식사중여부 = 식사중테이블.onCookingOrMeal();
        boolean 식사중조리중아닌여부 = 빈테이블.onCookingOrMeal();

        //then
        assertThat(조리중여부).isTrue();
        assertThat(식사중여부).isTrue();
        assertThat(식사중조리중아닌여부).isFalse();
    }

    @Test
    @DisplayName("테이블의 손님수는 음수가 될수 없다.")
    void noNumberOfGuestLessThanZero() {
        //given
        OrderTable 식사중테이블 = new OrderTable(1L, 0, false, Collections.singletonList(식사중));

        //when & then
        assertThatThrownBy(() -> 식사중테이블.changeNumberOfGuest(-1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("손님의 수는 0명 이하일 수 없습니다.");
    }

    @Test
    @DisplayName("빈 테이블은 손님 수를 변경할 수 없다.")
    void emptyTableChangeNumberOfGuestException() {
        //given
        OrderTable 빈테이블 = new OrderTable(1L, 0, true, Collections.emptyList());

        //when & then
        assertThatThrownBy(() -> 빈테이블.changeNumberOfGuest(2))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("빈 테이블의 손님 수를 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("손님 수 변경")
    void changeNumberOfGuest() {
        //given
        OrderTable 식사중테이블 = new OrderTable(1L, 0, false, Collections.singletonList(식사중));

        //when
        식사중테이블.changeNumberOfGuest(5);

        //then
        assertThat(식사중테이블.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("단체 지정 가능 여부 확인")
    void ableToGroup() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, true, Collections.singletonList(식사중));

        //when
        boolean ableToGroup = orderTable.ableToGroup();

        //then
        assertThat(ableToGroup).isTrue();
    }

    @Test
    @DisplayName("단체 지정 해제 성공")
    void ungroup() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, true, Collections.emptyList());

        //when
        orderTable.ungroup();

        //then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("단체 지정 해제 실패")
    void ungroupFail() {
        //given
        OrderTable orderTable = new OrderTable(1L, 0, true, Arrays.asList(식사중, 조리중));

        //when & then
        Assertions.assertThatThrownBy(() -> orderTable.ungroup())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("조리중이거나 식사중에는 단체 지정해제할 수 없습니다.");
    }
}

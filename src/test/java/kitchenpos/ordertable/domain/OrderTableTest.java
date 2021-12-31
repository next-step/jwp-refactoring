package kitchenpos.ordertable.domain;

import kitchenpos.common.exception.NegativeNumberOfGuestsException;
import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuId;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.product.domain.Product;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("주문 테이블 도메인 테스트")
class OrderTableTest {
    private TableGroup 테이블_그룹_1번;
    private OrderTable 주문_테이블_1번;
    private OrderTable 주문_테이블_2번;
    private OrderTable 주문_테이블_3번;
    private OrderTable 빈_주문_테이블;
    private Order 주문;
    private Menu 짜장면;
    private MenuId 짜장면_ID;
    private MenuProduct 짜장면_하나;
    private MenuProduct 짜장면_두개;
    private OrderLineItem 짜장면_주문1;
    private OrderLineItem 짜장면_주문2;
    private MenuProduct 짜장면_곱배기;
    private MenuProduct 짜장면_보통;
    private Product 짜장면_상품;

    @BeforeEach
    void setUp() {
        주문_테이블_1번 = new OrderTable(3);
        주문_테이블_2번 = new OrderTable(0);
        주문_테이블_3번 = new OrderTable(0);
        테이블_그룹_1번 = new TableGroup(Lists.newArrayList(주문_테이블_2번, 주문_테이블_3번));

        짜장면_상품 = new Product("짜장면", new BigDecimal(1000));
        짜장면_하나 = new MenuProduct(1L, new Menu(), 짜장면_상품.getId(), 1);
        짜장면_두개 = new MenuProduct(2L, new Menu(), 짜장면_상품.getId(), 2);
        짜장면 = new Menu("짜장면", 10000, new MenuGroup(), Lists.newArrayList(짜장면_하나, 짜장면_두개));
        짜장면_주문1 = new OrderLineItem(주문, 짜장면.getId(), 10);
        짜장면_주문2 = new OrderLineItem(주문, 짜장면.getId(), 3);

        주문 = new Order(주문_테이블_1번, Lists.newArrayList(짜장면_주문1, 짜장면_주문2));
        주문_테이블_1번.addOrder(주문);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTableTest() {
        assertThat(주문_테이블_1번.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void changeEmptyTableTest() {
        // when
        주문_테이블_1번.changeEmpty();

        // then
        assertAll(
                () -> assertThat(주문_테이블_1번.isEmpty()).isTrue(),
                () -> assertThat(주문_테이블_1번.getOrders().size()).isZero()
        );
    }

    @DisplayName("테이블 그룹에 속해있다면 빈 테이블로 변경할 수 없다.")
    @Test
    void changeEmptyTableNotHavingTableGroupExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            주문_테이블_2번 = new OrderTable(테이블_그룹_1번, 3);
            주문_테이블_2번.changeEmpty();

            // then
        }).isInstanceOf(NotEmptyOrderTableStatusException.class);
    }

    @DisplayName("테이블 게스트 숫자는 0 미만으로 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsNegativeNumberExceptionTest() {
        assertThatThrownBy(() -> {
            // when
            주문_테이블_2번.changeNumberOfGuests(-1);

            // then
        }).isInstanceOf(NegativeNumberOfGuestsException.class);
    }
}
package kitchenpos.tablegroup.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.Empty;
import kitchenpos.common.GuestCount;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체지정관련 단위테스트")
public class TableGroupTest {
    private OrderLineItems 주문상품;
    private OrderTable 빈_테이블_A;
    private OrderTable 빈_테이블_B;
    private OrderTable 채워진_테이블;

    @BeforeEach
    void setUp() {
        MenuGroup 중식 = MenuGroup.of("중식");
        Product 짜장면 = Product.of("짜장면", BigDecimal.valueOf(6000));
        Product 군만두 = Product.of("군만두", BigDecimal.valueOf(2000));
        MenuProduct 짜장면_1개 = MenuProduct.of(짜장면, 1);
        MenuProduct 군만두_2개 = MenuProduct.of(군만두, 2);
        Menu 짜장세트 = Menu.of("짜장세트", BigDecimal.valueOf(6000), 중식, Arrays.asList(짜장면_1개, 군만두_2개));
        주문상품 = OrderLineItems.of(Arrays.asList(OrderLineItem.of(짜장세트, 1)));
        채워진_테이블 = OrderTable.of(2, false);
        빈_테이블_A = OrderTable.of(2, true);
        빈_테이블_B = OrderTable.of(2, true);
    }

    @DisplayName("단체지정을 할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTables 주문_테이블_목록 = OrderTables.of(Arrays.asList(빈_테이블_A, 빈_테이블_B));
        // when
        TableGroup 단체지정 = TableGroup.of(주문_테이블_목록);
        assertAll(
                ()->assertThat(단체지정.getOrderTables().getSize()).isEqualTo(2),
                ()->assertThat(단체지정.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("채워진 테이블은 단체지정시 예외가 발생한다.")
    @Test
    void createTableGroup_when_not_empty_exception() {
        // given
        OrderTables 주문_테이블_목록 = OrderTables.of(Arrays.asList(빈_테이블_A, 채워진_테이블));
        // when - then
        assertThatThrownBy(() ->TableGroup.of(주문_테이블_목록))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_IS_NOT_ALL_EMPTY);
    }

    @DisplayName("이미 단체지정된 테이블은 단체지정시 예외가 발생한다.")
    @Test
    void createTableGroup_when_already_grouped_exception() {
        // given
        OrderTables 주문_테이블_목록 = OrderTables.of(Arrays.asList(빈_테이블_A, 빈_테이블_B));
        TableGroup.of(주문_테이블_목록);
        // when - then
        assertThatThrownBy(() ->TableGroup.of(주문_테이블_목록))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.CANNOT_TABLE_GROUP_WHEN_ALREADY_GROUPED);
    }
}

package kitchenpos.table.unit;

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
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.menu.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문테이블 관련 단위테스트")
public class OrderTableTest {

    @DisplayName("주문테이블을 생성할 수 있다.")
    @Test
    void createOrderTable() {
        // when
        OrderTable 채워진_테이블 = OrderTable.of(2, false);
        // then
        assertAll(
                ()->assertThat(채워진_테이블.getEmpty()).isEqualTo(Empty.of(false)),
                ()->assertThat(채워진_테이블.getGuestCounts()).isEqualTo(GuestCount.of(2))
        );
    }

    @DisplayName("주문테이블의 손님수를 변경할 수 있다.")
    @Test
    void updateNumberOfGuest() {
        // given
        OrderTable 채워진_테이블 = OrderTable.of(2, false);
        // when
        채워진_테이블.updateNumberOfGuest(GuestCount.of(3));
        // then
        assertThat(채워진_테이블.getGuestCounts()).isEqualTo(GuestCount.of(3));
    }

    @DisplayName("주문테이블을의 비움 상태를 변경할 수 있다.")
    @Test
    void updateEmptyStatus() {
        // given
        OrderTable 채워진_테이블 = OrderTable.of(2, false);
        // when
        채워진_테이블.updateEmptyStatus(Empty.of(true));
        // then
        assertThat(채워진_테이블.getEmpty()).isEqualTo(Empty.of(true));
    }

    @DisplayName("빈테이블의 손님수를 변경할때 예외가 발생한다.")
    @Test
    void updateNumberOfGuest_when_empty_exception() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(2, true);
        // when - then
        assertThatThrownBy(() ->빈_테이블.updateNumberOfGuest(GuestCount.of(3)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.CANNOT_CHANGE_NUMBER_OF_GUESTS_WHEN_TABLE_IS_EMPTY );
    }

    @DisplayName("이미 단체지정된 테이블의 비움상태를 변경할때 예외가 발생한다.")
    @Test
    void updateEmptyStatus_when_grouped_exception() {
        // given
        OrderTable 빈_테이블_A = OrderTable.of(2, true);
        OrderTable 빈_테이블_B = OrderTable.of(2, true);
        TableGroup.of(OrderTables.of(Arrays.asList(빈_테이블_A, 빈_테이블_B)));
        // when - then
        assertThatThrownBy(() ->빈_테이블_A.updateEmptyStatus(Empty.of(false)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorMessage.CANNOT_CHANGE_EMPTINESS_WHEN_TABLE_GROUPED );
    }

}

package kitchenpos.order.domain;

import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.tableGroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("방문한 손님 수 변경")
    @Test
    void updateNumberOfGuests() {
        OrderTable orderTable = OrderTableFixture.생성(7, false);

        orderTable.updateNumberOfGuests(5);

        assertThat(orderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("방문한 손님은 0명 이상이어야 한다.")
    @Test
    void validateNumber() {
        OrderTable orderTable = OrderTableFixture.생성(7, false);

        assertThatThrownBy(
                () -> orderTable.updateNumberOfGuests(-1)
        ).isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("빈테이블로 변경한다.")
    @Test
    void changeEmpty() {
        OrderTable orderTable = OrderTableFixture.생성(7, false);

        orderTable.changeEmpty(true);

        assertThat(orderTable.isEmpty()).isTrue();

    }
}

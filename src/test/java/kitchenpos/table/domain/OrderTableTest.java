package kitchenpos.table.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTableTest {

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createOrderTable() {
        주문테이블_생성();
    }

    @DisplayName("테이블 그룹에 소속된 주문 테이블은 상태를 변경할 수 없다.")
    @Test
    void validateChageEmpty() {
        // given
        OrderTable 생성된_주문테이블 = 단체그룹_주문테이블_생성();

        // when, then
        assertThatThrownBy(() -> {
            생성된_주문테이블.validateChageEmpty(true);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 인원을 0명 이하로 변경할 수 없다.")
    @Test
    void validateChageNumberOfGuests_numberOfGuests() {
        OrderTable 생성된_주문테이블 = 주문테이블_생성();

        // when, then
        assertThatThrownBy(() -> {
            생성된_주문테이블.validateChageNumberOfGuests(-5);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 주문 테이블에서는 인원을 변경할 수 없다.")
    @Test
    void validateChageNumberOfGuests_empty() {
        OrderTable 생성된_주문테이블 = 비어있는_주문테이블_생성();

        // when, then
        assertThatThrownBy(() -> {
            생성된_주문테이블.validateChageNumberOfGuests(10);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    public static TableGroup 단체그룹_생성() {
        return new TableGroup(1L, null);
    }

    public static OrderTable 주문테이블_생성() {
        return new OrderTable(1L, null, 3, true);
    }

    public static OrderTable 주문테이블_생성2() {
        return new OrderTable(2L, null, 3, true);
    }

    public static OrderTable 비어있는_주문테이블_생성() {
        return new OrderTable(1L, null, 3, true);
    }

    public static OrderTable 단체그룹_주문테이블_생성() {
        return new OrderTable(1L, 단체그룹_생성(), 3, false);
    }
}

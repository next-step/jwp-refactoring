package kitchenpos.domain;

import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.domain.TableGroupTestFixture.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 관련 도메인 테스트")
public class OrderTableTest {

    @DisplayName("주문 테이블의 그룹 상태를 해제한다.")
    @Test
    void ungroupOrderTable() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);
        OrderTable 주문테이블B = generateOrderTable(5, true);
        generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));

        // when
        주문테이블B.ungroup();

        // then
        assertAll(
                () -> assertThat(주문테이블B.getTableGroup()).isNull(),
                () -> assertThat(주문테이블A.isNotNullTableGroup()).isTrue(),
                () -> assertThat(주문테이블B.isNotNullTableGroup()).isFalse()
        );
    }

    @DisplayName("주문 테이블이 비어있으면 참이다.")
    @Test
    void orderTableIsEmpty() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);

        // when
        boolean isEmpty = 주문테이블A.isEmpty();

        // then
        assertThat(isEmpty).isTrue();
    }

    @DisplayName("주문 테이블에 단체를 지정하면, 비어있지 않다.")
    @Test
    void updateTableGroupMakeOrderTableNotEmpty() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);
        OrderTable 주문테이블B = generateOrderTable(5, true);

        // when
        generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));

        // then
        assertAll(
                () -> assertThat(주문테이블A.isEmpty()).isFalse(),
                () -> assertThat(주문테이블B.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블에 단체가 지정되어 있지 않으면, 해당 테이블에서 단체를 조회하면 null이 반환된다.")
    @Test
    void findTableGroupIdByNoTableGroupOrderTable() {
        // given
        OrderTable 주문테이블A = generateOrderTable(4, true);

        // when
        Long tableGroupId = 주문테이블A.findTableGroupId();

        // then
        assertThat(tableGroupId).isNull();
    }
}

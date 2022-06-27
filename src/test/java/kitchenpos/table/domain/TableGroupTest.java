package kitchenpos.table.domain;

import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정 관련 Domain 단위 테스트")
class TableGroupTest {

    @DisplayName("테이블들을 단체 지정 한다.")
    @Test
    void groupTable() {
        //given
        OrderTable emptyTable1 = 테이블_만들기(0, true);
        OrderTable emptyTable2 = 테이블_만들기(0, true);

        //when
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                new OrderTables(Arrays.asList(emptyTable1, emptyTable2)));

        //then
        assertThat(tableGroup.getOrderTables()).isNotNull();

    }

    @DisplayName("테이블이 2개 미만인 경우 단체지정 할 수 없다.")
    @Test
    void groupTable_less_than_two() {
        //given
        OrderTable emptyTable1 = 테이블_만들기(0, true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(
                        () -> new TableGroup(LocalDateTime.now(), new OrderTables(Arrays.asList(emptyTable1))))
                .withMessageContaining("단체 지정에는 최소 2개의 테이블이 필요합니다.");

    }

    @DisplayName("주문 테이블 있는 경우 단체 지정 할 수 없다.")
    @Test
    void groupTable_order_table() {
        //given
        OrderTable emptyTable1 = 테이블_만들기(0, true);
        OrderTable orderTable2 = 테이블_만들기(3, false);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(
                        () -> new TableGroup(LocalDateTime.now(), new OrderTables(Arrays.asList(emptyTable1, orderTable2))))
                .withMessageContaining("주문 테이블 있는 경우 단체 지정 할 수 없습니다");

    }

}

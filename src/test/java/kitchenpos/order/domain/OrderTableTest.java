package kitchenpos.order.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

public class OrderTableTest {

    OrderTable 빈테이블_1 = new OrderTable(1L, null,0, true);
    OrderTable 빈테이블_2 = new OrderTable(2L, null,0, true);
    private TableGroup 등록된_그룹 = new TableGroup(1L, new OrderTables(Arrays.asList(빈테이블_1, 빈테이블_2)));

    private OrderTable 그룹이_지정되지_않은_빈테이블;
    private OrderTable 그룹이_지정된_빈테이블;
    private OrderTable 그룹이_지정되지_않은_비어있지_않은_테이블;

    @BeforeEach
    void setUp() {
        그룹이_지정되지_않은_빈테이블 = new OrderTable(1L, null, 0, true);
        그룹이_지정된_빈테이블 = new OrderTable(2L, 등록된_그룹.getId(), 0, true);
        그룹이_지정되지_않은_비어있지_않은_테이블 = new OrderTable(3L, null, 0, false);
    }

    @Test
    @DisplayName("테이블 그룹 변경시, 테이블 상태가 비어있지 않음으로 변경되어야 한다.")
    void updateTableGroup() {
        //when
        그룹이_지정되지_않은_빈테이블.updateTableGroup(등록된_그룹.getId());

        //then
        assertThat(그룹이_지정되지_않은_빈테이블.getTableGroupId()).isEqualTo(등록된_그룹.getId());
        assertThat(그룹이_지정되지_않은_빈테이블.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 그룹 변경시, 테이블 상태가 비어있지 않음으로 변경되어야 한다.")
    void updateEmpty() {
        //when
        그룹이_지정되지_않은_빈테이블.updateEmpty(false);

        //then
        assertThat(그룹이_지정되지_않은_빈테이블.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 손님 수를 변경할 수 있다.")
    void updateNumberOfGuests() {
        //when
        int origin = 그룹이_지정되지_않은_비어있지_않은_테이블.getNumberOfGuests();
        그룹이_지정되지_않은_비어있지_않은_테이블.updateNumberOfGuests(origin + 3);

        //then
        assertThat(그룹이_지정되지_않은_비어있지_않은_테이블.getNumberOfGuests()).isEqualTo(origin + 3);
    }

    @Test
    @DisplayName("테이블 손님 수 변경시, 테이블 상태가 비어있다면 변경 할 수 없다.")
    void updateNumberOfGuestsWithEmptyTable() {
        //when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            그룹이_지정되지_않은_빈테이블.updateNumberOfGuests(5);
        }).withMessageMatching("빈 테이블의 손님 수는 변경할 수 없습니다.");
    }
}

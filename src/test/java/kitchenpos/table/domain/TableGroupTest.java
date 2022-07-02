package kitchenpos.table.domain;

import static kitchenpos.fixture.TableFixture.테이블_생성;
import static kitchenpos.fixture.TableFixture.테이블그룹_생성;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupTest {

    private OrderTable orderTable_1;
    private OrderTable orderTable_2;
    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        //given
        orderTable_1 = 테이블_생성(1L, 2, true);
        orderTable_2 = 테이블_생성(2L, 4, true);
        tableGroup = 테이블그룹_생성(1L, Arrays.asList(orderTable_1, orderTable_2));
    }

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        //when
        TableGroup newTableGroup = 테이블그룹_생성(1L, Arrays.asList(orderTable_1, orderTable_2));

        //then
        assertThat(newTableGroup).isNotNull();
        assertThat(newTableGroup.getOrderTables().size()).isEqualTo(2);
    }

    @DisplayName("테이블이 1개 이하인 경우, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidOrderTablesSize() {
        //when & then
        assertThatThrownBy(() -> 테이블그룹_생성(1L, Arrays.asList(orderTable_1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테이블이 2개 이상이어야 합니다.");
    }

    @DisplayName("그룹 내에 빈 테이블이 있으면, 테이블 그룹으로 등록할 수 없다.")
    @Test
    void create_invalidEmptyOrderTable() {
        //given
        orderTable_2.changeEmpty(false);

        //when & then
        assertThatThrownBy(() -> 테이블그룹_생성(1L, Arrays.asList(orderTable_1, orderTable_2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비어있지 않은 테이블이 존재합니다.");
    }
}

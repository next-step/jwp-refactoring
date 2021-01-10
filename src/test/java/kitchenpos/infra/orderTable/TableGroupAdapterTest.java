package kitchenpos.infra.orderTable;

import kitchenpos.domain.tableGroup.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupAdapterTest {
    private TableGroupAdapter tableGroupAdapter;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setup() {
        tableGroupAdapter = new TableGroupAdapter(tableGroupRepository);
    }

    @DisplayName("해당 주문 테이블의 단체 지정 여부를 알 수 있다.")
    @Test
    void isGroupedTest() {
        // given
        Long orderTableId = 1L;
        boolean expected = true;
        given(tableGroupRepository.existsByOrderTablesOrderTableId(1L)).willReturn(expected);

        // when
        boolean result = tableGroupAdapter.isGroupedOrderTable(orderTableId);

        // then
        assertThat(result).isEqualTo(expected);
    }
}
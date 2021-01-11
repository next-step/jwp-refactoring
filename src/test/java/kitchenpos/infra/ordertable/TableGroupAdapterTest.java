package kitchenpos.infra.ordertable;

import kitchenpos.domain.ordertable.exceptions.InvalidTryChangeEmptyException;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        given(tableGroupRepository.existsByOrderTablesOrderTableId(1L)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> tableGroupAdapter.canChangeEmptyStatus(orderTableId))
                .isInstanceOf(InvalidTryChangeEmptyException.class)
                .hasMessage("단체 지정된 주문 테이블의 자리 비움 상태를 바꿀 수 없습니다.");
    }
}
package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    TableGroupRepository tableGroupRepository;

    @Mock
    TableService tableService;

    @InjectMocks
    TableGroupService tableGroupService;

    @DisplayName("어떤 테이블의 주문 상태가 '조리'나 '식사'면 단체 지정 해제에 실패한다.")
    @Test
    void 단체_지정_해제_예외() {
        // given
        OrderTable 빈_테이블1 = new OrderTable(null, 0, true);
        OrderTable 빈_테이블2 = new OrderTable(null, 0, true);
        TableGroup 테이블_그룹 = new TableGroup(1L, Arrays.asList(빈_테이블1, 빈_테이블2));
        doReturn(Optional.of(테이블_그룹)).when(tableGroupRepository).findById(any(Long.class));
        doReturn(true).when(tableService).hasCookingOrMeal(any(OrderTable.class));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("조리 혹은 식사 상태인 테이블이 있어서 단체 지정 해제할 수 없습니다. id: " + 테이블_그룹.getId());
    }
}

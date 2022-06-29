package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    TableGroupRepository tableGroupRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    TableGroupService tableGroupService;

    @DisplayName("어떤 테이블의 주문 상태가 '조리'나 '식사'면 단체 지정 해제에 실패한다.")
    @Test
    void 단체_지정_해제_예외() {
        // given
        OrderTable 빈_테이블1 = new OrderTable(1L, null, 0, true);
        OrderTable 빈_테이블2 = new OrderTable(2L, null, 0, true);
        TableGroup 테이블_그룹 = new TableGroup(1L, Arrays.asList(빈_테이블1, 빈_테이블2));
        doReturn(테이블_그룹).when(tableGroupRepository).getById(any(Long.class));
        doReturn(true).when(orderRepository).existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList());

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("조리 혹은 식사 상태의 주문이 등록된 테이블이 있어서 단체 지정을 해제할 수 없습니다.");
    }
}

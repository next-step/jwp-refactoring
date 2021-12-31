package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("주문 상태가 완료가 아닌 테이블이 있으면 단체 지정을 해제할 수 없다.")
    @Test
    void ungroup() {
        TableGroup 단체_테이블_그룹 = mock(TableGroup.class);

        given(tableGroupRepository.findById(anyLong())).willReturn(Optional.of(단체_테이블_그룹));
        when(단체_테이블_그룹.getOrderTableIds()).thenReturn(Arrays.asList(1L, 2L));
        given(orderService.isAllOrderStatusCompleted(anyList())).willReturn(false);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
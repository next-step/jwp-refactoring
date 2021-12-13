package kitchenpos.tablegroup.application;

import kitchenpos.ServiceTest;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("테이블 그룹 서비스 Mock 테스트")
class TableGroupServiceMockTest extends ServiceTest {

    @MockBean
    private OrderTableRepository orderTableRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    @DisplayName("이미 테이블 그룹에 등록된 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void createThrowException() {
        // given
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        OrderTable orderTable1 = new OrderTable(tableGroup, 2, true);
        OrderTable orderTable2 = new OrderTable(tableGroup, 4, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(LocalDateTime.now(), Arrays.asList(1L, 2L));

        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @Test
    @DisplayName("올바르지 않은 주문으로 테이블 그룹에서 테이블을 제거하면 예외를 발생한다.")
    void ungroupThrowException1() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.ungroup(1L));
    }
}

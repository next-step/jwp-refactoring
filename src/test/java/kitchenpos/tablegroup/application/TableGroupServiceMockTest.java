package kitchenpos.tablegroup.application;

import kitchenpos.ServiceTest;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
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
    private OrderTableDao orderTableDao;

    @MockBean
    private OrderDao orderDao;

    @Test
    @DisplayName("이미 테이블 그룹에 등록된 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void createThrowException() {
        // given
        OrderTable orderTable1 = 테이블_생성();
        OrderTable orderTable2 = 테이블_생성();
        orderTable2.setTableGroupId(1L);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("올바르지 않은 주문으로 테이블 그룹에서 테이블을 제거하면 예외를 발생한다.")
    void ungroupThrowException1() {
        // given
        OrderTable orderTable1 = 테이블_생성();
        OrderTable orderTable2 = 테이블_생성();
        orderTable2.setTableGroupId(1L);
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable1, orderTable2));

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    private OrderTable 테이블_생성() {
        return new OrderTable(2, true);
    }
}

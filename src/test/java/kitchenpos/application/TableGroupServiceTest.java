package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블 목록을 단체 지정할 수 있다.")
    @Test
    void 테이블_그룹_등록() {
        // given
        Long tableGroupId = 1L;
        OrderTable orderTable1 = new OrderTable(1L,4, true);
        OrderTable orderTable2 = new OrderTable(2L, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        TableGroup expected = new TableGroup(tableGroupId, orderTables);

        given(orderTableDao.findAllByIdIn(anyList()))
            .willReturn(orderTables);
        given(orderTableDao.save(any()))
            .willReturn(orderTable1, orderTable2);
        given(tableGroupDao.save(expected))
            .willReturn(expected);

        // when
        TableGroup actual = tableGroupService.create(expected);

        // then
        assertThat(expected).isEqualTo(actual);
    }

    @DisplayName("주문 테이블 목록이 빈 경우 단체 지정할 수 없다.")
    @Test
    void 테이블_그룹_생성_예외_테이블_목록_없음() {
        // given
        TableGroup tableGroup = new TableGroup(1L, Lists.emptyList());

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.create(tableGroup)
        );
    }

    @DisplayName("주문 테이블 목록이 1개만 있는 경우 단체 지정할 수 없다.")
    @Test
    void 테이블_그룹_생성_예외_테이블_목록이_1개() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4, true);
        TableGroup tableGroup = new TableGroup(1L, Lists.newArrayList(orderTable1));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.create(tableGroup)
        );
    }

    @DisplayName("빈 테이블이 아닌 테이블이 있을 경우 단체 지정할 수 없다.")
    @Test
    void 테이블_그룹_생성_예외_빈테이블_포함() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 4, true);
        OrderTable notEmptyTable = new OrderTable(2L,3, false);
        List<OrderTable> orderTables = Lists.newArrayList(orderTable1, notEmptyTable);

        TableGroup tableGroup = new TableGroup(1L, orderTables);

        given(orderTableDao.findAllByIdIn(anyList()))
            .willReturn(orderTables);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.create(tableGroup)
        );
    }

    @DisplayName("이미 단체 지정된 테이블인 경우 단체 지정할 수 없다.")
    @Test
    void 테이블_그룹_생성_예외_이미_단체_지정됨() {
        // given
        Long tableGroupId = 5L;
        OrderTable orderTable1 = new OrderTable(1L, 4);
        OrderTable alreadyInGroupTable = new OrderTable(2L, 3, tableGroupId);
        List<OrderTable> orderTables = Lists.newArrayList(orderTable1, alreadyInGroupTable);

        TableGroup tableGroup = new TableGroup(1L, orderTables);

        given(orderTableDao.findAllByIdIn(anyList()))
            .willReturn(orderTables);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.create(tableGroup)
        );
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void 테이블_그룹_해제() {
        // given
        Long tableGroupId = 5L;
        OrderTable orderTable1 = new OrderTable(1L, 4, tableGroupId);
        OrderTable orderTable2 = new OrderTable(2L, 3, tableGroupId);
        List<OrderTable> orderTables = Lists.newArrayList(orderTable1, orderTable2);

        given(orderTableDao.findAllByTableGroupId(any()))
            .willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
            .willReturn(false);

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        verify(orderTableDao, times(orderTables.size()))
            .save(any());
    }

    @DisplayName("주문 테이블 목록 중 '조리' 나 '식사' 상태가 있는 경우 단체 지정을 해제할 수 없다.")
    @Test
    void 테이블_그룹_해제_예외_조리_혹은_식사_상태() {
        // given
        Long tableGroupId = 5L;
        OrderTable orderTable1 = new OrderTable(1L, 4, tableGroupId);
        OrderTable orderTable2 = new OrderTable(2L, 3, tableGroupId);
        List<OrderTable> orderTables = Lists.newArrayList(orderTable1, orderTable2);

        given(orderTableDao.findAllByTableGroupId(any()))
            .willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
            .willReturn(true);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableGroupService.ungroup(tableGroupId)
        );
    }
}

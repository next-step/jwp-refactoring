package kitchenpos.tablegroup;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 지정 관련 기능")
class TableGroupTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Test
    @DisplayName("단체 지정시 테이블의 개수가 2개 미만이면 예외가 발생한다.")
    void createTableGroupFailBecauseOfTableCountLessThanTwo() {
        // given
        final TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable()));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 존재하지 않으면 예외가 발생한다.")
    void createTableGroupFailBecauseOfNotExistTable() {
        // given
        final TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable(1L), new OrderTable(2L)));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(new OrderTable(1L)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블의 상태가 사용중이라면 예외가 발생한다.")
    void createTableGroupFailBecauseOfTableNotEmpty() {
        // given
        final TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable(1L), new OrderTable(2L)));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(new OrderTable(1L, true), new OrderTable(2L, false)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }

    @Test
    @DisplayName("단체 지정 하고자 하는 테이블이 단체 지정이 되어 있다면 예외가 발생한다.")
    void createTableGroupFailBecauseOfAlreadyTableGroup() {
        // given
        final TableGroup tableGroup = new TableGroup(Arrays.asList(new OrderTable(1L), new OrderTable(2L)));
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(new OrderTable(1L, 1L), new OrderTable(2L)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }

    @Test
    @DisplayName("단체 지정을 할 수 있다.")
    void createTableGroup() {
        // given
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(new OrderTable(1L, true), new OrderTable(2L, true)));
        when(tableGroupDao.save(any())).thenReturn(new TableGroup(1L, LocalDateTime.now(), Arrays.asList(new OrderTable(1L), new OrderTable(2L))));

        // when
        final TableGroup savedTableGroup = tableGroupService.create(new TableGroup(Arrays.asList(new OrderTable(1L), new OrderTable(2L))));

        // then
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isOne(),
                () -> assertThat(savedTableGroup.getOrderTables()).extracting("id").contains(1L,2L),
                () -> assertThat(savedTableGroup.getOrderTables()).extracting("tableGroupId").contains(savedTableGroup.getId()),
                () -> assertThat(savedTableGroup.getOrderTables()).extracting("empty").contains(false)
        );
    }

    @Test
    @DisplayName("단체 지정 해제시 테이블의 주문 상태가 조리 또는 식사 변경이 불가능하다.")
    void ungroupFailBecauseOfOrderStatusCookingOrMeal() {
        // given
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).thenReturn(true);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            tableGroupService.ungroup(1L);
        });
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        // given
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).thenReturn(false);
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(new OrderTable(1L,2L), new OrderTable(2L,2L)));

        // when
        tableGroupService.ungroup(2L);
    }
}

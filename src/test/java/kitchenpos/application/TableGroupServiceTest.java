package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    OrderTable 오더테이블_테이블1;
    OrderTable 오더테이블_테이블2;

    TableGroup 테이블그룹_테이블1_테이블2;

    @BeforeEach
    void setUp() {
        오더테이블_테이블1 = new OrderTable();
        오더테이블_테이블1.setId(1L);
        오더테이블_테이블1.setNumberOfGuests(4);
        오더테이블_테이블1.setEmpty(true);

        오더테이블_테이블2 = new OrderTable();
        오더테이블_테이블2.setId(2L);
        오더테이블_테이블2.setNumberOfGuests(4);
        오더테이블_테이블2.setEmpty(true);

        테이블그룹_테이블1_테이블2 = new TableGroup();
        테이블그룹_테이블1_테이블2.setId(1L);
        테이블그룹_테이블1_테이블2.setOrderTables(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));
    }

    @Test
    @DisplayName("단체 테이블을 생성한다.")
    void create() {
        //given
        when(orderTableDao.findAllByIdIn(Arrays.asList(오더테이블_테이블1.getId(), 오더테이블_테이블2.getId()))).thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));
        when(tableGroupDao.save(테이블그룹_테이블1_테이블2)).thenReturn(테이블그룹_테이블1_테이블2);

        //when
        TableGroup createdTableGroup = tableGroupService.create(테이블그룹_테이블1_테이블2);

        //then
        assertThat(createdTableGroup.getId()).isEqualTo(테이블그룹_테이블1_테이블2.getId());
    }

    @Test
    @DisplayName("빈테이블 일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_isEmpty() {
        //given
        테이블그룹_테이블1_테이블2.setOrderTables(Arrays.asList());

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블2)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("1개의 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_table_counting_is_one() {
        //given
        테이블그룹_테이블1_테이블2.setOrderTables(Arrays.asList(오더테이블_테이블1));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("동일 테이블일 경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_same_orderTables() {
        //given
        TableGroup 테이블그룹_테이블1_테이블1 = new TableGroup();
        테이블그룹_테이블1_테이블1.setId(2L);
        테이블그룹_테이블1_테이블1.setOrderTables(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블1));

        when(orderTableDao.findAllByIdIn(Arrays.asList(오더테이블_테이블1.getId(), 오더테이블_테이블1.getId()))).thenReturn(Arrays.asList(오더테이블_테이블1));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈테이블이 아닐경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_orderTable_is_not_empty() {
        //given
        오더테이블_테이블2.setEmpty(false);

        when(orderTableDao.findAllByIdIn(Arrays.asList(오더테이블_테이블1.getId(), 오더테이블_테이블2.getId()))).thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체테이블에 포함되어있는 테이블일경우 그룹테이블 생성요청은 실패한다.")
    void create_with_exception_when_() {
        //given
        오더테이블_테이블2.setTableGroupId(2L);

        when(orderTableDao.findAllByIdIn(Arrays.asList(오더테이블_테이블1.getId(), 오더테이블_테이블2.getId()))).thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));

        //when && then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_테이블1_테이블2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체테이블을 해체한다.")
    void ungroup() {
        //given
        오더테이블_테이블1.setTableGroupId(테이블그룹_테이블1_테이블2.getId());
        오더테이블_테이블2.setTableGroupId(테이블그룹_테이블1_테이블2.getId());

        when(orderTableDao.findAllByTableGroupId(테이블그룹_테이블1_테이블2.getId())).thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        //when
        tableGroupService.ungroup(테이블그룹_테이블1_테이블2.getId());

        //then
        assertAll(() -> {
            assertThat(오더테이블_테이블1.getTableGroupId()).isNull();
            assertThat(오더테이블_테이블2.getTableGroupId()).isNull();
        });
    }

    @Test
    @DisplayName("단체테이블을 해체한다.")
    void ungroup_with_exception_when_orderStatus_is_cooking_or_meal() {
        //given
        오더테이블_테이블1.setTableGroupId(테이블그룹_테이블1_테이블2.getId());
        오더테이블_테이블2.setTableGroupId(테이블그룹_테이블1_테이블2.getId());

        when(orderTableDao.findAllByTableGroupId(테이블그룹_테이블1_테이블2.getId())).thenReturn(Arrays.asList(오더테이블_테이블1, 오더테이블_테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        //when && then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹_테이블1_테이블2.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
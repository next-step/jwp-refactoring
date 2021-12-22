package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.*;

@DisplayName("테이블 그룹 서비스")
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

    private TableGroup 테이블그룹;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable(1L, 3, new TableState(true));
        주문테이블2 = new OrderTable(2L, 3, new TableState(true));
        테이블그룹 = crateTableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(tableGroupDao.save(any())).thenReturn(테이블그룹);

        TableGroup tableGroup = tableGroupService.create(테이블그룹);

        verify(orderTableDao, times(1)).findAllByIdIn(anyList());
        verify(tableGroupDao, times(1)).save(any(TableGroup.class));
        assertThat(tableGroup.getOrderTables()).hasSize(2);
        assertThat(tableGroup).extracting("id", "createdDate")
                .containsExactly(테이블그룹.getId(), 테이블그룹.getCreatedDate());
    }

    @Test
    @DisplayName("주문 테이블 목록이 비어 있는 경우 예외가 발생한다.")
    void validateOrderTableListEmpty() {
        TableGroup 테이블없는그룹 = crateTableGroup(2L, LocalDateTime.now(),
                Collections.emptyList());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(테이블없는그룹));
    }

    @Test
    @DisplayName("주문 테이블 목록이 2개 미만인 경우 예외가 발생한다.")
    void validateOrderTableSize() {
        TableGroup 테이블1개그룹 = crateTableGroup(2L, LocalDateTime.now(),
                Collections.singletonList(주문테이블1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(테이블1개그룹));
    }

    @Test
    @DisplayName("주문 테이블이 등록되어 있지 않은 경우 예외가 발생한다.")
    void validateOrderTable() {
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(Collections.singletonList(주문테이블1));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(테이블그룹));
        verify(orderTableDao, times(1)).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이 아닌(사용중) 경우 예외가 발생한다.")
    void validateOrderTableEmpty() {
        주문테이블1.changeSit();
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(테이블그룹));
        verify(orderTableDao, times(1)).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("다른 테이블 그룹에 등록되어 있는 주문 테이블인 경우 예외가 발생한다.")
    void validateExistTableGroup() {
        주문테이블2.setTableGroup(new TableGroup(LocalDateTime.now()));
        when(orderTableDao.findAllByIdIn(anyList()))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(테이블그룹));
        verify(orderTableDao, times(1)).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void ungroup() {
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        tableGroupService.ungroup(테이블그룹.getId());

        verify(orderTableDao, times(1)).findAllByTableGroupId(anyLong());
        assertThat(주문테이블1.getTableGroup()).isNull();
        assertThat(주문테이블2.getTableGroup()).isNull();
    }

    @Test
    @DisplayName("모든 테이블의 주문 상태가 계산 완료가 아닌 경우 예외가 발생한다.")
    void validateStatusUngroup() {
        when(orderTableDao.findAllByTableGroupId(1L))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .thenReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()));
        verify(orderTableDao, times(1)).findAllByTableGroupId(anyLong());
        verify(orderDao, times(1)).existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
    }

    private TableGroup crateTableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
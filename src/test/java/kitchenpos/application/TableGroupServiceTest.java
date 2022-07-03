package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
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
    private TableGroupService service;

    private List<OrderTable> 테이블목록;
    private OrderTable 일번테이블;
    private OrderTable 이번테이블;

    @BeforeEach
    void setUp() {
        일번테이블 = new OrderTable(1L, null, 2, true);
        이번테이블 = new OrderTable(2L, null, 4, true);
        테이블목록 = Arrays.asList(일번테이블, 이번테이블);
    }

    @DisplayName("단체 등록 성공")
    @Test
    void create() {
        //given
        TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), 테이블목록);
        when(orderTableDao.findAllByIdIn(Arrays.asList(일번테이블.getId(), 이번테이블.getId())))
                .thenReturn(Arrays.asList(일번테이블, 이번테이블));
        when(tableGroupDao.save(테이블그룹)).thenReturn(테이블그룹);
        when(orderTableDao.save(일번테이블)).thenReturn(일번테이블);
        when(orderTableDao.save(이번테이블)).thenReturn(이번테이블);

        //when
        TableGroup result = service.create(테이블그룹);

        //then
        assertAll(
                () -> assertThat(result.getOrderTables()).contains(일번테이블, 이번테이블),
                () -> assertThat(result.getOrderTables().stream()
                        .findFirst().map(OrderTable::getTableGroupId)
                        .get())
                        .isEqualTo(테이블그룹.getId()
                        )
        );
    }

    @DisplayName("테이블 수가 2 미만인 단체 등록")
    @Test
    void createWithOnlyOneTable() {
        //given
        TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Collections.singletonList(일번테이블));

        //when & then
        assertThatThrownBy(() -> service.create(테이블그룹)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있지 않은 테이블이 포함된 단체 등록")
    @Test
    void createWithNotExistsTable() {
        //given
        TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), 테이블목록);
        when(orderTableDao.findAllByIdIn(Arrays.asList(일번테이블.getId(), 이번테이블.getId())))
                .thenReturn(Collections.singletonList(이번테이블));

        //when & then
        assertThatThrownBy(() -> service.create(테이블그룹)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블이 포함된 단체 등록")
    @Test
    void createWithNotEmptyTable() {
        //given
        OrderTable 삼번테이블 = new OrderTable(3L, null, 1, false);
        TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(일번테이블, 이번테이블, 삼번테이블));
        when(orderTableDao.findAllByIdIn(Arrays.asList(일번테이블.getId(), 이번테이블.getId(), 삼번테이블.getId())))
                .thenReturn(Arrays.asList(일번테이블, 이번테이블, 삼번테이블));

        //when & then
        assertThatThrownBy(() -> service.create(테이블그룹)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 단체에 소속된 테이블이 포함된 단체 등록")
    @Test
    void createWithOtherGroupTable() {
        //given
        OrderTable 삼번테이블 = new OrderTable(3L, 2L, 1, true);
        TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(일번테이블, 이번테이블, 삼번테이블));
        when(orderTableDao.findAllByIdIn(Arrays.asList(일번테이블.getId(), 이번테이블.getId(), 삼번테이블.getId())))
                .thenReturn(Arrays.asList(일번테이블, 이번테이블, 삼번테이블));

        //when & then
        assertThatThrownBy(() -> service.create(테이블그룹)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 해제 성공")
    @Test
    void ungroup() {
        //given
        OrderTable 삼번테이블 = new OrderTable(3L, 2L, 1, true);
        OrderTable 사번테이블 = new OrderTable(4L, 2L, 3, true);
        List<OrderTable> 새테이블목록 = Arrays.asList(삼번테이블, 사번테이블);

        when(orderTableDao.findAllByTableGroupId(2L)).thenReturn(새테이블목록);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(삼번테이블.getId(), 사번테이블.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).thenReturn(false);
        when(orderTableDao.save(삼번테이블)).thenReturn(삼번테이블);
        when(orderTableDao.save(사번테이블)).thenReturn(사번테이블);

        //when
        service.ungroup(2L);

        //then
        assertAll(
                () -> assertThat(삼번테이블.getTableGroupId()).isNull(),
                () -> assertThat(사번테이블.getTableGroupId()).isNull()
        );
    }

    @DisplayName("조리/식사 중인 테이블이 포함된 단체 해제")
    @Test
    void ungroupWithEatingTable() {
        //given
        OrderTable 삼번테이블 = new OrderTable(3L, 2L, 1, true);
        OrderTable 사번테이블 = new OrderTable(4L, 2L, 3, true);
        List<OrderTable> 새테이블목록 = Arrays.asList(삼번테이블, 사번테이블);

        when(orderTableDao.findAllByTableGroupId(2L)).thenReturn(새테이블목록);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(삼번테이블.getId(), 사번테이블.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> service.ungroup(2L)).isInstanceOf(IllegalArgumentException.class);
    }
}

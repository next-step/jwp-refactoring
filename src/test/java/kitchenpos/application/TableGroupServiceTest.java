package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 등록")
    @Test
    void 테이블_그룹_등록() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);
        given(orderTableDao.findAllByIdIn(Arrays.asList(1L,2L))).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(tableGroup);

        //when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        assertThat(savedTableGroup.getId()).isEqualTo(tableGroup.getId());
    }

    @DisplayName("테이블의 수가 2개 미만일 경우 테이블 그룹으로 등록할 수 없다.")
    @Test
    void 테이블의_수가_두개_미만일_경우_테이블_그룹_등록_실패() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1);
        tableGroup.setOrderTables(orderTables);

        //when+then
        assertThrows(IllegalArgumentException.class, ()-> tableGroupService.create(tableGroup));
    }

    @DisplayName("등록된 테이블이 아닐 경우 테이블 그룹으로 등록할 수 없다.")
    @Test
    void 존재하지_않는_테이블일_경우_테이블_그룹_등록_실패() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable();
        List<OrderTable> orderTables = Arrays.asList(orderTable1);
        tableGroup.setOrderTables(orderTables);

        //when+then
        assertThrows(IllegalArgumentException.class, ()-> tableGroupService.create(tableGroup));
    }

    @DisplayName("테이블이 비어있지 않은 경우 테이블 그룹으로 등록할 수 없다.")
    @Test
    void 테이블이_비어있지_않은_경우_테이블_그룹_등록_실패() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, null, 3, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 2, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        //when+then
        assertThrows(IllegalArgumentException.class, ()-> tableGroupService.create(tableGroup));
    }

    @DisplayName("이미 테이블 그룹으로 지정이된 테이블일 경우 테이블 그룹으로 등록할 수 없다.")
    @Test
    void 이미_단체_지정이된_테이블일_경우_테이블_그룹_등록_실패() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, tableGroup.getId(), 3, true);
        OrderTable orderTable2 = new OrderTable(2L, tableGroup.getId(), 2, true);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);

        //when+then
        assertThrows(IllegalArgumentException.class, ()-> tableGroupService.create(tableGroup));
    }

    @DisplayName("테이블 그룹 등록 해제")
    @Test
    void 테이블_그룹_등록_해제() {
        //given
        TableGroup tableGroup = new TableGroup(1L);
        OrderTable orderTable1 = new OrderTable(1L, tableGroup.getId(), 3, false);
        OrderTable orderTable2 = new OrderTable(2L, tableGroup.getId(), 2, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        tableGroup.setOrderTables(orderTables);
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(),
                eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))).willReturn(false);
        //when
        tableGroupService.ungroup(1L);

        //then
        assertThat(tableGroup.getOrderTables().stream()
                .filter(orderTable -> orderTable.getTableGroupId() != null)
                .collect(Collectors.toList())).isEmpty();
    }

    @DisplayName("주문 상태가 계산 완료가 아닐경우 테이블 그룹 등록을 해제할 수 없다.")
    @Test
    void 주문상태가_계산_완료가_아니라면_등록_해제_실패() {
        //given
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(),
                eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))).willReturn(true);

        //when+then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(1L));
    }
}

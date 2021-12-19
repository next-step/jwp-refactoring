package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.application.TableServiceTest.getOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("단체지정 서비스 테스트")
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


    @DisplayName("주문 테이블 목록을 통해 단체 지정을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        final OrderTable firstOrderTable = getOrderTable(1L, true, 13);
        final OrderTable secondOrderTable = getOrderTable(2L, true, 13);
        List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable
        );
        TableGroup expectedTableGroup = getTableGroup(1L, orderTables);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(expectedTableGroup);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(firstOrderTable, secondOrderTable);
        // when
        TableGroup tableGroup = tableGroupService.create(expectedTableGroup);
        // then
        assertThat(tableGroup).isEqualTo(expectedTableGroup);
    }

    @DisplayName("단체 지정을 못하는 경우")
    @Nested
    class createFail {

        @DisplayName("주문 테이블은 2개 미만 인경우")
        @Test
        void createByTwoLessOrderTable() {
            // given
            final OrderTable orderTable = getOrderTable(2L, true, 13);
            TableGroup expectedTableGroup = getTableGroup(1L, Collections.singletonList(orderTable));
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(expectedTableGroup);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블 목록 각각의 아이디를 따른 주문 테이블이 존재하지 않는 경우")
        @Test
        void createByNotExistOrderTable() {
            // given
            final OrderTable firstOrderTable = getOrderTable(1L, true, 13);
            final OrderTable secondOrderTable = getOrderTable(2L, true, 13);
            final List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
            TableGroup expectedTableGroup = getTableGroup(1L, orderTables);

            given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.emptyList());

            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(expectedTableGroup);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("저장된 주문 테이블의 각각 단체 지정 아이디가 존재하는 경우")
        @Test
        void createByNotExistTargetGroupId() {
            // when
            final OrderTable firstOrderTable = getOrderTable(1L, true, 13, 1L);
            final OrderTable secondOrderTable = getOrderTable(2L, true, 13, 1L);
            final List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
            TableGroup expectedTableGroup = getTableGroup(1L, orderTables);

            given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
            // when
            ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(expectedTableGroup);
            // then
            assertThatThrownBy(callable).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정의 아이디를 통해서 단체 지정을 해제 할 수 있다.")
    @Test
    void ungroup() {
        // given
        OrderTable firstOrderTable = getOrderTable(1L, false, 4, 1L);
        OrderTable secondOrderTable = getOrderTable(2L, false, 4, 1L);
        List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        TableGroup tableGroup = getTableGroup(1L, orderTables);

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(firstOrderTable, secondOrderTable);
        // when
        tableGroupService.ungroup(tableGroup.getId());
        // then
        verify(orderTableDao, times(2)).save(any(OrderTable.class));
    }


    @DisplayName("주문 테이블이 조리나 식사 상태일 경우 해제 할 수 없다.")
    @Test
    void ungroupFail() {
        // given
        OrderTable firstOrderTable = getOrderTable(1L, false, 4, 1L);
        OrderTable secondOrderTable = getOrderTable(2L, false, 4, 1L);
        List<OrderTable> orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        TableGroup tableGroup = getTableGroup(1L, orderTables);

        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when
        ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.ungroup(tableGroup.getId());
        // then
        assertThatThrownBy(callable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup getTableGroup(Long id, List<OrderTable> orderTables) {
        TableGroup expectedTableGroup = new TableGroup();
        expectedTableGroup.setId(id);
        expectedTableGroup.setOrderTables(orderTables);
        return expectedTableGroup;
    }

}
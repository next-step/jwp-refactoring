package kitchenpos.order.application;

import kitchenpos.common.fixtrue.OrderTableFixture;
import kitchenpos.common.fixtrue.TableGroupFixture;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

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

    TableGroup 단체_지정_테이블;
    List<OrderTable> orderTables;
    OrderTable firstOrderTable;
    OrderTable secondOrderTable;

    @BeforeEach
    void setUp() {
        firstOrderTable = OrderTableFixture.of(1L, 2, true);
        secondOrderTable = OrderTableFixture.of(1L, 2, true);
        orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        단체_지정_테이블 = TableGroupFixture.of(1L, firstOrderTable, secondOrderTable);
    }

    @Test
    void 단체_지정() {
        // given
        List<Long> orderTableIds = 단체_지정_테이블.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(단체_지정_테이블);

        // when
        TableGroup actual = tableGroupService.create(단체_지정_테이블);

        // then
        assertAll(() -> {
            assertThat(actual).isEqualTo(단체_지정_테이블);
            assertThat(actual.getOrderTables())
                    .extracting("tableGroupId")
                    .containsExactly(1L, 1L);
        });
    }

    @Test
    void 단체_지정_시_주문_테이블은_두_테이블_이상이어야_한다() {
        // given
        단체_지정_테이블.setOrderTables(new ArrayList<>());

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정_시_등록하려는_주문_테이블은_주문테이블에_등록되어있어야_한다() {
        // given
        List<Long> orderTableIds = 단체_지정_테이블.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(new ArrayList<>());

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정_시_주문_테이블은_빈_테이블이어야_한다() {
        // given
        List<Long> orderTableIds = 단체_지정_테이블.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        for (OrderTable orderTable : orderTables) {
            orderTable.setEmpty(false);
        }

        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(orderTables);

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정_시_주문_테이블은_단체_지정이_되어있으면_안된다() {
        // given
        List<Long> orderTableIds = 단체_지정_테이블.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(1L);
        }

        given(orderTableDao.findAllByIdIn(orderTableIds)).willReturn(orderTables);

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_테이블);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(1L);
        }

        given(orderTableDao.findAllByTableGroupId(단체_지정_테이블.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableDao.save(firstOrderTable)).willReturn(firstOrderTable);
        given(orderTableDao.save(secondOrderTable)).willReturn(secondOrderTable);

        // when
        tableGroupService.ungroup(단체_지정_테이블.getId());

        // then
        assertThat(orderTables)
                .extracting("tableGroupId")
                .containsExactly(null, null);
    }

    @Test
    void 단체_지정_해제_시_주문_테이블이_조리이거나_식사이면_해제할_수_없다() {
        // given
        given(orderTableDao.findAllByTableGroupId(단체_지정_테이블.getId())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).willReturn(true);

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.ungroup(단체_지정_테이블.getId());

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }
}

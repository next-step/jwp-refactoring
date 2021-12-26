package kitchenpos.order.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
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

    TableGroupRequest 단체_지정_요청;
    List<OrderTableRequest> orderTables;
    OrderTableRequest firstOrderTableRequest;
    OrderTableRequest secondOrderTableRequest;

    @BeforeEach
    void setUp() {
        firstOrderTableRequest = OrderTableRequest.from(1L);
        secondOrderTableRequest = OrderTableRequest.from(1L);
        orderTables = Arrays.asList(firstOrderTableRequest, secondOrderTableRequest);
        단체_지정_요청 = TableGroupRequest.from(orderTables);
    }

    @Test
    void 단체_지정() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(2, true),
                OrderTable.of(3, true));

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(TableGroup.from(orderTables));

        // when
        TableGroupResponse actual = tableGroupService.create(단체_지정_요청);

        // then
        assertThat(actual.getOrderTables())
                .extracting("numberOfGuests")
                .containsExactly(2, 3);
    }

    @Test
    void 단체_지정_시_등록하려는_주문_테이블은_주문테이블에_등록되어있어야_한다() {
        // given
        given(orderTableDao.findAllByIdIn(단체_지정_요청.getOrderTableIds())).willReturn(new ArrayList<>());

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정_시_주문_테이블은_빈_테이블이어야_한다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                OrderTable.of(2, false),
                OrderTable.of(3, false));

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정_시_주문_테이블은_단체_지정이_되어있으면_안된다() {
        // given

        OrderTable firstOrderTable = OrderTable.of(2, true);
        firstOrderTable.group(1L);
        OrderTable secondOrderTable = OrderTable.of(3, true);
        secondOrderTable.group(1L);
        List<OrderTable> orderTables = Arrays.asList(
                firstOrderTable,
                secondOrderTable);

        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.create(단체_지정_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        OrderTable firstOrderTable = OrderTable.of(2, true);
        firstOrderTable.group(1L);
        OrderTable secondOrderTable = OrderTable.of(3, true);
        secondOrderTable.group(1L);
        List<OrderTable> orderTables = Arrays.asList(
                firstOrderTable,
                secondOrderTable);

        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(firstOrderTable);
        given(orderTableDao.save(any())).willReturn(secondOrderTable);

        // when
        tableGroupService.ungroup(1L);

        // then
        assertThat(orderTables)
                .extracting("tableGroupId")
                .containsExactly(null, null);
    }

    @Test
    void 단체_지정_해제_시_주문_테이블이_조리이거나_식사이면_해제할_수_없다() {
        // given
        OrderTable firstOrderTable = OrderTable.of(2, true);
        firstOrderTable.group(1L);
        OrderTable secondOrderTable = OrderTable.of(3, true);
        secondOrderTable.group(1L);
        List<OrderTable> orderTables = Arrays.asList(
                firstOrderTable,
                secondOrderTable);

        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), anyList())).willReturn(true);

        // when
        ThrowingCallable throwingCallable = () -> tableGroupService.ungroup(1L);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }

}

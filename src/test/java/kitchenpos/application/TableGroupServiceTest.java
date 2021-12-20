package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 서비스 테스트")
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

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void create() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(
            OrderTableFixture.of(1L, null, 4, true),
            OrderTableFixture.of(2L, null, 4, true)
        );
        final List<OrderTable> orderTablesRequest = createOrderTablesRequest(1L, 2L);
        final TableGroup request = createTableGroupRequest(LocalDateTime.now(), orderTablesRequest);
        final TableGroup expected = createTableGroup(1L, LocalDateTime.now(), orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(expected);

        // when
        final TableGroup actual = tableGroupService.create(request);

        // then
        assertAll(
            () -> assertThat(actual).isEqualTo(expected),
            () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
            () -> assertThat(orderTables.get(0).getTableGroupId()).isEqualTo(expected.getId()),
            () -> assertThat(orderTables.get(1).isEmpty()).isFalse(),
            () -> assertThat(orderTables.get(1).getTableGroupId()).isEqualTo(expected.getId())
        );
    }

    @DisplayName("2개 미만의 주문 테이블을 단체 지정할 수 없다.")
    @Test
    void create_fail_insufficientOrderTables() {
        // given
        final TableGroup request = createTableGroupRequest(
            LocalDateTime.now(),
            Collections.emptyList()
        );

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않은 경우, 주문 테이블들을 단체 지정할 수 없다.")
    @Test
    void create_fail_noSuchOrderTable() {
        // given
        final TableGroup request = createTableGroupRequest(
            LocalDateTime.now(),
            createOrderTablesRequest(1L, 2L)
        );

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어 있지 않은 경우, 주문 테이블들을 단체 지정할 수 없다.")
    @Test
    void create_fail_tableNotEmpty() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(
            OrderTableFixture.of(1L, null, 4, false),
            OrderTableFixture.of(2L, null, 4, false)
        );
        final List<OrderTable> orderTablesRequest = createOrderTablesRequest(1L, 2L);
        final TableGroup request = createTableGroupRequest(LocalDateTime.now(), orderTablesRequest);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 이미 단체 지정되어 있는 경우, 주문 테이블들을 단체 지정할 수 없다.")
    @Test
    void create_fail_tableAlreadyInGroup() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(
            OrderTableFixture.of(1L, 1L, 4, false),
            OrderTableFixture.of(2L, 1L, 4, false)
        );
        final List<OrderTable> orderTablesRequest = createOrderTablesRequest(1L, 2L);
        final TableGroup request = createTableGroupRequest(LocalDateTime.now(), orderTablesRequest);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.create(request);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 주문 테이블들을 해제할 수 있다.")
    @Test
    void ungroup() {
        // given
        final List<OrderTable> orderTables = Arrays.asList(
            OrderTableFixture.of(1L, 1L, 4, false),
            OrderTableFixture.of(2L, 1L, 4, false)
        );
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .willReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        assertAll(
            () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
            () -> assertThat(orderTables.get(1).getTableGroupId()).isNull()
        );
    }

    @DisplayName("주문 준비중인 주문 테이블이 속한 단체를 지정 해제할 수 없다.")
    @Test
    void ungroup_fail_orderCooking() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Collections.emptyList());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .willReturn(true);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> tableGroupService.ungroup(1L);

        // then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup createTableGroupRequest(
        final LocalDateTime createdDate,
        final List<OrderTable> orderTables
    ) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    private TableGroup createTableGroup(
        final Long id,
        final LocalDateTime createdDate,
        final List<OrderTable> orderTables
    ) {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    private List<OrderTable> createOrderTablesRequest(final Long... ids) {
        final List<OrderTable> orderTables = new ArrayList<>();
        for (final Long id : ids) {
            final OrderTable table = new OrderTable();
            table.setId(id);
            orderTables.add(table);
        }
        return orderTables;
    }
}

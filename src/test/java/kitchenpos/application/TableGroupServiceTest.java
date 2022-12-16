package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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

    @Test
    void 테이블_그룹_지정시_성공하고_그룹_정보를_반환한다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1, true),
                new OrderTable(2, true),
                new OrderTable(3, true)
        );
        TableGroup 테이블_그룹_지정_요청 = new TableGroup(orderTables);
        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(테이블_그룹_지정_요청);

        // when
        TableGroup tableGroup = tableGroupService.create(테이블_그룹_지정_요청);

        // then
        테이블_그룹_지정됨(tableGroup, orderTables);
    }

    @Test
    void 테이블_그룹_지정시_주문_테이블이_2개_미만인경우_예외처리되어_지정에_실패한다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1, true)
        );
        TableGroup 테이블_그룹_지정_요청 = new TableGroup(orderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_지정_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_지정시_등록된_주문_테이블_개수가_다른경우_예외처리되어_지정에_실패한다() {
        // given
        List<OrderTable> storedOrderTables = Arrays.asList(
                new OrderTable(1, true),
                new OrderTable(2, true),
                new OrderTable(3, true)
        );
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1, true),
                new OrderTable(2, true)
        );
        given(orderTableDao.findAllByIdIn(any())).willReturn(storedOrderTables);
        TableGroup 테이블_그룹_지정_요청 = new TableGroup(orderTables);

        // when
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_지정_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        테이블_그룹_지정_실패함();
    }

    @Test
    void 테이블_그룹_지정시_주문_테이블이_사용중인경우_예외처리되어_지정에_실패한다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1, false), // 사용중인 주문 테이블
                new OrderTable(2, true)
        );
        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
        TableGroup 테이블_그룹_지정_요청 = new TableGroup(orderTables);

        // when
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_지정_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        테이블_그룹_지정_실패함();
    }

    @Test
    void 테이블_그룹_지정시_주문_테이블이_다른_그룹에_속한경우_예외처리되어_지정에_실패한다() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(2L, 1, true), // 다른 그룹에 속해져있는 주문 테이블
                new OrderTable(2, true)
        );
        TableGroup 테이블_그룹_지정_요청 = new TableGroup(orderTables);
        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_지정_요청))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        테이블_그룹_지정_실패함();
    }

    @Test
    void 테이블_그룹_해지에_성공한다() {
        // given
        long 기존_테이블_아이디 = 1L;
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(기존_테이블_아이디, 1, true),
                new OrderTable(기존_테이블_아이디, 2, true)
        );
        TableGroup 기존_테이블_그룹 = new TableGroup(orderTables);
        기존_테이블_그룹.setId(기존_테이블_아이디);

        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        tableGroupService.ungroup(기존_테이블_아이디);

        // then
        테이블_그룹_해지됨(orderTables);
    }

    @Test
    void 테이블_그룹_해지시_속해져있는_테이블중_조리_또는_식사_상태인경우_예외처리되어_해지에_실패한다() {
        // given
        long 기존_테이블_아이디 = 1L;
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(기존_테이블_아이디, 1, true),
                new OrderTable(기존_테이블_아이디, 2, true)
        );
        TableGroup 기존_테이블_그룹 = new TableGroup(orderTables);
        기존_테이블_그룹.setId(기존_테이블_아이디);

        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(기존_테이블_아이디))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        테이블_그룹_해지_실패함();
    }

    private void 테이블_그룹_지정됨(TableGroup tableGroup, List<OrderTable> expectedOrderTables) {
        boolean isNotEmptyAllTables = tableGroup.getOrderTables().stream().noneMatch(OrderTable::isEmpty);
        assertAll(
                () -> assertThat(isNotEmptyAllTables).isTrue(),
                () -> assertThat(tableGroup.getOrderTables()).containsAll(expectedOrderTables)
        );
    }

    private void 테이블_그룹_지정_실패함() {
        then(orderTableDao).should(times(1)).findAllByIdIn(any());
    }

    private void 테이블_그룹_해지됨(List<OrderTable> orderTables) {
        assertThat(orderTables.stream()
                .noneMatch(table -> Objects.nonNull(table.getTableGroupId())))
                .isTrue();
    }

    private void 테이블_그룹_해지_실패함() {
        then(orderTableDao).should(times(1)).findAllByTableGroupId(any());
        then(orderDao).should(times(1)).existsByOrderTableIdInAndOrderStatusIn(any(), any());
    }
}

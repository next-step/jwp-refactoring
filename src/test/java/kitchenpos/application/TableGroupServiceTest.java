package kitchenpos.application;

import static kitchenpos.exception.ErrorCode.NOT_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES;
import static kitchenpos.exception.ErrorCode.ORDER_TABLES_MUST_BE_AT_LEAST_TWO;
import static kitchenpos.exception.ErrorCode.TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.BeforeEach;
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
    private TableGroupService tableGroupService;
    private OrderTable 주문_좌석_1;
    private OrderTable 주문_좌석_2;
    private OrderTable 주문_좌석_3;
    private OrderTable 주문_좌석_4;
    private TableGroup 좌석_그룹_1;
    private TableGroup 좌석_그룹_2;

    @BeforeEach
    void setUp() {
        주문_좌석_1 = new OrderTable(1L, null, 1, true);
        주문_좌석_2 = new OrderTable(2L, null, 2, true);
        주문_좌석_3 = new OrderTable(3L, 2L, 1, false);
        주문_좌석_4 = new OrderTable(4L, 2L, 1, false);
        좌석_그룹_1 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문_좌석_1, 주문_좌석_2));
        좌석_그룹_2 = new TableGroup(2L, LocalDateTime.now(), Arrays.asList(주문_좌석_3, 주문_좌석_4));
    }

    @Test
    void 생성() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_좌석_1, 주문_좌석_2));
        given(tableGroupDao.save(좌석_그룹_1)).willReturn(좌석_그룹_1);

        TableGroupResponse response = tableGroupService.create(좌석_그룹_1);

        assertAll(
                () -> assertThat(response.getOrderTables()).containsExactly(주문_좌석_1, 주문_좌석_2),
                () -> assertThat(response.getOrderTables().get(0).getTableGroupId()).isEqualTo(좌석_그룹_1.getId()),
                () -> assertThat(response.getOrderTables().get(1).getTableGroupId()).isEqualTo(좌석_그룹_1.getId())
        );
    }

    @Test
    void 좌석_그룹으로_지정하려고_하는_좌석_개수가_1개인_경우() {
        좌석_그룹_1 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문_좌석_1));

        assertThatThrownBy(
                () -> tableGroupService.create(좌석_그룹_1)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(ORDER_TABLES_MUST_BE_AT_LEAST_TWO.getDetail());
    }

    @Test
    void 좌석_그룹_지정을_요청한_좌석_개수와_실제_등록된_좌석_개수가_다른_경우() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_좌석_1, 주문_좌석_2, 주문_좌석_3));

        assertThatThrownBy(
                () -> tableGroupService.create(좌석_그룹_1)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_SAME_BETWEEN_ORDER_TABLES_COUNT_AND_SAVED_ORDER_TABLES.getDetail());
    }

    @Test
    void 사용중인_좌석을_그룹으로_지정하려_하는_경우() {
        주문_좌석_1 = new OrderTable(1L, null, 1, false);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_좌석_1, 주문_좌석_2));

        assertThatThrownBy(
                () -> tableGroupService.create(좌석_그룹_1)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP.getDetail());
    }

    @Test
    void 좌석_그룹_해제() {
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(주문_좌석_3, 주문_좌석_4));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        tableGroupService.ungroup(좌석_그룹_2.getId());

        assertAll(
                () -> assertThat(주문_좌석_3.getTableGroupId()).isNull(),
                () -> assertThat(주문_좌석_4.getTableGroupId()).isNull()
        );
    }

    @Test
    void 좌석_상태가_COMPLETION_상태가_아닌_경우() {
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(주문_좌석_3, 주문_좌석_4));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(
                () -> tableGroupService.ungroup(좌석_그룹_2.getId())
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_COMPLETION_STATUS.getDetail());
    }
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;
    private OrderTable 주문_좌석_1;
    private OrderTable 주문_좌석_2;
    private OrderTable 주문_좌석_3;
    private TableGroup 좌석_그룹;

    @BeforeEach
    void setUp() {
        주문_좌석_1 = new OrderTable(1L, null, 1, true);
        주문_좌석_2 = new OrderTable(2L, null, 2, true);
        좌석_그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문_좌석_1, 주문_좌석_2));
    }

    @Test
    void 생성() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_좌석_1, 주문_좌석_2));
        given(tableGroupDao.save(좌석_그룹)).willReturn(좌석_그룹);

        TableGroup tableGroup = tableGroupService.create(좌석_그룹);

        assertAll(
                () -> assertThat(tableGroup.getOrderTables()).containsExactly(주문_좌석_1, 주문_좌석_2),
                () -> assertThat(tableGroup.getOrderTables().get(0).getTableGroupId()).isEqualTo(좌석_그룹.getId()),
                () -> assertThat(tableGroup.getOrderTables().get(1).getTableGroupId()).isEqualTo(좌석_그룹.getId())
        );
    }

    @Test
    void 좌석_그룹으로_지정하려고_하는_좌석_개수가_1개인_경우() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_좌석_1));

        assertThatThrownBy(
                () -> tableGroupService.create(좌석_그룹)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 좌석_그룹_지정을_요청한_좌석_개수와_실제_등록된_좌석_개수가_다른_경우() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_좌석_1, 주문_좌석_2, 주문_좌석_3));

        assertThatThrownBy(
                () -> tableGroupService.create(좌석_그룹)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사용중인_좌석을_그룹으로_지정하려_하는_경우() {
        주문_좌석_1 = new OrderTable(1L, null, 1, false);

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_좌석_1, 주문_좌석_2));

        assertThatThrownBy(
                () -> tableGroupService.create(좌석_그룹)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}

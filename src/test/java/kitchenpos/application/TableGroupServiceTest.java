package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;


@DisplayName("테이블그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;


    private TableGroup 단체1;
    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private OrderTable 테이블_GUEST3_NOT_EMPTY;
    private OrderTable 테이블_TABLEGROUP;

    @BeforeEach
    void setUp() {
        테이블1 = new OrderTable(1L, 0, true);
        테이블2 = new OrderTable(2L, 0, true);
        테이블3 = new OrderTable(3L, 0, true);
        단체1 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블1, 테이블2, 테이블3));
        테이블_GUEST3_NOT_EMPTY = new OrderTable(4L, 3, false);
        테이블_TABLEGROUP = new OrderTable(5L, 3, false);
    }

    @Test
    @DisplayName("단체을 등록한다.")
    void createTableGroup() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));
        given(tableGroupDao.save(단체1)).willReturn(단체1);

        // when
        TableGroup saveTableGroup = tableGroupService.create(단체1);

        // then
        assertThat(saveTableGroup.getId()).isEqualTo(단체1.getId());
        assertThat(saveTableGroup.getCreatedDate()).isEqualTo(단체1.getCreatedDate());
        assertThat(saveTableGroup.getOrderTables()).isEqualTo(단체1.getOrderTables());
    }

    @Test
    @DisplayName("단체 내 주문테이블이 2개 미만이면 오류 발생한다.")
    void minumumOrderTableException() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블1));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 내 주문테이블이 등록되어 있지 않으면 오류 발생한다.")
    void notExistOrderTableException() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블2));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체1));
    }

    @Test
    @DisplayName("단체 내 주문테이블이 비어 있지 않으면 오류 발생한다.")
    void notEmptyOrderTableException() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블_GUEST3_NOT_EMPTY));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체1));
    }

    @Test
    @DisplayName("다른 단체에 지정된 주문테이블이 있으면 등록 시, 오류 발생한다.")
    void alreadyTableGroupException() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블1, 테이블_TABLEGROUP));

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(단체1));
    }

    @Test
    @DisplayName("단체을 삭제한다.")
    void unTableGroup() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));

        // when
        tableGroupService.ungroup(단체1.getId());

        // then
        assertThat(테이블1.getTableGroupId()).isNull();
        assertThat(테이블2.getTableGroupId()).isNull();
        assertThat(테이블3.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("단체 삭제 시, 주문 테이블의 상태가 주문 중 / 식사중 이면 오류 발생한다.")
    void unTableGroupException() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(테이블1, 테이블2, 테이블3));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(단체1.getId()));
    }
}

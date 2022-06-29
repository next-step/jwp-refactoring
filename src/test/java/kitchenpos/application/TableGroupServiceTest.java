package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.application.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

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

    private OrderTable 빈_테이블_1;
    private OrderTable 빈_테이블_2;
    private OrderTable 빈_테이블_3;
    private OrderTable 주문_테이블;
    private OrderTable 단체_테이블;
    private TableGroup 단체지정_테이블;

    @BeforeEach
    void setUp() {
        빈_테이블_1 = createOrderTable(1L, null, 0, true);
        빈_테이블_2 = createOrderTable(2L, null, 0, true);
        빈_테이블_3 = createOrderTable(3L, null, 0, true);
        주문_테이블 = createOrderTable(4L, null, 4, false);
        단체_테이블 = createOrderTable(5L, 1L, 0, true);
        단체지정_테이블 = createTableGroup(1L, Arrays.asList(빈_테이블_1, 빈_테이블_2, 빈_테이블_3));
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void create_success() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(빈_테이블_1, 빈_테이블_2, 빈_테이블_3));
        given(tableGroupDao.save(단체지정_테이블)).willReturn(단체지정_테이블);

        // when
        TableGroup saved = tableGroupService.create(단체지정_테이블);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved).isEqualTo(단체지정_테이블),
                () -> assertThat(saved.getOrderTables()).containsExactly(빈_테이블_1, 빈_테이블_2, 빈_테이블_3)
        );
    }

    @DisplayName("단체 지정 등록에 실패한다. (테이블 수가 2개 미만인 경우)")
    @Test
    void create_fail_invalidSize() {
        // given
        단체지정_테이블 = createTableGroup(1L, Collections.singletonList(빈_테이블_1));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록에 실패한다. (존재하지 않는 테이블이 있는 경우)")
    @Test
    void create_fail_empty() {
        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록에 실패한다. (빈 테이블이 아닌 테이블이 있는 경우)")
    @Test
    void create_fail_emptyTable() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(주문_테이블, 빈_테이블_2, 빈_테이블_3));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 등록에 실패한다. (이미 단체 지정된 테이블이 있는 경우)")
    @Test
    void create_fail_tableGroup() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(단체_테이블, 빈_테이블_2, 빈_테이블_3));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(단체지정_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void unGroup_success() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(빈_테이블_1, 빈_테이블_2, 빈_테이블_3));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(빈_테이블_1, 빈_테이블_2, 빈_테이블_3);

        // when
        tableGroupService.ungroup(단체지정_테이블.getId());

        // then
        assertAll(
                () -> assertThat(빈_테이블_1.getTableGroupId()).isNull(),
                () -> assertThat(빈_테이블_2.getTableGroupId()).isNull(),
                () -> assertThat(빈_테이블_3.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 지정 해제에 실패한다. (주문 상태가 '조리' 또는 '식사' 인 테이블이 있는 경우")
    @Test
    void unGroup_fail() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(Arrays.asList(빈_테이블_1, 빈_테이블_2, 빈_테이블_3));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(단체지정_테이블.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

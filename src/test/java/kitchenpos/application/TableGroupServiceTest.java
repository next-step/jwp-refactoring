package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.TestTableGroupFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)

class TableGroupServiceTest {
    @Mock
    private TableGroupDao tableGroupDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private TableGroup 단체_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블_1 = new OrderTable();
        주문_테이블_2 = new OrderTable();
        단체_테이블 = TestTableGroupFactory.create(1L);
    }
    
    @DisplayName("단체 테이블을 등록한다")
    @Test
    void create() throws Exception {
        // given
        단체_테이블.setOrderTables(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        주문_테이블_1.setEmpty(true);
        주문_테이블_2.setEmpty(true);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(tableGroupDao.save(any())).willReturn(단체_테이블);

        // when
        TableGroup tableGroup = tableGroupService.create(단체_테이블);

        // then
        assertThat(tableGroup).isEqualTo(단체_테이블);
    }

    @DisplayName("단체 테이블을 등록시 주문테이블이 1개라면 등록할 수 없다")
    @Test
    void createException() throws Exception {
        // given
        단체_테이블.setOrderTables(Collections.singletonList(주문_테이블_1));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블로 등록할 주문 테이블은 모두 존재해야 한다.")
    @Test
    void createException2() throws Exception {
        // given
        단체_테이블.setOrderTables(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        주문_테이블_1.setEmpty(true);
        주문_테이블_2.setEmpty(true);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.singletonList(주문_테이블_1));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블로 등록 시 모든 주문 테이블은 빈 테이블이어야 한다")
    @Test
    void createException3() throws Exception {
        // given
        단체_테이블.setOrderTables(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        주문_테이블_1.setEmpty(false);
        주문_테이블_2.setEmpty(true);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 테이블로 등록 시 모든 주문 테이블은 단체로 지정된 테이블이 아니어야 한다")
    @Test
    void createException4() throws Exception {
        // given
        단체_테이블.setOrderTables(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        주문_테이블_1.setEmpty(true);
        주문_테이블_2.setEmpty(true);
        주문_테이블_1.setTableGroupId(4L);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블)).isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("단체 테이블을 해제한다")
    @Test
    void ungroup() throws Exception {
        // given
        주문_테이블_1.setTableGroupId(단체_테이블.getId());
        주문_테이블_2.setTableGroupId(단체_테이블.getId());

        given(orderTableDao.findAllByTableGroupId(단체_테이블.getId())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        tableGroupService.ungroup(단체_테이블.getId());

        // then
        assertAll("모든 주문 테이블의 단체테이블 연관관계가 끊어진다",
                () -> assertThat(주문_테이블_1.getTableGroupId()).isNull(),
                () -> assertThat(주문_테이블_2.getTableGroupId()).isNull());
    }

    @DisplayName("해제시 주문테이블의 주문상태가 COOKING, MEAL 이면 해제할 수 없다.")
    @Test
    void ungroupException() throws Exception {
        // given
        주문_테이블_1.setTableGroupId(단체_테이블.getId());
        주문_테이블_2.setTableGroupId(단체_테이블.getId());

        given(orderTableDao.findAllByTableGroupId(단체_테이블.getId())).willReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(단체_테이블.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

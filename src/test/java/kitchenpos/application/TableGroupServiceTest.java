package kitchenpos.application;

import static kitchenpos.domain.OrderTableTest.주문_테이블_생성;
import static kitchenpos.domain.TableGroupTest.단체_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private TableGroup 단체;

    @BeforeEach
    public void setUp() {
        주문_테이블_1 = 주문_테이블_생성(1L, null, 0, true);
        주문_테이블_2 = 주문_테이블_생성(2L, null, 0, true);
        단체 = 단체_생성(1L, null, Arrays.asList(주문_테이블_1, 주문_테이블_2));
    }


    @Test
    @DisplayName("단체 지정 등록")
    void create() {
        // given
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        when(tableGroupDao.save(단체)).thenReturn(단체);

        // when
        TableGroup 등록된_단체 = tableGroupService.create(단체);

        // then
        assertThat(등록된_단체).isEqualTo(단체);
    }

    @Test
    @DisplayName("단체 지정 해제")
    void ungroup() {
        // given
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(any(OrderTable.class));

        // when
        tableGroupService.ungroup(단체.getId());

        // then
        assertThat(주문_테이블_1.getTableGroupId()).isNull();
        assertThat(주문_테이블_2.getTableGroupId()).isNull();
    }
}
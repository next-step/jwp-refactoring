package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;
    private TableGroup 단체_지정;

    @BeforeEach
    void setUp() {
        주문_테이블1 = OrderTable.of(1L, 2, true);
        주문_테이블2 = OrderTable.of(2L, 2, true);

        단체_지정 = TableGroup.of(1L, Arrays.asList(주문_테이블1, 주문_테이블2));
    }

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
        when(tableGroupDao.save(any())).thenReturn(단체_지정);
        when(orderTableDao.save(주문_테이블1)).thenReturn(주문_테이블1);
        when(orderTableDao.save(주문_테이블2)).thenReturn(주문_테이블2);

        TableGroup result = tableGroupService.create(단체_지정);

        assertThat(result).isEqualTo(단체_지정);
    }

    @DisplayName("단체 지정할 주문 테이블이 2개 이상이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException() {
        TableGroup tableGroup = TableGroup.of(1L, Arrays.asList(주문_테이블1));

        Assertions.assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정할 주문 테이블이 등록된 주문 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException2() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Collections.emptyList());

        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체_지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 주문 테이블 하나라도 빈 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createException3() {
        OrderTable 비어있지_않은_주문_테이블 = OrderTable.of(3L, 3, false);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블1, 비어있지_않은_주문_테이블);
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(주문_테이블_목록);

        TableGroup 단체지정 = TableGroup.of(1L, 주문_테이블_목록);
        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정이 된 주문 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createException4() {
        OrderTable 단체_지정된_주문_테이블 = OrderTable.of(3L, 2L, 3, true);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블1, 단체_지정된_주문_테이블);
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(주문_테이블_목록);

        TableGroup 단체지정 = TableGroup.of(1L, 주문_테이블_목록);
        Assertions.assertThatThrownBy(() -> tableGroupService.create(단체지정))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 취소할 수 있다.")
    @Test
    void ungroup() {
        OrderTable 단체_지정된_주문_테이블1 = OrderTable.of(1L, 1L, 3, true);
        OrderTable 단체_지정된_주문_테이블2 = OrderTable.of(2L, 1L, 3, true);

        when(orderTableDao.findAllByTableGroupId(any()))
                .thenReturn(Arrays.asList(단체_지정된_주문_테이블1, 단체_지정된_주문_테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .thenReturn(false);
        when(orderTableDao.save(단체_지정된_주문_테이블1)).thenReturn(단체_지정된_주문_테이블1);
        when(orderTableDao.save(단체_지정된_주문_테이블2)).thenReturn(단체_지정된_주문_테이블2);

        tableGroupService.ungroup(1L);

        assertAll(
                () -> assertThat(단체_지정된_주문_테이블1.getTableGroupId()).isNull(),
                () -> assertThat(단체_지정된_주문_테이블2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 지정된 주문 테이블들의 상태가 조리 또는 식사이면 단체 지정을 취소할 수 없다.")
    @Test
    void ungroupException() {
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(주문_테이블1, 주문_테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

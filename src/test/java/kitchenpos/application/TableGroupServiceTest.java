package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 관련 비즈니스 테스트")
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

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;
    private TableGroup 단체;

    @BeforeEach
    void setUp() {
        주문테이블A = new OrderTable(1L, null, 4, true);
        주문테이블B = new OrderTable(2L, null, 6, true);
        단체 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블A, 주문테이블B));
    }

    @DisplayName("주문 테이블의 단체를 지정한다.")
    @Test
    void createTableGroup() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()))).thenReturn(Arrays.asList(주문테이블A, 주문테이블B));
        when(tableGroupDao.save(단체)).thenReturn(단체);
        when(orderTableDao.save(주문테이블A)).thenReturn(주문테이블A);
        when(orderTableDao.save(주문테이블B)).thenReturn(주문테이블B);

        // when
        TableGroup result = tableGroupService.create(단체);

        // then
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(단체.getId()),
            () -> assertThat(result.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("단체 지정 시 주문테이블이 비어있으면 예외가 발생한다.")
    @Test
    void createTableGroupEmptyOrderTableException() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 2개 미만이면 예외가 발생한다.")
    @Test
    void createTableGroupUnderTwoSizeOrderTableException() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블A));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 비어있지 않은 주문 테이블이 존재하는 경우 예외가 발생한다.")
    @Test
    void notExistOrderTableException() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, false);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, 주문테이블B));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 시 주문 테이블이 다른 단체에 속해 있다면 예외가 발생한다.")
    @Test
    void alreadyTableGroupException() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 4, true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, 주문테이블B));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(orderTable, 주문테이블B));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        when(orderTableDao.findAllByTableGroupId(단체.getId())).thenReturn(Arrays.asList(주문테이블A, 주문테이블B));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(주문테이블A)).thenReturn(주문테이블A);
        when(orderTableDao.save(주문테이블B)).thenReturn(주문테이블B);

        // when
        tableGroupService.ungroup(단체.getId());

        // then
        assertAll(
            () -> assertThat(주문테이블A.getTableGroupId()).isNull(),
            () -> assertThat(주문테이블B.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 내 주문 테이블들의 상태가 조리, 식사일 때 단체 지정 해체 시 예외가 발생한다.")
    @Test
    void unGroupStateException() {
        // given
        when(orderTableDao.findAllByTableGroupId(단체.getId())).thenReturn(Arrays.asList(주문테이블A, 주문테이블B));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(단체.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

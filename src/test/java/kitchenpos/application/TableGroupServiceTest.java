package kitchenpos.application;

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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@DisplayName("단체 지정 관련 비즈니스 기능 테스트")
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

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private TableGroup 단체그룹;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable(1L, null, 1, true);
        주문테이블2 = new OrderTable(2L, null, 2, true);
        단체그룹 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1, 주문테이블2));
    }

    @DisplayName("주문 테이블 생성 테스트")
    @Test
    void createTableGroupTest() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(tableGroupDao.save(단체그룹)).thenReturn(단체그룹);
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);
        when(orderTableDao.save(주문테이블2)).thenReturn(주문테이블2);

        // when
        TableGroup result = tableGroupService.create(단체그룹);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(단체그룹.getId()),
                () -> assertThat(result.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("주문 테이블 생성 테스트 - 단체 지정 시 등록된 주문 테이블이 아닌 경우")
    @Test
    void createTableGroupTest2() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 생성 테스트 - 단체 지정 시 주문 테이블이 2개 미만인 경우")
    @Test
    void createTableGroupTest3() {
        // given
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문테이블1));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 생성 테스트 - 단체 지정 시 빈 주문 테이블이 존재하는 경우")
    @Test
    void createTableGroupTest4() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, 주문테이블2));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(new ArrayList<>());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 생성 테스트 - 단체 지정 시 주문 테이블이 다른 단체에 속해 있는 경우")
    @Test
    void createTableGroupTest5() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1L, 4, true);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable, 주문테이블2));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(orderTable, 주문테이블2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제 테스트")
    @Test
    void ungroupTest() {
        // given
        when(orderTableDao.findAllByTableGroupId(단체그룹.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);
        when(orderTableDao.save(주문테이블2)).thenReturn(주문테이블2);

        // when
        tableGroupService.ungroup(단체그룹.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroupId()).isNull(),
                () -> assertThat(주문테이블2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("단체 지정 해제 테스트 - 단체 내 주문 테이블들의 상태가 조리, 식사일 경우")
    @Test
    void ungroupTest2() {
        // given
        when(orderTableDao.findAllByTableGroupId(단체그룹.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(단체그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
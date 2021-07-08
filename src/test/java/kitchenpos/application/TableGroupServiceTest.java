package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("테이블 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;
    private TableGroup tableGroup;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("주문 테이블이 하나거나 없을 때 예외")
    void isBlankOrOnlyOneTable_exception() {
        // given
        단체_테이블_생성_되어_있음();

        // and
        주문_테이블_생성되어_있음();
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable));

        // than
        // 그룹 테이블 생성 요청 예외 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장되어 있지 않은 테이블 주문 시 예외")
    void isNotSavedTable_exception() {
        // given
        단체_테이블_생성_되어_있음();

        // and
        주문_테이블_생성되어_있음();
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        // when
        // 실제로 테이블은 하나만 저장되어 있음
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(firstOrderTable));

        // than
        // 단체 테이블 등록 요청시 예외 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체 테이블에 등록되어 있는 테이블 예외")
    void isDuplicateTableGroup_exception() {
        // given
        단체_테이블_생성_되어_있음();

        // and
        주문_테이블_생성되어_있음();
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        // when
        // 테이블 하나는 이미 단체 테이블에 속함
        secondOrderTable.setTableGroupId(1L);
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(firstOrderTable, secondOrderTable));

        // than
        // 단체 테이블 등록 요청시 예외 발생
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 정상 등록")
    void 단체_테이블_정상_등록() {
        // given
        단체_테이블_생성_되어_있음();

        // and
        주문_테이블_생성되어_있음();
        firstOrderTable.setEmpty(true);
        secondOrderTable.setEmpty(true);
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        // when
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(firstOrderTable, secondOrderTable));

        // and
        // 단체 테이블 등록 요청
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        // than
        // 단체테이블 등록됨
        TableGroup expected = tableGroupService.create(tableGroup);
        List<OrderTable> orderTables = expected.getOrderTables();
        assertThat(orderTables.stream()
                .noneMatch(orderTable -> orderTable.isEmpty())).isTrue();
    }

    @Test
    @DisplayName("요리 중 이거나 식사중인 테이블 단체 테이블 해제 예외")
    void isCookingOrMealTable_exception() {
        // given
        단체_테이블_생성_되어_있음();

        // and
        주문_테이블_생성되어_있음();
        firstOrderTable.setEmpty(true);
        secondOrderTable.setEmpty(true);
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        // when
        // 아직 COOKING 또는 MEAL 상태
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L,2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        // than
        // 단체테이블 해제 오류
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블 정상 해제")
    void 단체_테이블_정상_해제() {
        // given
        단체_테이블_생성_되어_있음();

        // and
        주문_테이블_생성되어_있음();
        firstOrderTable.setEmpty(true);
        secondOrderTable.setEmpty(true);
        tableGroup.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));

        // when
        // COOKING 또는 MEAL 상태가 아님
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L,2L), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);

        // than
        // 단체 테이블 해제
        List<OrderTable> orderTables = tableGroupService.ungroup(tableGroup.getId());
        assertThat(orderTables.stream()
                .noneMatch(orderTable -> orderTable.getTableGroupId() != null)).isTrue();
    }


    private void 단체_테이블_생성_되어_있음() {
        tableGroup = new TableGroup();
        tableGroup.setId(1L);
    }

    private void 주문_테이블_생성되어_있음() {
        firstOrderTable = new OrderTable();
        firstOrderTable.setId(1L);
        secondOrderTable = new OrderTable();
        secondOrderTable.setId(2L);
    }
}

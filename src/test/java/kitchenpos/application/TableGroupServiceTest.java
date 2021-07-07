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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
    }

    @Test
    @DisplayName("주문 테이블이 없거나 2개 이하인 경우 예외가 발생한다")
    void notValidOrderTableLengthTest() {

        tableGroup.setOrderTables(Collections.emptyList());

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("요청한 단체 지정 주문 테이블 목록 개수와 실제 주문 테이블 목록 개수가 다른 경우 예외가 발생한다")
    void notMatchedOrderTablesLengthTest() {

        // given
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Collections.singletonList(orderTable1));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @MethodSource("provideLongAndBoolean")
    @DisplayName("주문 테이블이 비어 있지 않거나 단체 지정 되어있는 경우 예외가 발생한다")
    void notExistOrderTableOrNotMappedTableGroupTest(Long tableGroupId, boolean isEmpty) {

        // given
        orderTable1.setTableGroupId(tableGroupId);
        orderTable1.setEmpty(isEmpty);
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 생성한다")
    void createTest() {

        // given
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup).isNotNull();
        assertThat(savedTableGroup.getOrderTables()).hasSameElementsAs(tableGroup.getOrderTables());
    }

    @Test
    @DisplayName("단체 주문 내 주문 상태가 조리중 또는 식사중인 주문 테이블이 있는 경우 예외가 발생한다")
    void containsNotValidOrderTableStatusTest() {

        // given
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 취소 한다")
    void ungroupTest() {

        // given
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);

        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(false);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    private static Stream<Arguments> provideLongAndBoolean() {
        return Stream.of(
                Arguments.of(1L, true),
                Arguments.of(null, false)
        );
    }
}
package kitchenpos.application;

import static kitchenpos.utils.generator.OrderTableFixtureGenerator.generateEmptyOrderTable;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.generateNotEmptyOrderTable;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
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
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:TableGroup")
class TableGroupServiceTest {

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable firstOrderTable, secondOrderTable;
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        firstOrderTable = generateEmptyOrderTable();
        secondOrderTable = generateEmptyOrderTable();
        orderTables = Arrays.asList(firstOrderTable, secondOrderTable);
        tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    public void creatTableGroup() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(any(TableGroup.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        tableGroupService.create(tableGroup);

        // Then
        verify(orderTableDao).findAllByIdIn(anyList());
        verify(tableGroupDao).save(any(TableGroup.class));
        verify(orderTableDao, times(orderTables.size())).save(any(OrderTable.class));
    }

    @ParameterizedTest(name = "case[{index}] : {0} => {1}")
    @MethodSource
    @DisplayName("그룹핑할 주문 테이블이 2개 미만인 경우 예외 발생 검증")
    public void throwException_WhenOrderTableSizeIsLessThanMinimumGroupingTargetSize(
        final List<OrderTable> givenOrderTables,
        final String givenDescription
    ) {
        // Given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(givenOrderTables);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private static Stream<Arguments> throwException_WhenOrderTableSizeIsLessThanMinimumGroupingTargetSize() {
        return Stream.of(
            Arguments.of(Collections.emptyList(), "테이블 그룹이 0개인 경우"),
            Arguments.of(Collections.singletonList(new OrderTable()), "테이블 그룹이 1개인 경우")
        );
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블이 포함된 경우 예외 발생 검증")
    public void throwException_WhenOrderTableIsNotExist() {
        // Given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.emptyList());

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.create(tableGroup));

        verify(orderTableDao).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("비어있는 주문 테이블이 포함된 경우 예외 발생 검증")
    public void throwException_WhenContainsIsNotEmptyOrderTable() {
        // Given
        List<OrderTable> givenContainsNotEmptyOrderTables = Arrays
            .asList(generateNotEmptyOrderTable(), generateEmptyOrderTable());
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(givenContainsNotEmptyOrderTables);

        TableGroup givenTableGroup = new TableGroup();
        givenTableGroup.setOrderTables(givenContainsNotEmptyOrderTables);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.create(givenTableGroup));

        verify(orderTableDao).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("이미 그룹핑 된 주문 테이블이 포함된 경우 예외 발생 검증")
    public void throwException_ContainsAlreadyHasTableGroupOrderTable() {
        // Given
        OrderTable givenAlreadyHasTableGroupOrderTable = generateEmptyOrderTable();
        givenAlreadyHasTableGroupOrderTable.setTableGroupId(1L);

        List<OrderTable> givenContainsAlreadyHasTableGroupOrderTables = Arrays.asList(givenAlreadyHasTableGroupOrderTable, generateEmptyOrderTable());
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(givenContainsAlreadyHasTableGroupOrderTables);

        TableGroup givenTableGroup = new TableGroup();
        givenTableGroup.setOrderTables(givenContainsAlreadyHasTableGroupOrderTables);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.create(givenTableGroup));

        verify(orderTableDao).findAllByIdIn(anyList());
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    public void ungroupTable() {
        // Given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);

        // When
        tableGroupService.ungroup(tableGroup.getId());

        // Then
        verify(orderTableDao).findAllByTableGroupId(any());
        verify(orderDao).existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
        verify(orderTableDao, times(orderTables.size())).save(any(OrderTable.class));
    }

    @Test
    @DisplayName("조리중이거나 식사중인 주문 테이블이 포함된 경우 예외 발생 검증")
    public void throwException_WhenOrderTablesOrderHasMealStatusOrCookingStatus() {
        // Given
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableGroupService.ungroup(any()));

        verify(orderDao).existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList());
    }
}

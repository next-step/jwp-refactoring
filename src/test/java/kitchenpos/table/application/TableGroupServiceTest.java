package kitchenpos.table.application;

import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.*;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    private TableService tableService;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
        tableGroupService = new TableGroupService(tableService, tableGroupRepository);
    }

    @DisplayName("테이블그룹 생성 테스트")
    @Test
    void createTableGroupTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        final List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(),
                new OrderTables(orderTables));

        for (OrderTable orderTable : orderTables) {
            when(orderTableRepository.findById(orderTable.getId()))
                    .thenReturn(Optional.ofNullable(orderTable));
        }
        given(tableGroupRepository.save(any(TableGroup.class)))
                .willReturn(테이블그룹);

        //when
        final TableGroupResponse result = tableGroupService.create(
                new TableGroupRequest(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList())));

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(테이블그룹.getId()),
                () -> assertThat(result.getOrderTables().stream().map(OrderTableResponse::getId).collect(Collectors.toList()))
                        .containsAll(orderTables.stream().map(OrderTable::getId).collect(Collectors.toList())),
                () -> assertThat(result.getOrderTables().stream().map(OrderTableResponse::isEmpty).collect(Collectors.toList()))
                        .allMatch(empty -> !empty),
                () -> assertThat(result.getOrderTables().stream().map(OrderTableResponse::getTableGroupId).collect(Collectors.toList()))
                        .allMatch(테이블그룹.getId()::equals)
        );
    }

    @DisplayName("테이블 목록이 비어있을 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupSizeZeroExceptionTest() {
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Arrays.asList())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 목록 개수가 1인 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupSizeOneExceptionTest() {
        //given
        final OrderTable orderTable = new OrderTable(1L, null, new NumberOfGuests(4), true);

        //when
        when(orderTableRepository.findById(orderTable.getId()))
                .thenReturn(Optional.ofNullable(orderTable));

        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Arrays.asList(orderTable.getId()))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 목록에 있는 테이블이 존재하지 않는 경우 테이블그룹 생성 오류 발생")
    @Test
    void createTableGroupContainNotExistTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        final List<Long> ids = Arrays.asList(주문테이블1.getId(), 주문테이블2.getId());

        when(orderTableRepository.findById(주문테이블1.getId()))
                .thenReturn(Optional.ofNullable(null));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(ids)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블로 테이블그룹 생성할 경우 오류 발생 테스트")
    @Test
    void createTableGroupEmptyTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), false);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), false);
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        List<Long> ids = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        for (OrderTable orderTable : orderTables) {
            when(orderTableRepository.findById(orderTable.getId()))
                    .thenReturn(Optional.ofNullable(orderTable));
        }

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(ids)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블그룹이 이미 존재하는 테이블로 테이블그룹을 생성할 경우 오류 발생 테스트")
    @Test
    void createTableGroupAlreadyExistTableGroupTableExceptionTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), new OrderTables(orderTables));
        테이블그룹.group();

        List<Long> ids = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        for (OrderTable orderTable : orderTables) {
            when(orderTableRepository.findById(orderTable.getId()))
                    .thenReturn(Optional.ofNullable(orderTable));
        }

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(ids)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹해제 테스트")
    @Test
    void unGroupTableTest() {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        Map<Long, OrderTable> orderTableMap = orderTables.stream()
                .collect(Collectors.toMap(OrderTable::getId, orderTable -> orderTable));
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), new OrderTables(orderTables));
        테이블그룹.group();

        final Product 참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
        final Product 라볶이 = new Product(3L, "라볶이", new Price(new BigDecimal(4500)));
        final Product 돈까스 = new Product(4L, "돈까스", new Price(new BigDecimal(7000)));

        final MenuProduct 라볶이세트참치김밥 = new MenuProduct(참치김밥, new Quantity(1));
        final MenuProduct 라볶이세트라볶이 = new MenuProduct(라볶이, new Quantity(1));
        final MenuProduct 라볶이세트돈까스 = new MenuProduct(돈까스, new Quantity(1));

        final MenuGroup 분식 = new MenuGroup(1L, "분식");

        final MenuProducts 라볶이세트구성 = new MenuProducts(Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));
        final Menu 라볶이세트 = new Menu(1L, "라볶이세트", new Price(new BigDecimal(14000)), 분식, 라볶이세트구성);

        final OrderLineItem 주문항목1 = new OrderLineItem(1L, null, 라볶이세트, new Quantity(1));
        final OrderLineItem 주문항목2 = new OrderLineItem(2L, null, 라볶이세트, new Quantity(2));

        final Order 주문 = new Order(1L, 주문테이블1, OrderStatus.COMPLETION, LocalDateTime.now(),
                new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        when(tableGroupRepository.findById(테이블그룹.getId()))
                .thenReturn(Optional.ofNullable(테이블그룹));

        for(Long id : orderTableIds) {
            OrderTable orderTable = orderTableMap.get(id);
            when(orderTableRepository.findById(id))
                    .thenReturn(Optional.ofNullable(orderTable));
            when(orderRepository.findOrderByOrderTable(orderTable))
                    .thenReturn(Optional.ofNullable(주문));
        }

        //when
        tableGroupService.ungroup(테이블그룹.getId());

        //then
        assertThat(orderTables.stream().map(OrderTable::getTableGroup).collect(Collectors.toList()))
                .allMatch(Objects::isNull);
    }

    @DisplayName("조리중이거나 식사중인 테이블을 테이블그룹에서 해제할 경우 오류발생 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void createTableGroupContainCookingOrMealTableExceptionTest(OrderStatus orderStatus) {
        //given
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        Map<Long, OrderTable> orderTableMap = orderTables.stream()
                .collect(Collectors.toMap(OrderTable::getId, orderTable -> orderTable));
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now(), new OrderTables(orderTables));
        테이블그룹.group();

        final Product 참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
        final Product 라볶이 = new Product(3L, "라볶이", new Price(new BigDecimal(4500)));
        final Product 돈까스 = new Product(4L, "돈까스", new Price(new BigDecimal(7000)));

        final MenuProduct 라볶이세트참치김밥 = new MenuProduct(참치김밥, new Quantity(1));
        final MenuProduct 라볶이세트라볶이 = new MenuProduct(라볶이, new Quantity(1));
        final MenuProduct 라볶이세트돈까스 = new MenuProduct(돈까스, new Quantity(1));

        final MenuGroup 분식 = new MenuGroup(1L, "분식");

        final MenuProducts 라볶이세트구성 = new MenuProducts(Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));
        final Menu 라볶이세트 = new Menu(1L, "라볶이세트", new Price(new BigDecimal(14000)), 분식, 라볶이세트구성);

        final OrderLineItem 주문항목1 = new OrderLineItem(1L, null, 라볶이세트, new Quantity(1));
        final OrderLineItem 주문항목2 = new OrderLineItem(2L, null, 라볶이세트, new Quantity(2));

        final Order 주문 = new Order(1L, 주문테이블1, orderStatus, LocalDateTime.now(),
                new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        when(tableGroupRepository.findById(테이블그룹.getId()))
                .thenReturn(Optional.ofNullable(테이블그룹));

        for(Long id : orderTableIds) {
            OrderTable orderTable = orderTableMap.get(id);
            when(orderTableRepository.findById(id))
                    .thenReturn(Optional.ofNullable(orderTable));
            when(orderRepository.findOrderByOrderTable(orderTable))
                    .thenReturn(Optional.ofNullable(주문));
        }

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

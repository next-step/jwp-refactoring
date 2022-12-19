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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TableGroupServiceTest {

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private List<OrderTable> orderTables;
    private Map<Long, OrderTable> orderTableMap;
    private TableGroup 테이블그룹;

    private Product 참치김밥;
    private Product 라볶이;
    private Product 돈까스;

    private MenuProduct 라볶이세트참치김밥;
    private MenuProduct 라볶이세트라볶이;
    private MenuProduct 라볶이세트돈까스;

    private MenuGroup 분식;

    private MenuProducts 라볶이세트구성;
    private Menu 라볶이세트;

    private OrderLineItem 주문항목1;
    private OrderLineItem 주문항목2;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderTableRepository orderTableRepository;
    @MockBean
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private ApplicationEventPublisher publisher;
    private TableService tableService;
    private TableGroupHandler tableGroupHandler;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        테이블그룹 = new TableGroup(1L, LocalDateTime.now());
        주문테이블1 = new OrderTable(1L, 테이블그룹, new NumberOfGuests(4), false);
        주문테이블2 = new OrderTable(2L, 테이블그룹, new NumberOfGuests(4), false);
        orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        orderTableMap = orderTables.stream()
                .collect(Collectors.toMap(OrderTable::getId, orderTable -> orderTable));

        참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
        라볶이 = new Product(2L, "라볶이", new Price(new BigDecimal(4500)));
        돈까스 = new Product(3L, "돈까스", new Price(new BigDecimal(7000)));

        라볶이세트참치김밥 = new MenuProduct(참치김밥, new Quantity(1));
        라볶이세트라볶이 = new MenuProduct(라볶이, new Quantity(1));
        라볶이세트돈까스 = new MenuProduct(돈까스, new Quantity(1));

        분식 = new MenuGroup(1L, "분식");

        라볶이세트구성 = new MenuProducts(Arrays.asList(라볶이세트참치김밥, 라볶이세트라볶이, 라볶이세트돈까스));
        라볶이세트 = new Menu(1L, "라볶이세트", new Price(new BigDecimal(14000)), 분식, 라볶이세트구성);

        주문항목1 = new OrderLineItem(1L, null, 라볶이세트, new Quantity(1));
        주문항목2 = new OrderLineItem(2L, null, 라볶이세트, new Quantity(2));

        tableService = new TableService(orderRepository, orderTableRepository);
        tableGroupHandler = new TableGroupHandler(orderTableRepository, orderRepository);
        tableGroupService = new TableGroupService(publisher, tableGroupRepository, tableService);
    }

    @DisplayName("테이블그룹 생성 테스트")
    @Test
    void createTableGroupTest() {
        //given
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now());
        final OrderTable 주문테이블1 = new OrderTable(1L, null, new NumberOfGuests(4), true);
        final OrderTable 주문테이블2 = new OrderTable(2L, null, new NumberOfGuests(4), true);
        final List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);

        when(tableGroupRepository.save(any(TableGroup.class)))
                .thenReturn(테이블그룹);
        for (OrderTable orderTable : orderTables) {
            when(orderTableRepository.findById(orderTable.getId()))
                    .thenReturn(Optional.ofNullable(orderTable));
        }
        when(orderTableRepository.findAllByTableGroupId(테이블그룹.getId()))
                .thenReturn(orderTables);

        //when
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final TableGroupResponse result = tableGroupService.create(
                new TableGroupRequest(orderTableIds));

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(테이블그룹.getId()),
                () -> assertThat(orderTableResponsesToIds(result.getOrderTables()))
                        .containsAll(orderTableToIds(orderTables)),
                () -> assertThat(orderTableResponsesToBooleans(result.getOrderTables()))
                        .allMatch(empty -> !empty),
                () -> assertThat(orderTableResponsesToTableGroupIds(result.getOrderTables()))
                        .allMatch(테이블그룹.getId()::equals)
        );
    }

    private List<Long> orderTableResponsesToIds(List<OrderTableResponse> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());
    }

    private List<Long> orderTableToIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private List<Boolean> orderTableResponsesToBooleans(List<OrderTableResponse> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::isEmpty)
                .collect(Collectors.toList());
    }

    private List<Long> orderTableResponsesToTableGroupIds(List<OrderTableResponse> orderTables) {
        return orderTables.stream()
                .map(OrderTableResponse::getTableGroupId)
                .collect(Collectors.toList());
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
        final TableGroup 테이블그룹 = new TableGroup(1L, LocalDateTime.now());
        final OrderTable 주문테이블1 = new OrderTable(1L, 테이블그룹, new NumberOfGuests(4), false);
        final OrderTable 주문테이블2 = new OrderTable(2L, 테이블그룹, new NumberOfGuests(4), false);
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

    @DisplayName("그룹해제 테스트")
    @Test
    void unGroupTableTest() {
        //given
        final Order 주문 = new Order(1L, 주문테이블1, OrderStatus.COMPLETION, LocalDateTime.now(),
                new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));

        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        when(orderTableRepository.findAllByTableGroupId(테이블그룹.getId()))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        when(tableGroupRepository.findById(테이블그룹.getId()))
                .thenReturn(Optional.ofNullable(테이블그룹));

        for(Long id : orderTableIds) {
            OrderTable orderTable = orderTableMap.get(id);
            when(orderRepository.findOrderByOrderTable(orderTable))
                    .thenReturn(Optional.ofNullable(주문));
        }

        //when
        tableGroupService.ungroup(테이블그룹.getId());

        //then
        assertThat(orderTables.stream()
                .map(OrderTable::getTableGroup)
                .collect(Collectors.toList()))
                .allMatch(Objects::isNull);
    }

    @DisplayName("조리중이거나 식사중인 테이블을 테이블그룹에서 해제할 경우 오류발생 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void createTableGroupContainCookingOrMealTableExceptionTest(OrderStatus orderStatus) {
        //given
        final Order 주문 = new Order(1L, 주문테이블1, orderStatus, LocalDateTime.now(),
                new OrderLineItems(Arrays.asList(주문항목1, 주문항목2)));

        when(orderTableRepository.findAllByTableGroupId(테이블그룹.getId()))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        when(tableGroupRepository.findById(테이블그룹.getId()))
                .thenReturn(Optional.ofNullable(테이블그룹));

        when(orderRepository.findOrderByOrderTable(orderTables.get(0)))
                .thenReturn(Optional.ofNullable(주문));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

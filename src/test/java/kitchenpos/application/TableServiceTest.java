package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 서비스에 관련한 기능")
@SpringBootTest
class TableServiceTest {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;

    private OrderTable orderTable;

    @BeforeEach
    void beforeEach() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);
    }

    @DisplayName("`주문 테이블`을 생성한다.")
    @Test
    void createTable() {
        // Given
        OrderTableRequest request = new OrderTableRequest(3, true);

        // When
        OrderTable actual = tableService.create(request);

        // Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(actual.isEmpty()).isEqualTo(request.isEmpty()),
                () -> assertThat(actual.getTableGroupId()).isNull()
        );
    }

    @DisplayName("모든 `주문 테이블` 목록을 조회한다.")
    @Test
    void findAllTables() {
        // Given
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(orderTable));

        // When
        List<OrderTable> actual = tableService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(OrderTable::getId)
                        .containsExactly(orderTable.getId()),
                () -> assertThat(actual).extracting(OrderTable::getNumberOfGuests)
                        .containsExactly(orderTable.getNumberOfGuests()),
                () -> assertThat(actual).extracting(OrderTable::isEmpty)
                        .containsExactly(orderTable.isEmpty()),
                () -> assertThat(actual).extracting(OrderTable::getTableGroupId)
                        .containsExactly(orderTable.getTableGroupId())
        );
    }

    @DisplayName("`주문 테이블`을 비어있는 상태로 변경한다.")
    @Test
    void changeEmpty() {
        // Given
        OrderTable orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderTableRequest request = new OrderTableRequest(true);

        // When
        OrderTable actual = tableService.changeEmpty(orderTable.getId(), request);

        // Then
        assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("`주문 테이블`이 `단체 지정`되어 있으면 비어있는 상태로 변경할 수 없다.")
    @Test
    void exceptionToChangeEmptyWithTableGroup() {
        // Given
        OrderTable orderTable1 = tableService.create(new OrderTableRequest(3, true));
        OrderTable orderTable2 = tableService.create(new OrderTableRequest(3, true));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        tableGroupService.create(tableGroup);

        // When & Then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), new OrderTableRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문 테이블`의 `주문 상태`가 'COOKING' 이나 'MEAL' 상태가 이면 비어있는 상태로 변경할 수 없다.")
    @Test
    void exceptionToChangeEmptyWithCookingAndMeal() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        Menu 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTable orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        Order orderParams = new Order();
        orderParams.setOrderTableId(orderTable.getId());
        orderParams.setOrderLineItems(Collections.singletonList(menuParams));
        orderService.create(orderParams);

        // when & Then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), new OrderTableRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문 테이블`에 `방문한 손님 수`를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        Menu 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTable orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderLineItem menuParams = new OrderLineItem();
        menuParams.setMenuId(추천메뉴.getId());
        menuParams.setQuantity(1);
        Order orderParams = new Order();
        orderParams.setOrderTableId(orderTable.getId());
        orderParams.setOrderLineItems(Collections.singletonList(menuParams));
        orderService.create(orderParams);

        // When
        int updateNumberOfGuests = 5;
        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableRequest(updateNumberOfGuests));

        // Then
        assertThat(actual.getNumberOfGuests()).isEqualTo(updateNumberOfGuests);
    }

    @DisplayName("하나의 `주문 테이블`의 `방문한 손님 수`가 0명보다 적으면, `방문한 손님 수`를 변경할 수 없다.")
    @Test
    void exceptionToChangeNumberOfGuestsWithoutGuests() {
        // Given
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(-1);

        // When & Then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문 테이블`이 비어있으면, `방문한 손님 수`를 변경할 수 없다.")
    @Test
    void exceptionToChangeNumberOfGuestsWithoutOrderTable() {
        // Given
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        orderTable.setEmpty(true);
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(5);

        // When & Then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

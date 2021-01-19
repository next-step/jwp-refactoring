package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 서비스에 관련한 기능")
@SpringBootTest
class TableServiceTest {
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

    @DisplayName("`주문 테이블`을 생성한다.")
    @Test
    void createTable() {
        // Given
        OrderTableRequest request = new OrderTableRequest(3, true);

        // When
        OrderTableResponse actual = tableService.create(request);

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
        OrderTableRequest request = new OrderTableRequest(3, true);
        OrderTableResponse expected = tableService.create(request);

        // When
        List<OrderTableResponse> actual = tableService.list();

        // Then
        assertThat(actual).containsAnyElementsOf(Collections.singletonList(expected));
    }

    @DisplayName("`주문 테이블`을 비어있는 상태로 변경한다.")
    @Test
    void changeEmpty() {
        // Given
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderTableRequest request = new OrderTableRequest(true);

        // When
        OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), request);

        // Then
        assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("`주문 테이블`이 `단체 지정`되어 있으면 비어있는 상태로 변경할 수 없다.")
    @Test
    void exceptionToChangeEmptyWithTableGroup() {
        // Given
        OrderTableResponse savedOrderTable1 = tableService.create(new OrderTableRequest(3, true));
        OrderTableResponse savedOrderTable2 = tableService.create(new OrderTableRequest(3, true));
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(savedOrderTable1.toOrderTable(), savedOrderTable2.toOrderTable()));
        tableGroupService.create(request);

        // When & Then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), new OrderTableRequest(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문 테이블`의 `주문 상태`가 'COOKING' 이나 'MEAL' 상태가 이면 비어있는 상태로 변경할 수 없다.")
    @Test
    void exceptionToChangeEmptyWithCookingAndMeal() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(orderLineItemRequest));
        orderService.create(orderRequest);

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
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(orderLineItemRequest));
        orderService.create(orderRequest);

        // When
        int updateNumberOfGuests = 5;
        OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableRequest(updateNumberOfGuests));

        // Then
        assertThat(actual.getNumberOfGuests()).isEqualTo(updateNumberOfGuests);
    }

    @DisplayName("하나의 `주문 테이블`의 `방문한 손님 수`가 0명보다 적으면, `방문한 손님 수`를 변경할 수 없다.")
    @Test
    void exceptionToChangeNumberOfGuestsWithoutGuests() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(orderLineItemRequest));
        orderService.create(orderRequest);

        // When & Then
        int invalidNumberOfGuests = -1;
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableRequest(invalidNumberOfGuests)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문 테이블`이 비어있으면, `방문한 손님 수`를 변경할 수 없다.")
    @Test
    void exceptionToChangeNumberOfGuestsWithoutOrderTable() {
        // Given
        ProductResponse 짬뽕 = productService.create(new ProductRequest("짬뽕", BigDecimal.valueOf(8_000)));
        ProductResponse 짜장면 = productService.create(new ProductRequest("짜장면", BigDecimal.valueOf(6_000)));
        MenuGroupResponse 신메뉴그룹 = menuGroupService.create(new MenuGroupRequest("신메뉴그룹"));
        MenuResponse 추천메뉴 = menuService.create(new MenuRequest("추천메뉴", BigDecimal.valueOf(14_000), 신메뉴그룹.getId(),
                Arrays.asList(new MenuProductRequest(짬뽕.getId(), 1L), new MenuProductRequest(짜장면.getId(), 1L)))
        );
        OrderTableResponse orderTable = tableService.create(new OrderTableRequest(3, false));

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.singletonList(orderLineItemRequest));
        OrderResponse order = orderService.create(orderRequest);

        orderService.changeOrderStatus(order.getId(), new OrderRequest(OrderStatus.COMPLETION.name()));
        tableService.changeEmpty(orderTable.getId(), new OrderTableRequest(true));

        // When & Then
        int updateNumberOfGuests = 5;
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTableRequest(updateNumberOfGuests)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

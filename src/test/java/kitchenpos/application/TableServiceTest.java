package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
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

    private OrderTableRequest request;
    private OrderTableResponse orderTableResponse;

    @BeforeEach
    void beforeEach() {
        request = new OrderTableRequest(3, false);
        orderTableResponse = tableService.create(request);
    }

    @DisplayName("`주문 테이블`을 생성한다.")
    @Test
    void createTable() {
        // Then
        assertAll(
                () -> assertThat(orderTableResponse.getId()).isNotNull(),
                () -> assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(orderTableResponse.isEmpty()).isEqualTo(request.isEmpty()),
                () -> assertThat(orderTableResponse.getTableGroupId()).isNull()
        );
    }

    @DisplayName("모든 `주문 테이블` 목록을 조회한다.")
    @Test
    void findAllTables() {
        // When
        List<OrderTableResponse> actual = tableService.list();

        // Then
        assertThat(actual).containsAnyElementsOf(Collections.singletonList(orderTableResponse));
    }

    @DisplayName("`주문 테이블`을 비어있는 상태로 변경한다.")
    @Test
    void changeEmpty() {
        // When
        OrderTableResponse actual = tableService.changeEmpty(orderTableResponse.getId(), new OrderTableRequest(true));

        // Then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("`주문 테이블`이 `단체 지정`되어 있으면 비어있는 상태로 변경할 수 없다.")
    @Test
    void exceptionToChangeEmptyWithTableGroup() {
        // Given
        OrderTableResponse orderTableResponse1 = tableService.create(new OrderTableRequest(0, true));
        OrderTableResponse orderTableResponse2 = tableService.create(new OrderTableRequest(0, true));
        List<OrderTableRequest> orderTableRequests = Arrays.asList(new OrderTableRequest(orderTableResponse1.getId()),
                new OrderTableRequest(orderTableResponse2.getId()));
        tableGroupService.create(new TableGroupRequest(orderTableRequests));

        // When & Then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableResponse1.getId(), new OrderTableRequest(true)))
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

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTableResponse.getId(), Collections.singletonList(orderLineItemRequest));
        orderService.create(orderRequest);

        // when & Then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableResponse.getId(), new OrderTableRequest(true)))
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

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTableResponse.getId(), Collections.singletonList(orderLineItemRequest));
        orderService.create(orderRequest);

        // When
        int updateNumberOfGuests = 5;
        OrderTableResponse actual = tableService.changeNumberOfGuests(orderTableResponse.getId(), new OrderTableRequest(updateNumberOfGuests));

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

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTableResponse.getId(), Collections.singletonList(orderLineItemRequest));
        orderService.create(orderRequest);

        // When & Then
        int invalidNumberOfGuests = -1;
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(), new OrderTableRequest(invalidNumberOfGuests)))
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

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(추천메뉴.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTableResponse.getId(), Collections.singletonList(orderLineItemRequest));
        OrderResponse order = orderService.create(orderRequest);

        orderService.changeOrderStatus(order.getId(), new OrderRequest(OrderStatus.COMPLETION.name()));
        tableService.changeEmpty(orderTableResponse.getId(), new OrderTableRequest(true));

        // When & Then
        int updateNumberOfGuests = 5;
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(), new OrderTableRequest(updateNumberOfGuests)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

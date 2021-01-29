package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableServiceTest {

    public static final int THREE_GUEST_NUMBER = 3;
    public static final int NEGATIVE_GUEST_NUMBER = -3;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private MenuService menuService;

    private MenuResponse createdMenu;
    private OrderLineItemRequest orderLineItem;

    @BeforeEach
    void setUp() {
        MenuGroupRequest menuGroup = new MenuGroupRequest("양식");
        MenuGroupResponse createMenuGroup = menuGroupService.create(menuGroup);

        ProductRequest product = new ProductRequest("알리오올리오", BigDecimal.valueOf(12_000));
        ProductResponse createdProduct = productService.create(product);

        MenuProductRequest menuProduct = new MenuProductRequest(createdProduct.getId(), 1L);

        MenuRequest menu = new MenuRequest("알리오올리오",
            BigDecimal.valueOf(12_000),
            createMenuGroup.getId(),
            Collections.singletonList(menuProduct));
        createdMenu = menuService.create(menu);

        orderLineItem = new OrderLineItemRequest(createdMenu.getId(), 1L);


    }

    @Test
    @DisplayName("table 생성")
    void table_create_test() {
        //given
        OrderTableRequest tableRequest = TABLE_REQUEST_생성(true);
        //when
        OrderTableResponse createdTable = TABLE_생성_테스트(tableRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdTable.getId()).isNotNull();
        });
    }

    @Test
    @DisplayName("table 리스트 조회")
    void table_show_test() {
        //given
        OrderTableResponse createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        OrderTableResponse createdTable2 = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        //when
        List<OrderTableResponse> list = TABLE_조회_테스트();
        //then
        Assertions.assertAll(() -> {
            List<Long> createdIds = list.stream().map(OrderTableResponse::getId).collect(Collectors.toList());
            List<Long> requestIds = Arrays.asList(createdTable.getId(), createdTable2.getId());
            assertThat(createdIds).containsAll(requestIds);
        });
    }

    @Test
    @DisplayName("table의 order 변경하기")
    public void change_empty_test() throws Exception {
        //Given
        OrderTableResponse createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(false));

        //When
        OrderTableRequest orderTableRequest = TABLE_REQUEST_생성(createdTable.getId(), createdTable.getTableGroupId(),
            createdTable.getNumberOfGuests(), true);
        OrderTableResponse orderTable = tableService.changeEmpty(createdTable.getId(), orderTableRequest);

        //Then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("tableGroup인 경우가 테이블 비우기가 가능하다.")
    public void change_empty_table_group_test() throws Exception {
        //Given
        OrderTableResponse createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        OrderTableResponse createdTable2 = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        OrderTableRequest orderTableRequest = TABLE_REQUEST_생성(createdTable.getId(), createdTable.getTableGroupId(),
            createdTable.getNumberOfGuests(), false);
        OrderTableRequest orderTableRequest1 = TABLE_REQUEST_생성(createdTable2.getId(), createdTable2.getTableGroupId(),
            createdTable2.getNumberOfGuests(), false);
        TableGroupRequest tableGroup = new TableGroupRequest(Arrays.asList(orderTableRequest, orderTableRequest1));

        TableGroupResponse tableGroup1 = tableGroupService.create(tableGroup);

        //When
        //then
        OrderTableRequest orderTableRequest2 = TABLE_REQUEST_생성(createdTable.getId(),
            createdTable.getTableGroupId(),
            createdTable.getNumberOfGuests(), false);
        assertThatThrownBy(() -> {
            OrderTableResponse orderTable = tableService.changeEmpty(createdTable.getId(), orderTableRequest2);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("table에 OrderStatus가 Cooking이거나 Meal인 경우가 존재하지 아니어야 한다.")
    public void change_empty_order_status_not_complete_test(String status) throws Exception {
        //Given
        OrderTableResponse createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(false));
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(orderLineItem.getMenuId(), 1L);
        OrderRequest orderRequest = new OrderRequest(createdTable.getId(), status,
            Collections.singletonList(orderLineItem));
        OrderResponse orderResponse = orderService.create(orderRequest);

        //When
        //then
        assertThatThrownBy(() -> {
            OrderTableRequest orderTableRequest = TABLE_REQUEST_생성(createdTable.getId(), createdTable.getTableGroupId(),
                0, true);
            OrderTableResponse orderTable = tableService.changeEmpty(orderTableRequest.getId(), orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("table의 Guest 숫자 변경하기")
    public void change_guest_number() throws Exception {
        //Given
        OrderTableResponse createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(false));
        OrderTableRequest orderTableRequest = TABLE_REQUEST_생성(createdTable.getId(), createdTable.getTableGroupId(),
            THREE_GUEST_NUMBER, false);
        //When
        OrderTableResponse orderTable = tableService.changeNumberOfGuests(orderTableRequest.getId(),
            orderTableRequest);

        //Then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(THREE_GUEST_NUMBER);
    }

    @Test
    @DisplayName("guest의 숫자가 음수이면 안된다.")
    public void change_guest_number_negative() throws Exception {
        //Given
        OrderTableResponse createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        OrderTableRequest orderTableRequest = TABLE_REQUEST_생성(createdTable.getId(), createdTable.getTableGroupId(),
            NEGATIVE_GUEST_NUMBER, false);

        //When
        assertThatThrownBy(() -> {
            OrderTableResponse orderTable = tableService.changeNumberOfGuests(orderTableRequest.getId(),
                orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("guest의 숫자를 바꾸려는 table이 비어있으면 안된다.")
    public void change_guest_number_empty_table() throws Exception {
        //Given
        OrderTableResponse createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        OrderTableRequest orderTableRequest = TABLE_REQUEST_생성(createdTable.getId(), createdTable.getTableGroupId(),
            THREE_GUEST_NUMBER, true);
        //When
        assertThatThrownBy(() -> {
            OrderTableResponse orderTable = tableService.changeNumberOfGuests(createdTable.getId(),
                orderTableRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTableResponse> TABLE_조회_테스트() {
        return tableService.list();
    }


    private OrderTableResponse TABLE_생성_테스트(OrderTableRequest tableRequest) {
        return tableService.create(tableRequest);
    }

    private OrderTableRequest TABLE_REQUEST_생성(Long id, Long tableGroupID, int numberOfGuests, boolean empty) {
        return new OrderTableRequest(id, tableGroupID, numberOfGuests, empty);
    }

    private OrderTableRequest TABLE_REQUEST_생성(boolean empty) {
        return new OrderTableRequest(empty);
    }
}

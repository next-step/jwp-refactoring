package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
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

    private Menu createdMenu;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("양식");
        MenuGroup createMenuGroup = menuGroupService.create(menuGroup);

        Product product = new Product();
        product.setName("알리오올리오");
        product.setPrice(BigDecimal.valueOf(12_000));
        Product createdProduct = productService.create(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(createdProduct.getId());
        menuProduct.setQuantity(1L);

        Menu menu = new Menu();
        menu.setName("알리오올리오");
        menu.setPrice(BigDecimal.valueOf(12_000));
        menu.setMenuGroupId(createMenuGroup.getId());
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        createdMenu = menuService.create(menu);

        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(createdMenu.getId());


    }

    @Test
    @DisplayName("table 생성")
    void table_create_test() {
        //given
        OrderTable tableRequest = TABLE_REQUEST_생성(true);
        //when
        OrderTable createdTable = TABLE_생성_테스트(tableRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdTable.getId()).isNotNull();
        });
    }

    @Test
    @DisplayName("table 리스트 조회")
    void table_show_test() {
        //given
        OrderTable createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        OrderTable createdTable2 = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        //when
        List<OrderTable> list = TABLE_조회_테스트();
        //then
        Assertions.assertAll(() -> {
            List<Long> createdIds = list.stream().map(OrderTable::getId).collect(Collectors.toList());
            List<Long> requestIds = Arrays.asList(createdTable.getId(), createdTable2.getId());
            assertThat(createdIds).containsAll(requestIds);
        });
    }

    @Test
    @DisplayName("table의 order 비우기")
    public void change_empty_test() throws Exception {
        //Given
        OrderTable createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(false));

        //When
        createdTable.setEmpty(true);
        OrderTable orderTable = tableService.changeEmpty(createdTable.getId(), createdTable);

        //Then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("tableGroup인 경우가 테이블 비우기가 가능하다.")
    public void change_empty_table_group_test() throws Exception {
        //Given
        OrderTable createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        OrderTable createdTable2 = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(createdTable, createdTable2));
        TableGroup tableGroup1 = tableGroupService.create(tableGroup);

        //When
        //then
        assertThatThrownBy(() -> {
            createdTable.setEmpty(true);
            OrderTable orderTable = tableService.changeEmpty(createdTable.getId(), createdTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }


    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    @DisplayName("table에 OrderStatus가 Cooking이거나 Meal인 경우가 존재하지 아니어야 한다.")
    public void change_empty_order_status_not_complete_test(String status) throws Exception {
        //Given
        OrderTable createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(false));

        Order orderRequest = new Order();
        orderRequest.setOrderLineItems(Collections.singletonList(orderLineItem));
        orderRequest.setOrderTableId(createdTable.getId());
        orderRequest.setOrderStatus(status);
        Order order = orderService.create(orderRequest);

        //When
        //then
        assertThatThrownBy(() -> {
            createdTable.setEmpty(true);
            OrderTable orderTable = tableService.changeEmpty(createdTable.getId(), createdTable);
        }).isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("table의 Guest 숫자 변경하기")
    public void change_guest_number() throws Exception {
        //Given
        OrderTable createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(false));
        createdTable.setNumberOfGuests(THREE_GUEST_NUMBER);
        createdTable.setEmpty(false);

        //When
        OrderTable orderTable = tableService.changeNumberOfGuests(createdTable.getId(), createdTable);

        //Then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(THREE_GUEST_NUMBER);
    }

    @Test
    @DisplayName("guest의 숫자가 음수이면 안된다.")
    public void change_guest_number_negative() throws Exception {
        //Given
        OrderTable createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(false));
        createdTable.setNumberOfGuests(NEGATIVE_GUEST_NUMBER);
        createdTable.setEmpty(false);

        //When
        assertThatThrownBy(() -> {
            OrderTable orderTable = tableService.changeNumberOfGuests(createdTable.getId(), createdTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("guest의 숫자를 바꾸려는 table이 비어있으면 안된다.")
    public void change_guest_number_empty_table() throws Exception {
        //Given
        OrderTable createdTable = TABLE_생성_테스트(TABLE_REQUEST_생성(true));
        createdTable.setNumberOfGuests(THREE_GUEST_NUMBER);
        createdTable.setEmpty(false);

        //When
        assertThatThrownBy(() -> {
            OrderTable orderTable = tableService.changeNumberOfGuests(createdTable.getId(), createdTable);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderTable> TABLE_조회_테스트() {
        return tableService.list();
    }


    private OrderTable TABLE_생성_테스트(OrderTable tableRequest) {
        return tableService.create(tableRequest);
    }

    private OrderTable TABLE_REQUEST_생성(boolean empty) {
        OrderTable table = new OrderTable();
        table.setEmpty(empty);
        return table;
    }
}

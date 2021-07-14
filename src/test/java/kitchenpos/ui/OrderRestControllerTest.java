package kitchenpos.ui;

import kitchenpos.application.*;
import kitchenpos.domain.*;
import kitchenpos.dto.request.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderRestControllerTest extends ControllerTest {

    private final String ORDER_URI = "/api/orders";

    @Autowired
    private ProductService productService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderRequest 주문;
    private MenuProduct 후라이드;
    private MenuProduct 양념치킨;
    private MenuGroup 후라이드양념반반메뉴;
    private Product 후라이드상품;
    private Product 양념치킨상품;

    private List<OrderTable> orderTables;
    private OrderTable 테이블_1번;
    private OrderTable 테이블_2번;

    @BeforeEach
    void setUp() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("후라이드양념반반메뉴");
        후라이드양념반반메뉴 = menuGroupService.create(menuGroupRequest);
        후라이드상품 = productService.findById(1l);
        양념치킨상품 = productService.findById(2l);
        MenuRequest menuRequest = 메뉴를_생성한다(32000, 후라이드양념반반메뉴);
        Menu menu = menuService.create(menuRequest);

        테이블_1번 = 테이블을_생성한다(0, true);
        테이블_2번 = 테이블을_생성한다(0, true);
        orderTables = new ArrayList<>();
        orderTables.add(테이블_1번);
        orderTables.add(테이블_2번);

        TableGroup tableGroup = 테이블_그룹을_생성한다(new TableGroup(orderTables));
        OrderTable orderTable = 테이블을_생성한다(tableGroup, 0, false);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2l);
        주문 = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));
    }


    @DisplayName("주문을 등록한다")
    @Test
    void create() throws Exception {
        String body = objectMapper.writeValueAsString(주문);
        컨트롤러_생성_요청_및_검증(ORDER_URI, body);
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() throws Exception {
        컨트롤러_조회_요청_및_검증(ORDER_URI);
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void change() throws Exception {
        String body = objectMapper.writeValueAsString(주문);
        컨트롤러_생성_요청_및_검증(ORDER_URI, body);

        주문.setOrderStatus(OrderStatus.MEAL);
        body = objectMapper.writeValueAsString(주문);

        주문_변경_요청_및_검증(body, 1l);
    }

    private OrderTable 테이블을_생성한다(TableGroup tableGroup, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTableRequest(tableGroup.getId(), numberOfGuest, empty));
    }

    private OrderTable 테이블을_생성한다(int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTableRequest(numberOfGuest, empty));
    }

    private void 주문_변경_요청_및_검증(String body, Long id) throws Exception {
        mockMvc.perform(put(ORDER_URI + "/{id}" + "/order-status", id)
            .content(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk());
    }

    private MenuRequest 메뉴를_생성한다(int price, MenuGroup menuGroup) {
        Menu menu = new Menu("후라이드양념반반", BigDecimal.valueOf(price), menuGroup);
        후라이드 = new MenuProduct(menu, 후라이드상품, 1);
        양념치킨 = new MenuProduct(menu, 양념치킨상품, 1);
        menu.updateMenuProducts(Arrays.asList(후라이드, 양념치킨));
        return MenuRequest.of(menu);
    }

    private TableGroup 테이블_그룹을_생성한다(TableGroup tableGroup) {
        return tableGroupService.create(TableGroupRequest.of(tableGroup));
    }
}
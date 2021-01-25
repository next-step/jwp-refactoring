package kitchenpos.ui;

import kitchenpos.application.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;

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

    private Order 주문;
    private MenuProduct 메뉴상품_후라이드;
    private MenuProduct 메뉴상품_양념치킨;
    private MenuGroup 후라이드양념반반메뉴;

    @BeforeEach
    void setUp() {
        후라이드양념반반메뉴 = 메뉴그룹을_생성한다("후라이드양념반반메뉴");
        Menu menu = 메뉴를_생성한다(후라이드양념반반메뉴, "후라이드양념반반", 32000);
        Product 후라이드 = productService.findById(1l);
        Product 양념치킨 = productService.findById(2l);
        메뉴상품_후라이드 = new MenuProduct(menu, 후라이드, 1);
        메뉴상품_양념치킨 = new MenuProduct(menu, 양념치킨, 1);
        menu.updateMenuProducts(Arrays.asList(메뉴상품_후라이드,메뉴상품_양념치킨));

        TableGroup tableGroup = tableGroupService.findTableGroupById(1l);
        OrderTable orderTable = 테이블을_생성한다(tableGroup, 0, false);

        OrderLineItem orderLineItem = new OrderLineItem(menu, 2l);
        주문 = new Order(orderTable, Arrays.asList(orderLineItem));
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
        return tableService.create(new OrderTable(tableGroup, numberOfGuest, empty));
    }


    private void 주문_변경_요청_및_검증(String body, Long id) throws Exception {
        mockMvc.perform(put(ORDER_URI + "/{id}" + "/order-status", id)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Menu 메뉴를_생성한다(MenuGroup menuGroup, String name, int price, MenuProduct... products) {
        Menu menu = new Menu(name, BigDecimal.valueOf(price), menuGroup, Arrays.asList(products));
        return menuService.create(menu);
    }

    private MenuGroup 메뉴그룹을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupService.create(menuGroup);
    }
}
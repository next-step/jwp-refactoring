package kitchenpos.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class DomainTestUtils {

    protected final String MENU_GROUP_URI = "/api/menu-groups";
    protected final String MENU_URI = "/api/menus";
    protected final String PRODUCT_URI = "/api/products";
    protected final String TABLE_URI = "/api/tables";
    protected final String TABLE_GROUP_URI = "/api/table-groups";
    protected final String ORDER_URI = "/api/orders";

    public static final boolean 비어있음 = true;
    public static final boolean 비어있지않음 = false;

    protected int 게스트수;
    protected MenuGroup 후라이드양념반반메뉴;
    protected MenuProduct 메뉴상품_후라이드;
    protected MenuProduct 메뉴상품_양념치킨;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderTableDao orderTableDao;

    protected void 컨트롤러_생성_요청_및_검증(MockMvc mockMvc, String uri, String body) throws Exception {
        mockMvc.perform(post(uri)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    protected void 컨트롤러_조회_요청_및_검증(MockMvc mockMvc, String uri) throws Exception {
        mockMvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk());
    }

    protected MenuGroup 메뉴그룹을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupService.create(menuGroup);
    }

    protected Menu 메뉴를_생성한다(MenuGroup menuGroup, String name, int price, MenuProduct... products) {
        Menu 후라이드양념반반 = new Menu(name, BigDecimal.valueOf(price), menuGroup.getId(), Arrays.asList(products));
        return menuService.create(후라이드양념반반);
    }

    protected OrderTable 테이블을_생성한다(Long id, int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(id, numberOfGuest, empty));
    }

    protected TableGroup 테이블_그룹을_생성한다(TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    protected void 테이블_그룹을_비운다(Long id) {
        tableGroupService.ungroup(id);
    }

    protected Order 주문을_등록한다(Order order) {
        return orderService.create(order);
    }

}

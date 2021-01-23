package kitchenpos.ui;

import kitchenpos.application.DomainTestUtils;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

@AutoConfigureMockMvc
class OrderRestControllerTest extends DomainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    private Order 주문;

    @BeforeEach
    void setUp() {
        Product 후라이드 = productService.findById(1l);
        Product 양념치킨 = productService.findById(2l);
        메뉴상품_후라이드 = new MenuProduct(후라이드.getId(), 1);
        메뉴상품_양념치킨 = new MenuProduct(양념치킨.getId(), 1);
        후라이드양념반반메뉴 = 메뉴그룹을_생성한다("후라이드양념반반메뉴");

        Menu menu = 메뉴를_생성한다(후라이드양념반반메뉴, "후라이드양념반반", 32000, 메뉴상품_후라이드, 메뉴상품_양념치킨);
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(2l);
        OrderTable orderTable = 테이블을_생성한다(1l, 게스트수, 비어있지않음);

        주문 = new Order(orderTable.getId(), Arrays.asList(orderLineItem));
    }


    @DisplayName("주문을 등록한다")
    @Test
    void create() {

        //컨트롤러_생성_요청_및_검증(mockMvc, ORDER_URI, body);
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() {

    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void change() {

    }
}
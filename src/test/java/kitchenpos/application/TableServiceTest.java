package kitchenpos.application;

import kitchenpos.domain.*;
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

@SpringBootTest
class TableServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuService menuService;

    @Test
    void create() {
        // when
        OrderTable 주문불가능테이블 = tableService.create(OrderTable.of(0, true));

        // then
        assertThat(주문불가능테이블.getId()).isNotNull();
    }

    @Test
    void changeEmpty() {
        // given
        OrderTable 주문불가능테이블 = tableService.create(OrderTable.of(0, true));

        // when
        OrderTable 주문가능테이블 = tableService.changeEmpty(주문불가능테이블.getId(), OrderTable.from(false));

        // then
        assertThat(주문가능테이블.isEmpty()).isFalse();
    }

    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 주문불가능테이블 = tableService.create(OrderTable.of(0, true));
        OrderTable 주문가능테이블 = tableService.changeEmpty(주문불가능테이블.getId(), OrderTable.from(false));

        // when
        OrderTable 방문손님수변경 = tableService.changeNumberOfGuests(주문가능테이블.getId(), OrderTable.from(4));

        // then
        assertThat(방문손님수변경.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void create_throwException_ifNonAllowedOrderStatus() {
        // given
        OrderTable 주문불가능테이블 = tableService.create(OrderTable.of(0, true));
        OrderTable 주문가능테이블 = tableService.changeEmpty(주문불가능테이블.getId(), OrderTable.from(false));
        Product 제육볶음 = productService.create(Product.of("제육볶음", BigDecimal.valueOf(1000)));
        Product 소불고기 = productService.create(Product.of("소불고기", BigDecimal.valueOf(1000)));
        List<MenuProduct> 고기반찬 = Arrays.asList(MenuProduct.of(제육볶음.getId(), 1), MenuProduct.of(소불고기.getId(), 1));
        MenuGroup 점심특선 = 메뉴묶음_요청("점심특선");
        Menu 점심특선A = menuService.create(Menu.of("점심특선", BigDecimal.valueOf(2000), 점심특선.getId(), 고기반찬));
        List<OrderLineItem> 주문항목 = Collections.singletonList(OrderLineItem.of(점심특선A.getId(), 1));
        orderService.create(Order.of(주문가능테이블.getId(), 주문항목));

        // when
        // then
        assertAll(
                () -> assertThatThrownBy(() -> tableService.changeEmpty(주문불가능테이블.getId(), OrderTable.from(false))),
                () -> assertThatThrownBy(() -> tableService.changeEmpty(주문가능테이블.getId(), OrderTable.from(true)))
        );
    }

    MenuGroup 메뉴묶음_요청(String name) {
        return menuGroupService.create(new MenuGroup(name));
    }
}
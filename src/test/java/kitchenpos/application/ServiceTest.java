package kitchenpos.application;

import kitchenpos.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest
public class ServiceTest {

    public static final boolean 비어있음 = true;
    public static final boolean 비어있지않음 = false;

    public static int 게스트수;
    public static MenuGroup 후라이드양념반반메뉴;
    public static MenuProduct 메뉴상품_후라이드;
    public static MenuProduct 메뉴상품_양념치킨;

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

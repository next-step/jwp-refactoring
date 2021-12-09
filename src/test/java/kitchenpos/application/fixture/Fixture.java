package kitchenpos.application.fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class Fixture {
    public static MenuGroup 치킨_메뉴그룹 = new MenuGroup();
    public static MenuGroup 사이드_메뉴그룹 = new MenuGroup();

    public static Menu 뿌링클콤보 = new Menu();

    public static MenuProduct 뿌링클콤보_뿌링클치킨 = new MenuProduct();
    public static MenuProduct 뿌링클콤보_치킨무 = new MenuProduct();
    public static MenuProduct 뿌링클콤보_코카콜라 = new MenuProduct();

    public static Product 뿌링클치킨 = new Product();
    public static Product 치킨무 = new Product();
    public static Product 코카콜라 = new Product();

    public static Order 치킨주문 = new Order();
    public static OrderTable 치킨_주문_개인테이블 = new OrderTable();
    public static OrderTable 치킨_주문_단체테이블 = new OrderTable();
    public static OrderTable 치킨2_주문_단체테이블 = new OrderTable();
    public static OrderLineItem 치킨_주문항목 = new OrderLineItem();

    public static TableGroup 단체주문테이블 = new TableGroup();
    
    static {
        치킨_메뉴그룹.setId(1L);
        치킨_메뉴그룹.setName("치킨");

        사이드_메뉴그룹.setId(2L);
        사이드_메뉴그룹.setName("사이드");

        뿌링클콤보.setId(1L);
        뿌링클콤보.setName("뿌링클콤보");
        뿌링클콤보.setPrice(BigDecimal.valueOf(18_000));
        뿌링클콤보.setMenuGroupId(치킨_메뉴그룹.getId());
        뿌링클콤보.setMenuProducts(List.of(뿌링클콤보_뿌링클치킨, 뿌링클콤보_치킨무, 뿌링클콤보_코카콜라));

        뿌링클치킨.setId(1L);
        뿌링클치킨.setName("뿌링클치킨");
        뿌링클치킨.setPrice(BigDecimal.valueOf(15_000));

        치킨무.setId(2L);
        치킨무.setName("치킨무");
        치킨무.setPrice(BigDecimal.valueOf(1_000));

        코카콜라.setId(3L);
        코카콜라.setName("코카콜라");
        코카콜라.setPrice(BigDecimal.valueOf(3_000));

        뿌링클콤보_뿌링클치킨.setSeq(1L);
        뿌링클콤보_뿌링클치킨.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_뿌링클치킨.setProductId(뿌링클치킨.getId());
        뿌링클콤보_뿌링클치킨.setQuantity(1L);

        뿌링클콤보_치킨무.setSeq(2L);
        뿌링클콤보_치킨무.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_치킨무.setProductId(치킨무.getId());
        뿌링클콤보_치킨무.setQuantity(1L);

        뿌링클콤보_코카콜라.setSeq(3L);
        뿌링클콤보_코카콜라.setMenuId(뿌링클콤보.getId());
        뿌링클콤보_코카콜라.setProductId(코카콜라.getId());
        뿌링클콤보_코카콜라.setQuantity(1L);

        치킨주문.setId(1L);
        치킨주문.setOrderTableId(치킨_주문_단체테이블.getId());
        치킨주문.setOrderStatus("");
        치킨주문.setOrderedTime(LocalDateTime.now());
        치킨주문.setOrderLineItems(List.of(치킨_주문항목));
        
        치킨_주문항목.setSeq(1L);
        치킨_주문항목.setOrderId(치킨주문.getId());
        치킨_주문항목.setMenuId(뿌링클콤보.getId());
        치킨_주문항목.setQuantity(1L);

        치킨_주문_개인테이블.setId(3L);
        치킨_주문_개인테이블.setEmpty(false);

        치킨_주문_단체테이블.setId(1L);
        치킨_주문_단체테이블.setEmpty(true);
        치킨2_주문_단체테이블.setId(2L);
        치킨2_주문_단체테이블.setEmpty(true);

        단체주문테이블.setId(1L);
        단체주문테이블.setOrderTables(List.of(치킨_주문_단체테이블, 치킨2_주문_단체테이블));
        단체주문테이블.setCreatedDate(LocalDateTime.now());
    }
}

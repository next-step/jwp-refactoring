package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

public class UnitTestFixture {
    public final MenuGroup 구이류 = new MenuGroup(1L, "구이류");
    public final MenuGroup 식사류 = new MenuGroup(2L, "식사류");

    public final Product 삼겹살 = new Product(1L, "삼겹살", new Price(14000L));
    public final Product 목살 = new Product(2L, "목살", new Price(15000L));
    public final Product 김치찌개 = new Product(3L, "김치찌개", new Price(8000L));
    public final Product 공깃밥 = new Product(4L, "공깃밥", new Price(1000L));

    public final MenuProduct 돼지모듬_삼겹살 = new MenuProduct(1L, null, 1L, 2);
    public final MenuProduct 돼지모듬_목살 = new MenuProduct(2L, null, 2L, 1);
    public final MenuProduct 김치찌개정식_김치찌개 = new MenuProduct(3L, null, 3L, 1);
    public final MenuProduct 김치찌개정식_공깃밥 = new MenuProduct(4L, null, 4L, 1);

    public final Menu 돼지모듬 = new Menu(
            1L, "돼지모듬", new Price(43000L), 구이류.getId(), new MenuProducts(Arrays.asList(돼지모듬_삼겹살, 돼지모듬_목살)));
    public final Menu 김치찌개정식 = new Menu(
            2L, "김치찌개정식", new Price(9000L), 식사류.getId(), new MenuProducts(Arrays.asList(김치찌개정식_김치찌개, 김치찌개정식_공깃밥)));

    public final OrderLineItem 주문_항목1 = new OrderLineItem(1L, 1L, 1L, 2);
    public final OrderLineItem 주문_항목2 = new OrderLineItem(2L, 1L, 2L, 1);
    public final OrderLineItem 완료된_주문_항목 = new OrderLineItem(3L, 3L, 2L, 2);

    public final OrderTable 테이블 = new OrderTable(1L, null, 4, false);
    public final OrderTable 빈_테이블1 = new OrderTable(2L, null, 0, true);
    public final OrderTable 빈_테이블2 = new OrderTable(3L, null, 0, true);
    public final OrderTable 단체_지정_빈_테이블 = new OrderTable(4L, 1L, 0, true);
    public final OrderTable 단체_지정_테이블1 = new OrderTable(5L, 1L, 4, false);
    public final OrderTable 단체_지정_테이블2 = new OrderTable(6L, 1L, 4, false);

    public final Order 주문 = new Order(
            1L, 테이블.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(주문_항목1, 주문_항목2));
    public final Order 완료된_주문 = new Order(
            2L, 테이블.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(완료된_주문_항목));

    public final TableGroup 단체1 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(단체_지정_테이블1, 단체_지정_테이블2));
}

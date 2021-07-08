package kitchenpos.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

public class TextFixture {
	public static final OrderMenu 주문_메뉴_후라이드 = OrderMenu.of(1L, Name.valueOf("후라이드"), Price.wonOf(10000));

	public static final Product 후라이드치킨 = new Product(Name.valueOf("후라이드치킨"), Price.wonOf(1000));
	public static final Product 피자 = new Product(Name.valueOf("피자"), Price.wonOf(2000));

	public static final MenuGroup 치킨그룹 = new MenuGroup(Name.valueOf("치킨그룹"));
}

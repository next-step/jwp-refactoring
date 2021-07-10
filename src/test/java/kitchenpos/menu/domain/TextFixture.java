package kitchenpos.menu.domain;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

class TextFixture {
	static final Product 후라이드치킨 = new Product(Name.valueOf("후라이드치킨"), Price.wonOf(1000));
	static final Product 피자 = new Product(Name.valueOf("피자"), Price.wonOf(2000));

	static final MenuProduct 후라이드치킨2개 = new MenuProduct(후라이드치킨, 2);
	static final MenuProduct 피자3개 = new MenuProduct(피자, 3);
}

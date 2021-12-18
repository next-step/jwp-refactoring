package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;

public class MenuProductTest {

	public static final MenuProduct 후라이드_둘_세트_후라이드 = new MenuProduct(1L, MenuTest.후라이드_둘_세트.getId(),
		ProductTest.후라이드.getId(), 2);
	public static final MenuProduct 후라이드_양념_세트_후라이드 = new MenuProduct(2L, MenuTest.후라이드_양념_세트.getId(),
		ProductTest.후라이드.getId(), 1);
	public static final MenuProduct 후라이드_양념_세트_양념치킨 = new MenuProduct(3L, MenuTest.후라이드_양념_세트.getId(),
		ProductTest.양념치킨.getId(), 1);
	
	public static final List<MenuProduct> 후라이드_둘_세트_후라이드들 = Arrays.asList(후라이드_둘_세트_후라이드);
	public static final List<MenuProduct> 후라이드_양념_세트_후라이드_양념치킨 = Arrays.asList(후라이드_양념_세트_후라이드, 후라이드_양념_세트_양념치킨);
}

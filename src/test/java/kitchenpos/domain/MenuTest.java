package kitchenpos.domain;

import static kitchenpos.domain.MenuProductTest.*;

import java.math.BigDecimal;
import java.util.Arrays;

public class MenuTest {

	public static final Menu 후라이드_둘_세트 = new Menu(1L, "후라이드+후라이드", new BigDecimal(21_000), MenuGroupTest.치킨류.getId(),
		Arrays.asList(MenuProductTest.후라이드_둘_세트_후라이드));
	public static final Menu 후라이드_양념_세트 = new Menu(1L, "후라이드+양념", new BigDecimal(23_000), MenuGroupTest.치킨류.getId(),
		Arrays.asList(후라이드_양념_세트_후라이드, 후라이드_양념_세트_양념치킨));
}

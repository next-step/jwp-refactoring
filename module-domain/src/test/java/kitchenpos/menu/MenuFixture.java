package kitchenpos.menu;

import static kitchenpos.menu.MenuProductFixture.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;

import java.math.BigDecimal;
import java.util.Collections;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.ValidMenuValidator;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuFixture {
	public static Menu 후라이드후라이드_메뉴() {
		return Menu.of(
			1L,
			Name.from("후라이드+후라이드"),
			Price.from(BigDecimal.valueOf(19000)),
			추천_메뉴_그룹().getId(),
			Collections.singletonList(MenuProductDto.from(후라이드치킨_2개_메뉴_상품())),
			new ValidMenuValidator());
	}
}

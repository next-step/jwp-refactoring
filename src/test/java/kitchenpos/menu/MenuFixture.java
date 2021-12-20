package kitchenpos.menu;

import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.product.ProductFixture.*;

import java.math.BigDecimal;
import java.util.Collections;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductDto;

public class MenuFixture {
	public static Menu 후라이드후라이드_메뉴() {
		Menu menu = new Menu();
		menu.setId(1L);
		menu.setName("후라이드+후라이드");
		menu.setPrice(BigDecimal.valueOf(19000L));
		menu.setMenuGroupId(추천_메뉴_그룹().getId());
		menu.setMenuProducts(Collections.singletonList(후라이드후라이드_메뉴_상품()));
		return menu;
	}

	public static MenuProduct 후라이드후라이드_메뉴_상품() {
		MenuProduct menuProduct = new MenuProduct();
		menuProduct.setSeq(1L);
		menuProduct.setMenuId(1L);
		menuProduct.setProductId(후라이드치킨_상품().getId());
		menuProduct.setQuantity(2);
		return menuProduct;
	}

	public static MenuCreateRequest 후라이드후라이드_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"후라이드+후라이드",
			BigDecimal.valueOf(19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 양념양념_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"양념+양념",
			BigDecimal.valueOf(25000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 이름없는_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			null,
			BigDecimal.valueOf(19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 음수가격_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"사시면+돈도드려요",
			BigDecimal.valueOf(-19000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}

	public static MenuCreateRequest 너무비싼_메뉴_요청(Long menuGroupId, Long productId) {
		return new MenuCreateRequest(
			"사면+호구",
			BigDecimal.valueOf(100000L),
			menuGroupId,
			Collections.singletonList(
				new MenuProductDto(null, productId, 2)
			)
		);
	}
}

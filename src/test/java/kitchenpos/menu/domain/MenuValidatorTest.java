package kitchenpos.menu.domain;

import static kitchenpos.menu.MenuProductFixture.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.infra.MenuGroupsImpl;
import kitchenpos.menu.infra.ProductsImpl;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.infra.repository.InMemoryMenuGroupRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.infra.repository.InMemoryProductRepository;

class MenuValidatorTest {
	private MenuGroupRepository menuGroupRepository;
	private ProductRepository productRepository;
	private MenuValidator menuValidator;

	@BeforeEach
	void setUp() {
		menuGroupRepository = new InMemoryMenuGroupRepository();
		productRepository = new InMemoryProductRepository();
		menuValidator = new MenuValidatorImpl(
			new MenuGroupsImpl(menuGroupRepository),
			new ProductsImpl(productRepository));
	}

	@DisplayName("메뉴 그룹이 존재하는지 검증한다.")
	@Test
	void validateMenuGroupExist() {
		// given
		menuGroupRepository.save(추천_메뉴_그룹());

		// when
		menuValidator.validateMenuGroupExist(추천_메뉴_그룹().getId());

		// then
	}

	@DisplayName("메뉴 그룹이 존재하는지 검증한다. (실패)")
	@Test
	void validateMenuGroupExistFail() {
		// given

		// when
		ThrowingCallable throwingCallable = () -> menuValidator.validateMenuGroupExist(추천_메뉴_그룹().getId());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("상품이 존재하는지 검증한다.")
	@Test
	void validateProductsExist() {
		// given
		productRepository.save(후라이드치킨_상품());

		// when
		menuValidator.validateProductsExist(Collections.singletonList(MenuProductDto.from(후라이드치킨_2개_메뉴_상품())));

		// then
	}

	@DisplayName("상품이 존재하는지 검증한다. (실패)")
	@Test
	void validateProductsExistFail() {
		// given

		// when
		ThrowingCallable throwingCallable = () -> menuValidator.validateProductsExist(
			Collections.singletonList(MenuProductDto.from(후라이드치킨_2개_메뉴_상품())));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 가격이 메뉴 상품들의 총 가격 보다 작거나 같은지 검증한다.")
	@Test
	void validateMenuPriceIsLessThanOrEqualToTotalMenuProductsPrice() {
		// given
		productRepository.save(후라이드치킨_상품());

		// when
		menuValidator.validateMenuPriceIsLessThanOrEqualToTotalMenuProductsPrice(
			Price.from(BigDecimal.valueOf(10000)),
			Collections.singletonList(MenuProductDto.from(후라이드치킨_2개_메뉴_상품())));

		// then
	}

	@DisplayName("메뉴 가격이 메뉴 상품들의 총 가격 보다 작거나 같은지 검증한다. (실패)")
	@Test
	void validateMenuPriceIsLessThanOrEqualToTotalMenuProductsPriceFail() {
		// given
		productRepository.save(후라이드치킨_상품());

		// when
		ThrowingCallable throwingCallable = () -> menuValidator.validateMenuPriceIsLessThanOrEqualToTotalMenuProductsPrice(
			Price.from(BigDecimal.valueOf(100000)),
			Collections.singletonList(MenuProductDto.from(후라이드치킨_2개_메뉴_상품())));

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}
}

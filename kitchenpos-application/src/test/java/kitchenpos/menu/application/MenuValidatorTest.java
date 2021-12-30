package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.fixture.MenuTestFixture;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.fixture.ProductTestFixture;

@ExtendWith(MockitoExtension.class)
public class MenuValidatorTest {

	@InjectMocks
	MenuValidator menuValidator;
	@Mock
	ProductService productService;

	@DisplayName("가격이 구성품의 가격의 합보다 높으면 안된다")
	@Test
	void checkOverPriceTest() {
		// given
		Menu menu = Menu.of(1L, "후라이드들", BigDecimal.valueOf(50_000), MenuTestFixture.추천메뉴);
		MenuProduct 후라이드둘 = MenuProduct.of(1L, menu, ProductTestFixture.후라이드.getId(), 2L);
		menu.addMenuProducts(Collections.singletonList(후라이드둘));

		given(productService.getById(any())).willReturn(ProductTestFixture.후라이드);

		// when, then
		assertThatThrownBy(() -> menuValidator.isOverPrice(menu))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

}

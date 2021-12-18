package kitchenpos.application;

import static kitchenpos.domain.MenuProductTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.ProductTest;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@InjectMocks
	private MenuService menuService;

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuProductDao menuProductDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private ProductDao productDao;

	@DisplayName("메뉴 목록을 조회한다")
	@Test
	void listTest() {
		// given
		List<Menu> persist = new ArrayList<>();
		Menu menu1 = new Menu();
		persist.add(menu1);
		given(menuDao.findAll()).willReturn(persist);
		given(menuProductDao.findAllByMenuId(any()))
			.willReturn(menu1.getMenuProducts());

		// when
		List<Menu> result = menuService.list();

		// then
		assertThat(result.size()).isEqualTo(1);
	}

	@DisplayName("메뉴를 등록한다")
	@Test
	void createTest() {
		// given
		Menu menu = new Menu(null, "후라이드+양념", new BigDecimal(10_000),
			1L, Arrays.asList(후라이드_양념_세트_후라이드));
		Menu persist = new Menu(1L, "후라이드+양념", new BigDecimal(10_000),
			1L, Arrays.asList(후라이드_양념_세트_후라이드));

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(ProductTest.후라이드.getId()))
			.willReturn(Optional.of(ProductTest.후라이드));
		given(menuDao.save(any())).willReturn(persist);
		given(menuProductDao.save(후라이드_양념_세트_후라이드)).willReturn(후라이드_양념_세트_후라이드);

		// when
		Menu result = menuService.create(menu);

		// then
		assertThat(result.getName()).isEqualTo(menu.getName());

	}

	@DisplayName("메뉴 가격이 0 미만이면 등록할 수 없다")
	@Test
	void createTest2() {
		// given
		Menu menu = new Menu(null, "후라이드+양념", new BigDecimal(-1),
			1L, Arrays.asList(후라이드_양념_세트_후라이드));

		// when, then
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 그룹을 지정하지 않으면 등록할 수 없다")
	@Test
	void createTest3() {
		// given
		Menu menu = new Menu(null, "후라이드+양념", new BigDecimal(10_000),
			1L, Arrays.asList(후라이드_양념_세트_후라이드));

		given(menuGroupDao.existsById(any())).willReturn(false);

		// when, then
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 상품이 모두 등록되어 있어야 등록할 수 있다")
	@Test
	void createTest4() {
		// given
		Menu menu = new Menu(null, "후라이드+양념", new BigDecimal(10_000),
			1L, Arrays.asList(후라이드_양념_세트_후라이드));

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(ProductTest.후라이드.getId()))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 가격이 제품들의 가격 합과 같아야 한다")
	@Test
	void createTest5() {
		// given
		Menu menu = new Menu(null, "후라이드+양념", new BigDecimal(90_000),
			1L, Arrays.asList(후라이드_양념_세트_후라이드));

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(ProductTest.후라이드.getId()))
			.willReturn(Optional.of(ProductTest.후라이드));

		// when, then
		assertThatThrownBy(() -> menuService.create(menu))
			.isInstanceOf(IllegalArgumentException.class);
	}

}

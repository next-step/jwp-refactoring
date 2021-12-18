package kitchenpos.application;

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
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
		Product product1 = new Product();
		product1.setId(1L);
		product1.setPrice(new BigDecimal(10_000));
		MenuProduct menuProduct1 = new MenuProduct();
		menuProduct1.setSeq(1L);
		menuProduct1.setMenuId(1L);
		menuProduct1.setProductId(1L);
		menuProduct1.setQuantity(1);
		Menu persist = new Menu();
		persist.setId(1L);
		persist.setName("후라이드+양념");
		persist.setPrice(new BigDecimal(10_000));
		persist.setMenuGroupId(1L);
		persist.setMenuProducts(Arrays.asList(menuProduct1));

		Menu request = new Menu();
		request.setName("후라이드+양념");
		request.setPrice(new BigDecimal(10_000));
		request.setMenuGroupId(1L);
		request.setMenuProducts(Arrays.asList(menuProduct1));

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any()))
			.willReturn(Optional.of(product1));
		given(menuDao.save(any())).willReturn(persist);
		given(menuProductDao.save(any())).willReturn(menuProduct1);

		// when
		Menu result = menuService.create(request);

		// then
		assertThat(result.getName()).isEqualTo(request.getName());

	}

	@DisplayName("메뉴 가격이 0 미만이면 등록할 수 없다")
	@Test
	void createTest2() {
		// given
		Menu request = new Menu();
		request.setName("후라이드+양념");
		request.setPrice(new BigDecimal(-1));
		request.setMenuGroupId(1L);

		// when, then
		assertThatThrownBy(() -> menuService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 그룹을 지정하지 않으면 등록할 수 없다")
	@Test
	void createTest3() {
		// given
		Menu request = new Menu();
		request.setName("후라이드+양념");
		request.setPrice(new BigDecimal(10_000));

		given(menuGroupDao.existsById(any())).willReturn(false);

		// when, then
		assertThatThrownBy(() -> menuService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴의 상품이 모두 등록되어 있어야 등록할 수 있다")
	@Test
	void createTest4() {
		// given
		MenuProduct menuProduct1 = new MenuProduct();
		menuProduct1.setSeq(1L);
		menuProduct1.setMenuId(1L);
		menuProduct1.setProductId(1L);
		menuProduct1.setQuantity(1);

		Menu request = new Menu();
		request.setName("후라이드+양념");
		request.setPrice(new BigDecimal(10_000));
		request.setMenuGroupId(1L);
		request.setMenuProducts(Arrays.asList(menuProduct1));

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(ProductTest.후라이드.getId()))
			.willReturn(Optional.empty());

		// when, then
		assertThatThrownBy(() -> menuService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴 가격이 제품들의 가격 합과 같아야 한다")
	@Test
	void createTest5() {
		// given
		MenuProduct menuProduct1 = new MenuProduct();
		menuProduct1.setSeq(1L);
		menuProduct1.setMenuId(1L);
		menuProduct1.setProductId(1L);
		menuProduct1.setQuantity(1);

		Menu request = new Menu();
		request.setName("후라이드+양념");
		request.setPrice(new BigDecimal(90_000));
		request.setMenuGroupId(1L);
		request.setMenuProducts(Arrays.asList(menuProduct1));

		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(ProductTest.후라이드.getId()))
			.willReturn(Optional.of(ProductTest.후라이드));

		// when, then
		assertThatThrownBy(() -> menuService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
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
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	@Mock
	private MenuDao menuDao;
	@Mock
	private MenuGroupDao menuGroupDao;
	@Mock
	private MenuProductDao menuProductDao;
	@Mock
	private ProductDao productDao;
	@InjectMocks
	private MenuService menuService;

	private Menu A세트;
	private Menu B세트;
	private MenuProduct 탕수육중;
	private MenuProduct 깐풍기중;
	private Product 탕수육;
	private Product 깐풍기;
	private MenuGroup 중식;
	private List<MenuProduct> menuProducts;

	@BeforeEach
	void setUp() {
		중식 = new MenuGroup(1L, "중식");
		Price 탕수육가격 = new Price(BigDecimal.valueOf(10000));
		Price 깐풍기가격 = new Price(BigDecimal.valueOf(12000));
		탕수육 = new Product(1L, "탕수육", 탕수육가격);
		깐풍기 = new Product(2L, "깐풍기", 깐풍기가격);

		탕수육중 = new MenuProduct(1L, 1L, 탕수육.getId(), 1);
		깐풍기중 = new MenuProduct(2L, 1L, 깐풍기.getId(), 1);

		menuProducts = new ArrayList<>();
		menuProducts.add(탕수육중);
		menuProducts.add(깐풍기중);

		BigDecimal A세트가격 = BigDecimal.valueOf(20000);
		BigDecimal B세트가격 = BigDecimal.valueOf(23000);
		A세트 = new Menu(1L, "A세트", A세트가격, 중식.getId(), menuProducts);
		B세트 = new Menu(1L, "B세트", B세트가격, 중식.getId(), menuProducts);
	}

	@DisplayName("Menu 생성을 테스트 - happy path")
	@Test
	void testCreateMenu() {
		when(menuGroupDao.existsById(A세트.getMenuGroupId())).thenReturn(true);
		when(productDao.findById(탕수육중.getProductId())).thenReturn(Optional.of(탕수육));
		when(productDao.findById(깐풍기중.getProductId())).thenReturn(Optional.of(깐풍기));
		when(menuDao.save(A세트)).thenReturn(A세트);
		when(menuProductDao.save(탕수육중)).thenReturn(탕수육중);
		when(menuProductDao.save(깐풍기중)).thenReturn(깐풍기중);

		Menu actual = menuService.create(A세트);

		List<Long> actualMenuProductsId = actual.getMenuProducts()
			.stream()
			.map(MenuProduct::getProductId)
			.collect(Collectors.toList());
		List<Long> expectedMenuProductIds = A세트.getMenuProducts()
			.stream()
			.map(MenuProduct::getProductId)
			.collect(Collectors.toList());
		assertThat(actualMenuProductsId).containsExactlyElementsOf(expectedMenuProductIds);
		assertThat(actual.getName()).isEqualTo(A세트.getName());
	}

	@DisplayName("메뉴 가격이 0보다 작은경우 오류발생")
	@Test
	void testCreateErrorPriceZero() {
		BigDecimal price = BigDecimal.valueOf(-1);
		Menu menu = new Menu(1L, "menu", price, 1L, null);

		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 가격은 0원 미만이 될 수 없습니다.");
	}

	@DisplayName("메뉴가 메뉴 그룹에 포함되어있지 않은경우 오류 발생")
	@Test
	void testMenuNotContainsInMenuGroup() {
		BigDecimal price = BigDecimal.valueOf(20000);
		Menu menu = new Menu(1L, "menu", price, 1L, null);
		Long menuGroupId = menu.getMenuGroupId();

		when(menuGroupDao.existsById(menuGroupId)).thenReturn(false);

		assertThatThrownBy(() -> {
			menuService.create(menu);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴가 메뉴그룹에 등록되지 않았습니다.");
	}

	@DisplayName("메뉴의 메뉴상품이 상품에 등록되어 있지 않은경우 오류 발생")
	@Test
	void testMenuProductNotSavedProduct() {
		when(menuGroupDao.existsById(A세트.getMenuGroupId())).thenReturn(true);
		when(productDao.findById(탕수육중.getProductId())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			menuService.create(A세트);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("상품에 없는 메뉴상품입니다.");
	}

	@DisplayName("메뉴 가격이 메뉴 상품의 가격의 합보다 크면 오류 발생")
	@Test
	void testMenuPriceBiggerThanTotalMenuProductPrice() {
		when(menuGroupDao.existsById(B세트.getMenuGroupId())).thenReturn(true);
		when(productDao.findById(탕수육중.getProductId())).thenReturn(Optional.empty());
		when(productDao.findById(탕수육중.getProductId())).thenReturn(Optional.of(탕수육));
		when(productDao.findById(깐풍기중.getProductId())).thenReturn(Optional.of(깐풍기));

		assertThatThrownBy(() -> {
			menuService.create(B세트);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("메뉴 가격은 메뉴 상품 가격의 합보다 작아야합니다.");
	}
}
package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
import kitchenpos.domain.Product;

@DisplayName("메뉴 그룹 기능 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

	private MenuGroup 메뉴그룹;
	private Menu 냉면;
	private Menu 삼겹살;
	private Product product;
	private MenuProduct menuProduct;
	private List<MenuProduct> menuProducts;

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuGroupDao menuGroupDao;

	@Mock
	private ProductDao productDao;

	@Mock
	private MenuProductDao menuProductDao;

	@InjectMocks
	private MenuService menuService;

	@BeforeEach
	void setup() {
		메뉴그룹 = new MenuGroup();
		메뉴그룹.setId(2L);
		메뉴그룹.setName("계절메뉴");

		냉면 = new Menu();
		냉면.setId(1L);
		냉면.setMenuGroupId(메뉴그룹.getId());
		냉면.setName("냉면");
		냉면.setPrice(new BigDecimal(9000));

		product = new Product();
		product.setId(1L);
		product.setName("물냉면");
		product.setPrice(new BigDecimal(9000));

		menuProduct = new MenuProduct();
		menuProduct.setMenuId(냉면.getId());
		menuProduct.setProductId(product.getId());
		menuProduct.setSeq(1L);
		menuProduct.setQuantity(5);

		menuProducts = new ArrayList<>();
		menuProducts.add(menuProduct);

		냉면.setMenuProducts(menuProducts);
	}

	@DisplayName("메뉴를 생성할 수 있다.")
	@Test
	public void create() {
		// given
		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.of(product));
		given(menuProductDao.save(menuProduct)).willReturn(menuProduct);
		given(menuDao.save(냉면)).willReturn(냉면);

		// when
		Menu createdMenu = menuService.create(냉면);

		// then
		assertThat(createdMenu.getId()).isEqualTo(this.냉면.getId());
		assertThat(createdMenu.getName()).isEqualTo(this.냉면.getName());
		assertThat(createdMenu.getPrice()).isEqualTo(this.냉면.getPrice());
	}

	@DisplayName("메뉴의 가격은 0원이하로 설정할 수 없다.")
	@Test
	public void createInvalidPrice() {
		// given
		냉면.setPrice(BigDecimal.ZERO);

		// when
		assertThrows(IllegalArgumentException.class, () -> {
			menuService.create(냉면);
		});
	}

	@DisplayName("메뉴의 속한 상품의 금액 합계와 메뉴 가격보다 크거나 같아야한다.")
	@Test
	public void overPrice() {
		// given
		냉면.setPrice(new BigDecimal(99999));
		given(menuGroupDao.existsById(any())).willReturn(true);
		given(productDao.findById(any())).willReturn(Optional.of(product));

		// when
		assertThrows(IllegalArgumentException.class, () -> {
			menuService.create(냉면);
		});
	}

	@DisplayName("메뉴는 특정 메뉴그룹에 속해있어야한다.")
	@Test
	public void menuExistByMenuGroup() {
		// given
		냉면.setMenuGroupId(null);
		// when
		assertThrows(IllegalArgumentException.class, () -> {
			menuService.create(냉면);
		});
	}

	@DisplayName("메뉴의 속한 상품이 존재하지 않는 경우, 등록할 수 없다.")
	@Test
	public void menuNotExistProduct() {
		// given
		given(menuGroupDao.existsById(any())).willReturn(true);

		// when
		assertThrows(IllegalArgumentException.class, () -> {
			menuService.create(냉면);
		});
	}


	@Test
	@DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
	public void list() {
		// given
		given(menuDao.findAll()).willReturn(Arrays.asList(냉면));
		given(menuProductDao.findAllByMenuId(냉면.getId())).willReturn(menuProducts);

		// when
		List<Menu> menus = menuService.list();

		// then
		assertThat(menus).containsExactly(냉면);
	}
}

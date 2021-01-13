package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.MySpringBootTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySpringBootTest
public class MenuServiceTest {

	@Autowired
	private MenuService menuService;
	@Autowired
	private MenuGroupDao menuGroupDao;
	@Autowired
	private ProductDao productDao;

	@DisplayName("메뉴를 등록한다.")
	@Test
	void create() {
		//given
		Optional<MenuGroup> menuGroup = menuGroupDao.findById(1L);
		Optional<Product> product1 = productDao.findById(1L);
		Optional<Product> product2 = productDao.findById(2L);
		BigDecimal totalPrice = product1.get().getPrice().add(product2.get().getPrice());

		List<MenuProduct> menuProducts = 메뉴_상품정보를_세팅한다(product1.get().getId(),
			  product2.get().getId());

		Menu menu = new Menu(
			  "치킨 두마리 세트",
			  totalPrice,
			  menuGroup.get().getId(),
			  menuProducts
		);

		//when
		Menu savedMenu = menuService.create(menu);

		//then
		assertThat(savedMenu.getMenuProducts()).hasSize(2);
		assertThat(menuService.list()).contains(savedMenu);
	}

	@DisplayName("미등록 메뉴그룹의 메뉴는 등록할 수 없다.")
	@Test
	void createWithNotExistMenuGroup() {
		Menu menu = new Menu(
			  "치킨 두마리 세트",
			  new BigDecimal(0),
			  999999L,
			  new ArrayList<>()
		);

		//when
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> menuService.create(menu))
			  .withMessage("메뉴그룹을 찾을 수 없습니다.");
	}

	@DisplayName("가격이 0원 미만이거나 메뉴상품 총 가격과 불일치하는 상품은 등록할 수 없다.")
	@Test
	void createWithWrongPrice() {
		Optional<MenuGroup> menuGroup = menuGroupDao.findById(1L);
		Optional<Product> product1 = productDao.findById(1L);
		Optional<Product> product2 = productDao.findById(2L);
		BigDecimal totalPrice = product1.get().getPrice().add(product2.get().getPrice());

		List<MenuProduct> menuProducts = 메뉴_상품정보를_세팅한다(product1.get().getId(),
			  product2.get().getId());

		final Menu menu = new Menu(
			  "치킨 두마리 세트",
			  new BigDecimal(-1),
			  menuGroup.get().getId(),
			  menuProducts
		);

		//when, then
		assertThatIllegalArgumentException()
			  .isThrownBy(() -> menuService.create(menu))
			  .withMessage("메뉴의 가격은 0원 이상이어야 합니다.");

		//when
		final Menu newMenu = new Menu(
			  "치킨 두마리 세트",
			  totalPrice.add(new BigDecimal(1)),
			  menuGroup.get().getId(),
			  menuProducts
		);

		assertThatIllegalArgumentException()
			  .isThrownBy(() -> menuService.create(newMenu))
			  .withMessage("메뉴의 가격과 메뉴 항목들의 총 가격의 합이 맞지 않습니다.");
	}

	private List<MenuProduct> 메뉴_상품정보를_세팅한다(long... productIdList) {
		return Arrays.stream(productIdList)
			  .mapToObj(productId -> {
				  MenuProduct menuProduct = new MenuProduct(productId, 1);
				  return menuProduct;
			  })
			  .collect(Collectors.toList());
	}
}

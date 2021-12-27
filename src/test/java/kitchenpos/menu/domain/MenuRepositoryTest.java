package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.common.Price;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

@DataJpaTest
class MenuRepositoryTest {

	private static Product product = new Product(1L, "양념치킨", Price.valueOf(new BigDecimal(15000)));
	private static MenuProduct menuProduct = new MenuProduct(null, product, 2);
	private static MenuGroup menuGroup = new MenuGroup(1L, "신메뉴");

	@Autowired
	private MenuRepository menuRepository;

	@Test
	@DisplayName("메뉴 저장 테스트")
	public void saveMenuTest() {
		//given
		//when
		MenuProducts menuProducts = new MenuProducts(Lists.newArrayList(menuProduct));
		Menu menu = new Menu("신양념", Price.valueOf(new BigDecimal(20000)), menuGroup, menuProducts);

		//when
		Menu save = menuRepository.save(menu);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(7L);
		assertThat(save.getName()).isEqualTo("신양념");
	}

	@Test
	@DisplayName("메뉴 목록 조회 테스트")
	public void findAllMenuTest() {
		//given

		//when
		List<Menu> menus = menuRepository.findAll();

		//then
		assertThat(menus).hasSizeGreaterThanOrEqualTo(6);
	}

	@Test
	@DisplayName("메뉴 id로 조회 테스트")
	public void findByIdMenuTest() {
		//given

		//when
		Menu menu = menuRepository.findById(6L).orElse(new Menu());

		//then
		assertThat(menu.getName()).isEqualTo("순살치킨");
	}

	@Test
	@DisplayName("없는 메뉴 id로 조회 테스트")
	public void findByIdMenuFailTest() {
		//given

		//when
		Menu menu = menuRepository.findById(99L).orElse(new Menu());

		//then
		assertThat(menu.getId()).isNull();
	}

}

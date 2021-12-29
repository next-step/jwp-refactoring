package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.common.domain.Quantity;

@DataJpaTest
class MenuProductRepositoryTest {

	private static Menu menu = new Menu(1L);

	@Autowired
	private MenuProductRepository menuProductRepository;

	@Test
	@DisplayName("메뉴상품 저장 테스트")
	public void saveMenuProductTest() {
		//given
		//when
		MenuProduct menuProduct = new MenuProduct(menu.getId(), 1L, Quantity.valueOf(2L));

		//when
		MenuProduct save = menuProductRepository.save(menuProduct);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getSeq()).isEqualTo(7L);
	}

	@Test
	@DisplayName("메뉴상품 목록 조회 테스트")
	public void findAllMenuProductTest() {
		//given

		//when
		List<MenuProduct> menuProducts = menuProductRepository.findAll();

		//then
		assertThat(menuProducts).hasSizeGreaterThanOrEqualTo(6);
	}

	@Test
	@DisplayName("메뉴상품 id로 조회 테스트")
	public void findByIdMenuProductTest() {
		//given

		//when
		MenuProduct menuProduct = menuProductRepository.findById(6L).orElse(new MenuProduct());

		//then
		assertThat(menuProduct.getSeq()).isEqualTo(6L);
	}

	@Test
	@DisplayName("없는 메뉴상품 id로 조회 테스트")
	public void findByIdMenuProductFailTest() {
		//given

		//when
		MenuProduct menuProduct = menuProductRepository.findById(99L).orElse(new MenuProduct());

		//then
		assertThat(menuProduct.getSeq()).isNull();
	}

	@Test
	@DisplayName("메뉴 id로 조회 테스트")
	public void findAllByMenuId() {
		//given

		//when
		List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(1L);

		//then
		assertThat(menuProducts).hasSize(1);
	}

}

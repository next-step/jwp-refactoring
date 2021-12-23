package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@Transactional
@SpringBootTest
class MenuRestControllerTest {

	@Autowired
	private MenuRestController menuRestController;

	@Test
	@DisplayName("메뉴 생성 테스트")
	public void crateMenuTest() {
		//given
		MenuProduct menuProduct = new MenuProduct(null, 1L, 1L, 2);
		Menu menu = new Menu(null, "후라이드+후라이드", new BigDecimal(19000), 1L, Lists.newArrayList(menuProduct));

		//when
		ResponseEntity<Menu> responseEntity = menuRestController.create(menu);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("/api/menus/7");
	}

	@Test
	@DisplayName("메뉴 목록 조회 테스트")
	public void findAllMenuTest() {
		//given
		//when
		ResponseEntity<List<Menu>> responseEntity = menuRestController.list();

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).hasSize(6);
	}

}

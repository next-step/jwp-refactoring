package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;

@Transactional
@SpringBootTest
class MenuGroupRestControllerTest {

	@Autowired
	private MenuGroupRestController menuGroupRestController;

	@Test
	@DisplayName("메뉴그룹 생성 테스트")
	public void crateMenuGroupTest() {
		//given
		MenuGroup menuGroup = new MenuGroup(null, "후라이드+양념");

		//when
		ResponseEntity<MenuGroup> responseEntity = menuGroupRestController.create(menuGroup);

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("/api/menu-groups/5");
	}

	@Test
	@DisplayName("메뉴그룹 목록 조회 테스트")
	public void findAllMenuGroupTest() {
		//given
		//when
		ResponseEntity<List<MenuGroup>> responseEntity = menuGroupRestController.list();

		//then
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseEntity.getBody()).hasSize(4);
	}

}

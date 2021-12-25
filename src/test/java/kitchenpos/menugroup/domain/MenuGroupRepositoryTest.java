package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuGroupRepositoryTest {

	@Autowired
	MenuGroupRepository menuGroupRepository;

	@Test
	@DisplayName("메뉴그룹 저장 테스트")
	public void saveMenuGroupTest() {
		//given
		MenuGroup menuGroup = new MenuGroup(null, "후라이드+양념");

		//when
		MenuGroup save = menuGroupRepository.save(menuGroup);

		//then
		assertThat(save).isNotNull();
		assertThat(save).isEqualTo(new MenuGroup(5L, "후라이드+양념"));
	}

	@Test
	@DisplayName("메뉴그룹 목록 조회 테스트")
	public void findAllMenuGroupTest() {
		//given

		//when
		List<MenuGroup> menuGroups = menuGroupRepository.findAll();

		//then
		assertThat(menuGroups).hasSize(4);
	}

	@Test
	@DisplayName("메뉴그룹 id로 조회 테스트")
	public void findByIdMenuGroupTest() {
		//given

		//when
		MenuGroup menuGroup = menuGroupRepository.findById(3L).orElse(new MenuGroup());

		//then
		assertThat(menuGroup.getName()).isEqualTo("순살파닭두마리메뉴");
	}

}

package kitchenpos.web.menu.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.menu.domain.MenuGroup;
import kitchenpos.web.menu.dto.MenuGroupRequest;
import kitchenpos.web.menu.dto.MenuGroupResponse;
import kitchenpos.web.menu.repository.MenuGroupRepository;
import kitchenpos.utils.IntegrationTest;

/**
 * @author : byungkyu
 * @date : 2021/01/19
 * @description :
 **/
@DisplayName("메뉴그룹")
class MenuGroupServiceTest extends IntegrationTest {

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private MenuGroupRepository menuGroupRepository;

	@AfterEach
	void cleanUp() {
		menuGroupRepository.deleteAllInBatch();
	}

	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void create() {
		// given
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest("한식");

		// when
		MenuGroupResponse savedMenuGroupResponse = menuGroupService.create(menuGroupRequest);

		// then
		assertThat(savedMenuGroupResponse.getId()).isNotNull();
		assertThat(savedMenuGroupResponse.getName()).isEqualTo(menuGroupRequest.getName());
	}

	@DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		MenuGroup dummyMenuGroup = new MenuGroup("한식");
		MenuGroup savedMenuGroup = menuGroupRepository.save(dummyMenuGroup);

		// when
		List<MenuGroupResponse> menuGroups = menuGroupService.list();
		List<Long> productNames = menuGroups.stream()
			.map(menuGroup -> menuGroup.getId())
			.collect(Collectors.toList());

		//then
		assertThat(productNames).isNotEmpty();
		assertThat(productNames).contains(savedMenuGroup.getId());
	}
}
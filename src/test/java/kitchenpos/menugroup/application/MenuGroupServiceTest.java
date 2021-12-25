package kitchenpos.menugroup.application;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupAddRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

	@InjectMocks
	private MenuGroupService menuGroupService;

	@Mock
	private MenuGroupRepository menuGroupRepository;

	@DisplayName("메뉴그룹 생성")
	@Test
	void create() {
		final MenuGroup 일식 = menuGroup(1L, "일식");
		given(menuGroupRepository.save(any())).willReturn(일식);

		final MenuGroupResponse created = menuGroupService.create(
			MenuGroupAddRequest.of("일식")
		);

		assertThat(created.getId()).isNotNull();
	}

	@DisplayName("메뉴그룹 목록 조회")
	@Test
	void list() {
		final MenuGroup 한식 = menuGroup(1L, "한식");
		final MenuGroup 중식 = menuGroup(2L, "중식");
		given(menuGroupRepository.findAll()).willReturn(Arrays.asList(한식, 중식));

		assertThat(menuGroupService.list().size()).isEqualTo(2);
	}
}

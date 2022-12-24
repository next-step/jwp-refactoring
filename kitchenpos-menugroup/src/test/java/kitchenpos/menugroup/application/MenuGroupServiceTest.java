package kitchenpos.menugroup.application;

import static kitchenpos.fixture.MenuGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 등록 API")
    @Test
    void create() {
        // given
        MenuGroupRequest menuGroupRequest = menuGroupParam("추천메뉴");
        MenuGroup savedMenuGroup = savedMenuGroup(1L, "추천메뉴");
        given(menuGroupRepository.save(any())).willReturn(savedMenuGroup);

        // when
        MenuGroupResponse actual = menuGroupService.create(menuGroupRequest);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(savedMenuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록 조회 API")
    @Test
    void list() {
        // given
        MenuGroup savedMenuGroup1 = savedMenuGroup(1L, "메뉴 그룹1");
        MenuGroup savedMenuGroup2 = savedMenuGroup(2L, "메뉴 그룹2");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(savedMenuGroup1, savedMenuGroup2));

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertAll(
            () -> assertThat(menuGroups).hasSize(2),
            () -> assertThat(menuGroups.get(0).getId()).isNotNull(),
            () -> assertThat(menuGroups.get(0).getName()).isEqualTo("메뉴 그룹1"),
            () -> assertThat(menuGroups.get(1).getId()).isNotNull(),
            () -> assertThat(menuGroups.get(1).getName()).isEqualTo("메뉴 그룹2")
        );
    }
}

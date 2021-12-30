package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;

import kitchenpos.dto.menu.MenuGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹 생성")
    @Test
    void 메뉴그룹_생성() {
        // given
        MenuGroup menuGroup = MenuGroup.of("추천메뉴");
        MenuGroup savedMenuGroup = new MenuGroup(1L, "추천메뉴");

        given(menuGroupRepository.save(menuGroup))
            .willReturn(savedMenuGroup);

        // when
        MenuGroupResponse result = menuGroupService.create(menuGroup);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("추천메뉴");
    }

    @DisplayName("메뉴그룹 조회")
    @Test
    void 메뉴그룹_조회() {
        // given
        MenuGroup savedMenuGroup1 = new MenuGroup(1L, "추천메뉴");
        MenuGroup savedMenuGroup2 = new MenuGroup(2L, "세트메뉴");

        given(menuGroupRepository.findAll())
            .willReturn(Lists.newArrayList(savedMenuGroup1, savedMenuGroup2));

        // when
        List<MenuGroupResponse> result = menuGroupService.list();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("추천메뉴");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("세트메뉴");
    }

}
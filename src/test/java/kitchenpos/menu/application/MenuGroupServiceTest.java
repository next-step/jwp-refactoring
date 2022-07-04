package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    public static final MenuGroup 햄버거_메뉴 = MenuGroup.of("햄버거메뉴");

    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 추가")
    void create() {
        // given
        given(menuGroupRepository.save(any()))
                .willReturn(햄버거_메뉴);
        // when
        final MenuGroupResponse menuGroup = menuGroupService.create(햄버거_메뉴);
        // then
        assertThat(menuGroup).isInstanceOf(MenuGroupResponse.class);
    }

    @Test
    @DisplayName("메뉴 그룹 조회")
    void list() {
        // given
        given(menuGroupRepository.findAll())
                .willReturn(Arrays.asList(햄버거_메뉴));
        // when
        final List<MenuGroupResponse> list = menuGroupService.list();
        // then
        assertThat(list).hasSize(1);
    }
}

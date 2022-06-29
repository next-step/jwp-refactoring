package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    MenuGroupDao menuGroupDao;
    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create_success() {
        // given
        MenuGroup 추천_메뉴 = createMenuGroup(1L, "추천 메뉴");
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(추천_메뉴);

        // when
        MenuGroup saved = menuGroupService.create(추천_메뉴);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved).isEqualTo(추천_메뉴),
                () -> assertThat(saved.getName()).isEqualTo("추천 메뉴")
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup 추천_메뉴 = createMenuGroup(1L, "추천 메뉴");
        MenuGroup 신규_메뉴 = createMenuGroup(2L, "신규 메뉴");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(추천_메뉴, 신규_메뉴));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).containsExactly(추천_메뉴, 신규_메뉴);
    }
}

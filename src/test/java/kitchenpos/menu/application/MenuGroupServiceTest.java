package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.testfixture.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    MenuGroupRepository menuGroupRepository;
    @InjectMocks
    MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create_success() {
        // given
        MenuGroupRequest 추천_메뉴 = createMenuGroupRequest("추천 메뉴");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(추천_메뉴.toMenuGroup());

        // when
        MenuGroupResponse saved = menuGroupService.create(추천_메뉴);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo("추천 메뉴")
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroupRequest 추천_메뉴 = createMenuGroupRequest("추천_메뉴");
        MenuGroupRequest 신규_메뉴 = createMenuGroupRequest("신규_메뉴");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(추천_메뉴.toMenuGroup(), 신규_메뉴.toMenuGroup()));

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(2);
    }
}

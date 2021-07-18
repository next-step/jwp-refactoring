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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 관리 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 추천_메뉴;
    private MenuGroup 세트_메뉴;

    @BeforeEach
    void setUp() {
        추천_메뉴 = new MenuGroup(1L, "추천 메뉴");
        세트_메뉴 = new MenuGroup(2L, "세트 메뉴");
    }

    @Test
    void 메뉴_그룹_생성() {
        MenuGroupRequest request = new MenuGroupRequest("추천 메뉴");
        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(추천_메뉴);
        MenuGroupResponse actual = menuGroupService.create(request);
        assertThat(actual.getId()).isEqualTo(추천_메뉴.getId());
        assertThat(actual.getName()).isEqualTo(추천_메뉴.getName());
    }

    @Test
    void 등록된_메뉴_그룹_리스트_조회() {
        MenuGroupResponse 추천_메뉴_결과 = MenuGroupResponse.of(추천_메뉴);
        MenuGroupResponse 세트_메뉴_결과 = MenuGroupResponse.of(세트_메뉴);

        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(추천_메뉴, 세트_메뉴));
        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        assertThat(menuGroups).containsExactly(추천_메뉴_결과, 세트_메뉴_결과);

    }
}

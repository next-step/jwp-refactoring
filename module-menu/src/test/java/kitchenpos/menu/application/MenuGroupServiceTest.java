package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import kitchenpos.menu.application.MenuGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.dao.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다")
    @Test
    void 메뉴그룹_등록() {
        // given
        MenuGroup 메뉴그룹 = MenuGroup.from("한식");
        given(menuGroupRepository.save(any())).willReturn(메뉴그룹);

        // when
        MenuGroupResponse 저장된_메뉴그룹 = menuGroupService.create(MenuGroupRequest.from(메뉴그룹.getName()));

        // then
        assertThat(저장된_메뉴그룹.getName()).isEqualTo(메뉴그룹.getName());

    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다")
    @Test
    void 메뉴그룹_목록_조회() {
        // given
        MenuGroup 첫번째_메뉴그룹 = MenuGroup.from("한식");
        MenuGroup 두번째_메뉴그룹 = MenuGroup.from("양식");
        
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(첫번째_메뉴그룹, 두번째_메뉴그룹));

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).containsExactly(MenuGroupResponse.from(첫번째_메뉴그룹), MenuGroupResponse.from(두번째_메뉴그룹));
    }
    
    @DisplayName("메뉴 그룹을 조회한다")
    @Test
    void 메뉴그룹_조회() {
        // given
        MenuGroup 메뉴그룹 = MenuGroup.from("한식");
        
        given(menuGroupRepository.findById(nullable(Long.class))).willReturn(Optional.of(메뉴그룹));

        // when
        MenuGroup 저장된_메뉴그룹 = menuGroupService.findById(메뉴그룹.getId());

        // then
        assertThat(저장된_메뉴그룹.getName()).isEqualTo(메뉴그룹.getName());
    }
    
    @DisplayName("등록되지 않은 메뉴그룹을 조회한다")
    @Test
    void 미등록_메뉴그룹_조회() {
        // given
        MenuGroup 메뉴그룹 = MenuGroup.from("한식");
        
        given(menuGroupRepository.findById(nullable(Long.class))).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> {
            menuGroupService.findById(메뉴그룹.getId());
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당하는 메뉴그룹이 없습니다");
    }

}

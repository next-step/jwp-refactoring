package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.assertj.core.api.Assertions;
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
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성에 성공한다.")
    @Test
    void 생성_성공() {
        // given
        MenuGroup menuGroup = new MenuGroup("양식");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(menuGroup);

        // when
        MenuGroupResponse result = menuGroupService.create(new MenuGroupRequest("양식"));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹 목록 조회에 성공한다.")
    @Test
    void 목록_조회() {
        // given
        MenuGroup 양식 = new MenuGroup(1L, "양식");
        MenuGroup 한식 = new MenuGroup(2L, "한식");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(양식, 한식));

        // when
        List<MenuGroupResponse> 메뉴_그룹_목록 = menuGroupService.list();

        // then
        Assertions.assertThat(메뉴_그룹_목록).hasSize(2);
    }
}

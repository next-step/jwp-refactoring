package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    @Test
    void 메뉴_그룹_생성() {
        // given
        MenuGroupRequest menuGroupRequest = MenuGroupRequest.from("분식");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(new MenuGroup(1L, "분식"));

        // when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        // then
        assertAll(() -> assertThat(menuGroupResponse).isNotNull(),
                () -> assertThat(menuGroupResponse.getId()).isNotNull());
    }

    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    @Test
    void 전체_메뉴_그룹_조회() {
        // given
        MenuGroup 분식 = new MenuGroup(1L, "분식");
        MenuGroup 중식 = new MenuGroup(2L, "중식");
        List<MenuGroup> 전체_메뉴_그룹 = Arrays.asList(분식, 중식);

        given(menuGroupRepository.findAll()).willReturn(전체_메뉴_그룹);

        // when / then
        assertThat(menuGroupService.list()).hasSize(2);
    }
}

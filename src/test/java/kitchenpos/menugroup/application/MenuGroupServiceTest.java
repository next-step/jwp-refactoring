package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void 메뉴_그룹_생성() {
        MenuGroupRequest 세트 = new MenuGroupRequest("세트");
        when(menuGroupRepository.save(세트.toMenuGroup())).thenReturn(세트.toMenuGroup());

        MenuGroupResponse 생성된_메뉴_그룹 = menuGroupService.create(세트);

        assertThat(생성된_메뉴_그룹.getName()).isEqualTo("세트");
    }

    @DisplayName("비어있는 이름으로 메뉴 그룹 생성 요청 시 예외처리")
    @Test
    void 비어있는_이름_메뉴_그룹_생성() {
        MenuGroupRequest 빈_이름_메뉴_그룹 = new MenuGroupRequest("");
        assertThatThrownBy(
                () -> menuGroupService.create(빈_이름_메뉴_그룹)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹 조회")
    @Test
    void 메뉴_그룹_조회() {
        MenuGroup 세트 = new MenuGroup(1L, "세트");
        MenuGroup 단품 = new MenuGroup(2L, "단품");
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(세트, 단품));
        List<MenuGroupResponse> 조회된_메뉴_그룹_목록 = menuGroupService.list();

        List<MenuGroup> menuGroups = Arrays.asList(세트, 단품);
        assertThat(조회된_메뉴_그룹_목록).containsAll(MenuGroupResponse.getMenuGroupResponses(menuGroups));
    }
}
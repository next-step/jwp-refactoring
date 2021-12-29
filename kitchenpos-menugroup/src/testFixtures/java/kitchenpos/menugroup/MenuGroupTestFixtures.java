package kitchenpos.menugroup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupTestFixtures {

    public static void 메뉴_그룹_존재여부_조회시_응답_모킹(MenuGroupService menuGroupService,
        MenuGroup menuGroup) {
        given(menuGroupService.findMenuGroupById(menuGroup.getId())).willReturn(menuGroup);
    }

    public static void 메뉴그룹_생성_결과_모킹(MenuGroupRepository menuGroupRepository, MenuGroup menuGroup) {
        given(menuGroupRepository.save(any())).willReturn(menuGroup);
    }

    public static void 메뉴그룹_전체조회_모킹(MenuGroupRepository menuGroupRepository,
        List<MenuGroup> menuGroups) {
        given(menuGroupRepository.findAll()).willReturn(menuGroups);
    }

    public static final MenuGroup 추천메뉴 = new MenuGroup(1L, "추천메뉴");
}


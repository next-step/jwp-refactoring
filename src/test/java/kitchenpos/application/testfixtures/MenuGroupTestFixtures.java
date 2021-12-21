package kitchenpos.application.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupTestFixtures {

    public static void 메뉴_그룹_존재여부_조회시_응답_모킹(MenuGroupDao menuGroupDao, MenuGroup menuGroup,
        boolean result) {
        given(menuGroupDao.existsById(menuGroup.getId())).willReturn(result);
    }

    public static void 메뉴그룹_생성_결과_모킹(MenuGroupDao menuGroupDao, MenuGroup menuGroup) {
        given(menuGroupDao.save(any())).willReturn(menuGroup);
    }

    public static void 메뉴그룹_전체조회_모킹(MenuGroupDao menuGroupDao, List<MenuGroup> menuGroups) {
        given(menuGroupDao.findAll()).willReturn(menuGroups);
    }
}


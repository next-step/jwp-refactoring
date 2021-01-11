package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static kitchenpos.util.TestHelper.두마리_메뉴그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    public void createMenuGroup() {
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(두마리_메뉴그룹);

        MenuGroup result = menuGroupService.create(두마리_메뉴그룹);

        assertThat(result).isEqualTo(두마리_메뉴그룹);
    }
}

package kitchenpos.application;

import static common.MenuGroupFixture.메뉴그룹_두마리;
import static common.MenuGroupFixture.메뉴그룹_신메뉴;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;


    @Test
    void 메뉴그룹을_생성한다() {
        // given
        MenuGroup 두마리그룹 = 메뉴그룹_두마리();

        //when
        when(menuGroupDao.save(두마리그룹)).thenReturn(두마리그룹);
        MenuGroup 저장된두마리그룹 = menuGroupService.create(두마리그룹);

        // then
        Assertions.assertThat(저장된두마리그룹).isEqualTo(두마리그룹);
    }

    @Test
    void 메뉴그룹을_조회한다() {
        // given
        MenuGroup 두마리그룹 = 메뉴그룹_두마리();
        MenuGroup 신메뉴그룹 = 메뉴그룹_신메뉴();

        //when
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(두마리그룹, 신메뉴그룹));
        List<MenuGroup> list = menuGroupService.list();

        // then
        Assertions.assertThat(list).containsExactly(두마리그룹, 신메뉴그룹);
    }
}

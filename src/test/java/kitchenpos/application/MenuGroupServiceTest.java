package kitchenpos.application;

import static common.MenuGroupFixture.from;
import static common.MenuGroupFixture.메뉴그룹_두마리;
import static common.MenuGroupFixture.메뉴그룹_신메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
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
        lenient().when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(두마리그룹);
        MenuGroupResponse 저장된_두마리_그룹 = menuGroupService.create(from(두마리그룹));

        // then
        assertThat(저장된_두마리_그룹.getName()).isEqualTo(두마리그룹.getName());
    }

    @Test
    void 메뉴그룹을_조회한다() {
        // given
        MenuGroup 두마리그룹 = 메뉴그룹_두마리();
        MenuGroup 신메뉴그룹 = 메뉴그룹_신메뉴();

        //when
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(두마리그룹, 신메뉴그룹));
        List<MenuGroupResponse> list = menuGroupService.list();

        // then
        assertThat(list).extracting("name").containsExactly(두마리그룹.getName(), 신메뉴그룹.getName());
    }
}

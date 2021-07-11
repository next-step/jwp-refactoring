package kitchenpos.menugroup.application;

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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성할수 있다")
    @Test
    void createTest() {
        //given
        MenuGroupRequest expect = new MenuGroupRequest("JAP메뉴그룹");
        MenuGroup menuGroup = new MenuGroup(1l, expect.getName());
        when(menuGroupRepository.save(any())).thenReturn(menuGroup);

        //when
        MenuGroupResponse result = menuGroupService.create(expect);

        //then
        assertThat(result.getName()).isEqualTo(expect.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다 ")
    @Test
    void findAll() {

        //given
        MenuGroupRequest expect = new MenuGroupRequest("JAP메뉴그룹");
        MenuGroup menuGroup = new MenuGroup(1l, expect.getName());
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(menuGroup));

        //when
        List<MenuGroupResponse> result = menuGroupService.list();

        //then
        assertThat(result.size()).isGreaterThan(0);
        assertThat(result.get(result.size()-1).getName()).isEqualTo(expect.getName());
    }


}

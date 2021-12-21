package kitchenpos.menuGroup.application;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.aplication.MenuGroupService;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
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
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 치킨;
    private MenuGroup 피자;

    @BeforeEach
    void setUp() {
        치킨 = MenuGroupFixture.치킨류();
        피자 = MenuGroupFixture.피자류();
    }

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        given(menuGroupRepository.save(any())).willReturn(치킨);

        MenuGroup createMenuGroup = menuGroupService.create(치킨);

        assertThat(createMenuGroup).isEqualTo(치킨);
    }

    @DisplayName("메뉴 그룹 목록")
    @Test
    void list() {
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(치킨, 피자));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(2);
    }
}

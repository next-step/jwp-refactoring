package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup group1;
    private MenuGroup group2;

    @BeforeEach
    void setUp() {
        group1 = MenuGroup.of(1L, "group1");
        group2 = MenuGroup.of(2L, "group2");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        when(menuGroupRepository.save(any())).thenReturn(group1);

        MenuGroupResponse result = menuGroupService.create(group1);

        assertThat(result.getName()).isEqualTo("group1");
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(group1, group2));

        List<MenuGroupResponse> list = menuGroupService.list();

        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list.get(0).getName()).isEqualTo("group1"),
                () -> assertThat(list.get(1).getName()).isEqualTo("group2")
        );
    }
}

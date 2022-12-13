package kitchenpos.menugroup.application;

import static kitchenpos.menugroup.domain.MenuGroupTest.메뉴그룹_생성;
import static kitchenpos.menugroup.dto.MenuGroupRequestTest.메뉴그룹_요청_객체_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.dto.MenuGroupResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 식사;
    private MenuGroup 요리;
    private MenuGroup 안주;

    @BeforeEach
    public void setUp() {
        식사 = 메뉴그룹_생성(1L, "식사");
        요리 = 메뉴그룹_생성(2L, "요리");
        안주 = 메뉴그룹_생성(3L, "안주");
    }

    @Test
    @DisplayName("메뉴그룹 등록")
    void create() {
        // given
        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(식사);
        MenuGroupRequest 식사_생성_요청_객체 = 메뉴그룹_요청_객체_생성(식사.getNameValue());

        // when
        MenuGroupResponse 등록된_식사 = menuGroupService.create(식사_생성_요청_객체);

        // then
        assertThat(등록된_식사.getId()).isEqualTo(식사.getId());
        assertThat(등록된_식사.getName()).isEqualTo(식사.getNameValue());
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회")
    void list() {
        // given
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(식사, 요리, 안주));

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).hasSize(3);
        assertThat(menuGroups).containsAll(MenuGroupResponseTest.메뉴그룹_응답_객체들_생성(식사, 요리, 안주));
    }
}
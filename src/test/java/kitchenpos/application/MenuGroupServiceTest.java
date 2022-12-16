package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;
    private MenuGroup 메뉴_그룹_1;
    private MenuGroup 메뉴_그룹_2;
    private List<MenuGroup> 메뉴_그룹_목록;

    @BeforeEach
    void setUp() {
        메뉴_그룹_1 = new MenuGroup(1L, "한마리메뉴");
        메뉴_그룹_2 = new MenuGroup(2L, "두마리메뉴");
        메뉴_그룹_목록 = Arrays.asList(메뉴_그룹_1, 메뉴_그룹_2);
    }

    @Test
    void 생성() {
        given(menuGroupRepository.save(any())).willReturn(메뉴_그룹_1);

        MenuGroupResponse response = menuGroupService.create(메뉴_그룹_1);

        assertThat(response.getName()).isEqualTo("한마리메뉴");
    }

    @Test
    void 조회() {
        given(menuGroupRepository.findAll()).willReturn(메뉴_그룹_목록);

        List<MenuGroupResponse> responses = menuGroupService.list();

        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void 아이디로_조회() {
        given(menuGroupRepository.findById(메뉴_그룹_1.getId())).willReturn(Optional.of(메뉴_그룹_1));

        MenuGroup response = menuGroupService.findById(메뉴_그룹_1.getId());

        assertAll(
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getId()).isEqualTo(1L),
                () -> assertThat(response.getName()).isEqualTo("한마리메뉴")
        );
    }
}

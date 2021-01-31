package kitchenpos.menugroup;

import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
public class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    public void create() {
        MenuGroup expected = new MenuGroup("테스트");
        MenuGroup actual = menuGroupRepository.save(expected);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

}

package kitchenpos.menu.domain.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.vo.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    private MenuGroup 추천메뉴그룹;

    @DisplayName("메뉴상품 저장")
    @BeforeEach
    void setUp() {
        추천메뉴그룹 = menuGroupRepository.save(new MenuGroup("추천메뉴"));
    }

    @Test
    void countByIdIn() {
        Menu 나홀로세트 = menuRepository.save(new Menu("나홀로세트", Price.valueOf(BigDecimal.ZERO), 추천메뉴그룹));
        Menu 커플세트 = menuRepository.save(new Menu("커플세트", Price.valueOf(BigDecimal.ZERO), 추천메뉴그룹));
        long count = menuRepository.countByIdIn(Arrays.asList(나홀로세트.getId(), 커플세트.getId()));
        assertThat(count).isEqualTo(2);
    }
}

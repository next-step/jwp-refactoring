package kitchenpos.menu.config;

import java.util.List;
import java.util.function.Consumer;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuEventConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MenuEventConfiguration.class);

    private final MenuRepository menuRepository;

    public MenuEventConfiguration(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Bean
    Consumer<List<MenuResponse>> cancelMenuCreate() {
        return list -> list.forEach(this::deleteMenu);
    }

    private void deleteMenu(MenuResponse menuResponse) {
        logger.info("[ 메뉴생성 취소 ] 메뉴ID : {} ", menuResponse.getId());
        menuRepository.deleteById(menuResponse.getId());
    }

}

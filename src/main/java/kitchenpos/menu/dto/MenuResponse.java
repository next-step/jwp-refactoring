package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

public class MenuResponse {

    private Long id;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
    }

    public Long getId() {
        return this.id;
    }
}

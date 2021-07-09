package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

import kitchenpos.manugroup.dto.MenuGroupResponse;

public class MenuResponse {

	private Long id;
	private String name;
	private BigDecimal price;
	private MenuGroupResponse menuGroupResponse;
	private List<MenuProductResponse> menuProductResponses;


}

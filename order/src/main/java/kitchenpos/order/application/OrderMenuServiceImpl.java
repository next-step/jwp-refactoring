package kitchenpos.order.application;

import kitchenpos.order.domain.MenuAdapter;
import kitchenpos.order.dto.OrderMenuResponse;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class OrderMenuServiceImpl {

    private final OrderMenuService orderMenuService;

    public OrderMenuServiceImpl(OrderMenuService orderMenuService) {
        this.orderMenuService = orderMenuService;
    }

    public List<MenuAdapter> findAllByIds(List<Long> ids) {

        try {
            Response<List<OrderMenuResponse>> response = orderMenuService.findAllByIds(ids).execute();
            List<OrderMenuResponse> menus = response.body();

            return menus.stream()
                    .map(OrderMenuResponse::toMenuAdapter)
                    .collect(toList());

        } catch (IOException e) {
            throw new IllegalStateException("api 호출 시 오류가 발생했습니다.");
        }
    }
}

package kitchenpos.order.application;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"test"})
public class MenuClient {
    public boolean isExistMenuByIds(List<Long> menuIds) {
        return true;
    }
}

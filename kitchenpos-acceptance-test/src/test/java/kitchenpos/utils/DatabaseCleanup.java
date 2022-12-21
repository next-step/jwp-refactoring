package kitchenpos.utils;

import org.springframework.beans.factory.InitializingBean;

public interface DatabaseCleanup extends InitializingBean {

    void execute();
}

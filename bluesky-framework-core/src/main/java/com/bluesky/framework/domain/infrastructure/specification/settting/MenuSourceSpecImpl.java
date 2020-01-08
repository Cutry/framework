package com.bluesky.framework.domain.infrastructure.specification.settting;

import com.bluesky.framework.domain.model.setting.MenuSourceOperationRepository;
import com.bluesky.framework.domain.model.setting.MenuSourceRepository;
import com.bluesky.framework.domain.specification.setting.MenuSourceSpec;
import com.bluesky.framework.setting.menu.MenuSourceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * setting spec
 *
 * @author linjiang
 */
@Service
public class MenuSourceSpecImpl implements MenuSourceSpec {

    @Autowired
    private MenuSourceOperationRepository menuSourceOperationRepository;
    @Autowired
    private MenuSourceRepository menuSourceRepository;

    @Override
    public boolean isOperationUnique(String operation) {
        MenuSourceOperation menuSourceOperation = menuSourceOperationRepository.findByOperation(operation);
        return menuSourceOperation == null;
    }

    @Override
    public boolean isMenuCodeUnique(String code) {
        return menuSourceRepository.findByCode(code) == null;
    }
}
package com.hdu.message.manager.service.impl;

import com.hdu.message.manager.enums.strategy.Strategy;
import com.hdu.message.manager.service.TestService;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public void testStrategy() {
        HashMap<String, Strategy> map = new HashMap<>();
        map.put("A", Strategy.A);
        map.put("B", Strategy.B);

        String str = "A";
        map.get(str).exe();
    }

}

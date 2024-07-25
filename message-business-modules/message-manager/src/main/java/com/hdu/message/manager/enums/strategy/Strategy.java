package com.hdu.message.manager.enums.strategy;

public enum Strategy {

    // 内部类继承抽象方法
    A {
        @Override
        public void exe() {
            System.out.println("执行业务逻辑A");
        }
    },

    B {
        @Override
        public void exe() {
            System.out.println("执行业务逻辑B");
        }
    };

    public abstract void exe();
}

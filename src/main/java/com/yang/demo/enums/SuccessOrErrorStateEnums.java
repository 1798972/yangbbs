package com.yang.demo.enums;



public enum SuccessOrErrorStateEnums {
    SUCCESS("success"),
    ERROR("error");
    private String state;

    SuccessOrErrorStateEnums(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}

package com.example.demo.Entity;

public enum
UserRoleEnum {

    PlAYER(Authority.PLAYER),
    PRODUCER(Authority.PRODUCER);


    private final String authority;

    UserRoleEnum(String authority){
        this.authority = authority;
    }

    public String getAuthority(){
        return this.authority;
    }

    public static class Authority{
        public static final String PLAYER = "ROLE_PLAYER";
        public static final String PRODUCER = "ROLE_PRODUCER";


    }
}

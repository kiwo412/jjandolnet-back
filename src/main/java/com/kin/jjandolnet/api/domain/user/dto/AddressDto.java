package com.kin.jjandolnet.api.domain.user.dto;

import com.kin.jjandolnet.api.domain.user.entity.Address;
import lombok.Builder;
import lombok.Getter;

public class AddressDto {

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private String name;

        public static Response from(Address address) {
            return Response.builder()
                    .id(address.getId())
                    .name(address.getName())
                    .build();
        }
    }
}

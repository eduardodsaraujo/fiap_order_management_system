package br.com.fiap.product_management.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

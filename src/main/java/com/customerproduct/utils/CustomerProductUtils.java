package com.customerproduct.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerProductUtils {
    private static final Logger log = LoggerFactory.getLogger(CustomerProductUtils.class);

    public static boolean isNotNullAndEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    public static JSONObject setResponse(String message, Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);

        if (object != null) {
            jsonObject.put("data", object);
        } else {
            jsonObject.put("data", JSONObject.NULL);
        }
        return jsonObject;
    }

    public static Object toJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

            ObjectNode objectNode = mapper.valueToTree(obj);
            return objectNode;
        } catch (Exception e) {
            log.error("Error processing JSON", e);
        }
        return JSONObject.NULL;
    }
//    public static JSONObject setResponse(String message, Object object) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("message", message);
//
//        if (object != null ) {
//            jsonObject.put("data", CustomerProductUtils.toJson(object));
//        }
//        return jsonObject;
//    }
//
//    public static String toJson(Object obj) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//
//            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
//            return mapper.writeValueAsString(obj);
//        } catch (JsonProcessingException ignore) {
//        }
//        return "";
//    }
}

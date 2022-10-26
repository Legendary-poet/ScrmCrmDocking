package com.nox.kol.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateVo {

    @JsonProperty("corpAccessToken")
    private String corpAccessToken;
    @JsonProperty("corpId")
    private String corpId;
    @JsonProperty("currentOpenUserId")
    private String currentOpenUserId;
    @JsonProperty("data")
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO {
        @JsonProperty("object_data")
        private ObjectDataDTO object_data;
        @JsonProperty("details")
        private DetailsDTO details;

        @NoArgsConstructor
        @Data
        public static class ObjectDataDTO {
            @JsonProperty("field_215ot__c")
            private String field_215ot__c;

            @JSONField(name = "_id")
            private String _id;
            @JsonProperty("dataObjectApiName")
            private String dataObjectApiName;
        }

        @NoArgsConstructor
        @Data
        public static class DetailsDTO {
        }
    }
}

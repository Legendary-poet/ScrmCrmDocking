package com.nox.kol.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CreateVo {

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
        @JsonProperty("dataObjectApiName")
        private String dataObjectApiName;
        @JsonProperty("object_data")
        private ObjectDataDTO object_data;
        @JsonProperty("details")
        private DetailsDTO details;

        @NoArgsConstructor
        @Data
        public static class ObjectDataDTO {
            @JsonProperty("name")
            private String name;
            @JsonProperty("field_aEh03__c")
            private String field_aEh03__c;
            @JsonProperty("mobile")
            private String mobile;
            @JsonProperty("email")
            private String email;
            @JsonProperty("address")
            private String address;
            @JsonProperty("company")
            private String company;
            @JsonProperty("job_title")
            private String job_title;
            @JsonProperty("department")
            private String department;
            @JsonProperty("remark")
            private String remark;
            @JsonProperty("field_21Y8q__c")
            private String field_21Y8q__c;
            @JsonProperty("field_m1ubR__c")
            private String field_m1ubR__c;
            @JsonProperty("owner")
            private List<String> owner;
            @JsonProperty("source")
            private Integer source;
            @JsonProperty("field_g26sD__c")
            private String field_g26sD__c;
            @JsonProperty("field_215ot__c")
            private String field_215ot__c;
            @JsonProperty("field_Rr4xw__c")
            private String field_Rr4xw__c;
            @JsonProperty("field_G6s6C__c")
            private String field_G6s6C__c;
            @JsonProperty("field_35AB2__c")
            private String field_35AB2__c;
            @JsonProperty("leads_pool_id")
            private String leads_pool_id;


            @JsonProperty("field_uM7hS__c")
            private String field_uM7hS__c;
            @JsonProperty("field_0253D__c")
            private String field_0253D__c;

            @JsonProperty("field_YX2nF__c")
            private String field_YX2nF__c;//线索类型


        }

        @NoArgsConstructor
        @Data
        public static class DetailsDTO {
        }
    }
}

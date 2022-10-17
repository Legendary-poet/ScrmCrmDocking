package com.nox.kol.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class QueryVo {

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
//        @JsonProperty("search_query_info")
        @JSONField(name = "search_query_info")
        private SearchQueryInfoDTO searchQueryInfo;

        @NoArgsConstructor
        @Data
        public static class SearchQueryInfoDTO {
            @JsonProperty("limit")
            private Integer limit;
            @JsonProperty("offset")
            private Integer offset;
            @JsonProperty("filters")
            private List<FiltersDTO> filters;
            @JsonProperty("orders")
            private List<OrdersDTO> orders;
            @JsonProperty("fieldProjection")
            private List<String> fieldProjection;

            @NoArgsConstructor
            @Data
            public static class FiltersDTO {
//                @JsonProperty("field_name")
                @JSONField(name = "field_name")
                private String fieldName;


//                @JsonProperty("field_values")
                @JSONField(name = "field_values")
                private List<String> fieldValues;
                @JsonProperty("operator")
                private String operator;
            }

            @NoArgsConstructor
            @Data
            public static class OrdersDTO {
                @JsonProperty("fieldName")
                private String fieldName;
                @JsonProperty("isAsc")
                private Boolean isAsc;
            }
        }
    }
}

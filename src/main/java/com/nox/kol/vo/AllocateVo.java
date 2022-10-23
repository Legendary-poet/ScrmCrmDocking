package com.nox.kol.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class AllocateVo {

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
        @JsonProperty("apiName")
        private String apiName;
        @JsonProperty("objectIds")
        private List<String> objectIds;
        @JsonProperty("objectPoolId")
        private String objectPoolId;
        @JsonProperty("ownerOpenUserId")
        private String ownerOpenUserId;
    }
}

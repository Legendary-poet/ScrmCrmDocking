package com.nox.kol.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Leads {

    @JsonProperty("birthday")
    private String birthday;
    @JsonProperty("lds_source")
    private String ldsSource;
    @JsonProperty("lds_record")
    private String ldsRecord;
    @JsonProperty("mobile_type")
    private String mobileType;
    @JsonProperty("level_id")
    private String levelId;
    @JsonProperty("lds_last_follow_type")
    private String ldsLastFollowType;
    @JsonProperty("lds_inviter")
    private String ldsInviter;
    @JsonProperty("source")
    private String source;
    @JsonProperty("lds_sql_time")
    private String ldsSqlTime;
    @JsonProperty("lds_mql_time")
    private String ldsMqlTime;
    @JsonProperty("lds_phase")
    private String ldsPhase;
    @JsonProperty("points")
    private Integer points;
    @JsonProperty("identification")
    private String identification;
    @JsonProperty("member_26416")
    private String member26416;
    @JsonProperty("member_26417")
    private String member26417;
    @JsonProperty("lds_source_pid")
    private String ldsSourcePid;
    @JsonProperty("lds_last_follow_time")
    private String ldsLastFollowTime;
    @JsonProperty("lds_id")
    private String ldsId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("lds_modify_time")
    private String ldsModifyTime;
    @JsonProperty("lds_last_allot_time")
    private String ldsLastAllotTime;
    @JsonProperty("address")
    private String address;
    @JsonProperty("member_wx_system_count")
    private String memberWxSystemCount;
    @JsonProperty("create_time")
    private String createTime;
    @JsonProperty("is_frozen")
    private String isFrozen;
    @JsonProperty("member_26228")
    private String member26228;
    @JsonProperty("sex")
    private String sex;
    @JsonProperty("mobile")
    private String mobile;
    @JsonProperty("bind_type")
    private String bindType;
    @JsonProperty("lds_belonger_branches")
    private String ldsBelongerBranches;
    @JsonProperty("lds_creator")
    private String ldsCreator;
    @JsonProperty("member_tags_num")
    private Integer memberTagsNum;
    @JsonProperty("lds_point")
    private Integer ldsPoint;
    @JsonProperty("name")
    private String name;
    @JsonProperty("points_total")
    private Integer pointsTotal;
    @JsonProperty("member_25956")
    private String member25956;
    @JsonProperty("member_25957")
    private String member25957;
    @JsonProperty("lds_create_time")
    private String ldsCreateTime;
    @JsonProperty("lds_phase_status")
    private String ldsPhaseStatus;
    @JsonProperty("lds_belonger")
    private String ldsBelonger;
}

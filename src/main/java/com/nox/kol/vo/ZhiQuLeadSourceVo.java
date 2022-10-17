package com.nox.kol.vo;

import com.nox.kol.entity.ZhiQuLeadSource;
import lombok.Data;

import java.util.List;

/**
 * @author tianxueyang
 * @version 1.0
 * @description
 * @date 2022/8/24 20:29
 */
@Data
public class ZhiQuLeadSourceVo extends ZhiQuLeadSource {
    private List<ZhiQuLeadSourceVo> sublist;
}

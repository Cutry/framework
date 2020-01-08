package com.bluesky.framework.domain.service.sysParam;

import com.bluesky.framework.SysParam.SysParamVO;
import com.bluesky.framework.sysParam.SysParam;

import java.util.List;

public interface SysParamService {

    Long insert(SysParam param);

    void update(SysParam param);

    List<SysParamVO> findAllTree(String mark);
}

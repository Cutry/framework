package com.bluesky.framework.SysParam;

import com.bluesky.common.vo.Page;
import com.bluesky.framework.sysParam.SysParam;

import java.util.List;

public interface SysParamManager {

    Long insert(SysParam param);

    SysParam findOne( Long id);

    List<SysParam> findByMark( String mark);

    void delete( Long id);

    void update(SysParam param);

    Page<SysParam> findAll(Integer pageNum, Integer pageSize, String mark);

    List<SysParamVO> findAllTree(String mark);
}

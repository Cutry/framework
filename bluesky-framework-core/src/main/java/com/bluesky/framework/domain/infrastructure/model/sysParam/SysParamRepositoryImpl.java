package com.bluesky.framework.domain.infrastructure.model.sysParam;

import com.bluesky.common.vo.Page;
import com.bluesky.core.common.PageBeanUtils;
import com.bluesky.framework.sysParam.SysParam;
import com.bluesky.framework.domain.infrastructure.model.sysParam.mapper.SysParamMapper;
import com.bluesky.framework.domain.model.sysParam.SysParamRepository;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SysParamRepositoryImpl implements SysParamRepository {

    @Autowired
    private SysParamMapper sysParamMapper;

    @Override
    public Long insert(SysParam param) {
        return sysParamMapper.insert(param);
    }

    @Override
    public SysParam findOne(Long id) {
        return sysParamMapper.findOne(id);
    }

    @Override
    public List<SysParam> findByMark(String mark) {
        return sysParamMapper.findByMark(mark);
    }

    @Override
    public void delete(Long id) {
        sysParamMapper.delete(id);
    }

    @Override
    public void update(SysParam param) {
        sysParamMapper.update(param);
    }

    @Override
    public Page<SysParam> findAll(Integer pageNum, Integer pageSize, String mark) {
        if (pageNum == null) {pageNum = 1;}
        if (pageSize == null) {pageSize = 15;}
        PageHelper.startPage(pageNum, pageSize);
        List<SysParam> list = sysParamMapper.findAll(mark);
        return PageBeanUtils.copyPageProperties(list);
    }
}

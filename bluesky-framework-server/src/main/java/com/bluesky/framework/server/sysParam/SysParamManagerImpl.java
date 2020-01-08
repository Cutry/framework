package com.bluesky.framework.server.sysParam;

import com.bluesky.common.vo.Page;
import com.bluesky.framework.SysParam.SysParamVO;
import com.bluesky.framework.sysParam.SysParam;
import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.framework.SysParam.SysParamManager;
import com.bluesky.framework.domain.model.sysParam.SysParamRepository;
import com.bluesky.framework.domain.service.sysParam.SysParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DubboService
public class SysParamManagerImpl implements SysParamManager {

    @Autowired
    private SysParamService sysParamService;
    @Autowired
    private SysParamRepository sysParamRepository;

    @Override
    public Long insert(SysParam param) {
        return sysParamService.insert(param);
    }

    @Override
    public SysParam findOne(Long id) {
        return sysParamRepository.findOne(id);
    }

    @Override
    public List<SysParam> findByMark(String mark) {
        return sysParamRepository.findByMark(mark);
    }

    @Override
    public void delete(Long id) {
        sysParamRepository.delete(id);
    }

    @Override
    public void update(SysParam param) {
        sysParamService.update(param);
    }

    @Override
    public Page<SysParam> findAll(Integer pageNum, Integer pageSize, String mark) {
        return sysParamRepository.findAll( pageNum, pageSize, mark);
    }

    @Override
    public List<SysParamVO> findAllTree(String mark) {
        return sysParamService.findAllTree(mark);
    }
}

package com.bluesky.framework.domain.infrastructure.service.sysParam;

import com.bluesky.framework.SysParam.SysParamVO;
import com.bluesky.framework.sysParam.SysParam;
import com.bluesky.framework.domain.model.sysParam.SysParamRepository;
import com.bluesky.framework.domain.service.sysParam.SysParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysParamServicceImpl implements SysParamService {

    @Autowired
    private SysParamRepository sysParamRepository;

    @Override
    public Long insert(SysParam param) {
        return sysParamRepository.insert(param);
    }

    @Override
    public void update(SysParam param) {
        sysParamRepository.update(param);
    }

    @Override
    public List<SysParamVO> findAllTree(String mark) {
        List<SysParamVO> result = new ArrayList<>();
        List<SysParam> list = sysParamRepository.findByMark(mark);
        if (list!=null && list.size()>0){
            for (com.bluesky.framework.sysParam.SysParam param : list) {
                SysParamVO vo = new SysParamVO(param);
                if("1".equals(param.getIsleaf())){
                    List<SysParamVO> vos = this.findAllTree(param.getParam());
                    vo.setChildren(vos);
                }
                result.add(vo);
            }
        }
        return result;
    }
}

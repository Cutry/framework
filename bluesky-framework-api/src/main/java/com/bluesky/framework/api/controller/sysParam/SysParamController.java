package com.bluesky.framework.api.controller.sysParam;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.SysParam.SysParamManager;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.sysParam.SysParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/sysParam")
public class SysParamController {

    @Autowired
    private SysParamManager sysParamManager;

    @GetMapping("/list")
    public DataResponse list(@AuthenticationPrincipal Account account, @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                             @RequestParam(required = false,defaultValue = "15")Integer pageSize,@RequestParam String mark){
        DataResponse result = new DataResponse();

        Page<SysParam> page = sysParamManager.findAll(pageNum, pageSize, mark);
        result.addData("page",page);

        return result;
    }

    @PostMapping("/add")
    public DataResponse add(@AuthenticationPrincipal Account account, @RequestParam String param,
                             @RequestParam String desc, @RequestParam String mark, @RequestParam String sort){
        DataResponse result = new DataResponse();

        SysParam newParam = SysParam.builder()
                .param(param)
                .desc(desc)
                .mark(mark)
                .sort(sort)
                .build();

        Long id = sysParamManager.insert(newParam);
        result.addData("id",id);

        return result;
    }

    @PostMapping("/update")
    public DataResponse update(@AuthenticationPrincipal Account account, @RequestParam String param,@RequestParam Long id,
                             @RequestParam String desc, @RequestParam String sort){
        DataResponse result = new DataResponse();

        SysParam newParam = SysParam.builder()
                .id(id)
                .param(param)
                .desc(desc)
                .sort(sort)
                .build();

        sysParamManager.update(newParam);
        result.addData("one",sysParamManager.findOne(id));
        return result;
    }

    @PostMapping("/delete")
    public DataResponse delete(@AuthenticationPrincipal Account account, @RequestParam Long id){
        DataResponse result = new DataResponse();

        sysParamManager.delete(id);

        result.addData("id",id);
        return result;
    }

    @GetMapping("/select")
    public DataResponse select(@AuthenticationPrincipal Account account, @RequestParam String mark){
        DataResponse result = new DataResponse();

        List<SysParam> list = sysParamManager.findByMark(mark);
        result.addData("list",list);

        return result;
    }

    /**
     * 查询树状结构
     *
     * @param account          账户
     */
    @GetMapping("/getTree")
    public DataResponse getAllRegionUnderLevelLikeTree(@AuthenticationPrincipal Account account) {
        DataResponse result = new DataResponse();

        result.addData("Tree", sysParamManager.findAllTree("0"));
        return result;

    }
}

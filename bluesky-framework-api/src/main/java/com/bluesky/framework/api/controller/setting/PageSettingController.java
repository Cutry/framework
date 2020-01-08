package com.bluesky.framework.api.controller.setting;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.framework.setting.PageSettingManager;
import com.bluesky.framework.setting.page.PageSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


/**
 * 系统配置管理
 */
@RestController
@RequestMapping("/setting/page")
public class PageSettingController {
    @Autowired
    private PageSettingManager pageSettingManager;

    /**
     * 根据页面获取页面配置信息
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_login_manage') or hasPermission('crm-api', 'sm_login_view')")
    @GetMapping("/find")
    public DataResponse findAll(@RequestParam String page) {
        DataResponse result = new DataResponse();
        List<PageSetting> pageSettingList = pageSettingManager.findByPage(page);
        result.addData("pageSettingList", pageSettingList);
        return result;
    }

    /**
     * 默认上传路径
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_login_manage') or hasPermission('crm-api', 'sm_login_view')")
    @GetMapping("/default/dir")
    public DataResponse defaultDir() {
        DataResponse result = new DataResponse();
        String defaultDir = null;
        List<PageSetting> pageSettingList = pageSettingManager.findByPage(PageSetting.Page.DEFAULT.getCode());
        if (!pageSettingList.isEmpty()) {
            defaultDir = pageSettingList.get(0).getValue();
        }
        result.addData("defaultDir", defaultDir);
        return result;
    }

    /**
     * 设置默认上传路径
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_login_manage')")
    @PostMapping("/default/dir/update")
    public DataResponse defaultDirUpdate(@RequestParam String value) {
        DataResponse result = new DataResponse();
        PageSetting pageSetting = PageSetting.builder().value(value).build();
        pageSettingManager.updateDefaultUploadDir(pageSetting);
        return result;
    }

    /**
     * 文件上传，上传到设置的默认路径
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_login_manage')")
    @PostMapping("/{id}/file/update")
    public DataResponse update(@PathVariable Long id, MultipartFile file,
                               Integer width, Integer height) throws IOException {
        DataResponse result = new DataResponse();
        String defaultDir;
        List<PageSetting> pageSettingList = pageSettingManager.findByPage(PageSetting.Page.DEFAULT.getCode());
        if (!pageSettingList.isEmpty()) {
            defaultDir = pageSettingList.get(0).getValue();
        } else {
            result.setFalseAndMessage("请先设置上传文件夹路径");
            return result;
        }
        //MultipartFile文件转换成java.io.File
        if (width != null || height != null) {
            BufferedImage image = ImageIO.read(file.getInputStream());
            //判断宽高是否符合要求
            if (width != null && image.getWidth() != width) {
                return result.setFalseAndMessage("图片尺寸不符");
            }
            if (height != null && image.getHeight() != height) {
                return result.setFalseAndMessage("图片尺寸不符");
            }
        }
        long time = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        String fileName = time + file.getOriginalFilename();
        file.transferTo(new File(defaultDir + File.separator + fileName));
        pageSettingManager.updateValueById(id, fileName);
        result.addData("value", fileName);
        return result;
    }

    /**
     * 设置配置项信息
     */
    @PreAuthorize("hasPermission('crm-api', 'sm_login_manage')")
    @PostMapping("/{id}/update")
    public DataResponse updateFile(@PathVariable Long id, @RequestParam String value) {
        DataResponse result = new DataResponse();
        pageSettingManager.updateValueById(id, value);
        return result;
    }

}
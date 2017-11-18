package com.kaishengit.crm.controller;

import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.service.DiskService;
import com.kaishengit.result.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 公司网盘控制层
 * @author Administrator.
 */
@Controller
@RequestMapping("/disk")
public class DiskController {

    @Autowired
    private DiskService diskService;

    /**
     * 跳转到网盘首页
     * @return
     */
    @GetMapping
    public String diskHome(Model model,
                           @RequestParam(required = false) Integer id) {

        Integer pId = 0;

        if (id != null && id != 0) {
            Disk disk = diskService.findDiskById(id);
            if (disk == null) {
                throw new NotFoundException();
            }

            pId = id;
            model.addAttribute("disk",disk);

        }

        List<Disk> diskList = diskService.findHomeAllDisk(pId);

        model.addAttribute("diskList",diskList);
        return "disk";
    }

    /**
     * 添加新的文件夹
     * @param pId
     * @param staffId
     * @param name
     * @return
     */
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult addDisk(@RequestParam(defaultValue = "0",required = false) Integer pId,
                              Integer staffId,
                              String name) {

        diskService.addNewDisk(pId,staffId,name);
        List<Disk> diskList = diskService.findHomeAllDisk(pId);
        return AjaxResult.success(diskList);
    }

    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult uploadDisk(@RequestParam(defaultValue = "0",required = false) Integer pId,
                                 Integer staffId,
                                 MultipartFile file) throws IOException {


        if (file.isEmpty()) {
            throw new NotFoundException();
        }
        InputStream inputStream = file.getInputStream();

        Long fileSize = file.getSize();

        String fileName = file.getOriginalFilename();

        diskService.uploadFile(pId,staffId,inputStream,fileSize,fileName);


        List<Disk> diskList = diskService.findHomeAllDisk(pId);


        return AjaxResult.success(diskList);

    }


}

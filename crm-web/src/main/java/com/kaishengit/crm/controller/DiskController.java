package com.kaishengit.crm.controller;

import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Disk;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.service.DiskService;
import com.kaishengit.result.AjaxResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    /**
     * 上传文件
     * @param pId
     * @param staffId
     * @param file
     * @return
     * @throws IOException
     */
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

    /**
     * 下载文件
     * @param id
     * @param response
     * @param fileName
     */
    @GetMapping("/download")
    public void downloadFile(Integer id,
                             HttpServletResponse response,
                             @RequestParam(required = false) String fileName) {

        try {
            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = diskService.downloadFileById(id);

            if (StringUtils.isNotEmpty(fileName)) {
                //设置下载的MIMEtype
                response.setContentType("application/octet-stream");
                fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
                //弹出下载对话框
                response.addHeader("Content-Disposition","attachment; filename=\""+fileName+"\"");
            }

            IOUtils.copy(inputStream,outputStream);
            inputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            throw new NotFoundException(e,"文件没有找到");
        }
    }

    /**
     * 删除文件
     * @param id
     * @return
     */
    @GetMapping("/delete/{id:\\d+}")
    @ResponseBody
    public AjaxResult deleteFile(@PathVariable Integer id) {

        try {
            diskService.deleteFileById(id);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            return AjaxResult.error(ex.getMessage());
        }
    }

    /**
     * 根据文件的id进行重命名
     * @param diskId
     * @return
     */
    @GetMapping("/rename")
    @ResponseBody
    public AjaxResult renameFile(Integer diskId,String name) {
        diskService.renameFielById(diskId,name);
        return AjaxResult.success();
    }
}

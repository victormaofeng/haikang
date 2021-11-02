package top.maof.haikang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.maof.haikang.model.DetectedFile;
import top.maof.haikang.mq.Message;
import top.maof.haikang.result.Result;
import top.maof.haikang.service.DetectedFileService;

import java.util.List;

@Controller
@RequestMapping("/detectedFile")
public class DetectedFileController {
    @Autowired
    DetectedFileService detectedFileService;

    @RequestMapping("/gets")
    @ResponseBody
    public Result gets(int page, int pageSize) {
        List<DetectedFile> list = detectedFileService.gets(page, pageSize);
        return Result.success(list);
    }
    @RequestMapping("/getMessage")
    @ResponseBody
    public Result message(int id){
        Message message = detectedFileService.getMessage(id);
        return Result.success(message);
    }
}

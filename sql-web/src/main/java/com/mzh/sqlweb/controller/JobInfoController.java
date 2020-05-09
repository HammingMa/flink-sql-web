package com.mzh.sqlweb.controller;

import com.mzh.sqlweb.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class JobInfoController {

    @Autowired
    private JobInfoService jobInfoService;

    @GetMapping("/jobInfo")
    public String jobInfo(){
        return "JobInfo";
    }


    @ResponseBody
    @PostMapping("/saveJobInfo")
    public String saveJobInfo(@RequestParam(name = "jobName", required = true) String jobName, @RequestParam(name = "sqls", required = true)String sqls){
        System.out.println(111111111);
        sqls=sqls.replaceAll("<br />","\n");
        System.out.println(jobName);
        System.out.println(sqls);


        jobInfoService.addJobInfo(jobName,sqls);

        return "sucess";
    }

    @ResponseBody
    @PostMapping("/runJob")
    public String runJob(@RequestParam(name = "jobName", required = true) String jobName, @RequestParam(name = "sqls", required = true)String sqls){
        sqls=sqls.replaceAll("<br />","\n");
        System.out.println(jobName);
        System.out.println(sqls);


        jobInfoService.addJobInfo(jobName,sqls);
        jobInfoService.runJob(jobName);

        return "sucess";
    }


}

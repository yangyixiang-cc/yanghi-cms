package com.example.sstest.controller;

import com.aizuda.common.toolkit.CollectionUtils;
import com.aizuda.monitor.DiskInfo;
import com.aizuda.monitor.OshiMonitor;
import com.example.sstest.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/monitor")
public class MonitorController {
    // 注入监控模块 Oshi 调用类
    @Resource
    private OshiMonitor oshiMonitor;

    @PostMapping("/server")
    public Result monitor() {
        Map<String, Object> server = new HashMap<>(5);
        // 系统信息
        server.put("sysInfo", oshiMonitor.getSysInfo());
        // CPU 信息
        server.put("cupInfo", oshiMonitor.getCpuInfo());
        // 内存信息
        server.put("memoryInfo", oshiMonitor.getMemoryInfo());
        // Jvm 虚拟机信息
        server.put("jvmInfo", oshiMonitor.getJvmInfo());
        // 磁盘信息
        List<DiskInfo> diskInfos = oshiMonitor.getDiskInfos();
        server.put("diskInfos", diskInfos);
        if (CollectionUtils.isNotEmpty(diskInfos)) {
            long usableSpace = 0;
            long totalSpace = 0;
            for (DiskInfo diskInfo : diskInfos) {
                usableSpace += diskInfo.getUsableSpace();
                totalSpace += diskInfo.getTotalSpace();
            }
            double usedSize = (totalSpace - usableSpace);
            // 统计所有磁盘的使用率
            server.put("diskUsePercent", oshiMonitor.formatDouble(usedSize / totalSpace * 100));
        }

        // 系统前 10 个进程
        List<OSProcess> processList = oshiMonitor.getOperatingSystem().getProcesses(null,
                OperatingSystem.ProcessSorting.CPU_DESC, 10);
        List<Map<String, Object>> processMapList = new ArrayList<>();
        for (OSProcess process : processList) {
            Map<String, Object> processMap = new HashMap<>(5);
            processMap.put("name", process.getName());
            processMap.put("pid", process.getProcessID());
            processMap.put("cpu", oshiMonitor.formatDouble(process.getProcessCpuLoadCumulative()));
            processMapList.add(processMap);
        }
        server.put("processList", processMapList);
        return Result.ok(server);
    }
}

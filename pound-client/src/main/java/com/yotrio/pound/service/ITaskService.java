package com.yotrio.pound.service;

import com.github.pagehelper.PageInfo;
import com.yotrio.common.domain.DataTablePage;
import com.yotrio.pound.model.Task;

import java.util.List;

/**
 * 模块名称：projects-parent com.yotrio.pound.service
 * 功能说明：<br>
 * 开发人员：Wangyq
 * 创建时间： 2018-09-27 14:25
 * 系统版本：1.0.0
 **/
public interface ITaskService {
    PageInfo findAllPaging(DataTablePage dataTablePage, Task task);

    Task findById(Integer id);

    int updateById(Task task);

    String executeTask(Task task);

    List<Task> findUnFinishTasksLimit(int taskAccount);

    int save(Task task);

    Task findByOtherId(String poundLogNo);
}

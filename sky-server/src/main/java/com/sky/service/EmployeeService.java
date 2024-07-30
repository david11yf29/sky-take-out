package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增員工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分頁查詢
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 啟用禁用員工帳號
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根據 id 查詢員工信息
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 編輯員工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}

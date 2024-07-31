package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "員工相關接口 [EmployeeController 類上方]")
public class EmployeeController {

    @Autowired // employeeService 是 interface 而非 class, EmployeeServiceImpl 會自動生成並儲存在 Bean 裡面等之後 DI
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "員工登入 [EmployeeController 類方法 login]")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("[Logger] 员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("員工退出 [EmployeeController 類方法 logout]")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增員工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增員工 [EmployeeController 類方法 save]")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("[Logger] 新增員工: {}", employeeDTO);
        System.out.println("當前線程的 id: " + Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 員工分頁查詢
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("員工分頁查詢 [EmployeeController 類方法 page]")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("[Logger] 員工分頁查詢, 參數為: {}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 啟用禁用員工帳號
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("啟用禁用員工帳號 [EmployeeController 類方法 startOrStop]")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("[Logger] 啟用禁用員工帳號: {}, {}", status, id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根據 id 查詢員工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根據 id 查詢員工信息 [EmployeeController 類方法 getById]")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 編輯員工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("編輯員工信息 [EmployeeController 類方法 update]")
    public Result update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("[Logger] 編輯員工信息: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

}

package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相關接口 [DishController 類上方]")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增菜品 [DishController 類方法 save]")
    public Result save(@RequestBody DishDTO dishDTO) { // @RequestBody 將 JSON 轉換成 Java 對象
        log.info("[Logger] 新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分頁查詢
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分頁查詢 [DishController 類方法 page]")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("[Logger] 菜品分頁查詢：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量刪除
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量刪除 [DishController 類方法 delete]")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("[Logger] 菜品批量刪除：{}", ids);
        dishService.deteleBatch(ids);
        return Result.success();
    }

    /**
     * 根據 id 查詢菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根據 id 查詢菜品 [DishController 類方法 getById]")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("[Logger] 根據 id 查詢菜品：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改菜品 [DishController 類方法 update]")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("[Logger] 修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }
}

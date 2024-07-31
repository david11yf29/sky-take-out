package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定義的註解, 用於標示某個方法需要進行功能字段自動填充處理
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

    // 數據操作類型: UPDATE INSERT, 之後套用 AutoFill 註解 () 裡面的設定
    OperationType value();

}

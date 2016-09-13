package com.example.pc.shacus.View.FloatMenu;

/**
 * Created by linroid on 15/3/9.
 *  * 按作者要求保留协议 修改部分内容
 * 李嘉文 引入
 */
public interface FloatMenuInterface {
    void collapse(boolean animate);
    void expand(boolean animate);
    void toggle(boolean animate);
    void setMenuLayout(FilterMenuLayout layout);
}

package com.hsasys.baiduocr.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface foodaddMapper {
    @Select( "select name from ingre_foodadditives" )
    List<String> listadd();

    @Select( "select name from ingre_addedSugar" )
    List<String> listsugar();

    @Select( "select name from ingre_transfattyacid" )
    List<String> listtran();
}

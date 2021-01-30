package com.baomidou.mybatisplus.extension.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.defaults.RawSqlSource;

/**
 * @Description: 通过唯一索引查询
 * @Author: 杨家星
 * @Date: 2021/01/30 22:33
 */
@Slf4j
public class SelectByKeyMethod extends AbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "SELECT %s FROM %s WHERE %s %s limit 1";
        SqlSource sqlSource = new RawSqlSource(configuration, String.format(sql,
                sqlSelectColumns(tableInfo, false),
                tableInfo.getTableName(), buildKeySql(tableInfo),
                tableInfo.getLogicDeleteSql(true, true)), Object.class);
        return this.addSelectMappedStatementForTable(mapperClass, "selectByKey", sqlSource, tableInfo);
    }
    private String buildKeySql(TableInfo tableInfo){
        StringBuilder builder = new StringBuilder();
        if(CollectionUtils.isNotEmpty(tableInfo.getUniqueKeyColumns())){
            for(int i=0;i<tableInfo.getUniqueKeyColumns().size();i++){
                if(i==0){

                    builder.append(tableInfo.getUniqueKeyColumns().get(i) + "=#{keys[" + i + "]},");
                }else{
                    builder.append(" and "+tableInfo.getUniqueKeyColumns().get(i) + "=#{keys[" + i + "]},");
                }
            }
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }

}
